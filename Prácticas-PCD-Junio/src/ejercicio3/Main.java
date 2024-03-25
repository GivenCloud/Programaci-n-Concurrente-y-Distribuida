package ejercicio3;

public class Main {

	//Se crean 100 hilos
	public static void main(String[] args) {

		Monitor monitor = new Monitor();
		
		Thread[] coche = new Thread[100];
		for (int i = 0; i < 100; i++) {
			coche[i] = new Coche(i, monitor);
			coche[i].start();
		}
		
		for (int j = 0; j < 100; j++) {
			try {
				coche[j].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("FIN DEL PROGRAMA");
	}
}
