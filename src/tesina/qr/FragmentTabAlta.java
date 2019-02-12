package tesina.qr;

import java.io.File;

import modelo.dao.Cache;
import modelo.dao.ProductoDao;
import modelo.vo.ProductoVo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
public class FragmentTabAlta extends Fragment {

	private ImageView imagenProducto;
	Bitmap bitmap = null;
	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 100;
	private static final int PICK_FROM_FILE = 2;
    private String ubicacionImagen;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alta, container, false);
        imagenProducto = ((ImageView) rootView.findViewById(R.id.imagenProducto));
        
        Button buttonAceptar= (Button) rootView.findViewById(R.id.buttonAceptar);
        ImageButton buttonCamara= (ImageButton) rootView.findViewById(R.id.buttonCamara);
        ImageButton buttonMemoria= (ImageButton) rootView.findViewById(R.id.buttonMemoria);
        
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.select_dialog_item, Cache.getInstance().getProductosPrecargadosList());
      
                AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.autoNombre);
                textView.setAdapter(autoCompleteAdapter);
        
        
        
        
      //Acciones Botones
        buttonCamara.setOnClickListener(onClickListener);
        buttonAceptar.setOnClickListener(onClickListener);
        buttonMemoria.setOnClickListener(onClickListener); 
        return rootView;
    }
    
    
    private OnClickListener onClickListener = new OnClickListener() {
	     @Override
	     public void onClick(View v) {
	switch(v.getId()) {
	//Funcionamiento para Boton Aceptar
   case R.id.buttonAceptar:
   	   AutoCompleteTextView nombre = (AutoCompleteTextView) getView().findViewById(R.id.autoNombre);
   	   CalendarView fechaCaducidad = (CalendarView) getView().findViewById(R.id.calendarioFechaCaducidad);
   	
       ProductoVo producto = new ProductoVo();
       producto.setNombre(nombre.getText().toString());
       producto.setFechaCaducidad(fechaCaducidad.getDate());
       producto.setImagen(ubicacionImagen);
       ProductoDao productoDao = new ProductoDao();
		try {
			productoDao.registrarProducto(producto);
			Toast.makeText(v.getContext(), "Se ha registrado " + producto.getNombre() +" correctamente" , Toast.LENGTH_SHORT).show();
			producto = new ProductoVo();
			producto.setNombre("");
			producto.setImagen("");
			imagenProducto.setImageBitmap(null);
			nombre.setText("");
		} catch (ExcepcionesGenerales e1) {
			Toast.makeText(v.getContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
		}
       
   break;
   case R.id.buttonCamara:
	   Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       File file        = new File(Environment.getExternalStorageDirectory(),
                           "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
       mImageCaptureUri = Uri.fromFile(file);
       ubicacionImagen = mImageCaptureUri.getPath();
       try {
           intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
           intent.putExtra("return-data", true);

           startActivityForResult(intent, PICK_FROM_CAMERA);
       } catch (Exception e) {
           e.printStackTrace();
       }
   break;
   case R.id.buttonMemoria:
	   intent = new Intent();
	   
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);

       startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
   break;
 } 	
}
};      

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != -1) return;
    
    Bitmap bitmap   = null;
    this.ubicacionImagen     = "";

    if (requestCode == PICK_FROM_FILE) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        ubicacionImagen = cursor.getString(columnIndex);
        cursor.close();
        bitmap  = BitmapFactory.decodeFile(ubicacionImagen);
    } else {
    	ubicacionImagen    = mImageCaptureUri.getPath();
        bitmap  = BitmapFactory.decodeFile(ubicacionImagen);
    };
    imagenProducto.setImageBitmap(bitmap);
}
}