package ejercicio4;
import messagepassing.*;

class Main {
	public static void main(String[] args) {

		MailBox[] buzonesUsuarios = new MailBox[120];
		MailBox pantalla = new MailBox();
		MailBox registro = new MailBox();
		MailBox votar = new MailBox();
		MailBox admin = new MailBox();
		MailBox server = new MailBox();
		MailBox listaCandidatos = new MailBox();

		pantalla.send("");

		for (int i = 0; i < 120; i++) {
			buzonesUsuarios[i] = new MailBox();
		}

		Admin administrador = new Admin(admin, server);
		Servidor servidor = new Servidor(pantalla, registro, votar, server, admin, listaCandidatos, buzonesUsuarios);
		Usuario[] usuarios = new Usuario[120];

		servidor.start();
		administrador.start();

		for (int i = 0; i < 120; i++) {
			usuarios[i] = new Usuario(i, buzonesUsuarios[i], pantalla, registro, votar, listaCandidatos);
			usuarios[i].start();
		}
		
		try {
			servidor.join();
			administrador.join();
			for (int i = 0; i < 120; i++) {
				usuarios[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("FIN DEL PROGRAMA");
	}

}