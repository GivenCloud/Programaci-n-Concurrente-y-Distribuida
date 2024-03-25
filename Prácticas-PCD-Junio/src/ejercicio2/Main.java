package ejercicio2;

import java.util.concurrent.Semaphore;

public class Main {

	//CREAMOS LOS 5 PANELES DE FORMA GLOBAL PARA QUE LOS HILOS PUEDAN ACCEDER A ELLOS PARA ESCRIBIR
	public static Panel panel1 = new Panel("P1", 0, 0);
	public static Panel panel2 = new Panel("P2", 400, 0);
	public static Panel panel3 = new Panel("P3", 800, 0);
	public static Panel panel4 = new Panel("P4", 1200, 0);
	public static Panel panel5 = new Panel("P5", 1600, 0);

	//CREAMOS 5 SEMAFOROS 
	//UNO PARA EL PANEL 1 (MÚLTIPLO DE 5) 
	public static Semaphore semP1 = new Semaphore(1);
	//UNO DE DOS BITS PARA EL PANEL 2 Y 3 (RESULTADOO IMPAR Y NO ES MULTIPLO DE 5)
	public static Semaphore semP2P3 = new Semaphore(2);
	//UNO DE DOS BITS PARA EL PANEL 4 Y 5 (RESULTADOO PAR Y NO ES MULTIPLO DE 5)
	public static Semaphore semP4P5 = new Semaphore(2);
	//UNO DE UN BIT PARA EVITAR QUE DOS O MÁS HILOS ACCEDAN A LA MISMA VARIABLE GLOBAL A LA VEZ (PARA LOS PANELES 2 Y 3)
	public static Semaphore mutexP2P3 = new Semaphore(1);
	////UNO DE UN BIT PARA EVITAR QUE DOS O MÁS HILOS ACCEDAN A LA MISMA VARIABLE GLOBAL A LA VEZ (PARA LOS PANELES 4 Y 5)
	public static Semaphore mutexP4P5 = new Semaphore(1);

	//VARIABLES GLOBALES PARA INDICAR SI EL PANEL ESTA LIBRE O NO
	public static boolean p2 = true;
	public static boolean p3 = true;
	public static boolean p4 = true;
	public static boolean p5 = true;

	//SE CREAN 50 HILOS
	public static void main(String[] args) {
		Thread[] hilo = new Thread[50];
		for (int i = 0; i < 50; i++) {
			hilo[i] = new Hilo(i);
			hilo[i].start();
		}
		for (int j = 0; j < 50; j++) {
			try {
				hilo[j].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
