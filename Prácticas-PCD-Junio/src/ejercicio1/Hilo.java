package ejercicio1;

//HILO QUE SE ENCARGA DE SUMAR MATRICES

public class Hilo extends Thread {

	private MatrizCompartida compar;

	public Hilo(MatrizCompartida a) {
		compar = a;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			compar.sumarMatriz();
		}

	}

}
