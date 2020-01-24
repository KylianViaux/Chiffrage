import java.math.BigInteger;

public class Pair {
	private BigInteger _premier;
	private BigInteger _deuxieme;
	
	Pair(BigInteger premier, BigInteger deuxieme){
		_premier = new BigInteger(premier.toString());
		_deuxieme = new BigInteger(deuxieme.toString());
	}

	public BigInteger get_premier() {
		return _premier;
	}

	public void set_premier(BigInteger _premier) {
		this._premier = _premier;
	}

	public BigInteger get_deuxieme() {
		return _deuxieme;
	}

	public void set_deuxieme(BigInteger _deuxieme) {
		this._deuxieme = _deuxieme;
	}
}
