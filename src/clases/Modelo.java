package clases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Modelo {

	private Connection connexio;

	private boolean esAdmin = false;


	/**
     * Constructor buit de la classe Modelo.
     */
	public Modelo() {
		super();
	}

	 /**
     * Obte el valor que indica si l'usuari te permisos d'administrador.
     *
     * @return True si l'usuari es administrador, fals altrament.
     */
	public boolean isEsAdmin() {
		return esAdmin;
	}

	/**
     * Obte la connexio a la base de dades.
     *
     * @return L'objecte de connexio a la base de dades.
     */
	public Connection getConnexio() {
		return connexio;
	}

	
	
	/**
     * Connecta a la base de dades utilitzant les dades proporcionades en un fitxer XML segons el rol especificat.
     *
     * @param rol El rol de l'usuari ("admin" o "client").
     * @return True si la connexio ha estat exitosa, fals altrament.
     */

	public boolean conectarBD(String rol) {

		String url = "";
		String user = "";
		String pass = "";

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			String xmlPath = "";

			if ("admin".equals(rol)) {

				esAdmin = true;

				xmlPath = "./src/arxius/conAdmin.xml";

				Document document = dBuilder.parse(new File(xmlPath));
				NodeList nodeList = document.getElementsByTagName("database");
				int nombreObjectes = nodeList.getLength();

				for (int i = 0; i < nombreObjectes; i++) {
					Node node = nodeList.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) node;

						url = eElement.getElementsByTagName("url").item(0).getTextContent();
						user = eElement.getElementsByTagName("username").item(0).getTextContent();
						pass = eElement.getElementsByTagName("password").item(0).getTextContent();

					}
				}

			} else if ("client".equals(rol)) {

				esAdmin = false;

				xmlPath = "./src/arxius/conClient.xml";

				Document document = dBuilder.parse(new File(xmlPath));
				NodeList nodeList = document.getElementsByTagName("database");
				int nombreObjectes = nodeList.getLength();

				for (int i = 0; i < nombreObjectes; i++) {
					Node node = nodeList.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) node;

						url = eElement.getElementsByTagName("url").item(0).getTextContent();
						user = eElement.getElementsByTagName("username").item(0).getTextContent();
						pass = eElement.getElementsByTagName("password").item(0).getTextContent();

					}
				}

			}

			Class.forName("com.mysql.cj.jdbc.Driver");
			connexio = DriverManager.getConnection(url, user, pass);
			
			if(connexio != null) {return true;}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/**
     * Tanca la connexio a la base de dades.
     */
	public void tancaConnexio() {
		try {
			connexio.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
	/**
     * Autentica un usuari comparant les dades d'inici de sessio amb la base de dades.
     *
     * @param user     Nom d'usuari.
     * @param password Contrasenya de l'usuari.
     * @return El rol de l'usuari ("admin" o "client") si l'autenticacio es exitosa, una cadena buida altrament.
     */
	public String comprovaLogin(String user, String password) {
		ResultSet rol = null;
		String result = "";

		try {
			PreparedStatement preparedStatement = connexio
					.prepareStatement("select type from users where user = ? and pass=MD5(?)");
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, password);
			rol = preparedStatement.executeQuery();
			if (rol.next()) { 
	            result = rol.getString(1); 
	        }
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("error login");
		}

		return result;//si result està buit, la contrasenya és incorrecta
	}

	
	/**
     * Comprova si un usuari existeix a la base de dades.
     *
     * @param user Nom d'usuari a comprovar.
     * @return True si l'usuari existeix, fals altrament.
     */
	public boolean existeixUsuari(String user) {
		ResultSet rol = null;
		String result = "";

		try {
			PreparedStatement preparedStatement = connexio.prepareStatement("select type from users where user = ?");
			preparedStatement.setString(1, user);
			rol = preparedStatement.executeQuery();
			if (rol.next()) { 
	            result = rol.getString(1); 
	        }
		} catch (Exception e) {
			
			System.out.println("error Existe");
			e.printStackTrace();
		}

		return !result.isEmpty();
	}

	
	/**
     * Executa una consulta SQL que no sigui de tipus "SELECT" a la base de dades.
     *
     * @param consultaSQL La consulta SQL a executar.
     * @return El nombre de files afectades per la consulta o -1 en cas d'error.
     */
	public int executarConsultaNoSelect(String consultaSQL) {
		int resultadoConsulta = -1;
		try {
			Statement statement = connexio.createStatement();
			resultadoConsulta = statement.executeUpdate(consultaSQL);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultadoConsulta;
	}
	
	
	/**
     * Comprova si una consulta SQL es de tipus "SELECT".
     *
     * @param consulta La consulta SQL a comprovar.
     * @return True si la consulta es de tipus "SELECT", fals altrament.
     */
	public boolean esSelect(String consulta) {
		if (consulta != null && !consulta.isEmpty()) {
			
	        String[] palabras = consulta.split(" ");
	        
	        if (palabras.length > 0) {
	            
	            String primeraPalabra = palabras[0].toLowerCase();
	            	            
	            if (primeraPalabra.equals("select")) {
	                return true;
	            }
	        }
	    }
	    
	    return false;
	}

	
	/**
     * Executa una consulta SQL de tipus "SELECT" a la base de dades.
     *
     * @param consultaSQL La consulta SQL de tipus "SELECT" a executar.
     * @return Un objecte ResultSet amb els resultats de la consulta o null en cas d'error.
     */
	public ResultSet executarConsultaSelect(String consultaSQL) {
		ResultSet resultados = null;

		try {
			Statement statement = connexio.createStatement();
			resultados = statement.executeQuery(consultaSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultados;
	}

}