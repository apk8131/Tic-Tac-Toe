import java.io.*;
import java.net.*;

public class TCPClient
{
 public static void main(String argv[]) throws IOException
 {
  String sentence;
  String modifiedSentence;
  Socket clientSocket;
  
  while(true)
  {	  
	try {
		clientSocket = new Socket("localhost", 6789);
		break;
	 } catch (Exception e) {
		// TODO Auto-generated catch block
		
	 }
	}
  
  BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  sentence = inFromUser.readLine();
  outToServer.writeBytes(sentence + '\n');
  modifiedSentence = inFromServer.readLine();
  System.out.println("FROM SERVER: " + modifiedSentence);
 }
}