import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ChatServer {
    
    private static PrintWriter remoteOut;
    private static BufferedReader remoteIn;
    private static Socket socket;
    private static ServerSocket serversocket;
    private static final Scanner sc = new Scanner(System.in);
    private static String clientname;
    public static void main(String[] args) {
        System.out.print("Enter username : ");
        String username = sc.next();
        System.out.println("Listening at port 1999 \n Waiting for client ... ");

        if (!connect(username)) return;
        //  Printing incoming messages
        Thread incomingMessagesHandlingThread = new Thread(() -> {
            try{
            clientname = remoteIn.readLine();
            remoteIn.readLine();
            } catch (IOException ignore){ 
                System.out.println("Disconnected From Server");
            }
            try {
                while (socket.isConnected()){
                    String temp =remoteIn.readLine();
                   if(temp != null) 
                       System.out.println("\n| "+clientname+ ": "+temp);
                else{
                    System.out.println("Disconnected From Server");
                    socket.close();
                    sc.close();
                    break;
                }
                }
            } catch (IOException ignore) {
                System.out.println("Disconnected From Server");
            }
        });

        incomingMessagesHandlingThread.start();

        // Sending Messages
        sc.nextLine(); //to ignore new line created while entering username
       // remoteOut.println(username);
        try {
            String message;
            while ((message = sc.nextLine()) != null && socket.isConnected()) {
                System.out.print("you: ");
                message = sc.nextLine();
                if (message.equals(""))
                break;
                remoteOut.println(message);
            }
            if (socket.isConnected()) socket.close();
        } catch (IOException ignore) {
        }
    }

    private static boolean connect(String serverName) {

        System.out.println("Connecting to localhost...");
        try {
            serversocket = new ServerSocket(1999);
            socket =serversocket.accept();
            remoteOut = new PrintWriter(socket.getOutputStream(), true);
            remoteIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected To Client, press enter without text to disconnect");
            remoteOut.println(serverName); // Sending Server Name as the first message
            return true;
        } catch (IOException e) {
            System.out.println("Connection To Server Failed");
            return false;
        }
    }
}
