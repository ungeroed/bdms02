package serverAndClient;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import xml.Cal;
import xml.CalSerializer;
import xml.Task;

import javax.xml.bind.JAXBException;

public class TaskManagerTCPServer {
	int serverPort = 7890;

    Cal cal;
    CalSerializer cs;

	public TaskManagerTCPServer() {
        cs = new CalSerializer();
        try {
            cal = cs.deserialize();
        } catch (JAXBException e) {
            System.out.println("Couldn't start server. JAXBException: ");
            e.printStackTrace();
        } catch (FileNotFoundException e) {

            System.out.println("Couldn't start server. FileNotFoundException: ");
            e.printStackTrace();
        }
        try {
            serve();
        } catch (IOException e) {
            System.out.println("Couldn't start server. IOException:");
            e.printStackTrace();
        }
    }
	
	public void serve() throws IOException{

        ServerSocket serverSocket = new ServerSocket(serverPort);
		serverSocket.setReuseAddress(true);

        while(true){
			Socket socket = serverSocket.accept();
		    new HandleIncomingClient(socket);
		}
	}
	
	private class HandleIncomingClient extends Thread{
		Socket socket;

		//The address of the client
		InetAddress inetAddress;
		
	    ObjectInputStream in;
	    ObjectOutputStream out;
	    
		private HandleIncomingClient(Socket sock)  throws IOException{

            socket = sock;
			socket.setReuseAddress(true);
			inetAddress = sock.getInetAddress();

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();


            in = new ObjectInputStream(socket.getInputStream());


            this.start();
		}
		
		public void run() {
			try{

                // Protocol is specified first
                String protocol = in.readObject().toString(); // blocking call

                // Data in bytestream
                Object data = in.readObject();

                out.writeObject(protocol);
                // handle protocls differently
                    if(protocol.equalsIgnoreCase("GET")){

                        // In this case the data is a userid
                        // @todo check this data

                        Task[] tasks = get(data.toString());

                        out.writeObject(tasks);
                        out.flush();
                    }

                    if(protocol.equalsIgnoreCase("POST")){
                        // In this case data is a task

                        Task task = (Task) data;
                        String result = post(task);

                        out.writeObject(result);
                        out.flush();
                    }

                    if(protocol.equalsIgnoreCase("PUT")){

                        Task task = (Task) data;

                        String result = put(task);

                        out.writeObject(result);
                        out.flush();
                    }

                    if(protocol.equalsIgnoreCase("DELETE")){

                        // In this case the data is an id
                        String result = delete(data.toString());

                        out.writeObject(result);
                        out.flush();
                    }

                    out.close();
                    in.close();
			} catch(IOException ioe){				
				ioe.printStackTrace();				
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            CalSerializer.PrintTaskObject(task);
			if(task.attendants != null && task.attendants.contains(userID))
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
        System.out.println("Posting "+task);
        System.out.println(cal);
        System.out.println(cal.tasks);
        cal.tasks.add(task);
        try {
            cs.serialize(cal);
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
            if(cal.tasks.get(i).id == null) response = "No task by that id";
            else if(cal.tasks.get(i).id.equals(task.id)){
                /* out with the old, in with the new */
                cal.tasks.remove(i);
                cal.tasks.add(task);
                response = "Task updated!";
                try {
                    cs.serialize(cal);
                } catch (JAXBException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
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
            if(cal.tasks.get(i).id == null) response = "No task by that id";
            else if(cal.tasks.get(i).id.equals(id)){
                response = "Task deleted!";
                cal.tasks.remove(i);
                try {
                    cs.serialize(cal);
                } catch (JAXBException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
		
		if(response.isEmpty()) response = "No task with that id found";
		
        return response;
	}


    public static void main(String[] args) throws IOException{
        TaskManagerTCPServer server = new TaskManagerTCPServer();
    }
}
