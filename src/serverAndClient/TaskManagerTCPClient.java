package serverAndClient;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import xml.Task;

public class TaskManagerTCPClient {
	int serverPortObject = 7890;
	int serverPortText = 7880;
	InetAddress serverAddress;	
	
	public TaskManagerTCPClient(){
		
	}
	
	public Task[] getTasks(String userID)
	{
		Task[] receivedTasks = new Task[0];
		try {
			serverAddress = InetAddress.getByName("localhost");
			Socket socketText = new Socket(serverAddress, serverPortText);
			socketText.setReuseAddress(true);
			
			//**********
            //Incoming stream
            //**********
            
            //Input for data (text)
            DataInputStream textIn = new DataInputStream(new BufferedInputStream(socketText.getInputStream()));
            
            //Output for data (text)
            DataOutputStream textOut = new DataOutputStream(new BufferedOutputStream(socketText.getOutputStream()));
            
            String send = "get";
			
            textOut.writeUTF(send);
            textOut.flush();
            
            String ping = textIn.readUTF();
            //System.out.println("Message from Server: " + response);
            
            textOut.writeUTF(userID);
            textOut.flush();
           
            //Will now receive a object - get ready for this...
            ServerSocket serverSocketObject = new ServerSocket(serverPortObject);
            Socket socketObject = serverSocketObject.accept();        	
        	
        	//System.out.println("inetAddress in get: " + socketText.getInetAddress() + " - " + serverPortObject);
			
            //**********
            //Object streams
            //**********
                   	
        	
            //Input for objects (non-text)
            ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(socketObject.getInputStream()));
            
            receivedTasks = (Task[]) objectIn.readObject();
            
            //System.out.println("Object received!");
            
            //Close everything
            textIn.close();
            textOut.close();            
            objectIn.close();            
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

	private String post(Task task) {
		String response = ""; 
		try {
			serverAddress = InetAddress.getByName("localhost");
			Socket socketText = new Socket(serverAddress, serverPortText);
			socketText.setReuseAddress(true);
			
			// Send text
            DataOutputStream textOut = new DataOutputStream(new BufferedOutputStream(socketText.getOutputStream()));
            String send = "post";
            textOut.writeUTF(send);
            textOut.flush();
            
            socketText.flush();
            // Receive text
            DataInputStream textIn = new DataInputStream(new BufferedInputStream(socketText.getInputStream()));
            String ping = textIn.readUTF();
            
            //System.out.println("Message from Server: " + response);
            
            Socket socket = new Socket(serverAddress, serverPortObject);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            oos.writeObject(task);
		    oos.flush();
		    oos.close();
            
            response = textIn.readUTF();
            
          //Close everything
            textIn.close();
            textOut.close();
            socketText.close();            

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;		
	}
	
	private String put(Task task) {
		String response = ""; 
		try {
			serverAddress = InetAddress.getByName("localhost");
			Socket socketText = new Socket(serverAddress, serverPortText);
			socketText.setReuseAddress(true);
			
			// Send text
            DataOutputStream textOut = new DataOutputStream(new BufferedOutputStream(socketText.getOutputStream()));
            String send = "put";
            textOut.writeUTF(send);
            textOut.flush();
            
            // Receive text
            DataInputStream textIn = new DataInputStream(new BufferedInputStream(socketText.getInputStream()));
            String ping = textIn.readUTF();
            
            //System.out.println("Message from Server: " + response);
            
            Socket socket = new Socket(serverAddress, serverPortObject);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            oos.writeObject(task);
		    oos.flush();
		    oos.close();
            
            response = textIn.readUTF();
            
          //Close everything
            textIn.close();
            textOut.close();            
            socketText.close();            

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
		
	}
	
	private String delete(String taskID) {
		String response = ""; 
		try {
			serverAddress = InetAddress.getByName("localhost");
			Socket socketText = new Socket(serverAddress, serverPortText);
			socketText.setReuseAddress(true);
			
			//**********
            //Incoming stream
            //**********
            
            //Input for data (text)
            BufferedInputStream bufInData = new BufferedInputStream(socketText.getInputStream());
            DataInputStream textIn = new DataInputStream(bufInData);
            
            //Output for data (text)
            BufferedOutputStream bufOutData = new BufferedOutputStream(socketText.getOutputStream());
            DataOutputStream textOut = new DataOutputStream(bufOutData);
            
            String send = "delete";
			
            textOut.writeUTF(send);
            textOut.flush();
            
            String ping = textIn.readUTF();
            System.out.println("Message from Server: " + response);
            
            textOut.writeUTF(taskID);
            textOut.flush();
            
            response = textIn.readUTF();
            
          //Close everything
            textIn.close();
            textOut.close();            
            socketText.close();            

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskManagerTCPClient client = new TaskManagerTCPClient();	
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
		*/
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
