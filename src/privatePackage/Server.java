package privatePackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import xml.Cal;
import xml.CalSerializer;
import xml.Task;

public class Server {

	int port = 60000;
	Cal cal;
    CalSerializer cs;
    
    public Server() throws IOException{
        cs = new CalSerializer();
        cal = cs.deserialize();
        serve();
	}
    
	private void serve() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			Socket clientSocket = serverSocket.accept();
			clientSocket.setReuseAddress(true);
			
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			String command = (String) in.readObject();
			out.writeObject(command);
			out.flush();
			
			if(command.equalsIgnoreCase("get")){
				String userID = (String) in.readObject();
				Task[] tasks= get(userID);
				out.writeObject(tasks);
				out.flush();				
			}
			if(command.equalsIgnoreCase("post")){
				Task task_post = (Task) in.readObject();
				String response_post = post(task_post);
				out.writeObject(response_post);
				out.flush();
			}
			if(command.equalsIgnoreCase("put")){
				Task task_put = (Task) in.readObject();
				String response_put = post(task_put);
				out.writeObject(response_put);
				out.flush();
			}
			if(command.equalsIgnoreCase("delete")){
				String taskID = (String) in.readObject();
				String response_delete = delete(taskID);
				out.writeObject(response_delete);
				out.flush();
			}
			out.close(); 
		    in.close(); 
		    clientSocket.close(); 
		    serverSocket.close(); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server server = new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
