package privatePackage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import xml.Task;

public class Client {

	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;			

	public Client()
	{
		try {
			int port = 60000;
			InetAddress serverAddress;
			serverAddress = InetAddress.getByName("localhost");
			socket = new Socket(serverAddress, port);
			socket.setReuseAddress(true);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());			

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	public Task[] get(String userID)
	{
		Task[] tasks = new Task[0];
		try {
			String command = "get";
			out.writeObject(command);
			out.flush();
			
			String ping = (String) in.readObject();
			
			if(ping.equalsIgnoreCase(command))
			{
				out.writeObject(userID);
				out.flush();
				
				tasks = (Task[]) in.readObject();
			}			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}

	public String post(Task task)
	{
		StringBuilder response = new StringBuilder("");
		try {
		String command = "post";
		out.writeObject(command);		
		out.flush();
		
		String ping = (String) in.readObject();
		
		if(ping.equalsIgnoreCase(command))
		{
			out.writeObject(task);
			out.flush();
			
			response.append((String) in.readObject());
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}

	public String put(Task task)
	{
		StringBuilder response = new StringBuilder("");
		try {
		String command = "put";
		out.writeObject(command);		
		out.flush();
		
		String ping = (String) in.readObject();
		
		if(ping.equalsIgnoreCase(command))
		{
			out.writeObject(task);
			out.flush();
			
			response.append((String) in.readObject());
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}

	public String delete(String taskID)
	{

		StringBuilder response = new StringBuilder("");
		try {
		String command = "delete";
		out.writeObject(command);		
		out.flush();
		
		String ping = (String) in.readObject();
		
		if(ping.equalsIgnoreCase(command))
		{
			out.writeObject(taskID);
			out.flush();
			
			response.append((String) in.readObject());
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}
	
	private void close() {
		try {
			socket.close();
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client();

		// test get
		System.out.println("***********");
		System.out.println("Testing GET");
		System.out.println("***********");
		Task[] tasks = client.get("Attendant1");
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
		
		client.close();
	}
}
