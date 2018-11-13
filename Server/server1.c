//#include <signal.h>
#include <stdio.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <sys/wait.h> 
#include <pthread.h>
#include <sys/select.h>
//


const int PORT = 1997;
const int DEFAULT_SIZE = 1024;
const int DEFAULT_LENGTH = 20;
const char* LOCAL_HOST =  "127.0.0.1";
const char* MESSAGE = "Hello ,server saw your request";

int fileCounter = 0;
int clientIder = 0;
int k,j;
pthread_mutex_t mutexLock;
const char *dictionary[10][2];

// void * handleThread( void *);
int handleThread(void *);

int sendFile(char* fileName, int socket)
{
    int size = 0, maxTransUnit = 1240;
    char segment[1240] = {0};

    int totalSize = 0;
    FILE *file = fopen(fileName, "r");
    if(file == NULL) {
    char cwd[100];   
            
            if (getcwd(cwd, sizeof(cwd)) != NULL) {
                //printf("Current working dir: %s\n", cwd);
             } else {
                 perror("getcwd() error");
                return 0;
   }
            //perror(" fopen ");
			printf(" \t \t \t File not found (%ld)  : %s !! \n \n \n",sizeof(fileName)/sizeof(char),fileName);
			totalSize = 0;
			write(socket, &totalSize, sizeof(totalSize));
            return 0;
            
    } else {
            fseek(file, 0L, SEEK_END);
            totalSize = ftell(file);
            write(socket, &totalSize, sizeof(totalSize));
            fclose(file);
            if (totalSize > 0)
            {
                file = fopen(fileName, "r");
                while (sizeof(segment) <= maxTransUnit)
                {
                    maxTransUnit = fread(segment, 1, 1240, file);
                    segment[maxTransUnit] = 0;
                    write(socket, segment, maxTransUnit);
                    size += maxTransUnit;
                }
                printf("\t \t \t Sent %s  ! \n   ",fileName); 
                
               
                fclose(file);
                return 1;
            }
        }
    return 1;
}



int main(int argc, char *argv[]){
    int welcomeSocket, portNumber ;
    char buffer[DEFAULT_SIZE];
    struct sockaddr_in serverAddr;
    struct sockaddr_in clientAddr;
	socklen_t cli_addr_size;
    int* acceptedSocket;

    // Multi thread server 
    pthread_t threadId;

    if(argc < 2)
    {
        fprintf(stderr, "No port provided\n");
        exit(1);
    }
        
    welcomeSocket = socket(AF_INET, SOCK_STREAM, 0);
    ///// enable  socket options 
    int enable = 1;
    if (setsockopt(welcomeSocket, SOL_SOCKET, SO_REUSEADDR, (const char*)&enable, sizeof(int)) < 0)
    	perror("setsockopt(SO_REUSEADDR) failed");
    if (setsockopt(welcomeSocket, SOL_SOCKET, SO_REUSEPORT, (const char*)&enable, sizeof(int)) < 0) 
        perror("setsockopt(SO_REUSEPORT) failed");

    if(welcomeSocket < 0)
    {
        perror("Error opening socket");
    }
	// activate server address and port
    portNumber = atoi(argv[1]);
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(portNumber);
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);  
    cli_addr_size = sizeof(clientAddr);
    bind(welcomeSocket, (struct sockaddr *) &serverAddr, sizeof(serverAddr));
	//puts(serverAddr.sin_port);
    if(listen(welcomeSocket,5)==0)
        printf("Server Running \n Listening on port %d...  \n",portNumber);
    else
        printf("Error when listening\n");

    // initiate mutex lock    
    if (pthread_mutex_init(&mutexLock, NULL) != 0) 
    { 
        printf("\n mutex init has failed\n"); 
        return 1; 
    } 
     
    //signal(SIGCHLD, SIG_IGN);
    fd_set rfds; // 
    /* read file descriptor set */
    fd_set afds;
    /* active file descriptor set */
    int fd;
    FD_ZERO (&afds);
    FD_SET (welcomeSocket, &afds);
    // file desciptor
    /* accepting new connections*/
    while(1){
        memcpy(&rfds, &afds, sizeof(rfds));
        // set select value 
        if ( select (FD_SETSIZE, &rfds, (fd_set *) 0,(fd_set *) 0, (struct timeval *) 0) < 0)
             printf("select: %s\n", strerror(errno));
        // handle welcome socket and new client-connected socket    
        if ( FD_ISSET (welcomeSocket, &rfds)) {
            int ssock;
            
            ssock = accept(welcomeSocket,(struct sockaddr *)&clientAddr,&cli_addr_size);
            if ( ssock < 0)
                printf ("accept: %s\n", strerror (errno));
            char ip[30];
            int port;
            strcpy(ip, (char *) inet_ntoa((struct in_addr)clientAddr.sin_addr));
            printf("New client connected ! Client IP is %s port %d \n \n", ip, (int)htons(clientAddr.sin_port));
            FD_SET (ssock, &afds);
        }
        // handle client-connected sockets // handle one request per loop
        for ( fd = 0; fd < FD_SETSIZE; ++fd)
            if (fd!= welcomeSocket && FD_ISSET(fd, &rfds))
                if (handleThread((void *)&fd) == 0 ) {
                    (void) close (fd);
                    FD_CLR (fd, &afds);
                }
            

        /*
        acceptedSocket = malloc(sizeof(int));
        *acceptedSocket = accept(welcomeSocket, (struct sockaddr *) &clientAddr, &cli_addr_size);
        if (errno == EINTR) continue;
            else;// perror("accept error");
        pthread_create(&threadId, NULL ,&handleThread,(void *)acceptedSocket);
        */
        
        
    }

    return 0;
}

int handleThread(void *socketInfo){
   

    int filesSent = 0;
    int clientId = 0;
    // get socket id
    int socketId;
    //pthread_mutex_lock(&mutexLock);
    clientIder++;
        clientId = clientIder;
    //pthread_mutex_unlock(&mutexLock);
    socketId = *((int *)socketInfo);
    //pthread_detach(pthread_self());
    // get client info 
    socklen_t len;
    struct sockaddr_storage addr;
    char ipstr[30];
    int port;
    len = sizeof addr;
    getpeername(socketId, (struct sockaddr*)&addr, &len);
    struct sockaddr_in *s = (struct sockaddr_in *)&addr;
    port = ntohs(s->sin_port);
    inet_ntop(AF_INET, &s->sin_addr, ipstr, sizeof ipstr);
    printf(" ////// Client IP address: %s  port %d  REQUEST_ID %d \n ", ipstr,port,clientId );
    
   // printf("Thread created id %ld socket id %d \n \n \n ",pthread_self(),socketId);
    // start accepting tranfer request
    int nBytes  = 1, a = 1;
    char buffer[DEFAULT_SIZE];
    char file_name[DEFAULT_SIZE];
    //
    bzero(buffer,sizeof(buffer));
    nBytes = recv(socketId,buffer,1024,0);
            // printf("%d\n",nBytes);
    if( nBytes != 0 ){
        // {
        //     printf(" \t \t Client %d At %s port %d has closed it's connection \n \n \n",clientId,ipstr,port);
        //     return 0;
        // }
        // else {
                printf(" \t \t \t Processing request from client with request number : %d  ip : %s port : %d \n \n  ",clientId, ipstr,port);
                dictionary[clientId][0] = ipstr;
                dictionary[clientId][1] = buffer;
                for (k = 0; k < 10; ++k){
                    for(j = 0; j < 2; ++j){
                        if (dictionary[k][j] != NULL){
                            if (j!= 1){
                                printf("%s:   ",dictionary[k][j]);
                            }else{
                                printf("%s",dictionary[k][j]);
                            }
                        }
                    }
                    if (dictionary[k][j-1] != NULL){
                        printf("\n");
                    }
                }
    }
    printf("%s", "aa");

    while (1){
        bzero(file_name,sizeof(file_name));
        a = recv(socketId,file_name,1024,0);
        printf("%s",file_name);

        if( a == 0 ){
                printf(" \t \t Client %d At %s port %d has closed it's connection \n \n \n",clientId,ipstr,port);
                break;
            }
    }

    // if(strcmp(file_name,"QUIT") == 0){
    //     printf(" \t \t Client %d At %s port %d has closed it's connection \n \n \n",clientId,ipstr,port);
	// 	return 0;
    // }

    //close(socketId);
    return 5;
    
}