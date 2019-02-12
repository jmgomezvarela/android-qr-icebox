package modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tesina.qr.ExcepcionesGenerales;
import tesina.qr.utils.Utils;

import modelo.conexion.Conexion;
import modelo.vo.ProductoVo;

/**
 * Clase que permite el acceso a la base de datos
 * @author jgomezva
 *
 */
public class ProductoDao extends Thread 
{	
	private ProductoVo producto;
	private List<ProductoVo> listaProducto;
	private String[] listaProductosPrecargados;
	private static String operacion;
	private List<Integer> identificadores;
		  	 	
	public void registrarProducto(ProductoVo miProducto) throws ExcepcionesGenerales{
		operacion = "alta";
		if(miProducto == null){
			throw new ExcepcionesGenerales("Se deben ingresar datos"); 
		}
		Date fechaVencimiento = new Date(miProducto.getFechaCaducidad());      		 
		int dias = Utils.getInstance().getDifferenceDays(new Date(),fechaVencimiento); 
		if(miProducto.getNombre().length()<1 || miProducto.getNombre()==null){
			throw new ExcepcionesGenerales("No se ha ingresado nombre"); 
		}else if(dias < 1){
			throw new ExcepcionesGenerales("Fecha vencimiento menor a la actual"); 
		}
		this.setProducto(miProducto);
		this.start();
    }
	
	public void modificarProducto(ProductoVo miProducto) throws ExcepcionesGenerales{
		operacion = "modificacion";
		if(miProducto == null){
			throw new ExcepcionesGenerales("Se deben ingresar datos"); 
		}
		Date fechaVencimiento = new Date(miProducto.getFechaCaducidad());      		 
		int dias = Utils.getInstance().getDifferenceDays(new Date(),fechaVencimiento); 
		if(miProducto.getNombre().length()<1 || miProducto.getNombre()==null){
			throw new ExcepcionesGenerales("No se ha ingresado nombre"); 
		}else if(dias < 1){
			throw new ExcepcionesGenerales("Fecha vencimiento menor a la actual"); 
		}
		this.setProducto(miProducto);
		this.start();
    }
	
	public List<ProductoVo> obtenerProductosAll(){
		operacion = "selectAll";
		this.start();
		return listaProducto;
    }
	
	public String[] obtenerProductosPrecargados(){
		operacion = "selectProductosPrecargados";
		this.start();
		return listaProductosPrecargados;
    }
	
	public void eliminarProducto(List<Integer> idEliminarList){
		operacion = "eliminar";
		this.identificadores = idEliminarList;
		this.start();
	}
	
	public ProductoVo getProducto() {
		return producto;
	}

	public void setProducto(ProductoVo producto) {
		this.producto = producto;
	}
	
	public String getFechaCaducidadFormato(long fechaCaducidad){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechacaducidadResultado = sdf.format(fechaCaducidad);
		return fechacaducidadResultado;
	}
	
	public String getFechaActual(){
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	
	public void resultSetToArrayList(ResultSet rs) throws SQLException{
		listaProducto = new ArrayList<ProductoVo>();
		while (rs.next()) {
			 ProductoVo producto = new ProductoVo();
			 producto.setIdProducto(rs.getInt("idProducto"));
		     producto.setNombre(rs.getString("nombre"));
		     producto.setImagen(rs.getString("imagen"));
		     producto.setFechaCaducidad(rs.getDate("fechaCaducidad").getTime());		   
		     listaProducto.add(producto);
        }
	}
	
	public void resultSetToArrayPrecargadosList(ResultSet rs) throws SQLException{
		int size= 0;
		if (rs != null)   
		{  
		  rs.beforeFirst();  
		  rs.last();  
		  size = rs.getRow();  
		  rs.beforeFirst();  
 	
		  listaProductosPrecargados = new String[size];
		  int i = 0;
		  while (rs.next()) {
			  listaProductosPrecargados[i]=rs.getString("pp_nombre");	  
			  i++;
		  }
		} 
	}

	@Override
	public void run() {
		//SelectAll
		 if (operacion =="selectAll"){
			 try{
   	         //obtenemos el driver de para mysql
   	         Class.forName("com.mysql.jdbc.Driver");
   	         //obtenemos la conexion
   	         Connection conn = DriverManager.getConnection(Conexion.getUrl(),Conexion.getLogin(),Conexion.getPassword()); 	    	         
   	    	 String stsql = "SELECT * FROM  `Productos` where visible = 1 ORDER BY `fechaCaducidad` ";
   	    	 Statement st = conn.createStatement();
   	    	 resultSetToArrayList(st.executeQuery(stsql));
   	    	 conn.close();	    	    		         
   	         if (conn!=null){   	          
   	            conn=null;
   	         }
   	      }
   	      catch(SQLException e){
   	         System.out.println(e);
   	      }catch(ClassNotFoundException e){
   	         System.out.println(e);
   	      }catch(Exception e){
   	         System.out.println(e);
   	      }
		 }
		//SelectProductosPrecargados
		 if (operacion =="selectProductosPrecargados"){
			 try{
   	         //obtenemos el driver de para mysql
   	         Class.forName("com.mysql.jdbc.Driver");
   	         //obtenemos la conexion
   	         Connection conn = DriverManager.getConnection(Conexion.getUrl(),Conexion.getLogin(),Conexion.getPassword()); 	    	         
   	    	 String stsql = "SELECT DISTINCT * FROM ((SELECT DISTINCT pp_nombre FROM `PRODUCTOS_PRECARGADOS`) UNION (SELECT DISTINCT nombre FROM `Productos`)) AS a ORDER BY a.pp_nombre";
   	    	 Statement st = conn.createStatement();
   	    	 resultSetToArrayPrecargadosList(st.executeQuery(stsql));
   	    	 conn.close();	    	    		         
   	         if (conn!=null){
   	            conn=null;
   	         }
   	      }
   	      catch(SQLException e){
   	         System.out.println(e);
   	      }catch(ClassNotFoundException e){
   	         System.out.println(e);
   	      }catch(Exception e){
   	         System.out.println(e);
   	      }
		 }
		//Alta
		 if (operacion == "alta"){
		 try{
	         //obtenemos el driver de para mysql
	         Class.forName("com.mysql.jdbc.Driver");
	         //obtenemos la conexion
	         Connection conn = DriverManager.getConnection(Conexion.getUrl(),Conexion.getLogin(),Conexion.getPassword()); 	    	         
	    	 String stsql = "INSERT INTO `Productos`(`idProducto`, `nombre`, `descripcion`, `fechaRegistro`, `fechaCaducidad`, `imagen`, `idUsuario`) VALUES (NULL,'"+producto.getNombre()+"','','"+getFechaActual()+"','"+getFechaCaducidadFormato(producto.getFechaCaducidad())+"','"+producto.getImagen()+"',0)";
	    	 Statement st = conn.createStatement();
	    	 st.executeUpdate(stsql);
	    	 conn.close();	    	    		         
	         if (conn!=null){
	            System.out.println("Coneccion a base de datos OK");  
	            conn=null;
	         }
	      }
	      catch(SQLException e){
	         System.out.println(e);
	      }catch(ClassNotFoundException e){
	         System.out.println(e);
	      }catch(Exception e){
	         System.out.println(e);
	      }
		 }
		//Modificacion
		 if (operacion == "modificacion"){
		 try{
	         //obtenemos el driver de para mysql
	         Class.forName("com.mysql.jdbc.Driver");
	         //obtenemos la conexion
	         Connection conn = DriverManager.getConnection(Conexion.getUrl(),Conexion.getLogin(),Conexion.getPassword()); 	    	         
	    	 String stsql = "UPDATE `Productos` SET `nombre` = '"+producto.getNombre()+"', `fechaCaducidad` = '"+getFechaCaducidadFormato(producto.getFechaCaducidad())+ "' ,  `imagen` = '"+producto.getImagen()+"' Where idProducto = " + producto.getIdProducto();
	    	 Statement st = conn.createStatement();
	    	 st.executeUpdate(stsql);
	    	 conn.close();	    	    		         
	         if (conn!=null){	          
	            conn=null;
	         }
	      }
	      catch(SQLException e){
	         System.out.println(e);
	      }catch(ClassNotFoundException e){
	         System.out.println(e);
	      }catch(Exception e){
	         System.out.println(e);
	      }
		 }
		 //Eliminar
		 if(operacion == "eliminar"){
			 try{
		         //obtenemos el driver de para mysql
		         Class.forName("com.mysql.jdbc.Driver");
		         //obtenemos la conexion
		         Connection conn = DriverManager.getConnection(Conexion.getUrl(),Conexion.getLogin(),Conexion.getPassword()); 	    	         
		    	 
		         for(Integer identificador: this.identificadores){
		        	 String stsql = "UPDATE `Productos` SET visible = 0 WHERE idProducto = '"+identificador+"'";
		        	 Statement st = conn.createStatement();
		        	 st.executeUpdate(stsql);
		         }
		    	 
		    	 conn.close();	    	    		         
		         if (conn!=null){	          
		            conn=null;
		         }
		      }
		      catch(SQLException e){
		         System.out.println(e);
		      }catch(ClassNotFoundException e){
		         System.out.println(e);
		      }catch(Exception e){
		         System.out.println(e);
		      }
		 }
	}

	public List<ProductoVo> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<ProductoVo> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public String[] getListaProductosPrecargados() {
		return listaProductosPrecargados;
	}

	public void setListaProductosPrecargados(String[] listaProductosPrecargados) {
		this.listaProductosPrecargados = listaProductosPrecargados;
	}
}