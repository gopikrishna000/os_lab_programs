
#include <iostream>
#include <unistd.h>
#include <stdio.h>

using namespace std;

int main(int argc, char const *argv[])
{
    int pipeChild[2];
    int pipeParent[2];

    char childMsg[1024], parentMsg[1024], childRecMsg[1024], parentRecMsg[1024];
    cout << "Enter the message to send to child : ";
    cin >> parentMsg;

    if (pipe(pipeChild) < -1)
    {
        perror("child Pipe creation error : ");
    }

    if (pipe(pipeParent) < -1)
    {
        perror("child Pipe creation error : ");
    }

    int pid = fork();

    if (pid == 0)
    { //child process
        close(pipeChild[0]);
        close(pipeParent[1]);
        read(pipeParent[0], childRecMsg, sizeof(childRecMsg));
        printf("Reading message from Parent process :  %s\n", childRecMsg);
        cout << "Enter Message to send to parent :  ";
        cin >> childMsg;
        printf("Writing message to Parent process :  %s\n", childMsg);
        write(pipeChild[1], childMsg, sizeof(childMsg));
    }
    else
    { //Parent process
        close(pipeChild[1]);
        close(pipeParent[0]);
        printf("Sending message to child : %s\n", parentMsg);
        write(pipeParent[1], parentMsg, sizeof(parentMsg));
        read(pipeChild[0], parentRecMsg, sizeof(parentRecMsg));
        printf("Reading Message from child process : %s ", parentRecMsg);
    }

    return 0;
}
