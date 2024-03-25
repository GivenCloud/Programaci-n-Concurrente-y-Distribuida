package ejercicio3;

import java.util.Random;

public class Coche extends Thread {
	
	private int id;
	private Monitor monitor;
	private int surtidor;
	private int tunel;
	private int[] array;
	private int dinero;
	private int tiempo;
	private int lavado;
	private boolean haRepostado = false;
	
	final int BASICO = 50;
	final int NORMAL = 55;
	final int PREMIUM = 60;

	//se calcula el tiempo que tarda en repostar y se asigna el plan de lavado (tiempo que tarda en lavar el coche)
	public Coche(int id, Monitor monitor) {
		this.id = id;
		this.monitor = monitor;
		Random rand = new Random();
		this.dinero = rand.nextInt(31) + 20;
		tiempo = dinero;
		Random rand1 = new Random();
		int n = rand1.nextInt(3) + 1;
		if (n == 1) {
			lavado = BASICO;
		} else if (n == 2) {
			lavado = NORMAL;
		} else {
			lavado = PREMIUM;
		}
	}

	public void imprimirPantalla() {
		if (haRepostado) {
			System.out.println("Cliente "+ id +" ha sido atendido en el surtidor " + (surtidor+1) + "\n"
					+ "Repostado " + dinero + " euros en un tiempo " + tiempo + "\n"
					+ "Tiempo estimado de lavado " + lavado + "\n"
					+ "Seleccionado túnel " + tunel + "\n"
					+ "Tiempo estimado de espera para el lavado en la cola1=" + array[0] + ", cola2=" + array[1] + ", cola3=" + array[2] + ", "
					+ "cola4=" + array[3] + ", cola5=" + array[4] + "\n");
		
		} else {
			System.out.println("Cliente "+ id +" solo quiere lavar el coche\n"
					+ "Tiempo estimado de lavado " + lavado + "\n"
					+ "Tiempo estimado de espera para el lavado en la cola5=" + array[4] + "\n");
		}
	}

	public void run() {
		//Elige un número aleatorio de 1 a 10 y si es menor o igual que 7 repostará, si no solo lavará el coche.
		Random rand = new Random();
		int n = rand.nextInt(10) + 1;

		try {
			if (n > 3) { //Coches que repostan y lavan el coche
				haRepostado = true;
				surtidor = monitor.repostar();
				sleep(tiempo);
				monitor.terminarRepostar(surtidor);
				array = monitor.buscarCola(lavado, haRepostado);
				tunel = array[5];
				imprimirPantalla();
				monitor.entraLavar(tunel);
				sleep(lavado);
				monitor.salirLavar(tunel, lavado);
			} else { //Coches que solo lo lavan
				array = monitor.buscarCola(lavado, haRepostado);
				tunel = array[5];
				imprimirPantalla();
				monitor.entraLavar(5);
				sleep(lavado);
				monitor.salirLavar(5, lavado);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
