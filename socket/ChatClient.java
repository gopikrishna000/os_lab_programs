import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient {

    private static PrintWriter remoteOut;
    private static BufferedReader remoteIn;
    private static Socket socket;
    private static final Scanner sc = new Scanner(System.in);
    private static String servername;
    public static void main(String[] args) {
        System.out.print("Enter client username : ");
        String username = sc.next();

        if (!connect(username)) return;

        //  Printing incoming messages
        Thread incomingMessagesHandlingThread = new Thread(() -> {
            try{
                servername = remoteIn.readLine();
                } catch (IOException ignore){
                    System.out.println("Disconnected From Server");
                }
    
            try {
                while (socket.isConnected())
                System.out.println("\n| "+servername+ ": "+remoteIn.readLine());
            } catch (IOException ignore) {
                System.out.println("Disconnected From Server");
            }
        });
        
        incomingMessagesHandlingThread.start();
              
        // Sending Messages
        sc.nextLine(); //to ignore new line created while entering username
        remoteOut.println(username);
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

    private static boolean connect(String clientName) {

        System.out.println("Connecting to localhost...");
        try {
            socket = new Socket("localhost", 1999);
            remoteOut = new PrintWriter(socket.getOutputStream(), true);
            remoteIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected To Server, press enter without text to disconnect");
            remoteOut.println(clientName); // Sending Client Name as the first message
            return true;
        } catch (IOException e) {
            System.out.println("Connection To Server Failed");
            return false;
        }
    }

}

