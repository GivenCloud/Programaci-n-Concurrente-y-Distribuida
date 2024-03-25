package ejercicio4;
import messagepassing.*;

class Admin extends Thread {
	MailBox admin, servidor;

	public Admin(MailBox admin, MailBox servidor) {
		this.admin = admin;
		this.servidor = servidor;
	}

	public void run() {
		admin.receive();
		try {
			Thread.sleep((long) (1000));
		} catch (InterruptedException e) {		// El servidor hace un sleep(1000) cuando recibe el mensaje de que la votación ha empezado,
			e.printStackTrace();				// cuando se despierta manda un mensaje al servidor para que termine la votación.
		}
		servidor.send("Cerrar la votación");
	}
}
