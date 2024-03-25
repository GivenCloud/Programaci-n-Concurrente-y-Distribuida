package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	//ReentrantLock para controlar las funciones
	private ReentrantLock l = new ReentrantLock();

	//Condition para controlar la cola de surtidores (solo una porque da igual en que surtidor reposten)
	private Condition colaSurtidores = l.newCondition();
	private int surtidoresDisponibles = 4;
	//Array para saber si los surtidores están ocupados
	static boolean[] surtidores = { true, true, true, true };

	//Una condition para controlar cada túnel de lavado
	private Condition tunel1 = l.newCondition();
	private Condition tunel2 = l.newCondition();
	private Condition tunel3 = l.newCondition();
	private Condition tunel4 = l.newCondition();
	private Condition tunel5 = l.newCondition();
	//Array para saber si los túneles están ocupados
	private boolean[] tuneles = { true, true, true, true, true };
	//Array con los tiempos de espera de cada túnel
	private int[] tiempoEspera = { 0, 0, 0, 0, 0 };

	//Condition para controlar la pantalla
	private Condition pantalla = l.newCondition();
	private boolean pantallaOcupada = false;

	private int surtidor;
	private int tunel;

	
	public int getTiempoEspera(int n) {
		return tiempoEspera[n];
	}

	// Si no hay surtidores disponibles se espera y cuando se libere uno se le asigna el surtidor
	public int repostar() throws InterruptedException {
		l.lock();
		try {
			while (surtidoresDisponibles == 0) {
				colaSurtidores.await();
			}
			surtidoresDisponibles--;
			for (int i = 0; i < surtidores.length; i++) {
				if (surtidores[i]) {
					surtidor = i;
					surtidores[i] = false;
					break;
				}
			}
			
		} finally {
			l.unlock();
		}
		//Devuelve el surtidor en el que va a repostar
		return surtidor;
	}

	// Libera el surtidor que estaba usando
	public void terminarRepostar(int surtidor) {

		l.lock();
		try {
			surtidoresDisponibles++;
			surtidores[surtidor] = true;
			colaSurtidores.signal();
		} finally {
			l.unlock();
		}
	}

	//Busca la cola con menos tiempo de espera
	public int[] buscarCola(int lav, boolean haRepostado) throws InterruptedException {
		int[] tiempos = { 0, 0, 0, 0, 0, 0 };
		l.lock();
		try {
			while (pantallaOcupada) {
				pantalla.await();
			}
			pantallaOcupada = true;
			for (int j = 0; j < 5; j++) {
				tiempos[j] = tiempoEspera[j];
			}
			if (!haRepostado) { // Si no ha repostado se va directamente a la cola 5 y se suma el tiempo de
				tunel = 5; // espera de su lavado
				tiempoEspera[4] += lav;
			} else { // Si ha repostado se le asigna la cola con menos tiempo de espera 
				int cola = 0;
				int min = tiempoEspera[0];
				for (int i = 1; i < tiempoEspera.length; i++) {
					if (tiempoEspera[i] < min) {
						min = tiempoEspera[i];
						cola = i;
					}
				}

				tunel = cola + 1;
				tiempoEspera[cola] += lav;
				tiempos[5] = tunel;
			}
		} finally {
			l.unlock();
		}
		return tiempos;
	}

	// Se espera hasta que el tunel que se le asignó esté libre y lo ocupa
	public void entraLavar(int tun) throws InterruptedException {
		l.lock();
		try {
			pantallaOcupada = false;
			pantalla.signal();
			switch (tun) {
			case 1: {
				while (!tuneles[0]) {
					tunel1.await();
				}
				tuneles[0] = false;
				break;
			}
			case 2: {
				while (!tuneles[1]) {
					tunel2.await();
				}
				tuneles[1] = false;
				break;
			}
			case 3: {
				while (!tuneles[2]) {
					tunel3.await();
				}
				tuneles[2] = false;
				break;
			}
			case 4: {
				while (!tuneles[3]) {
					tunel4.await();
				}
				tuneles[3] = false;
				break;
			}
			case 5: {
				while (!tuneles[4]) {
					tunel5.await();
				}
				tuneles[4] = false;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + tun);
			}
		} finally {
			l.unlock();
		}
	}

	// Libera el tunel que estaba usando
	public void salirLavar(int tun, int lavado) { 
													
		l.lock();
		try {
			switch (tun) {
			case 1: {
				tuneles[0] = true;
				tiempoEspera[0] = tiempoEspera[0] - lavado;
				tunel1.signal();
				break;
			}
			case 2: {
				tuneles[1] = true;
				tiempoEspera[1] = tiempoEspera[1] - lavado;
				tunel2.signal();
				break;
			}
			case 3: {
				tuneles[2] = true;
				tiempoEspera[2] = tiempoEspera[2] - lavado;
				tunel3.signal();
				break;
			}
			case 4: {
				tuneles[3] = true;
				tiempoEspera[3] = tiempoEspera[3] - lavado;
				tunel4.signal();
				break;
			}
			case 5: {
				tuneles[4] = true;
				tiempoEspera[4] = tiempoEspera[4] - lavado;
				tunel5.signal();
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + tun);
			}
		} finally {
			l.unlock();
		}
	}

}