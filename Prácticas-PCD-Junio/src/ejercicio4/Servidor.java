package ejercicio4;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import messagepassing.MailBox;
import messagepassing.Selector;

class Servidor extends Thread {

	private ArrayList<String> listaCandidatos = new ArrayList<String>();
	private int[] votosCandidatos = new int [5]; 
	private int[] conjuntoUsuarioRegistrados = new int[100];
	private MailBox[] buzonesUsuarios = new MailBox[120];
	private Set<Integer> conjuntoTokens = new HashSet<>();
	private Selector s;

	private MailBox pantalla, registro, voto, servidor, admin, candidatos;

	private int usuariosRegistrados = 0;
	private boolean fin = false;
	private boolean votacionAbierta = false;

	public Servidor(MailBox pantalla, MailBox registro, MailBox voto, MailBox servidor, MailBox admin, MailBox candidatos, MailBox[] buzonesUsuarios) {

		this.pantalla = pantalla;
		this.registro = registro;
		this.voto = voto;
		this.servidor = servidor;
		this.admin = admin;
		this.candidatos = candidatos;
		this.buzonesUsuarios = buzonesUsuarios;
		this.listaCandidatos.add("Alberto");
		this.listaCandidatos.add("Roberto");
		this.listaCandidatos.add("Antonio");
		this.listaCandidatos.add("Lucía");
		this.listaCandidatos.add("María");
		s = new Selector();						// Orden de los case:
		s.addSelectable(registro, false); 		// 1
		s.addSelectable(candidatos, false); 	// 2
		s.addSelectable(voto, false); 			// 3
		s.addSelectable(servidor, false); 		// 4 

	}

	public void run() {
		while (!fin) {
			//voto.setGuardValue(usuariosRegistrados >= 50);
			switch (s.selectOrTimeout(1000)) {
			case 1: 
				int id = (int) registro.receive(); 
				if (usuariosRegistrados < 100) {    // Si hay menos de 100 usuarios registrados se registra a el usuario y se le genera un token.
					int token = id;
					conjuntoTokens.add(token);
					buzonesUsuarios[id].send(token);
					conjuntoUsuarioRegistrados[usuariosRegistrados] = token;
					usuariosRegistrados++;
					if (usuariosRegistrados == 50) {	// Si ya hay 50 usuarios registrados se abre la votación.
						votacionAbierta = true;
						pantalla.receive();
						System.out.println("Votación abierta: ");
						pantalla.send("");
						admin.send("");
					}
				} else {
					buzonesUsuarios[id].send(-1);
				}
				break;
			
			case 2: 
				int id2 = (int) candidatos.receive();
				buzonesUsuarios[id2].send(listaCandidatos);	// Se le manda la lista de candidados al usuario.
				break;
			
			case 3: 
				int paquete[] = (int[]) voto.receive();		// Recibe el voto del usuario y lo guarda.
				int id3 = paquete[0];						// Si la votación ha sido exitosa devuelve true, si no devuelve false.
				if (votacionAbierta) {
					if (conjuntoTokens.contains(id3)) {
						votosCandidatos[paquete[1]]++;
						buzonesUsuarios[id3].send(true);
					} else {
						buzonesUsuarios[id3].send(false);
					}
				} else {
					buzonesUsuarios[id3].send(false);
				}
				break;
				
			case 4:
				servidor.receive();				// Recibe el mesanje del admin para cerrar la votación.
				votacionAbierta = false;
				fin = true;
				break;
			default:
			}
		}
		int max = -1;
		String candidatoGanador = "";			// Si hay mas de 25 votos en total, la votación es válida y se muestra al ganador por pantalla.
		int totalVotos = 0;
		for (int i = 0; i < votosCandidatos.length; i++) {
			if (votosCandidatos[i] > max) {
				max = votosCandidatos[i];
				candidatoGanador = listaCandidatos.get(i);
			}
			totalVotos += votosCandidatos[i];
		}
		if (totalVotos >= 25) {
			pantalla.receive();
			System.out.println("El candidato ganador es " + candidatoGanador + " con un total de " + max + " votos.");
			pantalla.send("");
		} else {
			System.out.println("La votacion no ha sido valida.");
		}
	}
}