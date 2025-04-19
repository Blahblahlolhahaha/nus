#include <asm-generic/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>
#include <netinet/in.h>

typedef struct HashedPair{
    int key;
    void* value;
} HashedPair;

typedef struct req{
    char* method;
    char* path;
    char* params;
    char* body;
} Req;

typedef struct route{
    char* method;
    char* path;
    void (*handler)(int,Req*);
} Route;

char METHOD_NOT_ALLOWED[] = "405 MethodNotAllowed  ";
char NOT_FOUND[] = "404 NotFound  ";
char OK[] = "200 OK ";
Route** routes; 
int MAX = 6* sizeof(Route*);
int filled = 0;
HashedPair* keyStore[128] = {0};
HashedPair* counterStore[128] = {0};

void setHandler(char*, char*, void (*)(int,Req*));

void (*findHandler(Req* req))(int,Req*);

void handleRequest(int);

int parseRequest(char*, Req*);

void getKey(int, Req*);

void postKey(int, Req*);

void deleteKey(int, Req*);

void getCounter(int, Req*);

void postCounter(int, Req*);

void deleteCounter(int, Req*);


int hash(char*);

int main(int argc, char** argv){
    int socket_fd, new_socket;
    struct sockaddr_in address;
    int opt = 1;
    socklen_t addrlen = sizeof(address);

    if(argc == 1){
       perror("Specify port number!");
       exit(-1);
    } 

    if((socket_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
        perror("sadge");
        exit(-1);
    }

    if(setsockopt(socket_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt))){
        perror("Set sock opt failed");
        exit(-1);
    }

    address.sin_family = AF_INET;
    address.sin_port = htons(atoi(argv[1]));
    address.sin_addr.s_addr = INADDR_ANY;

    if(bind(socket_fd,(struct sockaddr*)&address, sizeof(address)) < 0){
        perror("bind failed");
        exit(-1);
    }

    if(listen(socket_fd,1) < 0){
        perror("listen failed");
        exit(-1);
    }

    routes = (Route**) malloc(MAX);
    if(!routes){
        perror("malloc failed");
        exit(-1);
    }
    memset(routes,0,MAX);
    setHandler("GET", "/key/:id", getKey);
    setHandler("POST", "/key/:id", postKey);
    setHandler("DELETE", "/key/:id", deleteKey);
    setHandler("GET","/counter/:id",getCounter);
    setHandler("POST", "/counter/:id",postCounter);
    setHandler("DELETE","/counter/:id",deleteCounter);
    while(1){
        if((new_socket = accept(socket_fd, (struct sockaddr*)&address, &addrlen)) < 0){
            perror("accept failed");
            exit(-1);
        }
        handleRequest(new_socket);
    }
}

void cleanReq(Req* req){
    if(req->body){
        int bodyLen = strlen(req->body);
        memset(req->body,0,bodyLen);
        free(req->body);
        req->body = 0;
    }
    if(req->method){
        int len = strlen(req->method);
        memset(req->method,0,len);
        free(req->method);
        req->method = 0;
    }
    if(req->path){
        int len = strlen(req->path);
        memset(req->path,0,len);
        free(req->path);
        req->path = 0;
    }
    if(req->params){
        int len = strlen(req->params);
        memset(req->params,0,len);
        free(req->params);
        req->params = 0;
    }
}

void cleanKeyPair(HashedPair* pair){
    memset(pair->value,0,strlen(pair->value));
    free(pair->value);
    pair->value = NULL;
    pair->value = 0;
    memset(pair,0,sizeof(HashedPair));
    free(pair);
    pair = NULL;
}

void handleRequest(int socket){
    while(1){
        char buffer[1024] = {0};
        int valread = read(socket, buffer, 1023);
        int len = strlen(buffer);
        if(valread == 0){
            break;
        }
        int read = 0;
        while(read < len){
            Req req = {0};
            read += parseRequest(buffer + read,&req);
            if(!req.method){
                break;
            }
            void (*ptr)(int,Req*) = findHandler(&req);
            if(ptr == NULL){
                write(socket, METHOD_NOT_ALLOWED, strlen(METHOD_NOT_ALLOWED));
            }
            ptr(socket,&req);
            cleanReq(&req);

        }
    }
}

void (*findHandler(Req* req))(int,Req*){
    for(int i = 0; i < MAX; i++){
        Route* route = routes[i];
        if(!route){
            break;
        }
        if(strncmp(req->method,route->method,strnlen(route->method,6))){
            continue;
        }
        char* path = route->path;
        char* colon = strstr(path,":");
        if(colon){
            int mainLen = colon - path + 1;
            char* newPath = (char*) malloc(mainLen);
            if(!newPath){
                perror("Error getting handler");
                exit(-1);
            }
            memset(newPath,0,mainLen);
            strncpy(newPath,path,mainLen-1);
            char* test = strstr(req->path,newPath);
            if(test == req->path){
                req->path += strlen(newPath);
                int paramLen = strlen(req->path);
                char *param = (char*) malloc(paramLen + 1);
                if(!param){
                    perror("Error getting handler");
                    exit(-1);
                }
                memset(param,0,paramLen+1);
                strncpy(param,req->path,paramLen);
                req->path = newPath;
                req->params = param;
                memset(test,0,strlen(test));
                free(test);
                test = NULL;
                return route->handler;
            }
        } else if(!strncmp(path,req->path,strlen(path))){
            return route->handler;
        }
    }
    return NULL;
}

int parseRequest(char* buf, Req* req){
    int read = 0;
    char* space = strstr(buf, " ");
    if(!space){
        return -1;
    }
    int methodLength = space - buf + 1;
    char* method = (char*) malloc(methodLength);
    if(!method){
        perror("Error parsing request");
        exit(-1);
    }
    memset(method,0,methodLength);
    strncpy(method,buf,methodLength-1);
    buf+= methodLength;
    read += methodLength - 1;
    space = strstr(buf, " ");
    if(!space){
        memset(method,0, methodLength);
        free(method);
        method = NULL;
        return -1;
    } 
    int pathLength = space - buf + 1;
    char* path = (char*) malloc(pathLength);
    if(!path){
        memset(method,0,methodLength);
        free(method);
        perror("Error parsing request");
        exit(-1);
    }
    memset(path,0,pathLength);
    strncpy(path, buf, pathLength - 1);
    read += pathLength - 1;
    if(!strncmp(method, "POST", 4)){
        buf += pathLength;
        space = strstr(buf,"Content-Length ");
        if(!space){
            space = strstr(buf,"content-length ");
            if(!space){
                memset(method,0,methodLength);
                free(method);
                memset(path,0,strlen(path));
                free(path);
                return -1;
            }
        }
        buf += strlen("Content-Length ");
        read += strlen("Content-Length ");
        space = strstr(buf,"  ");
        if(!space){
            memset(method,0,methodLength);
            free(method);
            memset(path,0,strlen(path));
            free(path);
            return -1;
        }
        int numLen = space - buf + 1;
        char* num = (char*) malloc(numLen);
        if(!num){
           memset(method,0,methodLength);
           free(method);
           memset(path,0,strlen(path));
           free(path);
           perror("Error parsing request");
           exit(-1);
        } 
        memset(num,0,numLen);
        strncpy(num, buf, numLen - 1);
        read += numLen;
        int contentLength = atoi(num);
        char* content = (char*)malloc(contentLength + 1);
        if(!content){
            memset(method,0,methodLength);
            free(method);
            memset(path,0,strlen(path));
            free(path);
            memset(num,0,numLen);
            free(num);
            perror("Error parsing request");
            exit(-1);
        }
        memset(content,0,contentLength + 1);
        strncpy(content, space + 2, contentLength);
        read += contentLength + 3;
        req->method = method;
        req->path = path;
        req->body = content;
        return read;
    }
    read += 3;
    req->path = path;
    req->method = method;
    return read;
}

void setHandler(char* method, char* path, void (*handler)(int, Req*)){
    Route* route = (Route*)malloc(sizeof(Route));
    if(!route){
        perror("Route malloc");
        exit(-1);
    }
    memset(route,0,sizeof(Route));
    
    route->method = (char*) malloc(strnlen(method,6) + 1);
    if(!route->method){
        perror("Route malloc");
        exit(-1);
    }
    memset(route->method,0,strnlen(method,6) + 1);
    strncpy(route->method, method, 6);
    
    route->path = (char*) malloc(strnlen(path,128) + 1);
    if(!route->method){
        perror("Route malloc");
        exit(-1);
    }
    memset(route->path,0,strnlen(path,128) + 1);
    strcpy(route->path, path);
    route->handler = handler;
    routes[filled] = route;
    filled++;
}

int containsKey(int hash){
    for(int i = 0;i < 128;i++){
        HashedPair* pair = keyStore[i];
        if(pair && pair->key == hash){
            return 1;
        }
    }
    return 0;
}

int getCounter(int hash){
    for(int i = 0;i < 128;i++){
        HashedPair* pair = counterStore[i];
        if(pair && pair->key == hash){
            return (*(int*)(pair->value));
        }
    }
    return -1;
}

HashedPair* getPair(int hash){
    for(int i = 0; i < 128; i++){
        if(keyStore[i] && keyStore[i]->key == hash){
            return keyStore[i];
        }
    }
    return 0;
}

HashedPair* getCounterPair(int hash){
    for(int i = 0; i < 128; i++){
        if(counterStore[i] && counterStore[i]->key == hash){
            return counterStore[i];
        }
    }
    return 0;
}

void getKey(int fd, Req* req){
    int hashKey = hash(req->params);
    HashedPair* pair = getPair(hashKey);
    if(pair){ 
        char* value = pair->value;
        char buffer[256] = {0};
        strcpy(buffer,OK);
        strcpy(buffer + strlen(buffer), "Content-Length ");
        sprintf(buffer+strlen(buffer),"%d",strlen(value));
        strcpy(buffer + strlen(buffer), "  ");
        strcpy(buffer + strlen(buffer), value);
        write(fd,buffer,strlen(buffer));
    }
}

void getCounter(int fd, Req* req){
    int hashKey = hash(req->params);
    HashedPair* pair = getCounterPair(hashKey);
    if(pair){
        int *value = pair->value;
        char buffer[256] = {0};
        char number[128] = {0};
        sprintf(number,"%d",*value);
        strcpy(buffer,OK);
        strcpy(buffer + strlen(buffer), "Content-Length ");
        sprintf(buffer+strlen(buffer),"%d", strlen(number));
        strcpy(buffer + strlen(buffer), "  ");
        strcpy(buffer + strlen(buffer), number);
        write(fd,buffer,strlen(buffer));
    }
}

void postKey(int fd, Req* req){
    int hashKey = hash(req->params);
    if(strstr(req->path,"/key") == req->path){
        if(containsKey(hashKey)){
            write(fd, METHOD_NOT_ALLOWED, strlen(METHOD_NOT_ALLOWED));
            return;
        }
        HashedPair* pair = (HashedPair*) malloc(sizeof(HashedPair));
        if(!pair){
            perror("cannot add pair");
            exit(-1);
        }
        pair->key = hashKey;
        int len = strlen(req->body);
        char* value = malloc(len + 1);
        if(!value){
            perror("cannot add pair");
            exit(-1);
        }
        memset(value,0,len + 1);
        strncpy(value,req->body,len);
        pair->value = value;
        for(int i = 0;i< 128;i++){
            if(!keyStore[i]){
                keyStore[i] = pair;
                break;
            }
        }
        char buffer[9] = {0};
        strcpy(buffer,OK);
        strcpy(buffer+7, " ");
        write(fd,buffer,9);
    }
}

void deleteKey(int fd, Req* req){
    int hashKey = hash(req->params);
    if(strstr(req->path,"/key") == req->path){
        if(!containsKey(hashKey)){
            write(fd, NOT_FOUND, strlen(NOT_FOUND));
            return;
        }
        if(getCounter(hashKey) > 0){
            write(fd, METHOD_NOT_ALLOWED, strlen(METHOD_NOT_ALLOWED));
            return;
        }
        getKey(fd, req);
        cleanKeyPair(getPair(hashKey));
    }
}

int hash(char* str){
    int len = strlen(str);
    int seed = 7;
    for(int i = 0;i < len; i++){
        seed = seed * 101 + str[i];
    }
    return seed;
}
