#include <asm-generic/errno.h>
#include <errno.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/ip6.h>
#include <netinet/tcp.h>
#include <netinet/ip_icmp.h>
#include <sys/select.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <memory.h>
#include <sys/time.h>
#include <curl/curl.h>
#include <curl/easy.h>
#include <curl/typecheck-gcc.h>
typedef struct PseudoHeader {
    uint32_t srcIP;
    uint32_t dstIP;
    uint8_t fixed;
    uint8_t  protocol;
    uint16_t tcpLen;
} PseudoHeader;

struct MemoryStruct{
    char* memory;
    size_t size;
};

uint16_t calculateChecksum(uint16_t* buffer, int len){
    int sum = 0;
    while(len > 1){
        sum += *buffer++;
        len -= 2;
    }
    if(len == 1){
        sum += *((uint8_t*)buffer) << 8;
    }
    while(sum >> 16){
        sum = (sum & 0xffff) + (sum >> 16);
    }
    return ~sum & 0xffff;
}

size_t WriteMemoryCallback(void* contents, size_t size, size_t nmemb, void* userp){
    int realsize = size * nmemb;
    struct MemoryStruct *mem = (struct MemoryStruct *) userp;
    if(mem->memory == NULL){
        return 0;
    }

    memcpy(&(mem->memory[mem->size]), contents, realsize);
    mem->size += realsize;
    mem->memory[mem->size] =  0;
    return realsize;
}

void getGeoLocaton(char* ip, char* res){
    struct MemoryStruct chunk;
    chunk.memory = calloc(512,1);
    chunk.size = 0;
    if(!res){
        printf("Error callocing\n");
        exit(0);
    }
    CURL* curl = curl_easy_init();
    char url[256] = {0};
    if(!curl){
        printf("An error occured initialising curl\n");
        exit(0);
    }
    sprintf(url,"http://ip-api.com/line/%s?fields=59905",ip); 
    curl_easy_setopt(curl, CURLOPT_URL, url);
    curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void*)&chunk);
    curl_easy_setopt(curl, CURLOPT_USERAGENT, "libcurl-agent/1.0");
    CURLcode status = curl_easy_perform(curl);
    if(status != CURLE_OK){
        printf("An error occured in curl\n");
        exit(0);
    }

    char* exist = strstr(chunk.memory, "\n");
    int sucLen = exist - chunk.memory;
    char geoStatus[sucLen + 1];
    strncpy(geoStatus, chunk.memory, sucLen);
    geoStatus[sucLen] = 0;
    chunk.memory = exist + 1;
    exist = strstr(chunk.memory, "\n");
    if(!strcmp(geoStatus,"success")){
        for(int i = 0; i < 3; i++){
            strncpy(res + strlen(res), chunk.memory, exist - chunk.memory);
            strncpy(res + strlen(res), ", ", 3); 
            chunk.memory = exist + 1;
            exist = strstr(chunk.memory, "\n");
        }
    }
    else{
        strcpy(res, "Singapore, local router");
    }
}

void setPacketDetails(struct iphdr* ip, struct tcphdr* tcp, struct sockaddr_in* source, struct addrinfo* dest, int ttl, int id){
    tcp->seq = 0;
    tcp->ack_seq = 0;
    tcp->doff = 5;
    tcp->syn = 1;
    tcp->window = htons(5850);
    tcp->check = 0;
    tcp->urg_ptr = 0; 
    tcp->source = htons(id);
    tcp->dest = htons(80);

    ip->ihl = 5;
    ip->version = 4;
    ip->tos = 0;
    ip->tot_len = htons(sizeof(struct tcphdr) + sizeof(struct iphdr));
    ip->id = 32000;
    ip->ttl = ttl;
    ip->protocol = IPPROTO_TCP;
    ip->check = 0;
    ip->saddr = source->sin_addr.s_addr;
    struct sockaddr_in* ipv4Dst = (struct sockaddr_in*)dest->ai_addr;
    ip->daddr = ipv4Dst->sin_addr.s_addr;
    ip->version = 4;

    ip->check = calculateChecksum((uint16_t*)ip, ip->ihl * 4);
    uint8_t sad[sizeof(PseudoHeader) + sizeof(struct tcphdr)];
    PseudoHeader psh = { 
        source->sin_addr.s_addr,
        ipv4Dst->sin_addr.s_addr,
        0,
        IPPROTO_TCP,
        htons(sizeof(struct tcphdr)),
    };
   memcpy(sad, &psh, sizeof(PseudoHeader));
   memcpy(sad + sizeof(PseudoHeader), tcp, sizeof(struct tcphdr));
   int sum = 0;
   sum += calculateChecksum((uint16_t*)(sad), sizeof(struct tcphdr)+  sizeof(PseudoHeader));
   tcp->check = sum;
}

int main(int argc, char** argv){
    if(argc < 2){
        printf("Usage: %s <url>\n", argv[0]);
        exit(0);
    }
    const char* target = argv[1];
    struct addrinfo* serv_addr = NULL;
    struct addrinfo hint = {0,0,0,0,0,0,0,0};
    hint.ai_family = AF_INET;
    if(getaddrinfo(target, "80", &hint, &serv_addr)){
        printf("Failed to get info\n");
        exit(0);
    }

    int sendTCPSocket = socket(AF_INET, SOCK_RAW, IPPROTO_TCP);
    int recvSocket = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
    int recvTCPSocket = socket(AF_INET, SOCK_RAW, IPPROTO_TCP);
    if(sendTCPSocket == -1 || recvSocket == -1 || recvTCPSocket == -1){
        printf("Failed to initialise socket %d\n", errno);
        exit(0);
    }
    int sad = 1;
    if(setsockopt(sendTCPSocket, IPPROTO_IP, IP_HDRINCL, &sad, sizeof(sad))){
        printf("Error setting raw socket\n");
        exit(0);
    }
    //if(setsockopt(recvSocket, SOL_SOCKET, SO_RCVTIMEO, &timeout, sizeof(timeout))){
    //    printf("Error setting timeout %d\n", errno);
    //    exit(0);
    //}
    uint8_t* buf = (uint8_t* )calloc(256,1);
    if(!buf){
        printf("Error calloc\n");
        exit(0);
    }
    struct iphdr* ip = (struct iphdr*)buf;
    struct tcphdr* tcp = (struct tcphdr*)(buf + sizeof(struct iphdr));
    char hostBuffer[256];
    sad = gethostname(hostBuffer, 256);
    if(sad){
        printf("Error getting hostname\n");
        free(buf);
    }
    struct hostent* entries = gethostbyname(hostBuffer);
    if(!entries){
        printf("si diao diao\n");
        exit(0);
    }
    struct sockaddr_in host_addr;
    host_addr.sin_family = AF_INET;
    host_addr.sin_addr  = *(struct in_addr*)entries->h_addr_list[0];
    printf("[Destination - %s]\n", inet_ntoa(((struct sockaddr_in*)serv_addr->ai_addr)->sin_addr));
    uint8_t* recvBuf = (uint8_t*) calloc(2048,1);
    if(!recvBuf){
        printf("Error calloc\n");
        exit(0);
    }
    for(int i = 1; i < 30; i ++){ 
        double min,avg,max,sum;
        min = 9999;
        avg = 0;
        max = 0;
        sum = 0;
        int success = 0;
        int reached = 0;
        int dropped = 0;
        char ipStr[16] = {0}; 
        for(int j = 0; j < 3; j++){
            host_addr.sin_family = AF_INET;
            host_addr.sin_port = htons(40000+i);
            host_addr.sin_addr  = *(struct in_addr*)entries->h_addr_list[0];
            setPacketDetails(ip,tcp, &host_addr, serv_addr,i, 40000+i);
            struct timeval start;
            gettimeofday(&start, NULL);  
            if(sendto(sendTCPSocket, buf, 40, 0, serv_addr->ai_addr, serv_addr->ai_addrlen) < 0 ){        
                printf("Send error %d\n", errno);
                exit(0);
            }
            socklen_t len = sizeof(host_addr);
            while(1){
                struct timeval check;
                gettimeofday(&check, NULL);
                if(check.tv_sec - start.tv_sec > 5){
                    if(success == 0){
                        dropped = 1;
                    }
                    break;
                }  
                struct timeval time;
                fd_set fds;
                time.tv_sec=5; 
                FD_ZERO(&fds);
                FD_SET(recvSocket, &fds);
                FD_SET(recvTCPSocket, &fds);
                int maxFD = (recvSocket > recvTCPSocket ? recvSocket : recvTCPSocket) + 1;
                int ret = select(maxFD,&fds, 0, 0, &time);
                if(ret == 0){
                    if(success != 0){
                        break;
                    }
                    dropped = 1;
                    break;
                }
                if(ret < 0){
                    printf("Error ;-; %d\n", errno);
                    exit(0);
                }
                int recv = 0;
                if(FD_ISSET(recvSocket, &fds)){
                    recv = recvfrom(recvSocket, recvBuf, 2048, 0, (struct sockaddr*)&host_addr, &len);
                }
                else if(FD_ISSET(recvTCPSocket, &fds)){
                        
                    recv = recvfrom(recvTCPSocket, recvBuf, 2048, 0, (struct sockaddr*)&host_addr, &len);
                }
                struct iphdr* incoming = (struct iphdr*) recvBuf;
                int gdrecv = 0;
                if(incoming->protocol == IPPROTO_TCP){
                    struct tcphdr* testTCP = (struct tcphdr*)(recvBuf + sizeof(struct iphdr));
                    if(testTCP->dest == htons(40000 +i) && (testTCP->rst || testTCP->syn)){
                        gdrecv = 1;
                        reached = 1;
                    }
                }
                if(incoming->protocol == IPPROTO_ICMP){
                    struct icmphdr* icmp = (struct icmphdr*)(recvBuf + sizeof(struct iphdr));
                    if(icmp->type == ICMP_TIME_EXCEEDED){
                      struct iphdr* test = (struct iphdr*)(recvBuf + sizeof(struct iphdr) + sizeof(struct icmphdr));  
                      if(test->id == 32000){
                          struct tcphdr* testTCP = (struct tcphdr*)(recvBuf + sizeof(struct iphdr) + sizeof(struct icmphdr) + sizeof(struct iphdr));
                          if(testTCP->th_sport == htons(40000 + i)){
                              gdrecv = 1; 
                          }
                      }
                    }
                    else if(icmp->type == ICMP_PORT_UNREACH){
                        printf("Reached!\n");
                    }
                }
                if(gdrecv){
                    uint8_t gg[4];
                    for(int k = 0; k < 4; k ++){
                        gg[k] = incoming->saddr >> (k * 8);
                    }
                    snprintf(ipStr, 16, "%d.%d.%d.%d",gg[0],gg[1],gg[2],gg[3]);
                    struct timeval end;
                    gettimeofday(&end, NULL);
                    double duration = ((double)(end.tv_sec - start.tv_sec)) * 1000 + ((double)(end.tv_usec - start.tv_usec))/1000;
                    sum += duration;
                    success ++;
                    if(min > duration){
                        min = duration;
                    }
                    if(max < duration){
                        max = duration;
                    }
                    break;
                }
            }
            if(dropped){
                break;
            }
        }
        avg = sum / success;
        if(dropped){
            printf("Hop %d: *\n",i);
        }
        else{
            char* res = (char*) calloc(512,1);
            if(!res){
                printf("An error occured in calloc");
                exit(0);
            }
            getGeoLocaton(ipStr, res);
            printf("Hop %d: %s (%s) - min/avg/max RTT = %f / %f / %f ms\n", i, ipStr,res ,min,avg,max);
            memset(res, 0, 512);
            free(res);
        }
        if(reached){
            printf("-------------------\nTotal hops: %d\n", i);
            break;
        }
    }
    memset(recvBuf,0, 2048);    
    memset(buf, 0, 256);
    free(buf);
    free(recvBuf);
}

