package clases;

import vista.*;

public class Principal {
	public static void main(String[] args) {

		Modelo m = new Modelo();
		Vista v = new Vista();
		Login l = new Login();
		@SuppressWarnings("unused")
		Controlador c = new Controlador(m, v,l);
	}
}

