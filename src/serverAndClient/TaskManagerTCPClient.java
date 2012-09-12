package serverAndClient;

import java.io.*;
import java.net.*;

import xml.Task;

public class TaskManagerTCPClient {
	int serverPortObject = 7890;
	int serverPortText = 7880;
	InetAddress serverAddress;
	
	
	public TaskManagerTCPClient(){
		int counter = 0;
		while(true){
			getTask("Attendant1");
			if(counter++ > 20) break;
		}
		
	}
	
	public Task[] getTask(String userID)
	{
		Task[] receivedTasks = new Task[0];
		try {
			serverAddress = InetAddress.getByName("localhost");
			Socket socketText = new Socket(serverAddress, serverPortText);

			//**********
            //Incoming stream
            //**********
            
            //Input for data (text)
            BufferedInputStream bufInData = new BufferedInputStream(socketText.getInputStream());
            DataInputStream textIn = new DataInputStream(bufInData);
            
            //Output for data (text)
            BufferedOutputStream bufOutData = new BufferedOutputStream(socketText.getOutputStream());
            DataOutputStream textOut = new DataOutputStream(bufOutData);
            
            String send = "get";
			
            textOut.writeUTF(send);
            textOut.flush();
            
            String response = textIn.readUTF();
            System.out.println("Message from Server: " + response);
            
            textOut.writeUTF(userID);
            textOut.flush();
           
            //Will now receive a object - get ready for this...
            ServerSocket serverSocketObject = new ServerSocket(serverPortObject);
            Socket socketObject = serverSocketObject.accept();
        	
        	
        	System.out.println("inetAddress in get: " + socketText.getInetAddress() + " - " + serverPortObject);
			
            //**********
            //Object streams
            //**********
            
        	
        	
            //Input for objects (non-text)
            BufferedInputStream bufInObject = new BufferedInputStream(socketObject.getInputStream());
            ObjectInputStream objectIn = new ObjectInputStream(bufInObject);
            
            
            //Output for objects (non-text)
//            BufferedOutputStream bufOutObject = new BufferedOutputStream(socketObject.getOutputStream());
//            ObjectOutputStream objectOut = new ObjectOutputStream(bufOutObject);
            
            //**********
           
            receivedTasks = (Task[]) objectIn.readObject();
            
            System.out.println("Object received!");
            
            //Close everything
            textIn.close();
            textOut.close();
            
            objectIn.close();
//            objectOut.close();
            
            socketObject.close();
            socketText.close();
            
            serverSocketObject.close();
            
            
		} catch (IOException ioe){
		
			ioe.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return receivedTasks;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskManagerTCPClient client = new TaskManagerTCPClient();		
	}

}
