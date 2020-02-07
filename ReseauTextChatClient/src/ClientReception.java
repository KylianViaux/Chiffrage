import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;


public class ClientReception extends Client {

	public void run(){
		try {
			BufferedReader entree;
			entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
			boolean fin = false;
			while(!fin){
				
				String recept = "";
				
				//récupération du message
				try{
					recept = entree.readLine();
				} catch(NullPointerException npe){
					System.out.println("Nous avons un objet de null :(   : " + npe.getMessage());
					fin = true;
					so.close();
				}
				
				if(recept != ""){
					
					String[] arrOfStr = recept.split(" ", 0); 
					
					//si le message reçu commence par "/cp" on à reçu une clé publique 
					if(arrOfStr[0].matches("/cp")) {
						chiffrage.clePubliqueExterne = new Pair(new BigInteger(arrOfStr[1]), new BigInteger(arrOfStr[2]));
					}	
					//sinon c'est un message standart
					else {
						System.out.println("Message reçu : " + recept);
			
						
						//String[] arrOfStr = recept.split(" ", 0); 
						ArrayList<BigInteger> listeNombresChiffres = new ArrayList<BigInteger>();
						for (String number : arrOfStr) 
							listeNombresChiffres.add(BigInteger.valueOf(Integer.parseInt(number)));
						
						//Déchiffrement du message
						ArrayList<Integer> listeNombresMiDechiffrement = chiffrage.DechiffrementTexteDebut(listeNombresChiffres);	
						String texteDechiffre = chiffrage.DechiffrementTexteFin(listeNombresMiDechiffrement);
						System.out.println("Message reçu déchiffré : " + texteDechiffre + "\n");
					}
					
					
				}
			}
			so.close();
		} catch(NullPointerException npe){
			System.out.println("Problème de pointer null : " + npe.getMessage());
		} catch (IOException e) {
			System.out.println("Problème : " + e.getMessage());
		}
	}
}
