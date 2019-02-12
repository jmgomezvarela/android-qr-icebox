package tesina.qr.utils;

import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Utils { 	
	
	   // El constructor debe ser Protected para evitar su acceso desde fuera.
	   protected Utils() {
	   }
	   
	 // Clase estatica oculta. Tan solo se instanciara el singleton una vez
	   private static class UtilsHolder { 
	  // El constructor de Singleton puede ser llamado desde aquí al ser protected
	     private final static Utils INSTANCE = new Utils();
	   } 
	   
	   // Método para obtener la instancia de nuestra clase
	   public static Utils getInstance() {
	     return UtilsHolder.INSTANCE;
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
		
	   public int getDifferenceDays(Date d1, Date d2){
		   int daysdiff = 0;
		   long diff = d2.getTime() - d1.getTime();
		   long diffDays = diff / (24 * 60 * 60 * 1000)+1;
		   daysdiff = (int) diffDays;
		   return daysdiff;
	   }
}
