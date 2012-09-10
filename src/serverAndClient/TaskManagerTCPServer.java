package serverAndClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import xml.*;

public class TaskManagerTCPServer {
	int serverPort = 7896;
	ServerSocket listenSocket;
	
	public static void main(String[] args) {
		TaskManagerTCPServer server = new TaskManagerTCPServer();
	}

	public TaskManagerTCPServer(){
		serve();
	}

	public void serve(){
		try {
			listenSocket = new ServerSocket(serverPort);
			Socket clientSocket = listenSocket.accept();
			InputStream is = clientSocket.getInputStream();
			DataInputStream dataStream = new DataInputStream(is);
			String message = dataStream.readUTF();
			System.out.println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Task[] get(String userID){}
	private void post(Task task){}
	private void put(Task task){}
	private void delete(String id){}
}
