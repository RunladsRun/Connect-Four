import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	String ip, name, port;
	
	
	private Consumer<Serializable> connection, infoPass;
	
	Client(Consumer<Serializable> call, Consumer<Serializable> call2, 
			String pName, String Ip, String portText){
		port = portText;
		ip = Ip;
		name = pName;
		connection = call;
		infoPass = call2;
	}
	
	public void run() {
		try {
			socketClient= new Socket(ip, Integer.parseInt(port));
			connection.accept("1");
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		    out.writeObject(name);
		    CFourInfo startInfo = (CFourInfo) in.readObject();
		    infoPass.accept(startInfo);
		} catch(Exception e) {
			connection.accept("0");
		}
		
		while(true) {
			try {
				CFourInfo moveInfo = (CFourInfo) in.readObject();
				infoPass.accept(moveInfo);
			} catch (Exception e) {
				break;
			}
		}
	}
	
	public void sendInfo(CFourInfo info) {
		try {
			out.writeObject(info);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	
}