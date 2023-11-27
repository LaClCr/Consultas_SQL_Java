package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JDialog dialogInicioSesion;
	private JTextField txtUserName;
	private JPasswordField txtContra;
	private JPanel contentPane;
	private JTextArea txtConsulta;
	private JButton btnEnviaConsulta;
	private JTable tablaDatos;
	private JButton btnTancaSessio;
	private JButton btnTancaConnexio;
	private JButton btnAceptar;
	private DefaultTableModel tableModel;
	private static Font font = new Font("Consolas", Font.PLAIN, 14);

	
	/**
     * Constructor de la classe Vista. Inicialitza la finestra principal i els seus components.
     */
	public Vista() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Gestor BBDD Biblioteca");
		setSize(800, 600);
		setLocation(350, 100);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		JPanel panelIzquierdo = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panelIzquierdo.setLayout(gridBagLayout);
		panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

		txtConsulta = new JTextArea(10, 10);
		txtConsulta.setFont(font);
		txtConsulta.setForeground(Color.BLACK);

		btnEnviaConsulta = new JButton("Enviar consulta");
		btnEnviaConsulta.setFont(font);

		tableModel = new DefaultTableModel();
		tablaDatos = new JTable(tableModel);
		tablaDatos.setFont(font);
		JScrollPane scrollPaneTabla = new JScrollPane(tablaDatos);

		GridBagConstraints cConsulta = new GridBagConstraints();
		cConsulta.fill = GridBagConstraints.BOTH;
		cConsulta.weightx = 3.0;
		cConsulta.weighty = 1.0;

		GridBagConstraints cBoton = new GridBagConstraints();
		cBoton.fill = GridBagConstraints.BOTH;
		cBoton.weightx = 3.0;
		cBoton.weighty = 0.1;

		GridBagConstraints cTabla = new GridBagConstraints();
		cTabla.fill = GridBagConstraints.BOTH;
		cTabla.weightx = 3.0;
		cTabla.weighty = 1.0;

		cConsulta.gridx = 0;
		cConsulta.gridy = 0;
		panelIzquierdo.add(new JScrollPane(txtConsulta), cConsulta);

		cBoton.gridx = 0;
		cBoton.gridy = 1;
		panelIzquierdo.add(btnEnviaConsulta, cBoton);

		cTabla.gridx = 0;
		cTabla.gridy = 2;
		panelIzquierdo.add(scrollPaneTabla, cTabla);

		contentPane.add(panelIzquierdo, BorderLayout.CENTER);

		JPanel panelDerecho = new JPanel();
		panelDerecho.setLayout(new GridLayout(2, 1));
		panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		btnTancaSessio = new JButton("Tanca Sessió");
		btnTancaSessio.setFont(font);
		btnTancaSessio.setForeground(Color.BLACK);

		btnTancaConnexio = new JButton("Tanca Connexió");
		btnTancaConnexio.setFont(font);
		btnTancaConnexio.setForeground(Color.BLACK);

		panelDerecho.add(btnTancaSessio);
		panelDerecho.add(btnTancaConnexio);

		contentPane.add(panelDerecho, BorderLayout.EAST);
		
	}
	
	
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JTextField getTxtUserName() {
		return txtUserName;
	}

	public JPasswordField getTxtContra() {
		return txtContra;
	}

	public JTextArea getTxtConsulta() {
		return txtConsulta;
	}

	public JButton getBtnEnviaConsulta() {
		return btnEnviaConsulta;
	}

	public JButton getBtnTancaSessio() {
		return btnTancaSessio;
	}

	public JButton getBtnTancaConnexio() {
		return btnTancaConnexio;
	}

	public void mostrarLogin() {

		dialogInicioSesion.setVisible(true);
	}
	
	
	
	/**
     * Mostra un missatge en una finestra d'informacio.
     *
     * @param missatge Missatge a mostrar.
     * @param titol  Titol de la finestra.
     */

	public void mostrarMissatge(String missatge, String titol) {
		JOptionPane.showMessageDialog(null, missatge, titol, JOptionPane.INFORMATION_MESSAGE);
	}

	
	
	
	
	/**
     * Demana una confirmacio de l'usuari.
     *
     * @param pregunta Pregunta a mostrar.
     * @return True si l'usuari ha confirmat amb "Si," fals si ha confirmat amb "No" o ha tancat la finestra.
     */
	public boolean demanarConfirmacio(String pregunta) {
		int resposta = JOptionPane.showConfirmDialog(null, pregunta, "Confirmación", JOptionPane.YES_NO_OPTION);
		return resposta == JOptionPane.YES_OPTION;
	}
	
	/**
     * Tanca la finestra principal.
     */
	public void tancaVista() {
		this.dispose();
	}
	
	
	/**
     * Buida la taula de resultats.
     */
	public void buidaTabla() {
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
	}
	
	 /**
     * Buida els camps de consulta i la taula de resultats.
     */
	public void buidaCamps() {
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
		txtConsulta.setText("");
	}
	
	
	/**
     * Actualitza la taula de resultats amb dades d'un objecte ResultSet.
     *
     * @param resultados Objecte ResultSet amb les dades de la consulta.
     * @throws Exception En cas d'error en l'obtenció de dades del ResultSet.
     */
	public void actualitzarTabla(ResultSet resultados) throws Exception {

		

		ResultSetMetaData metaData = resultados.getMetaData();
		int numColumnes = metaData.getColumnCount();
        
       
		String[] nomColumnes = new String[numColumnes];

		
		for (int i = 1; i <= numColumnes; i++) {
			nomColumnes[i -1 ] = metaData.getColumnName(i);
		}
		
		
        tableModel.setColumnIdentifiers(nomColumnes);
		while (resultados.next()) {
			Vector<Object> fila = new Vector<>();
			for (int i = 1; i <= numColumnes; i++) {
				fila.add(resultados.getObject(i));
			}
			tableModel.addRow(fila);
			tablaDatos.repaint();
		}
	}


}