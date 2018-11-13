
// Includes
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>
 #include <time.h>
 #include <dirent.h>
#include <unistd.h>


// Constant
const int PORT = 1997;
const int DEFAULT_SIZE = 1024;
const int DEFAULT_LENGTH = 20;
const char* LOCAL_HOST =  "127.0.0.1";
const char* CAT_STRING = "received_";

// Main that take agurment from command line window 
void  receiveFile(char* fileName, int socket){
	clock_t time = 0;
	int maxTransUnit = 1240;
	int size = 0, totalSize = 0;
    char segment[1240] = {0};
	char str[80];
	fileName[0] = 'R';
	
    read(socket, &totalSize, sizeof(totalSize));
    if(totalSize <= 0) {
        printf("Server response with file size = 0 \n");
    } else {
		
        FILE *file = fopen(fileName, "w");
		time = clock();
		printf(" File %s is opened for writing %d bytes \n",fileName,totalSize);
        while(sizeof(segment) <= maxTransUnit) {
            maxTransUnit = read(socket, segment, sizeof(segment));
            segment[maxTransUnit] = 0;
            fwrite(segment, 1, maxTransUnit, file);
            size += maxTransUnit;
        }
		time  = clock() - time;
		double time_taken = ((double)time)/CLOCKS_PER_SEC;
        printf("Received %d bytes in %lf seconds \n\n\n\n",size, time_taken);
        fclose(file);
    }
}


int main(int argc, char *argv[]){
	int clientSocket;
	int portNumber;
	char serverName[DEFAULT_LENGTH];
	char clientBuffer[DEFAULT_SIZE];
	char selection[DEFAULT_SIZE];
	struct sockaddr_in serverAddr;
	struct hostent * server;
	socklen_t addr_size;
	
	// check usage 
	if(argc < 2 ) {
		
		fprintf(stderr,"usage %s hostname port \n", argv[0]);
		exit(1);
	
	}
	strcpy(serverName,argv[1]);
	//serverName = argv[1];
	portNumber = atoi(argv[2]);
	puts(serverName);
	//printf("Connecting to Server :  %s \n", server);
	printf("On port %d \n", portNumber); 
	
	
	//fgets(selection, DEFAULT_LENGTH, stdin);
	//CONNECT TO SERVER ON 
	clientSocket = socket(AF_INET, SOCK_STREAM, 0 );
	int sendbuff = 1000;
	//printf(" Enter buffer size\n");
	//fflush(stdin);
	//scanf("%d",&sendbuff);
 	printf("sets the send buffer to %d\n", sendbuff);
 	int hh = setsockopt(clientSocket, SOL_SOCKET, SO_SNDBUF, &sendbuff, sizeof(sendbuff));
	
 	 //printf("Nice %d \n",hh);
	if(clientBuffer < 0 ) {
		perror(" Error creating socket ");
		}
	serverAddr.sin_family = AF_INET;
	serverAddr.sin_port = htons(portNumber);
	serverAddr.sin_addr.s_addr = inet_addr(LOCAL_HOST);
	addr_size = sizeof(serverAddr);


	struct dirent *de; // Pointer for directory entry 

	// opendir() returns a pointer of DIR type. 
	DIR *dr = opendir("./share"); 

	if (dr == NULL) // opendir returns NULL if couldn't open directory 
	{ 
		printf("Could not open current directory" ); 
		return 0; 
	}


	int connectStatus = connect (clientSocket, (struct sockaddr *) &serverAddr, addr_size);
	if(connectStatus == -1){
		printf("Connect Failed \n");
		
	}
	else 
	{
		printf("Connected \n ");
		char filename[100];
		while ((de = readdir(dr)) != NULL){
			if (de->d_type != 4){
				if (strlen(filename) == 0){
					strcpy(filename, de->d_name);
					strcat(filename, "_");
				}else{
					strcat(filename, de->d_name);
					strcat(filename, "_");
				}
			}
		}
		closedir(dr);
		filename[strlen(filename) - 1] = '\0';
		send(clientSocket,filename,DEFAULT_SIZE,0);
		
		while(1){
	
			
			printf("------Enter the file name to download : \n" ) ;
			fflush(stdin);
			
			scanf("%s",&selection);
			if('\n' == selection[strlen(selection) - 1]) //remove \n
					selection[strlen(selection) - 1] = '\0';
			
			if(strcmp(selection,"QUIT") == 0)
			break;
			printf(" You entered :  %s \n",selection);
			
			int sentBytes = send(clientSocket,selection,DEFAULT_SIZE,0);
			
			printf(" Sent ! Waiting for reponse \n");
			/*
			int receivedBytes = recv(clientSocket, selection, DEFAULT_SIZE, 0);
			printf("Server Responded :  %s \n \n \n " , selection);*/
			// receiveFile(selection,clientSocket);
		
		
		}
	}
	
	

	//puts(selection);
	//printf("%d Result",strcmp(selection,"QUIT"));

	
	close(clientSocket);
	printf(" Quiting program.. \n " ) ;
	 //
	
	
	return 0;
	
}
	

