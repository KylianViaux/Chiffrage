import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Chiffrage {

	public static BigInteger m;
	
	public static Pair clePublique;	
	public static Pair clePrivee;
	public static Pair clePubliqueExterne = null;
	
	//Créer une clé publique.
	public static Pair creationClePublique() {
		/**
		 * p et q nombres premiers aléatoires où p != q
		 */
		Random random1 = new Random();
		Random random2 = new Random();
		BigInteger p = BigInteger.probablePrime(500, random1);
		BigInteger q = BigInteger.probablePrime(500, random2);
		BigInteger un = new BigInteger("1");

		BigInteger n = p.multiply(q);
		m = (p.subtract(un)).multiply(q.subtract(un));
		
		// Création de l'exposant public.
		BigInteger e = choisirExposantPublic();
		// Création de la clé publique.
		clePublique = new Pair(n,e);
		// Renvoie la cé publique.	
		return clePublique;
	}
	
	/**
	 * Renvoie l'exposant publique e, petit entier impair et premier avec m.
	 * @return
	 */
	public static BigInteger choisirExposantPublic() {
		BigInteger e = new BigInteger("0");
		BigInteger temp = new BigInteger("0");
		temp = e.gcd(m);
		// Tant que e n'est pas le gcd de m, on prend une nouvelle aléatoire pour e.
		while(temp.toString().equals("0") || !temp.toString().equals("1")) {
			double valeurRandomDouble = Math.random()*10000;
			long valeurRand = (long)valeurRandomDouble;
			e = BigInteger.valueOf(valeurRand);
			// e ne peut pas être pair.
			if(e.intValue() % 2 == 0) {
				e = e.add(BigInteger.valueOf(1));
			}
			temp = e.gcd(m);
		}
		return e;
	}
	
	
	/**
	 * Méthode créant une clé privée à partir d'une clé public et d'un BigInteger.
	 * @return : La clé privée ayant été créée.
	 */
	public static Pair creationClePrivee() {
		// Valeur temporaire pour 2 pour les tests.
		BigInteger valeurDeux = BigInteger.valueOf(2);
		// La valeur e
		BigInteger r1 = clePublique.get_deuxieme();
		// La valeur m
		BigInteger r2 = m;
		// Les différentes valeurs pour les valeurs r3, u1, u2, u2, u3, v1, v2, v3.
		BigInteger r3 = BigInteger.valueOf(1);
		BigInteger u1 = BigInteger.valueOf(1);
		BigInteger u2 = BigInteger.valueOf(0);
		BigInteger u3 = BigInteger.valueOf(1);
		BigInteger v1 = BigInteger.valueOf(0);
		BigInteger v2 = BigInteger.valueOf(1);
		BigInteger v3 = BigInteger.valueOf(1);
		// Tant que les étapes n'ont pas finies et donc que la valeur r3 ne vaut pas 0.
		while(r3.intValue() != 0) {
			// valeur temporaire utilisés plusieurs.
			BigInteger valeurR1R2 = r1.divide(r2);
			// On avance d'une étape en appliquant les calculs et en répercutant les calculs précédants.
			r3 = r1.subtract((valeurR1R2.multiply(r2)));
			u3 = u1.subtract((valeurR1R2.multiply(u2)));
			v3 = v1.subtract((valeurR1R2.multiply(v2)));
			r1 = r2;
			r2 = r3;
			u1 = u2;
			u2 = u3;
			v1 = v2;
			v2 = v3;
		}
		
		// Si la valeur final de u n'est pas entre 2 et m, il faut exécuter des opérations supplémentaires.
		if((u1.compareTo(valeurDeux) == -1) || u1.compareTo(m) == 1) {
			// On doit soustraire -m à u.
			BigInteger k = BigInteger.valueOf(-1);
			BigInteger result = u1.subtract((k.multiply(m)));
			// Tant que u n'est pas entre 2 et m on réapplique le même traitement.
			while((result.compareTo(valeurDeux) == -1 || result.compareTo(m) == 1)) {
				k = k.subtract(BigInteger.valueOf(1));
				result = u1.subtract((k.multiply(m)));
			}
			// On est sortie de la boucle, on peut créer la clé privée à partir de n et de u.
			clePrivee = new Pair(clePublique.get_premier(), result);
			return clePrivee;	
		} else {
			// On peut directemment créer la clé privée à partir de n et de u.
			clePrivee = new Pair(clePublique.get_premier(), u1);
			return clePrivee;
		}
	}
	
	
	
	/**
	 * Méthode permettant de passer d'un texte à une liste de nombre ASCII.
	 * @param texte : Texte à passer en forme ASCII.
	 * @return : Liste de nombre ASCII correspondant au texte.
	 */
	public static ArrayList<Integer> ChiffrementTexteDebut(String texte) {
		ArrayList<Integer> listeNombres = new ArrayList<Integer>();
		for(int i = 0; i < texte.length(); i++) {
			// Passage d'un caractère à un nombre ASCII.
			listeNombres.add(ChiffrementCaractere(texte.charAt(i)));
		}
		return listeNombres;
	}
	
	
	// Partie du chiffrement où on transforme un caractère en un nombre ASCII.
	public static int ChiffrementCaractere(char caractere) {
		return (int)caractere;
	}
	
	
	/**
	 * Méthode permettant de chiffrer des nombres ASCII grâce à une clé public.
	 * @param listeNombresMiChiffrement : Liste de nombres ASCII à chiffrer.
	 * @return : Liste de nombres ASCII chiffrés.
	 */
	public static ArrayList<BigInteger> ChiffrementTexteFin(ArrayList<Integer> listeNombresMiChiffrement){
		ArrayList<BigInteger> listeNombresChiffres = new ArrayList<BigInteger>();
		for(int i= 0; i < listeNombresMiChiffrement.size(); i++) {
			BigInteger valeurTempo = BigInteger.valueOf(listeNombresMiChiffrement.get(i));
			// Chiffrement d'un caractère ASCII.
			if(clePubliqueExterne != null) {
				valeurTempo = valeurTempo.modPow(clePubliqueExterne.get_deuxieme(), clePubliqueExterne.get_premier());
			}
			listeNombresChiffres.add(valeurTempo);
		}
		return listeNombresChiffres;
	}

	
	/**
	 * Méthode permettant de déchiffrer une liste de nombre ASCII chiffrés pour obtenir une liste de nombre ASCII grâce à une clé privée.
	 * @param listeNombresChiffres : Liste de nombres ASCII chiffrés.
	 * @return : Liste de nombres ASCII.
	 */
	public static ArrayList<Integer> DechiffrementTexteDebut(ArrayList<BigInteger> listeNombresChiffres){
		ArrayList<Integer> listeNombresMiDechiffres = new ArrayList<Integer>();
		for(int i= 0; i < listeNombresChiffres.size(); i++) {
			BigInteger valeurTempo = listeNombresChiffres.get(i);
			// Déchiffrage d'un nombre chiffré.
			valeurTempo = valeurTempo.modPow(clePrivee.get_deuxieme(), clePrivee.get_premier());
			listeNombresMiDechiffres.add(valeurTempo.intValue());
		}
		return listeNombresMiDechiffres;
	}
	
	
	/**
	 * Méthode permettant de passer d'une liste de nombres ASCII à un texte correspondant.
	 * @param listeNombresDechiffres : Liste de nombres ASCII.
	 * @return : Texte correspondant à la liste de nombres ASCII.
	 */
	public static String DechiffrementTexteFin(ArrayList<Integer> listeNombresDechiffres) {
		String texte = "";
		for(int i = 0; i < listeNombresDechiffres.size(); i++) {
			texte = texte.concat(DechiffrementCaractere(listeNombresDechiffres.get(i)));
		}
		return texte;
	}
	
	/**
	 * Méthode permettant de passer d'un nombre ASCII à un String.
	 * @param nombre : nombre ASCII
	 * @return : String
	 */
	public static String DechiffrementCaractere(int nombre) {
		return Character.toString((char) nombre);
	}

}
