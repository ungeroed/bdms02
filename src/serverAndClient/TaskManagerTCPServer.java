package serverAndClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import xml.*;

public class TaskManagerTCPServer {
	int serverPortObject = 7890;
	int serverPortText = 7880;
	ServerSocket listenSocket;
    Cal cal;
    CalSerializer cs;
    
	public static void main(String[] args) {

		TaskManagerTCPServer server = new TaskManagerTCPServer();
	}

	public TaskManagerTCPServer(){

        cs = new CalSerializer();

        cal = cs.deserialize();
        serve();
	}

	
	
	
	public void serve(){
		try {
           
            
            ServerSocket serverSocketObj = new ServerSocket(serverPortObject);
            ServerSocket serverSocketText = new ServerSocket(serverPortText);
//            serverSocketObj.setReuseAddress(true);
            System.out.println("Server started at:\nObjects: " + serverPortObject + "\nText: " + serverPortText);
            

            //Note the order!!!
            Socket socketText = serverSocketText.accept();
            Socket socketObject = serverSocketObj.accept();
            
            System.out.println("Before the server streams");
            
            
            //**********
            //Incoming stream
            //**********
            
            //Input for data (text)
            BufferedInputStream bufInData = new BufferedInputStream(socketText.getInputStream());
            DataInputStream textIn = new DataInputStream(bufInData);
            
            //Output for data (text)
            BufferedOutputStream bufOutData = new BufferedOutputStream(socketText.getOutputStream());
            DataOutputStream textOut = new DataOutputStream(bufOutData);
            
            
            String message =  textIn.readUTF(); // blocking call
            System.out.println("Message from Client: " + message);

            textOut.writeUTF(message);
            textOut.flush();

            
            
            
            System.out.println("Server output streams");
            
            //**********
            //Outgoing streams
            //**********
            
            
            
            //**********
            
            
                        
            Task t = new Task();
            
            System.out.println("Before if-statements");
            if(message.equalsIgnoreCase("get")){
            	String userID = textIn.readUTF();
				Task[] tasks= get(userID);

				//Output for objects (non-text)
	            BufferedOutputStream bufOutObject = new BufferedOutputStream(socketObject.getOutputStream());
	            ObjectOutputStream objectOut = new ObjectOutputStream(bufOutObject);
	            
	            //Input for objects (non-text)
//	            BufferedInputStream bufInObject = new BufferedInputStream(socketObject.getInputStream());
//	            ObjectInputStream objectIn = new ObjectInputStream(bufInObject);

				
				objectOut.writeObject(tasks);
			    objectOut.flush();
			}
			
			if(message.equalsIgnoreCase("post")){
	            //Output for objects (non-text)
	            BufferedOutputStream bufOutObject = new BufferedOutputStream(socketObject.getOutputStream());
	            ObjectOutputStream objectOut = new ObjectOutputStream(bufOutObject);
	            
	            //Input for objects (non-text)
	            BufferedInputStream bufInObject = new BufferedInputStream(socketObject.getInputStream());
	            ObjectInputStream objectIn = new ObjectInputStream(bufInObject);

				Task task = (Task) objectIn.readObject();
				String result = post(task);
				textOut.writeUTF(result);
				textOut.flush();
                cs.serialize(cal);
			}
			
			if(message.equalsIgnoreCase("put")){
	            //Output for objects (non-text)
	            BufferedOutputStream bufOutObject = new BufferedOutputStream(socketObject.getOutputStream());
	            ObjectOutputStream objectOut = new ObjectOutputStream(bufOutObject);
	            
	            //Input for objects (non-text)
	            BufferedInputStream bufInObject = new BufferedInputStream(socketObject.getInputStream());
	            ObjectInputStream objectIn = new ObjectInputStream(bufInObject);

				Task task = (Task) objectIn.readObject();
				String result = put(task);
				textOut.writeUTF(result);
				textOut.flush();
                cs.serialize(cal);
			}
			
			if(message.equalsIgnoreCase("delete")){
				String taskID = textIn.readUTF();
				String result = delete(taskID);
                textOut.writeUTF(result);
                textOut.flush();
                cs.serialize(cal);
			}
			System.out.println("Method is done");
//            socket.close();

		} catch (ClassNotFoundException e){
		
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			
		} catch (IOException ioe){
			
			System.out.println("IOException: ");
			ioe.printStackTrace();
			
		}
            
            
            
            
//            int serverPort = 7896;
//
//            ServerSocket serverSocket = new ServerSocket(serverPort);
//
//            System.out.println("Server started at :" + serverPort);
//            Socket socket = serverSocket.accept();
//
//            InputStream is = socket.getInputStream();
//            DataInputStream dis = new DataInputStream(is);
//
//            String message = dis.readUTF(); // blocking call
//            System.out.println("Message from Client: " + message);
//
//            
//            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//            outputStream.writeUTF(message);
//            outputStream.flush();
            

            
            
            
            
            
            
            
            
            
            
            
            
            
            


        		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		int serverPort = 7890;
//		ServerSocket listenSocket = null;;
//		Socket clientSocket = null;
//		try {			
//			listenSocket = new ServerSocket(serverPort);
//			listenSocket.setReuseAddress(true);
//			clientSocket = listenSocket.accept();
//			clientSocket.setReuseAddress(true);
//			InputStream is = clientSocket.getInputStream();
//			ObjectInputStream inStream = new ObjectInputStream(is);
//			String command = inStream.readUTF();
//			
//			System.out.println("Server here: " + command + "received");
//			
//			OutputStream os = clientSocket.getOutputStream();			
//			ObjectOutputStream outStream = new ObjectOutputStream(os);
//			outStream.writeUTF(command);
//						
//			if(command.equals("GET")){
//				String userID = inStream.readUTF();
//				Task[] tasks= get(userID);
//				outStream.writeObject(tasks);
//			}
//			
//			if(command.equals("POST")){
//				Task task = (Task)inStream.readObject();
//				String result = post(task);
//				outStream.writeUTF(result);
//                cs.serialize(cal);
//			}
//			
//			if(command.equals("PUT")){
//				Task task = (Task)inStream.readObject();
//				String result = put(task);
//				outStream.writeUTF(result);
//                cs.serialize(cal);
//			}
//			
//			if(command.equals("DELETE")){
//				String taskID = inStream.readUTF();
//				String result = delete(taskID);
//                outStream.writeUTF(result);
//                cs.serialize(cal);
//			}
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				listenSocket.close();
//				clientSocket.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
		
	}

    /**
     * Method to get all the tasks belonging to the specified user id
     * do this by iterating the tasks in the current cal object.
     * Add all the task with the user in the attendants list
     * @param userID to search for
     * @return the list of tasks (might be size 0)
     */
	private Task[] get(String userID){
		ArrayList<Task> returnTasks = new ArrayList<Task>();
		System.out.println("Length of cal.tasks: " + cal.tasks.size());
		System.out.println("userID: " + userID);
		for(Task task : cal.tasks){
			if(task.attendants.contains(userID))
				returnTasks.add(task);
		}
		System.out.println("Length of returnTasks: " + returnTasks.size());
		return returnTasks.toArray(new Task[0]);
		
	}

    /**
     * Simple method to append a task to the list of current cal object
     * @param task
     * @return Succes message (confidence wins)
     * @todo implement fault handling
     */
	private String post(Task task){

        cal.tasks.add(task);
		return "Task inserted.";
	}

    /**
     * Method to update the current task list with the provided task
     * Do this by iterating the list until the task with the right id is found
     * Then simply switch the tasks
     * @param task the new task to insert (it aso specifies the id to search for)
     * @return response of how well we did
     */
	private String put(Task task){
        String response = "";

        for(int i=0; i < cal.tasks.size(); i++)
            if(cal.tasks.get(i).id.equals(task.id)){
                /* out with the old, in with the new */
                cal.tasks.remove(i);
                cal.tasks.add(task);
                response = "Task updated!";
            }

        if(response.isEmpty()) response = "No task with that id found";

        return "Task updated.";
	}

    /**
     * Function to remove the task with the specified id
     * do this by iterate through the current list, if the specified id is found remove the task from list
     * @param id
     * @return Human readable response
     */
	private String delete(String id){
		String response = "";
        for(int i=0; i < cal.tasks.size(); i++)
            if(cal.tasks.get(i).id.equals(id)){
                response = "Task deleted!";
                cal.tasks.remove(i);
            }
		
		if(response.isEmpty()) response = "No task with that id found";


        return response;
	}
}
