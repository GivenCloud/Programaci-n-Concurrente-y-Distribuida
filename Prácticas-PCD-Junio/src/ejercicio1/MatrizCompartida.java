package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

public class MatrizCompartida {
	//INICIALIZACION DE LAS VARIABLES Y DEL CANDADO
	private int[][] A = {{6,7,5}, {3,8,4}, {1,0,2}};
	private int[][] C = {{6,7,5,0,6,7,5}, {3,8,4,0,3,8,4}, {1,0,2,0,1,0,2}};
	private ReentrantLock l = new ReentrantLock();
	//FUNCION PARA HACER LA SUMA DE MATRICES
	public void sumarMatriz() {
		int[][] B = new int[3][3];
		for (int i = 0; i < 3; i++) {	
			for (int j = 0; j < 3; j++) {
				B[i][j] = A[i][j] + A[i][j];
			}
		}	
		//TENEMOS QUE PROTEGER LA PANTALLA AL IMPRIMIR EN ELLA
		//PARA ESO USAMOS EL METODO REENTRANTLOCK
		l.lock();
		try {
			System.out.println("A + A");
			for (int filas = 0; filas < 3; filas++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int columnas = 0; columnas < 7; columnas++) {
					System.out.print(C[filas][columnas] + " ");
					if (columnas == 2 && filas != 1) {
						System.out.print("    ");
						columnas++;
					}
					else if (columnas == 2 && filas == 1) {
						System.out.print(" +  ");
						columnas++;
					}
					
					
				}
				
				System.out.println();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\n2A");
			
			for (int i = 0; i < 3; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int j = 0; j < 3; j++) {
					System.out.print(B[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println();
		//LIBERAMOS LA PANTALLA PORQUE NO LA VAMOS A USAR MAS
		} finally {
			l.unlock();
		}
	}
	
	//FUNCION PARA HACER LA MULTIPLICACION DE MATRICES
	public void multiplicarMatriz() {
		int[][] B = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					B[i][j] += A[i][k] * A[k][j];
				}
			}
		}
		//VOLVEMOS A PROTEGER LA PANTALLA AL IGUAL QUE ANTES PARA IMPRIMIR
		l.lock();
		try {
			System.out.println("A x A");
			for (int filas = 0; filas < 3; filas++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int columnas = 0; columnas < 7; columnas++) {
					System.out.print(C[filas][columnas] + " ");
					if (columnas == 2 && filas != 1) {
						System.out.print("    ");
						columnas++;
					}
					else if (columnas == 2 && filas == 1) {
						System.out.print(" x  ");
						columnas++;
					}
					
					
				}
				
				System.out.println();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\nA^2");
			
			for (int i = 0; i < 3; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int j = 0; j < 3; j++) {
					System.out.print(B[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
		//VOLVEMOS A LIBERARLA
			l.unlock();
		}
	}
}
