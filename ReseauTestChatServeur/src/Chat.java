import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Chat {

	private static int port;
	private static ServerSocket ecoute = null;
	static Socket so;
	static ArrayList<Socket>  listSocket;
	static ArrayList<ClientProcessor> listProcessor;
	
	public static void main(String[] args) {
		
		listSocket = new ArrayList<Socket>();
		listProcessor = new ArrayList<ClientProcessor>();
		
			if(args.length == 1){
				try{
				port = Integer.parseInt(args[0]);
				ecoute = new ServerSocket(port);
				System.out.println("Serveur lancé sur le port " + port + "\n");
				while(true){
					try{
						so = ecoute.accept();
						
						listSocket.add(so);
						MajPlusListSocket();
						ClientProcessor cp = new ClientProcessor(so, listSocket);
						listProcessor.add(cp);
						
						//récupération de la clé publique du client a sa connexion 
						BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
						String recept = entree.readLine();
						
						if(listSocket.size() == 1) {
							System.out.println("Alice est connectée. Sa clé publique est : " + recept);
						}else if(listSocket.size() == 2) {
							System.out.println("Bob est connecté. Sa clé publique est : " + recept);
						}
						
						System.out.println("Il y a actuellement : " + listSocket.size() + " client(s) connecté(s)");
						
						cp.start();
					} catch(IOException e){
						System.out.println("Problème : " + e.getMessage());
					}
				}
			} catch(IOException e){
				System.out.println("Problème : " + e.getMessage());
			}
		}else {
			System.out.println("Il faut renseigner un port pour le serveur");
		}
	}
	
	
	public static void MajPlusListSocket(){
		for(int i = 0; i < listProcessor.size(); i++){
			listProcessor.get(i).MajPlusListSocket(listSocket.get(listSocket.size()-1));
		}
	}
	
	public static void MajMoinsListSocket(Socket so){
		for(int i = 0; i < listProcessor.size(); i++){
			listProcessor.get(i).MajMoinsListSocket(so);
		}
	}
	
	public static synchronized void removeClient(ClientProcessor processor, Socket so){
		for(int i = 0; i < listSocket.size(); i++){
			if(listSocket.get(i).equals(so)){
				listSocket.remove(i);
			}
		}
		for(int i = 0; i < listProcessor.size(); i++){
			if(listProcessor.get(i).equals(processor)){
				listProcessor.remove(i);
				i--;
			} else {
				listProcessor.get(i).MajMoinsListSocket(so);
			}
		}
	}
	
	
	/*
	 * Envoie le message reçu d'un client à tous les autres clients.
	 */
	public static synchronized void broadcast(Socket so, String message){
		for(int i = 0; i < listSocket.size(); i++){
			try {
				if(!listSocket.get(i).equals(so)){
					//System.out.println(" + message + "   et il y a " + listSocket.size() + " clients");
					//System.out.println("On envoie le message à un client : " + message + "   et il y a " + listSocket.size() + " clients");
					PrintWriter writer = new PrintWriter(listSocket.get(i).getOutputStream(), true);
					writer.println(message);
					writer.flush();
				}else {
					if(i == 0) {
						System.out.println("Alice a envoyé le message chiffré : "  + message);
					}else {
						System.out.println("Bob a envoyé le message chiffré : "  + message);
					}
				}
			}catch (IOException e) {
				System.out.println("Problème lors du broadcast : " + e.getMessage());
			}
		}
	}
	
	
	
}
