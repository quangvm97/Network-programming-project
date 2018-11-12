// Client side C/C++ program to demonstrate Socket programming
// Created by QuangVM
#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>
#include <time.h>
#define PORT 8080
#define SIZE_BUFFER 1024

int main(int argc, char const* argv[])
{
    struct sockaddr_in address;
    int sock = 0, size_file;
    struct sockaddr_in serv_addr;
    char buffer[SIZE_BUFFER] = { 0 };
    char ip_add[20];
    FILE* file;

    printf("Enter server ip address:\n");
    gets(ip_add);

    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        printf("\n Socket creation error \n");
        return -1;
    }

    memset(&serv_addr, '0', sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Convert IPv4 and IPv6 addresses from text to binary form
    if (inet_pton(AF_INET, ip_add, &serv_addr.sin_addr) <= 0) {
        printf("\nInvalid address/ Address not supported \n");
        return -1;
    }

    if (connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
        printf("\nConnection Failed \n");
        return -1;
    }
    while (1) {
        char file_name[100];
        printf("\nEnter a file name download from server: ");
        fflush(stdin);
        scanf("%s", &file_name);
        send(sock, file_name, strlen(file_name), 0);
        clock_t begin = clock();

        if (strcmp(file_name,"QUIT") == 0)
        	break;
        read(sock, &size_file, sizeof(int));

        if (size_file == -1) {
            printf("File is not exist.\n");
            continue;
        }
        else if (size_file == 0)
            printf("File is empty");
        else
            printf("Downloading ...\n");

        file = fopen(file_name, "wb");

        while (size_file > 0) {
            if (size_file >= SIZE_BUFFER) {
                read(sock, buffer, SIZE_BUFFER);
                fwrite(buffer, SIZE_BUFFER, 1, file);
            }
            else {
                read(sock, buffer, size_file);
                fwrite(buffer, size_file, 1, file);
            }
            size_file = size_file - SIZE_BUFFER;
        }
        clock_t end = clock();
        double time_spent = (double)(end - begin);
        printf("time download: %f\n",time_spent);
        printf("DONE!\n");
        fclose(file);
    }
    close(sock);
    return 0;
}