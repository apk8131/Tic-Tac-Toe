import java.io.*;
import java.net.*;

public class TCPServer
{
   public static void main(String argv[]) throws Exception
      {
         String clientSentence;
         String capitalizedSentence;
         ServerSocket welcomeSocket = new ServerSocket(6790);

         while(true)
         {
            Socket connectionSocket = welcomeSocket.accept();
            OutputStream o =  connectionSocket.getOutputStream();
            InputStream i = connectionSocket.getInputStream();
            //DataOutputStream dos = new DataOutputStream(o);
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(i));
                 DataOutputStream outToClient = new DataOutputStream(o);
                
                 byte data [] = new byte[1024];
                 data = "my test \n ooo noo".getBytes();
                 o.write(data);
                 clientSentence = inFromClient.readLine();
                 outToClient.writeBytes("capitalizedSentence");
                 
                 
                 /*System.out.println("Received: " + clientSentence);
                 capitalizedSentence = clientSentence.toUpperCase() + '\n';
                  
            
            
            
            
            i.read(data);
  
            if(data.toString().contains("yes")) {
            	System.out.println("got it");
            }
            
            
            data = new byte [1024];
            i.read(data);
            
            System.out.println(new String(data));
            */
         }
      }
}