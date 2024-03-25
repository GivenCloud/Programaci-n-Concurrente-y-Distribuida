package ejercicio1;

//HILO QUE SE ENCARGA DE MULTIPLICAR MATRICES

public class Hilo2 extends Thread {

	private MatrizCompartida compar;

	public Hilo2(MatrizCompartida a) {
		compar = a;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			compar.multiplicarMatriz();;
		}
	}

}
