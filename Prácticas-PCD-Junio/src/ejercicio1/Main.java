package ejercicio1;

public class Main {

	public static void main(String[] args) {
		// CREAMOS LAS VARIABLES E INICIAMOS LOS HILOS
		MatrizCompartida compar = new MatrizCompartida();
		Hilo2 a = new Hilo2(compar);
		Hilo b = new Hilo(compar);
		a.start();
		b.start();
		try {
			// EJECUTAMOS LOS HILOS DE FORMA CONCURRENTE
			a.join();
			b.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("FIN DEL PROGRAMA");
	}

}
