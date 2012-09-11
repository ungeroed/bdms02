package serverAndClient;

import java.io.*;
import java.net.*;

import xml.Task;

public class TaskManagerTCPClient {
	int serverPortObject = 7890;
	int serverPortText = 7880;
    
	
	public TaskManagerTCPClient(){
		getTask();
	}
	
	public void getTask()
	{
		try {
			
			//TODO Add a bufferStream
			
			InetAddress serverAddress = InetAddress.getByName("localhost");
			Socket socketObject = new Socket(serverAddress, serverPortObject);
			Socket socketText = new Socket(serverAddress, serverPortText);
//            socket.setReuseAddress(true);
            
			System.out.println("Before client streams");
			
			//**********
            //Incoming stream
            //**********
            
            //Input for data (text)
            BufferedInputStream bufInData = new BufferedInputStream(socketText.getInputStream());
            DataInputStream textIn = new DataInputStream(bufInData);
            
            //Output for data (text)
            BufferedOutputStream bufOutData = new BufferedOutputStream(socketText.getOutputStream());
            DataOutputStream textOut = new DataOutputStream(bufOutData);
            
            
            
            System.out.println("Client output streams");
            
            
            String send = "get";
			
            textOut.writeUTF(send);
            textOut.flush();
            
            String response = textIn.readUTF();
            
            
            System.out.println("Message from Server: " + response);
            
            String requireTask = "Attendant1";
            textOut.writeUTF(requireTask);
            textOut.flush();
           
            
            
            
            
            
            
            //**********
            //Object streams
            //**********
            
            //Input for objects (non-text)
            BufferedInputStream bufInObject = new BufferedInputStream(socketObject.getInputStream());
            ObjectInputStream objectIn = new ObjectInputStream(bufInObject);
            
            
            
            //Output for objects (non-text)
            BufferedOutputStream bufOutObject = new BufferedOutputStream(socketObject.getOutputStream());
            ObjectOutputStream objectOut = new ObjectOutputStream(bufOutObject);
            
            //**********
           
         //   System.out.println("After client streams");
            
	
             
            System.out.println("Task required - but not yet received.");
            
            
            
            Task[] receivedTasks = (Task[]) objectIn.readObject();
            System.out.println("Received " + receivedTasks.length + " tasks: " + receivedTasks);
            
//            socket.close();
            
            
		} catch (IOException ioe){
		
			ioe.printStackTrace();
			
		}
//		
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			int serverPort = 7890;						
//			InetAddress serverAddress = InetAddress.getByName("localhost");
//			Socket transmitterSocket = new Socket(serverAddress, serverPort);
//			transmitterSocket.setReuseAddress(true);
//						
//			OutputStream os = transmitterSocket.getOutputStream();
//			ObjectOutputStream dos = new ObjectOutputStream(os);
//						
//			InputStream is = transmitterSocket.getInputStream();
//			ObjectInputStream inStream = new ObjectInputStream(is);
//	
//			String command = "get";
//			dos.writeUTF(command);
//			dos.flush();
//			System.out.println("Client here: " + command + "transmitted");
//		
//			String ping = inStream.readUTF();
//			if (ping.equalsIgnoreCase("get")) {
//				System.out.println("Client here: ping received");			
//				dos.writeUTF("rao");
//				dos.flush();
//				Task task = (Task)inStream.readObject();
//			}
//			transmitterSocket.close();
//			
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskManagerTCPClient client = new TaskManagerTCPClient();		
	}

}
