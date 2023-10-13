import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
	ServerThread server;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private Consumer<Serializable> connection, message;
	String port;
	boolean p1Turn, end;
	
	Server(Consumer<Serializable> call, Consumer<Serializable> call2, String num){
		
		connection = call;
		message = call2;
		p1Turn = true;
		end = false;
		port = num;
		server = new ServerThread();
		server.start();
	}
	
	public class ServerThread extends Thread {
		public void run() {
			try (ServerSocket mysocket = new ServerSocket(Integer.parseInt(port));){
				connection.accept("1");
				message.accept("Now server is connected! (Port Number: " + port + ")");
				message.accept("Waiting for Players to join...");
				while (true) {
					ClientThread c = new ClientThread(mysocket.accept());
					clients.add(c);
					c.start();
				}
				
			} catch(Exception e) {
				connection.accept("0");
			}
			
		}
		
		
	}
	
	
	public class ClientThread extends Thread {
		Socket connection;
		ObjectInputStream in;
		ObjectOutputStream out;
		String name;
		
		ClientThread(Socket s){
			this.connection = s;
		}
		
		public void run() {
			try {
				end = false;
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
				name = in.readObject().toString();
				message.accept("Player ("+ this.name + ") joined!");
			}
			catch(Exception e) {
				System.out.println("Something failed");
			}

			
			if(clients.size() == 2) {
				try {
					clients.get(0).out.writeObject(startGameP1());
					clients.get(1).out.writeObject(startGameP2());
					message.accept("A new Game started");
					message.accept(clients.get(0).name + "'s turn first");
				} catch (Exception e) {}
			}
			
			while(true) {
				try {
					CFourInfo info = (CFourInfo) in.readObject();
					if(end) {
						break;
					}
					if(info.tie) {
						message.accept("It's tie.");
						message.accept("Waiting for Players to join again...");
						if(p1Turn) {
							clients.get(1).out.writeObject(info);
						} else {
							clients.get(0).out.writeObject(info);
						}
						p1Turn = true;
						clients.clear();
						end = true;
						break;
					}
					if(info.invalid) {
						message.accept(info.name + " tried invalid move on ("
								+ info.move.getKey() + ", " + info.move.getValue()+ ")");
						continue;
					}
					message.accept(info.name + " made a move on ("
							+ info.move.getKey() + ", " + info.move.getValue()+ ")");
					if(p1Turn) {
						p1Turn = false;
						clients.get(1).out.writeObject(info);
					} else {
						p1Turn = true;
						clients.get(0).out.writeObject(info);
					}
					if(info.four.size() == 4) {
						message.accept(info.name + " won the game.");
						message.accept("Waiting for Players to join again...");
						p1Turn = true;
						clients.clear();
						end = true;
						break;
					}
					
				} catch (Exception e) {
					message.accept(this.name + " left the game.");
					clients.remove(this);
					if(clients.size() == 1 ) {
						try {
							clients.get(0).out.writeObject(disconnect());
						} catch (IOException ioe) {e.printStackTrace();}
						p1Turn = true;
					}
			    	break;
				}
			}

		}
		
		
		public CFourInfo startGameP1() {
			CFourInfo gameInfo = new CFourInfo();
			gameInfo.first = true;
			gameInfo.name = clients.get(1).name;
			return gameInfo;
		}
		
		public CFourInfo startGameP2() {
			CFourInfo gameInfo = new CFourInfo();
			gameInfo.first = false;
			gameInfo.name = clients.get(0).name;
			return gameInfo;
		}
		
		public CFourInfo disconnect() {
			CFourInfo gameInfo = new CFourInfo();
			gameInfo.disconnected = true;
			return gameInfo;
		}
		
	}
	
	
	
	
}