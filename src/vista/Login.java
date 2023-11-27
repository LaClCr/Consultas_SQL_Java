package vista;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtUserName;
	private JPasswordField txtContra;
	private JButton btnAceptar;
	private static Font font = new Font("Consolas", Font.PLAIN, 14);

	/**
	 * Constructor de la classe Login. Inicialitza la finestra d'inici de sessio i
	 * els seus components.
	 */
	public Login() {
		setTitle("Inici de Sessió");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setLocation(550, 350);

		JPanel panelLogin = new JPanel(new GridLayout(3, 1, 10, 10));

		JPanel userPanel = new JPanel(new BorderLayout());
		userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		txtUserName = new JTextField(20);
		txtUserName.setFont(font);

		JLabel lblUsuari = new JLabel("Usuari:");
		lblUsuari.setFont(font);
		userPanel.add(lblUsuari, BorderLayout.NORTH);
		userPanel.add(txtUserName, BorderLayout.CENTER);

		JPanel passwordPanel = new JPanel(new BorderLayout());
		passwordPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		txtContra = new JPasswordField(20);
		JLabel lblContra = new JLabel("Contrasenya:");
		lblContra.setFont(font);
		passwordPanel.add(lblContra, BorderLayout.NORTH);
		passwordPanel.add(txtContra, BorderLayout.CENTER);

		btnAceptar = new JButton("Aceptar");

		panelLogin.add(userPanel);
		panelLogin.add(passwordPanel);
		panelLogin.add(btnAceptar);

		add(panelLogin);
	}

	public JTextField getTxtUserName() {
		return txtUserName;
	}

	public JPasswordField getTxtContra() {
		return txtContra;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	
	/**
	 * Tanca la finestra d'inici de sessio i buida els camps de text.
	 */
	public void tancaLogin() {
		this.dispose();
		txtContra.setText("");
		txtUserName.setText("");
	}

}