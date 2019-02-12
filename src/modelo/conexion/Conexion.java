package modelo.conexion;

/**
 * Clase que permite conectar con la base de datos
 * @author jgomezva
 *
 */
public class Conexion { 
   private static final String bd = "QRIceBox";
   private static final String login = "root";
   private static final String password = "naranja6655321";
   private static final String url = "jdbc:mysql://10.0.2.2/"+bd;

   public static String getBd() {
	return bd;
   }

   public static String getLogin() {
	return login;
   }

   public static String getPassword() {
	return password;
   }

   public static String getUrl() {
	return url;
   }
}