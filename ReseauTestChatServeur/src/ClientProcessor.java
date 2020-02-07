import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class ClientProcessor extends Thread {

	Socket so;
	ArrayList<Socket> listSocket;
	
	public ClientProcessor(Socket so, ArrayList<Socket> listSocket){
		this.so = so;
		this.listSocket = new ArrayList<Socket>(listSocket);
	}
	
	/*
	 * Rajoute une socket à la liste de socket.
	 */
	public void MajPlusListSocket(Socket so){
		this.listSocket.add(so);
	}

	/*
	 * Enlève une socket à la liste de socket 
	 */
	public void MajMoinsListSocket(Socket so){
		for(int i = 0; i < listSocket.size(); i++){
			if(listSocket.get(i) == so){
				listSocket.remove(i);
			}
		}
	}
	
	@Override
	public void run() {
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(so.getInputStream()));
			String message;
			while(true){
				message = reader.readLine();
				//System.out.println(message);
				if(message.equals("null")){
					System.out.println("On a reçu le message : " + message);
				}
				Chat.broadcast(so,message);
			}
		} catch(NullPointerException npe){
			//System.out.println("Nous avons un objet null  : " + npe.getMessage());
			System.out.println("Un client s'est déconnecté");
			Chat.removeClient(this, so);
		} catch(SocketException se){
			System.out.println("Problème, un socket c'est déconnecté, on ne fait donc rien : " + se.getMessage());
			Chat.removeClient(this, so);
		} catch(IOException e){
			System.out.println("Problème lors du run : " + e.getMessage());
		}
	}
}
