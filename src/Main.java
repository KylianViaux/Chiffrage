import java.math.BigInteger;
import java.util.Random;

public class Main {

	public static BigInteger p;
	public static BigInteger q;
	public static BigInteger n;
	public static BigInteger m;
	public static BigInteger e;

	public static Pair clePublique;	
	
	//Créer une clé publique.
	public static Pair creationClePublique() {
		Random random1 = new Random();
		Random random2 = new Random();
		p = BigInteger.probablePrime(500, random1);
		q = BigInteger.probablePrime(500, random2);
		BigInteger un = new BigInteger("1");
		
		n = p.multiply(q);
		m = p.subtract(un).multiply(q.subtract(un));
		
		e = choisirExposantPublic(m);
		clePublique = new Pair(n,e);
		return clePublique;
	}
	
	// Renvoi l'exposant publique à partir de m.
	public static BigInteger choisirExposantPublic(BigInteger m) {
		BigInteger e = new BigInteger("0");
		BigInteger temp = new BigInteger("0");
		temp = e.gcd(m);
		while(temp.toString().equals("0") || !temp.toString().equals("1")) {
			System.out.println("valeur : " + e.toString());
			e = new BigInteger((int)((long)Math.random()));
			temp = e.gcd(m);
		}
		return e;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Bouahahaha on est ici pour la guerre   \\O-o--- |-O-o---");
		
		Pair clePublique = creationClePublique();
		
		System.out.println("Yo vla les results : " + clePublique.get_premier().toString() 
				+ "  " + clePublique.get_deuxieme().toString());
	}
}