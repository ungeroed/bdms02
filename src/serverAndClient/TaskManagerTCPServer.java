package serverAndClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import xml.Cal;
import xml.CalSerializer;
import xml.Task;

public class TaskManagerTCPServer {
	int serverPortObject = 7890;
	int serverPortText = 7880;
	
    Cal cal;
    CalSerializer cs;
    
	public static void main(String[] args) throws IOException{

		TaskManagerTCPServer server = new TaskManagerTCPServer();
	}

	public TaskManagerTCPServer() throws IOException{
        cs = new CalSerializer();
        cal = cs.deserialize();
        serve();
	}	
	
	public void serve() throws IOException{
		ServerSocket serverSocketText = new ServerSocket(serverPortText);
		serverSocketText.setReuseAddress(true);
		
	    System.out.println("Server started at:\nObjects: " + serverPortObject + "\nText: " + serverPortText);
		while(true){
			//Every new command will be of the type String
			Socket socketText = serverSocketText.accept();
		    System.out.println("A new client is connected...");
		    new HandleIncomingClient(socketText);
		}
	}
	
	private class HandleIncomingClient extends Thread{
		Socket socketText;

		//The address of the client
		InetAddress inetAddress;
		
	    DataInputStream textIn;
	    DataOutputStream textOut;
	    
		private HandleIncomingClient(Socket sText)  throws IOException{
			socketText = sText;
			socketText.setReuseAddress(true);
			inetAddress = sText.getInetAddress();
			
			//Input for data (text)
            BufferedInputStream bufInData = new BufferedInputStream(socketText.getInputStream());
            textIn = new DataInputStream(bufInData);
            
            //Output for data (text)
            BufferedOutputStream bufOutData = new BufferedOutputStream(socketText.getOutputStream());
            textOut = new DataOutputStream(bufOutData);
            
            //It's now ready to receive and send text
            this.start();
		}
		
		public void run() {
			try{
				String message =  textIn.readUTF(); // blocking call
	            System.out.println("Message - from Client: " + message);
	
	            textOut.writeUTF(message);
	            textOut.flush();
	
	            //Knows which kind of request to handle (get, put, delete,....)
	            if(message.equalsIgnoreCase("get")){
	            	
	            	String userID = textIn.readUTF();
	            	Task[] tasks= get(userID);
	
					//Output stream for objects (non-text)
					Socket socket_get = new Socket(inetAddress, serverPortObject);
		            ObjectOutputStream oos_get = new ObjectOutputStream(new BufferedOutputStream(socket_get.getOutputStream()));
		            
		            oos_get.writeObject(tasks);
				    oos_get.flush();
				    oos_get.close();
				}
				
				if(message.equalsIgnoreCase("post")){
		            
					//Set up to receive objects
					ServerSocket server_post = new ServerSocket(serverPortObject);
					Socket socket_post = server_post.accept();
		            ObjectInputStream ois_post = new ObjectInputStream(new BufferedInputStream(socket_post.getInputStream()));
	
					Task task = (Task) ois_post.readObject();
					String result = post(task);
					
					textOut.writeUTF(result);
					textOut.flush();
					
					ois_post.close();
				}
				
				if(message.equalsIgnoreCase("put")){
					//Set up to receive objects
					ServerSocket server_put = new ServerSocket(serverPortObject);
					Socket socket_put = server_put.accept();
		            ObjectInputStream ois_put = new ObjectInputStream(new BufferedInputStream(socket_put.getInputStream()));
	
					Task task = (Task) ois_put.readObject();
					String result = put(task);
					
					textOut.writeUTF(result);
					textOut.flush();
	                
					ois_put.close();
				}
				
				if(message.equalsIgnoreCase("delete")){
					String taskID = textIn.readUTF();
					String result = delete(taskID);
	                textOut.writeUTF(result);
	                textOut.flush();	                
				}
				
				textOut.close();
			    textIn.close();
			} catch(IOException ioe){				
				ioe.printStackTrace();				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
		}		
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
		for(Task task : cal.tasks){
			if(task.attendants.contains(userID))
				returnTasks.add(task);
		}
		
		return returnTasks.toArray(new Task[0]);
		
	}

    /**
     * Simple method to append a task to the list of current cal object
     * @param task
     * @return Success message (confidence wins)
     * @todo implement fault handling
     */
	private String post(Task task){
        cal.tasks.add(task);
        cs.serialize(cal);
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
                cs.serialize(cal);
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
                cs.serialize(cal);
            }
		
		if(response.isEmpty()) response = "No task with that id found";
		
        return response;
	}
}
