package serverAndClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import xml.*;

public class TaskManagerTCPServer {
	int serverPort = 7890;
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
			listenSocket = new ServerSocket(serverPort);
			Socket clientSocket = listenSocket.accept();
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream inStream = new ObjectInputStream(is);
			String command = inStream.readUTF();
			
			OutputStream os = clientSocket.getOutputStream();			
			ObjectOutputStream outStream = new ObjectOutputStream(os);
			outStream.writeUTF(command);
						
			if(command.equals("GET")){
				String userID = inStream.readUTF();
				Task[] tasks= get(userID);
				outStream.writeObject(tasks);
			}
			
			if(command.equals("POST")){
				Task task = (Task)inStream.readObject();
				String result = post(task);
				outStream.writeUTF(result);
                cs.serialize(cal);
			}
			
			if(command.equals("PUT")){
				Task task = (Task)inStream.readObject();
				String result = put(task);
				outStream.writeUTF(result);
                cs.serialize(cal);
			}
			
			if(command.equals("DELETE")){
				String taskID = inStream.readUTF();
				String result = delete(taskID);
                outStream.writeUTF(result);
                cs.serialize(cal);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Task[] get(String userID){
		ArrayList<Task> returnTasks = new ArrayList<Task>();
		
		for(Task task : cal.tasks){
			if(task.attendants.contains(userID))
				returnTasks.add(task);
		}
		
		return returnTasks.toArray(new Task[0]);
		
	}
	
	private String post(Task task){
		
		// read and write xml
		
		return "Task inserted.";
	}
	
	private String put(Task task){
		

		
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
