package modelo.dao;

import java.util.List;
import modelo.vo.ProductoVo;

public class Cache { 
	private List<ProductoVo> listaProductos;
	private static String[] productosPrecargadosList;
	private int indice;
	
	   // El constructor debe ser Protected para evitar su acceso desde fuera.
	   protected Cache() {
		 //Se carga lista de productos precargados desde la BD.
	    	  ProductoDao productoDao = new ProductoDao();
	          productoDao.obtenerProductosPrecargados();
	          try {
	  			productoDao.join();
	  		} catch (InterruptedException e1) {
	  			e1.printStackTrace();
	  		}
	          productosPrecargadosList = productoDao.getListaProductosPrecargados();
		
	   }
	 // Clase estatica oculta. Tan solo se instanciara el singleton una vez
	   private static class CacheHolder { 
	  // El constructor de Singleton puede ser llamado desde aquí al ser protected
	     private final static Cache INSTANCE = new Cache();
	   } 
	   // Método para obtener la instancia de nuestra clase
	   public static Cache getInstance() {
	     return CacheHolder.INSTANCE;
	   }

	   public List<ProductoVo> getListaProductos() {
		   return listaProductos;
	   }
	   public void setListaProductos(List<ProductoVo> listaProductos) {
		   this.listaProductos = listaProductos;
	   }
	   public int getIndice() {
		   return indice;
	   }
	   public void setIndice(int indice) {
		   this.indice = indice;
	   }
	   public static String[] getProductosPrecargadosList() {
		   return productosPrecargadosList;
	   }
	   public static void setProductosPrecargadosList(String[] productosPrecargadosList) {
		   Cache.productosPrecargadosList = productosPrecargadosList;
	   }
	}