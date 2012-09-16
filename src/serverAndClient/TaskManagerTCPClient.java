package serverAndClient;

import java.io.*;
import java.net.*;

import xml.Task;

public class TaskManagerTCPClient {

	int serverPort = 7890;

	InetAddress serverAddress;
    Socket socket;

    ObjectInputStream in;
    ObjectOutputStream out;


    public TaskManagerTCPClient(String host) throws IOException {
        serverAddress = InetAddress.getByName(host);
	}

    public void open() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        socket.setReuseAddress(true);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // ObjectOutputStream has a block constructor. This fixes it. #java-io-api-hacks

        in = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        socket.close();
        out.close();
        in.close();
    }

    public void reset() throws IOException {
        close();
        open();
    }

	
	public Task[] get(String userID) throws IOException, ClassNotFoundException {

        out.writeObject("GET");

        out.writeObject(userID);

        out.flush();

        String responseProtocol = in.readObject().toString();

        Task[] receivedTasks = (Task[]) in.readObject();

		return receivedTasks;
	}	

	private String post(Task task) throws IOException, ClassNotFoundException {

        out.writeObject("POST");

        out.writeObject(task);

        out.flush();

        String responseProtocol = in.readObject().toString();

        String response = in.readObject().toString();

        return response;
	}
	
	private String put(Task task) throws IOException, ClassNotFoundException {

        out.writeObject("PUT");

        out.writeObject(task);

        out.flush();

        String responseProtocol = in.readObject().toString();

        String response = in.readObject().toString();

        return response;
	}
	
	private String delete(String taskID) throws IOException, ClassNotFoundException {

        out.writeObject("DELETE");

        out.writeObject(taskID);

        out.flush();

        String responseProtocol = in.readObject().toString();

        String response = in.readObject().toString();

        return response;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)  {
        try {
            TaskManagerTCPClient client = new TaskManagerTCPClient("localhost");

            client.open();

            Task task1 = new Task("1", "Do MDS Mandatory Exercise 1", "17-9-2012", "done");
            task1.attendants.add("eeng");
            task1.attendants.add("lynd");
            task1.attendants.add("mrof");
            task1.attendants.add("cstp");

            System.out.println("Response to post: "+client.post(task1));

            client.reset();

            System.out.println("Response to get: ");

            Task[] tasks = client.get("eeng");
            
            for(Task task : tasks){
                //xml.CalSerializer.PrintTaskObject(task);
            	System.out.println("Task: " + task.name);
            	System.out.println("ID: " + task.id);
            	System.out.println("status: " + task.status);
            	
            	System.out.println("Attendants:");
            	for(String s : task.attendants)
            		System.out.println(" - " + s);
            }

            client.reset();          
            
            Task task2 = new Task("2412", "Christmas", "24/12/2014", "not executed");
            task2.attendants.add("santa");
            client.post(task2);            
            client.reset();
            System.out.println("Response to delete: "+client.delete("2412"));
            
            client.reset();

            Task task3 = new Task("tch-01", "teach lect 01", "8/11/2002", "not executed");
            task3.attendants.add("hilde");
            client.post(task3);         
            client.reset();            
            Task task4 = new Task("tch-01", "teach lect 01", "8/11/2002", "executed");
            task4.attendants.add("hilde");
            System.out.println("Response to put: "+client.put(task4));

            client.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

	}
}
