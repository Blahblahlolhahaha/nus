#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <memory.h>
#include <openssl/ssl.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <unistd.h> 
int main(){
    int client_fd;
    struct addrinfo* serv_addr = NULL;
    if(getaddrinfo("varlabs.comp.nus.edu.sg", "443", 0, &serv_addr)){
        printf("failed to get info\n");
        exit(0);
    }
    client_fd = socket(AF_INET, SOCK_STREAM, 0);
    if(client_fd == -1){
        printf("Failed to create socket\n");
        exit(0);
    }
    if(connect(client_fd, serv_addr->ai_addr, serv_addr->ai_addrlen)){
        printf("Failed to connect");
        exit(0);
    }
    SSL_METHOD* tls = TLS_method();
    SSL_CTX* ctx = SSL_CTX_new(tls);
    if(!ctx){
        printf("Failed to initialise context\n");
        exit(0);
    }
    SSL* ssl = SSL_new(ctx);
    if(!ssl){
        printf("Failed to initialise ssl\n");
        exit(0);
    }
    SSL_set_fd(ssl, client_fd);
    if(SSL_connect(ssl) <= 0){
        printf("Failed to connect via TLS\n");
        exit(0);
    }

    const char* request = "GET /tools/yourip.php HTTP/1.0\r\nHost: varlabs.comp.nus.edu.sg\r\n\r\n";
    SSL_write(ssl, request, strlen(request)); 
    char buffer[4096] = {0};
    SSL_read(ssl, buffer, 4095);
    const char* tag1 = "<body>";
    const char* tag2 = "</body>"; 
    char*start = strstr(buffer, tag1) + strlen(tag1);
    start[strstr(start,tag2) - start] = 0;
    printf("%s\n", start);
    SSL_shutdown(ssl);
    SSL_free(ssl);
    SSL_CTX_free(ctx);
    close(client_fd);
}


