import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends Thread{
	
	static int port;
	static String serveur;
	static Socket so;
	static Chiffrage chiffrage;
	
	public static void main(String[] args) {
		if(args.length == 2){
			port = Integer.parseInt(args[0]);
			serveur = args[1];
			
			try {
				so = new Socket(serveur,port);
				PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
				
				//initialisation des classe pour la gestion des messages et leur chiffrement 
				ClientEnvoie envoie = new ClientEnvoie();
				ClientReception reception = new ClientReception();
				Chiffrage chiffrage = new Chiffrage();
				
				//création de la paire de clé
				Pair clePublique = chiffrage.creationClePublique();
				Pair clePrivee = chiffrage.creationClePrivee();
				
				//envoie de la clé publique au serveur à la connexion
				sortie.println("n(" + clePublique.get_premier() + "), e(" + clePublique.get_deuxieme() + ")");
				
				//lancement des thread d'envoi et de réception
				envoie.start();
				reception.start();
				
			} catch (UnknownHostException e) {
				System.out.println("Problème de host inconnue : " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Problème : " + e.getMessage());
			}
			
		}else {
			System.out.println("il faut renseigner une adresse et un port");
		}
		
	}

}
