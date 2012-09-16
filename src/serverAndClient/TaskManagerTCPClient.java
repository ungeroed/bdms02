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
        out.flush();

        out.writeObject(userID);
        out.flush();

        //String responseProtocol = in.readObject().toString();

        Task[] receivedTasks = (Task[]) in.readObject();

		return receivedTasks;
	}	

	private String post(Task task) throws IOException, ClassNotFoundException {

        out.writeObject("POST");
        out.flush();

        out.writeObject(task);
        out.flush();

        //String responseProtocol = in.readObject().toString();

        String response = in.readObject().toString();

        return response;

	}
	
	private String put(Task task) throws IOException, ClassNotFoundException {

        out.writeObject("PUT");
        out.flush();
        
        out.writeObject(task);
        out.flush();

        //String responseProtocol = in.readObject().toString();

        String response = in.readObject().toString();

        return response;
	}
	
	private String delete(String taskID) throws IOException, ClassNotFoundException {

        out.writeObject("DELETE");
        out.flush();

        out.writeObject(taskID);
        out.flush();

        //String responseProtocol = in.readObject().toString();

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

            Task task = new Task("1", "Do MDS Mandatory Exercise 1", "17-9-2012", "done");

            task.attendants.add("eeng");
            task.attendants.add("lynd");
            task.attendants.add("mrof");
            task.attendants.add("cstp");

            System.out.println("Response to post: "+client.post(task));

            client.reset();

            /*
            System.out.println("Response to get: ");

            Task[] tasks = client.get("Attendant3");

            for(Task task : tasks){
                xml.CalSerializer.PrintTaskObject(task);
            }

            client.reset();


            System.out.println("Response to delete: "+client.delete("1"));

            client.reset();

            System.out.println("Response to put: "+client.put(new Task()));

            client.close();
            */
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

		//client.post(new Task());
		/*
		// test get
		System.out.println("***********");
		System.out.println("Testing GET");
		System.out.println("***********");
		Task[] tasks = client.getTasks("Attendant1");
		for(Task task : tasks){
			// print name, date, status, description
			System.out.println("Task: " + task.name);
			System.out.println("id: " + task.id);
			System.out.println("date: " + task.date);
			System.out.println("status: " + task.status);
			System.out.println("" + task.description);
		}		
		System.out.println();		
		
		// test post
		System.out.println("***********");
		System.out.println("Testing POST");
		System.out.println("***********");
		Task task1 = new Task();
    	task1.id = "tch-01";
    	task1.name = "teach lect 01";
    	task1.date = "8/11/2002";
    	task1.status = "not-executed";
    	task1.description = "Teaching on Distributed Systems and System Model";
    	task1.attendants = new ArrayList<String>();
    	task1.attendants.add("hilde");		
		String response_post1 = client.post(task1);
		System.out.println(response_post1);
		/*
		Task task2 = new Task();
    	task2.id = "ex-01";
    	task2.name = "exercise 01";
    	task2.date = "9/05/2014";
    	task2.status = "executed";
    	task2.description = "Do MDS Mandatory Exercise 1";
    	task2.attendants = new ArrayList<String>();
    	task2.attendants.add("eeng");	
    	task2.attendants.add("lynd");	
    	task2.attendants.add("cstp");	
    	task2.attendants.add("mrof");	
		String response_post2 = client.post(task2);
		System.out.println(response_post2);
		
		Task task3 = new Task();
    	task3.id = "2412";
    	task3.name = "Christmas";
    	task3.date = "24/12/2014";
    	task3.status = "not-executed";
    	task3.description = "Deliver to families with chimney";
    	task3.attendants = new ArrayList<String>();
    	task3.attendants.add("santa");    		
		String response_post3 = client.post(task3);
		System.out.println(response_post3);
	
		// test put
		System.out.println("***********");
		System.out.println("Testing PUT");
		System.out.println("***********");
		Task task4 = new Task();
    	task4.id = "tch-01";
    	task4.name = "teach lect 01";
    	task4.date = "8/11/2002";
    	task4.status = "executed";
    	task4.description = "Teaching on Distributed Systems and System Model";
    	task4.attendants = new ArrayList<String>();
    	task4.attendants.add("hilde");		
		String response_put = client.put(task4);
		System.out.println(response_put);
		
		// test delete
		System.out.println("**************");
		System.out.println("Testing DELETE");
		System.out.println("**************");
		String response_delete = client.delete("2412");
		System.out.println(response_delete);
		*/

	}
}
