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

void handleRequest(int);

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

    while(1){
        if((new_socket = accept(socket_fd, (struct sockaddr*)&address, &addrlen)) < 0){
            perror("accept failed");
            exit(-1);
        }
        handleRequest(new_socket);
    }
}

void handleRequest(int socket){
    char buffer[1024];
    while(1){
        int valread = read(socket, buffer, 1023);
        if(valread == 0){
            break;
        }
        printf("%s",buffer);
    }
}
