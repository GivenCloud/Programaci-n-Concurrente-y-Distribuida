package ejercicio4;

import java.util.ArrayList;
import java.util.Random;

import messagepassing.*;

class Usuario extends Thread {

	private int id, token, voto;
	private MailBox pantalla, registro, votar, listaCandidatos, usuario;
	private boolean decidoVotar = true;
	private boolean estoyRegistrado = false;
	private boolean heVotado = false;
	private boolean pidoListaCandidatos = true;
	private int[] paquete = new int[2];
	private ArrayList<String> lista = new ArrayList<String>();
	private Selector s;

	public Usuario(int id, MailBox usuario, MailBox pantalla, MailBox registro, MailBox votar, MailBox listaCandidatos) {
		this.id = id;
		this.pantalla = pantalla;
		this.registro = registro;
		this.votar = votar;
		this.listaCandidatos = listaCandidatos;
		this.usuario = usuario;
		
		s = new Selector();
		s.addSelectable(usuario, false);

		Random rand = new Random();
		int numeroAleatorio = rand.nextInt(5); // 1 de cada 5 usuarios decide abstenerse
		if (numeroAleatorio == 1) {
			decidoVotar = false;
		}
		
		Random rand2 = new Random();
		int numeroAleatorio2 = rand2.nextInt(4);  // 1 de cada 4 usuarios no pide la lista
		if (numeroAleatorio2 == 2) {
			pidoListaCandidatos = false;
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		registro.send(id);         // Mando la petición para registrarme, si hay menos de 100 usuarios registrados se genera un token (en este caso es el mismo que la id)
		switch (s.selectOrTimeout(1000)) {     // sino se manda un -1 por el buzon indicando que no se ha podido registrar.
		case 0:
			pantalla.receive();
			System.out.println("Soy el usuario " + id + " y no me he podido registrar.");
			pantalla.send("");
		case 1: 
			token = (int) usuario.receive();
			if (token != -1) {
				estoyRegistrado = true;
				pantalla.receive();
				System.out.println("Soy el usuario " + id + " y me he registrado correctamente, y este es mi token: " + token + ".");
				pantalla.send("");
			} else {
				pantalla.receive();
				System.out.println("Soy el usuario " + id + " y no me he podido registrar.");
				pantalla.send("");
			}
			break;
		}
		
		if (pidoListaCandidatos) {    // El usuario puede pedir la lista de candidatos siempre que quiera.
			listaCandidatos.send(id);
			switch (s.selectOrTimeout(1000)) {
			case 1:	
				lista = (ArrayList<String>) usuario.receive();
			}
		}	
		
		if (estoyRegistrado && decidoVotar) {  // Si el usuario está registrado y quiere votar, se comprueba si ha pedido la lista para ver a los candidatos.
			if (lista.isEmpty()) {			   // Si no la tiene, la pide.
				listaCandidatos.send(id);
				lista = (ArrayList<String>) usuario.receive();
			}
			
			Random rand2 = new Random();	   // Vota a un candidato de forma aleatoria mandando un paquete con su token (es el mismo que su id) y su voto.
			voto = rand2.nextInt(lista.size());
			paquete[0] = token;
			paquete[1] = voto;
			
			votar.send(paquete);
			switch (s.selectOrTimeout(1000)) {
			case 1:
				heVotado = (boolean) usuario.receive();  // Dependiendo de si la votación ha sido exitosa se imprime una de las dos opciones.
				if (heVotado) {
					pantalla.receive();
					System.out.println("Soy el usuario " + id + " y he votado a " + voto + ".");
					pantalla.send("");
				} else {
					pantalla.receive();
					System.out.println("Soy el usuario " + id + " y no han admitido mi voto.");
					pantalla.send("");
				}
			}
		}
		
		if (estoyRegistrado && !decidoVotar) {  // Si el usuario pudo registrarse, pero no ha querido votar.
			pantalla.receive();
			System.out.println("Soy el usuario " + id + " y he decidido abstenerme de votar.");
			pantalla.send("");
		}
												// **IMPORTANTE**
		if (!estoyRegistrado && decidoVotar) {	// Si el usuario quería votar, pero no pudo registrarse.
			pantalla.receive();
			System.out.println("Soy el usuario " + id + " y no han admitido mi voto.");
			pantalla.send("");
		}
	}
}