#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>

int main(int argc, char const *argv[])
{
    char a[] = "a";
    int sock = 0, valread;
    struct sockaddr_in address;
    char *hello = "hello";
    char *fileName = "abc cba aa anc nmc mnhnmnh";
    char buffer[1024] = {0};

    //Tao socket
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        printf("\n Socket creation error \n");
        return -1;
    }
    //Xu ly buffer
    memset(&address, '0', sizeof(address));
    //gan port, dia chi
    bzero(&address, sizeof(address));
    address.sin_family = AF_INET;
    address.sin_port = htons(8080);
    //ham chuyen doichuoi ky tu thanh dia chi dang so
    inet_pton(AF_INET, "127.0.0.1", &address.sin_addr);
    //ket noi
    if (connect(sock, (struct sockaddr *)&address, sizeof(address)) < 0)
    {
        printf("\nConnection Failed \n");
        return -1;
    }
    //Vong lap, neu nhap vao ki tu khac khong phai ki tu dac biet thi gui no cho server


    // send(sock , hello , strlen(hello) , 0 );

    //Neu la ki tu dac biet thi dong ket noi
    fflush(stdout);
    // send(sock , fileName , strlen(fileName) , 0 );
    valread = read( sock , buffer, 1024);
    if(strcmp(buffer,"1") != 0){
        send(sock , fileName , strlen(fileName) , 0 );
    }
    printf("%s", fileName);
    printf("%s\n",buffer );
    close(sock);
    return 0;
}