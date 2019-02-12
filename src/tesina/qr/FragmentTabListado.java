package tesina.qr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.dao.Cache;
import modelo.dao.ProductoDao;
import modelo.vo.ProductoVo;
import tesina.qr.utils.Utils;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
 
public class FragmentTabListado extends Fragment {
	private static Date fechaActual;
	private List<Integer> listaCheckBox;
	private List<ProductoVo> productosList;
	
	public FragmentTabListado(){
		fechaActual = new Date();
		listaCheckBox = new ArrayList<Integer>();
		productosList = new ArrayList<ProductoVo>();
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listado, container, false);
        ImageButton botonBorrar = (ImageButton)  rootView.findViewById(R.id.buttonBorrar);
        botonBorrar.setOnClickListener(onClickListener);
        TableLayout tabla = (TableLayout) rootView.findViewById(R.id.tabla_cuerpo);
        
        ProductoDao productoDao = new ProductoDao();
        productoDao.obtenerProductosAll();
        try {
			productoDao.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        productosList = productoDao.getListaProducto();
        
        if (productosList != null && !productosList.isEmpty()){
        	Cache.getInstance().setListaProductos(productosList);
        	int dias = 0;
        	int i=0;
        	for(ProductoVo producto: productosList){
        		if(producto.getIdProducto()!=null){
        		  TableRow row= new TableRow(this.getActivity());
        		  TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        		  row.setLayoutParams(lp);
        		  
        		  ImageView estadoVencimiento = new ImageView(this.getActivity());
        		  CheckBox checkBox = new CheckBox(this.getActivity()); 
        		  TextView textDias = new TextView(this.getActivity());
        		  TextView textNombre = new TextView(this.getActivity());
        		  textNombre.setText(producto.getNombre());
        		  ImageButton botonVer=  new ImageButton(this.getActivity());
        		  Date fechaVencimiento = new Date(producto.getFechaCaducidad());      		 
        		  dias = Utils.getInstance().getDifferenceDays(fechaActual,fechaVencimiento);      		 
        		  textDias.setText(" "+Integer.toString(dias));
        		  
        		  Drawable drawable = this.getResources().getDrawable(R.drawable.semasforo_rojo);
        		  
        		  if(dias<0){
        			   textDias.setText("Vencido("+Integer.toString(dias*-1)+")");
        			   textDias.setTextColor(Color.GRAY);
        			   drawable = this.getResources().getDrawable(R.drawable.semasforo_gris);  
        		  }else if(dias<=3){
        			  textDias.setTextColor(Color.RED);
        			   drawable = this.getResources().getDrawable(R.drawable.semasforo_rojo);
        		  }else if(dias>3 && dias <7){
        			  textDias.setTextColor(Color.YELLOW);
        			   drawable = this.getResources().getDrawable(R.drawable.semasforo_amarillo);
        		  }else{
        			   textDias.setTextColor(Color.GREEN);
        			   drawable = this.getResources().getDrawable(R.drawable.semasforo_verde);  
        		  }      		  
        		 
       		      estadoVencimiento.setImageDrawable(drawable);
        		  checkBox.setId(producto.getIdProducto());
        		  listaCheckBox.add(checkBox.getId());      		
        		  
        		  botonVer.setId(i++);
        		  botonVer.setImageResource(R.drawable.ic_menu_lupa);
        		  row.setBackgroundColor(Color.BLACK);
        		  darAccion(botonVer);
        		  row.addView(checkBox);
        		  row.addView(textNombre);    		        		  
        		  row.addView(botonVer);
        		  row.addView(estadoVencimiento);
        		  row.addView(textDias); 
        		  tabla.addView(row);
        	  }
        } 
      }else{
    	  Toast.makeText(rootView.getContext(), "No se encuentran productos, para cargarlos dirigirse a prestaña ALTA." , Toast.LENGTH_LONG).show();
      }
      return rootView;
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
	     @Override
	     public void onClick(View v) {
	    	    List<Integer> idsList = new ArrayList<Integer>();
	    	    ProductoDao productoDao = new ProductoDao();
	           	if(v.getId()==R.id.buttonBorrar){
	           		for(Integer identificador: listaCheckBox){
	           			CheckBox checkBox = (CheckBox) getActivity().findViewById(identificador);
	           			if (checkBox.isChecked()){
	           				idsList.add(identificador);
	           			}
	           		}
	           				productoDao.eliminarProducto(idsList);
	           				try {
								productoDao.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	           			           			
	           		// Refrescar Fragment
	           		Fragment newFragment = new FragmentTabListado();
	    		    FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    		    transaction.replace(R.id.fragment_container, newFragment);
	    		    transaction.addToBackStack(null);
	    		    transaction.commit();
	           	}else{
				    FragmentManager fm = getFragmentManager();
				    DialogDetalle dialogDetalle = new DialogDetalle(Cache.getInstance().getListaProductos(),v.getId());
	           		dialogDetalle.setRetainInstance(true);
	           		dialogDetalle.show(fm, "fragment_name");
	           	}
	           }
	       }; 
	       
   public void darAccion(ImageButton boton)
   {      
	   Cache.getInstance().setIndice(boton.getId());
	   boton.setOnClickListener(onClickListener);
   }
}