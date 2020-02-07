import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;


public class ClientEnvoie extends Client {
	
	public void run(){
		try {
			PrintWriter sortie;
			sortie = new PrintWriter(so.getOutputStream(), true);
			boolean fin = false;
			
			while(!fin){
				
				BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("**Envoyer un message : ");
				String texte = clavier.readLine();
				
				//lecture du texte noté par l'utilisateur
				
				//si texte="exit" , déconnexion de l'utilisateur 
				if(texte.matches("exit")){
					fin = true;
				} 
				//si texte="/cp" envoi de la clé publique
				if(texte.matches("/cp")){
					System.out.println("**Partage de votre clé publique**");
					sortie.println("/cp " + chiffrage.clePublique.get_premier() + " " + chiffrage.clePublique.get_deuxieme());
				}
				
				else {
					//si le texte n'est pas égale a null ou vide on l'envoi
					if(texte!="null" && texte!=""){
					
						//si on n'a pas réçu de clé publique d'un autre utilisateur alors on envoi le message en clair
						if(chiffrage.clePubliqueExterne == null) {
							sortie.println(texte);
						}
						//sinon on chiffre le message avec cette clé avant de l'envoyer
						else {
							ArrayList<Integer> listeNombresMiChiffrement = chiffrage.ChiffrementTexteDebut(texte);
							ArrayList<BigInteger> listeNombresChiffres = chiffrage.ChiffrementTexteFin(listeNombresMiChiffrement);
							String value = "";
							for(int i=0; i< listeNombresChiffres.size(); i++) {
								value += listeNombresChiffres.get(i) + " ";
							}
								
							//envoi du texte chiffré
							sortie.println(value);
						}
						
					} else {
						System.out.println("erreur message avec le message : " + texte + ", fin de connexion");
						fin = true;
					}
				}
			}
			so.close();
		} catch(NullPointerException npe){
			System.out.println("Problème : " + npe.getMessage());
		} catch (IOException e) {
			System.out.println("Problème : " + e.getMessage());
		}
	}
}
