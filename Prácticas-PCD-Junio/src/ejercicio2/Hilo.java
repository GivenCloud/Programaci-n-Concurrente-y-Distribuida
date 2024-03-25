package ejercicio2;

import java.util.Random;

public class Hilo extends Thread {

	private int id;

	public Hilo(int id) {
		this.id = id;
	}

	// Panel 1 operaciones que sean múltiplo de 5.
	// Panel 2 y 3 resultado impar y no es múltiplo de 5.
	// Panel 4 y 5 resultado par y no es múltiplo de 5.

	
	public void run() {
		for (int i = 0; i < 30; i++) {

			//SE ESCOGE UNA OPERACIÓN Y DOS NÚMEROS AL AZAR ENTRE 0 Y 100
			Random rand = new Random();
			int num1 = rand.nextInt(101);
			int num2 = rand.nextInt(101);
			int operacion = rand.nextInt(3);
			int resultado;
			String op;

			if (operacion == 0) {
				resultado = num1 + num2;
				op = "Suma";
			} else if (operacion == 1) {
				resultado = num1 - num2;
				op = "Resta";
			} else {
				resultado = num1 * num2;
				op = "Multiplicación";
			}

			//SI EL RESULTADO ES MÚLTIPLO DE 5 SE ESCOGE EL PANEL 1
			if (resultado % 5 == 0) {
				try {
					//SE CIERRA EL SEMAFORO PARA QUE NINGÚN HILO ENTRE MIENTRAS ESTÁ ESCRIBIENDO
					Main.semP1.acquire();
				} catch (InterruptedException e) {
				}
				//IMPRIME EN EL PANEL
				Main.panel1.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados " + num1 + " " + num2
						+ "\nOperación a realizar: " + op + "\nResultado: " + resultado + "\nFin hilo " + id + "\n");
				//SE LIBERA EL SEMAFORO
				Main.semP1.release();

			} else if (resultado % 2 != 0) {
				int p = 0;
				try {
					Main.semP2P3.acquire();
				} catch (InterruptedException e) {
				}

				try {
					Main.mutexP2P3.acquire();
				} catch (InterruptedException e) {
				}

				//SE ESCOGE EL PANEL
				if (Main.p2 == true) {
					Main.p2 = false;
					p = 2;
				} else {
					Main.p3 = false;
					p = 3;
				}
				Main.mutexP2P3.release();

				if (p == 2) {
					Main.panel2.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados " + num1 + " " + num2
							+ "\nOperación a realizar: " + op + "\nResultado: " + resultado + "\nFin hilo " + id
							+ "\n");

				} else {
					Main.panel3.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados " + num1 + " " + num2
							+ "\nOperación a realizar: " + op + "\nResultado: " + resultado + "\nFin hilo " + id
							+ "\n");
				}

				try {
					Main.mutexP2P3.acquire();
				} catch (InterruptedException e) {
				}
				
				//SE LIBERA EL PANEL
				if (p == 2) {
					Main.p2 = true;
				} else {
					Main.p3 = true;
				}

				Main.mutexP2P3.release();
				Main.semP2P3.release();

			} else {
				int p = 0;
				try {
					Main.semP4P5.acquire();
				} catch (InterruptedException e) {
				}

				try {
					Main.mutexP4P5.acquire();
				} catch (InterruptedException e) {
				}

				if (Main.p4 == true) {
					Main.p4 = false;
					p = 4;
				} else {
					Main.p5 = false;
					p = 5;
				}
				Main.mutexP4P5.release();

				if (p == 4) {
					Main.panel4.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados " + num1 + " " + num2
							+ "\nOperación a realizar: " + op + "\nResultado: " + resultado + "\nFin hilo " + id
							+ "\n");

				} else {
					Main.panel5.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados " + num1 + " " + num2
							+ "\nOperación a realizar: " + op + "\nResultado: " + resultado + "\nFin hilo " + id
							+ "\n");
				}

				try {
					Main.mutexP4P5.acquire();
				} catch (InterruptedException e) {
				}

				if (p == 4) {
					Main.p4 = true;
				} else {
					Main.p5 = true;
				}

				Main.mutexP4P5.release();
				Main.semP4P5.release();

				/*
				 * int p = 0; while (p == 0) { try { Main.mutexP4P5.acquire(); } catch
				 * (InterruptedException e) { }
				 * 
				 * if (Main.p4 == true) { Main.p4 = false; p = 4; } else { Main.p5 =
				 * false; p = 5; } Main.mutexP4P5.release(); } try {
				 * Main.semP4P5.acquire(); } catch (InterruptedException e) { }
				 * 
				 * if (p == 4) { Main.panel4.escribir_mensaje("Hilo con ID " + id +
				 * "\nNúmeros generados " + num1 + " " + num2 + "\nOperación a realizar: " + op
				 * + "\nResultado: " + resultado + "\nFin hilo " + id + "\n"); } else {
				 * Main.panel5.escribir_mensaje("Hilo con ID " + id + "\nNúmeros generados "
				 * + num1 + " " + num2 + "\nOperación a realizar: " + op + "\nResultado: " +
				 * resultado + "\nFin hilo " + id + "\n"); } Main.semP4P5.release();
				 * 
				 * try { Main.mutexP4P5.acquire(); } catch (InterruptedException e) { }
				 * 
				 * if (p == 4) { Main.p4 = true; } else { Main.p5 = true; }
				 * Main.mutexP4P5.release();
				 * 
				 */
			}
		}
	}

}
