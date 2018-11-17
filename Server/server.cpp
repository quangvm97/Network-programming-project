// Server side C/C++ program to demonstrate Socket programming
// Created by QuangVM

#include <unistd.h>
#include <stdio.h>
#include <iostream>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string>
#include <ctype.h>
#include <stdbool.h>
#include <vector>
#include <cstring>
#include <arpa/inet.h>
#include <jsoncpp/json/json.h>

//#define _BSD_SOURCE
#define PORT 8080
#define SIZE_BUFFER 1024

using namespace std;
class Client
{
public:
    int id;
    std::string ip;
    int port;
};

class File
{
public:
    std::string name;
    int client_id;
};

vector<Client> clients;
vector<File> files;
void register_client(string ip, int port, int id)
{
    Client new_client;
    new_client.ip = ip;
    new_client.port = port;
    new_client.id = id;
    clients.push_back(new_client);
}

void convert(char* buffer, int id)
{
    char str[4000];
    memset(&str, '\0', sizeof(str));
    strcpy(str, buffer);
    memset(&buffer, '\0', sizeof(buffer));
    char* p = strtok(str, " ");
    // get name file
    while (p)
    {
        string tmp = p;
        File new_file;
        new_file.name = tmp;
        new_file.client_id = id;
        p = strtok(NULL, " ");
        files.push_back(new_file);
        // cout << files.size();
    }
}

int main(int argc, char const* argv[])
{
    int num_file = 0;
    int server_fd, new_socket, valread, size_file;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[SIZE_BUFFER] = { 0 };
    char s;
    bool is_conn = false;
    fd_set rfds, afds;
    int connfd;
    int id = 0;
    char* status = "1";
    char hello = '1';

    // Creating socket file descriptor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0)
    {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // Forcefully attaching socket to the port 8080
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt)))
    {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // Forcefully attaching socket to the port 8080
    if (bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0)
    {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }

    if (listen(server_fd, 3) < 0)
    {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    printf("\nlistening...\n");
    // FD_ZERO(&afds);
    // FD_SET(server_fd, &afds);


    if ((new_socket = accept(server_fd, (struct sockaddr*)&address, (socklen_t*)&addrlen)) < 0)
    {
        perror("accept");
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in* sock_client = (struct sockaddr_in*)&address;

    char str_cli_ip[INET_ADDRSTRLEN];
    char str_cli_port[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sock_client->sin_addr, str_cli_ip, INET_ADDRSTRLEN);
    printf("\nclient ip: %s", str_cli_ip);
    printf("\nclient port: %d\n", ntohs(sock_client->sin_port));
    fflush(stdout);
    // FD_SET(new_socket, &afds);
    register_client(str_cli_ip, ntohs(sock_client->sin_port), id);
    id++;
    send(new_socket, status, strlen(status), 0);
    // Gui thong bao ok
    fflush(stdout);
    // Nhan danh sach file tu client
    memset(&buffer, '\0', sizeof(buffer));
    valread = read(new_socket, buffer, 1024);
    // Neu co tra ve danh sach file thi se luu vao
    if (strcmp(buffer, "") != 0)
    {
        convert(buffer, id);
    }

    int count = files.size();
    cout << "Tong so file: " << count;
    cout << "Danh sach file la: ";
    for (int i = 0; i < count; i++)
    {
        cout << (files.at(i).name) << " ";
    }
    Json::Value ch;
    ch[0]["ip"] = "127.0.0.1";
    ch[0]["port"] = 12;
    ch[1]["ip"] = "127.0.0.2";
    ch[1]["port"] = 134;

    Json::Value val;
    val["list"] = ch;
    Json::FastWriter fast;
    string sFast = fast.write(val);
    send(new_socket, sFast.data(), sFast.size(), 0);
    cout<<sFast;

    // cout << clients.size();
    fflush(stdout);


    return 0;
}