import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Chiffrage {

	public static BigInteger p;
	public static BigInteger q;
	public static BigInteger n;
	public static BigInteger m;
	public static BigInteger e;
	public static BigInteger u;
	
	public static Pair clePublique;	
	public static Pair clePrivee;
	public static Pair clePubliqueExterne = null;
	
	//Créer une clé publique.
	public static Pair creationClePublique() {
		Random random1 = new Random();
		Random random2 = new Random();
		/*
		p = BigInteger.probablePrime(500, random1);
		q = BigInteger.probablePrime(500, random2);
		BigInteger un = new BigInteger("1");
		n = p.multiply(q);
		m = (p.subtract(un)).multiply(q.subtract(un));
		e = choisirExposantPublic(m);
		clePublique = new Pair(n,e);
		*/
		
		p = BigInteger.valueOf(53);
		q = BigInteger.valueOf(97);
		m = BigInteger.valueOf(4992);
		n = BigInteger.valueOf(5141);
		e = BigInteger.valueOf(7);
		clePublique = new Pair(n,e);
		
		
		return clePublique;
	}
	
	// Renvoi l'exposant publique à partir de m.
	public static BigInteger choisirExposantPublic(BigInteger m) {
		BigInteger e = new BigInteger("0");
		BigInteger temp = new BigInteger("0");
		temp = e.gcd(m);
		while(temp.toString().equals("0") || !temp.toString().equals("1")) {
			double valeurRandomDouble = Math.random()*10000;
			long valeurRand = (long)valeurRandomDouble;
			e = BigInteger.valueOf(valeurRand);
			if(e.intValue() % 2 == 0) {
				e = e.add(BigInteger.valueOf(1));
			}
			temp = e.gcd(m);
		}
		return e;
	}
	
	
	public static Pair creationClePrivee() {
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
		if(!(u1.intValue() < 2) || !(u1.intValue() > m.intValue())) {
			BigInteger k = BigInteger.valueOf(-1);
			BigInteger result = u1.subtract((k.multiply(m)));
			System.out.println("Message au-dessus du while de la création de la clé privée : result " + result.toString() 
			+ " u2 " + u1.toString() + " k " + k.toString() + " m " + m.toString());			
			while((result.intValue() < 2 || result.intValue() > m.intValue()) && k.intValue() > -50 ) {
				System.out.println("Message dans le while de la création de la clé privée : result " + result.toString() 
				+ " u1 " + u1.toString() + " k " + k.toString() + " m " + m.toString());
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

}
