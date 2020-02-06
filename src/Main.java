import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;

/*
 * 
 * Pour l'instant problème avec la clé public ou la clé privée.
 * 
 * 
 */



public class Main {

	public static BigInteger p;
	public static BigInteger q;
	public static BigInteger n;
	public static BigInteger m;
	public static BigInteger e;
	public static BigInteger u;
	
	public static Pair clePublique;	
	public static Pair clePrivee;
	
	//Créer une clé publique.
	public static Pair creationClePublique() {
		/**
		 * p et q nombres premiers aléatoires où p != q
		 */
		Random random1 = new Random();
		Random random2 = new Random();
		p = BigInteger.probablePrime(500, random1);
		q = BigInteger.probablePrime(500, random2);
		BigInteger un = new BigInteger("1");
		
		
		/**
		 * n = p*q
		 * m = (p-1) * (q-1)
		 */
		n = p.multiply(q);
		m = (p.subtract(un)).multiply(q.subtract(un));
		
		e = choisirExposantPublic(m);
		clePublique = new Pair(n,e);
		
		return clePublique;
	}
	
	/**
	 * Renvoie l'exposant publique e, petit entier impair et premier avec m.
	 * @param m
	 * @return
	 */
	public static BigInteger choisirExposantPublic(BigInteger m) {
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
	
	// 
	public static Pair creationClePrivee() {
		System.out.println("valeur de e : " + e.intValue());
		BigInteger r1 = BigInteger.valueOf(e.intValue());
		BigInteger r2 = BigInteger.valueOf(m.intValue());
		BigInteger r3 = BigInteger.valueOf(1);
		BigInteger u1 = BigInteger.valueOf(1);
		BigInteger u2 = BigInteger.valueOf(0);
		BigInteger u3 = BigInteger.valueOf(1);
		BigInteger v1 = BigInteger.valueOf(0);
		BigInteger v2 = BigInteger.valueOf(1);
		BigInteger v3 = BigInteger.valueOf(1);
		while(r3.intValue() != 0) {
			System.out.println("valeurs temp : r " + r2.toString() + " u " + u2.toString() + " v " + v2.toString());
			BigInteger valeurR1R2 = r1.divide(r2);
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
		if((u1.intValue() < 2) || (u1.intValue() > m.intValue())) {
			BigInteger k = BigInteger.valueOf(-1);
			BigInteger result = u1.subtract((k.multiply(m)));
			System.out.println("Message au-dessus du while de la création de la clé privée : "
			+ " u2 " + u1.toString() + " k " + k.toString());			
			while((result.intValue() < 2 || result.intValue() > m.intValue()) && k.intValue() > -50 ) {
				System.out.println("Message dans le while de la création de la clé privée : " + " u1 " + u1.toString() + " k " + k.toString());
				k = k.subtract(BigInteger.valueOf(1));
				result = u1.subtract((k.multiply(m)));
			}
			clePrivee = new Pair(n, result);
			u = result;
			return clePrivee;			
		} else {
			clePrivee = new Pair(n, u1);
			u = u1;
			return clePrivee;
		}
	}
	
	
	
	public static ArrayList<Integer> ChiffrementTexteDebut(String texte) {
		ArrayList<Integer> listeNombres = new ArrayList<Integer>();
		for(int i = 0; i < texte.length(); i++) {
			listeNombres.add(ChiffrementCaractere(texte.charAt(i)));
		}
		return listeNombres;
	}
	
	
	public static int ChiffrementCaractere(char caractere) {
		return (int)caractere;
	}
	
	
	public static ArrayList<BigInteger> ChiffrementTexteFin(ArrayList<Integer> listeNombresMiChiffrement){
		ArrayList<BigInteger> listeNombresChiffres = new ArrayList<BigInteger>();
		for(int i= 0; i < listeNombresMiChiffrement.size(); i++) {
			//double valeurTempo = Math.pow(listeNombresMiChiffrement.get(i), 7);//e.intValue());
			BigInteger valeurTempo = BigInteger.valueOf(listeNombresMiChiffrement.get(i));
			valeurTempo = valeurTempo.modPow(BigInteger.valueOf(e.intValue()), BigInteger.valueOf(n.intValue()));
			listeNombresChiffres.add(valeurTempo);
			//listeNombresChiffres.add((int)(valeurTempo % 5141));//n.doubleValue()));
		}
		return listeNombresChiffres;
	}

	
	public static ArrayList<Integer> DechiffrementTexteDebut(ArrayList<BigInteger> listeNombresChiffres){
		ArrayList<Integer> listeNombresMiDechiffres = new ArrayList<Integer>();
		for(int i= 0; i < listeNombresChiffres.size(); i++) {
			BigInteger valeurTempo = BigInteger.valueOf(listeNombresChiffres.get(i).intValue());
			valeurTempo = valeurTempo.modPow(BigInteger.valueOf(u.intValue()), BigInteger.valueOf(n.intValue()));
			listeNombresMiDechiffres.add(valeurTempo.intValue());
			//double valeurTempo = Math.pow(listeNombresChiffres.get(i), 4279);//e.intValue());
			//listeNombresMiDechiffres.add((int)(valeurTempo % 5141));//n.doubleValue()));
		}
		return listeNombresMiDechiffres;
	}
	
	public static String DechiffrementTexteFin(ArrayList<Integer> listeNombresDechiffres) {
		String texte = "";
		for(int i = 0; i < listeNombresDechiffres.size(); i++) {
			texte = texte.concat(DechiffrementCaractere(listeNombresDechiffres.get(i)));
		}
		return texte;
	}
	
	
	public static String DechiffrementCaractere(int nombre) {
		return Character.toString((char) nombre);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Bouahahaha on est ici pour la guerre   \\O-o--- |-O-o---");
		
		Pair clePublique = creationClePublique();
		
		Pair clePrivee = creationClePrivee();
		
		String texte = "Bonjour !";
		
		System.out.println("Valeurs : " + e.intValue());
		
		ArrayList<Integer> listeNombresMiChiffrement = ChiffrementTexteDebut(texte);
		
		ArrayList<BigInteger> listeNombresChiffres = ChiffrementTexteFin(listeNombresMiChiffrement);
		
		ArrayList<Integer> listeNombresMiDechiffrement = DechiffrementTexteDebut(listeNombresChiffres);
		
		String texteDechiffre = DechiffrementTexteFin(listeNombresMiDechiffrement);
		
		System.out.println("les results clé publics : " + clePublique.get_premier().toString() 
				+ "  " + clePublique.get_deuxieme().toString());
		
		System.out.println("les results clé privées : " + clePrivee.get_premier().toString() 
				+ "  " + clePrivee.get_deuxieme().toString());
		
		System.out.println(texte);
		
		for(int i = 0; i < listeNombresMiChiffrement.size(); i++) {
			System.out.print(" " + listeNombresMiChiffrement.get(i));
		}
		
		System.out.println("");
		System.out.println(texte);
		
		for(int i = 0; i < listeNombresChiffres.size(); i++) {
			System.out.print(" " + listeNombresChiffres.get(i).toString());
		}
		
		System.out.println("");
		System.out.println(texte);
		
		for(int i = 0; i < listeNombresMiDechiffrement.size(); i++) {
			System.out.print(" " + listeNombresMiDechiffrement.get(i).toString());
		}

		System.out.println("\n" + texteDechiffre + " " + texteDechiffre);
		
		
		System.out.println("");
		System.out.println(texte);
		System.out.println(texte);

		
	}
}