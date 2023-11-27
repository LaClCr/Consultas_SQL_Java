package clases;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import vista.*;

public class Controlador {

	private Modelo model;
	private Vista vista;
	private Login login;
	private ActionListener actionListenerConectaLogin;
	private ActionListener actionListenerRealitzaConsulta;
	private ActionListener actionListenerTancaSessio;
	private ActionListener actionListenerTancaConnexio;

	
	
	 /**
     * Constructor del controlador.
     *
     * @param model Model de l'aplicacio.
     * @param vista Vista de l'aplicacio.
     * @param login Pantalla d'inici de sessio.
     */
	
	public Controlador(Modelo model, Vista vista, Login login) {
		super();
		this.model = model;
		this.vista = vista;
		this.login = login;

		boolean ok = model.conectarBD("client");
		
		if (ok) {
			login.setVisible(true);
		} else {
			vista.mostrarMissatge("Ha fallat la connexió a la base de dades", "Error");
		}

		this.actionListenerConectaLogin = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Login();

			}
		};

		login.getBtnAceptar().addActionListener(actionListenerConectaLogin);

		this.actionListenerRealitzaConsulta = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String consulta = vista.getTxtConsulta().getText();
				vista.buidaTabla();
				try {
					if(!model.getConnexio().isClosed()) {
						
						if (model.esSelect(consulta)) {

							try {
								ResultSet rs = model.executarConsultaSelect(consulta);
								
								if (rs == null) {
									vista.mostrarMissatge("No hi han registres", "Resultats");
								}else {
									vista.actualitzarTabla(rs);
								}
								
							} catch (Exception e1) {
								vista.mostrarMissatge("ERROR AL CARREGAR LA TABLA", "ERROR");
								e1.printStackTrace();
							}

						} else {
							
							if(model.isEsAdmin()) {
								
								boolean confirmacio = vista.demanarConfirmacio("Estàs segur/a de que vols realitzar aquesta consulta? \n" + consulta);
								if(confirmacio) {
									int filesAfectades = model.executarConsultaNoSelect(consulta);

									if (filesAfectades == -1) {
										vista.mostrarMissatge("No s'ha pogut realitzar la consulta", "Operació incorrecta");
									} else {
										vista.mostrarMissatge("Files afectades: " + filesAfectades, "Operació exitosa");
									}
								}
							
							}else {
								vista.mostrarMissatge("No tens privilegis d'admin per a aquesta consulta", "Operació no realitzada");
							}

						}
					}else {
						vista.mostrarMissatge("Connexió a la base de dades no disponible", "Operació no realitzada");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};

		vista.getBtnEnviaConsulta().addActionListener(actionListenerRealitzaConsulta);
		
		this.actionListenerTancaConnexio = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean confirmacio = vista.demanarConfirmacio("Estàs segur/a de que vols tancar la connexió a la base de dades?\nL'aplicacó es tancarà.");
				
				if(confirmacio) {
					model.tancaConnexio();
					vista.tancaVista();
				}
				
			}
		};
		
		vista.getBtnTancaConnexio().addActionListener(actionListenerTancaConnexio);
		
		
		this.actionListenerTancaSessio = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean confirmacio = vista.demanarConfirmacio("Estàs segur/a de que vols tancar la sessió?");
				
				if(confirmacio) {
					model.tancaConnexio();
					model.conectarBD("client");
					login.setVisible(true);
					vista.tancaVista();
					vista.buidaCamps();
				}
				
			}
		};
		
		vista.getBtnTancaSessio().addActionListener(actionListenerTancaSessio);
	}

	
	/**
     * Gestiona el proces d'inici de sessio de l'usuari.
     */
	public void Login() {

		String user = login.getTxtUserName().getText();
		char[] contrasenyaCharArray = login.getTxtContra().getPassword();
		String contrasenya = new String(contrasenyaCharArray);
		if (model.existeixUsuari(user)) {
			String r = model.comprovaLogin(user, contrasenya);

			if (r.isEmpty()) {
				vista.mostrarMissatge("Contrasenya incorrecta.", "Error");
			} else {

				if (r.equalsIgnoreCase("admin")) {
					model.tancaConnexio();
					model.conectarBD("admin");
				}

				login.tancaLogin();
				vista.setVisible(true);

			}
		}else {
			vista.mostrarMissatge("El usuari o contrasenya no son correctes", "Error");
		}

	}

}