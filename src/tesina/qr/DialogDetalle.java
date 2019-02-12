package tesina.qr;

import java.io.File;
import java.util.List;

import modelo.dao.ProductoDao;
import modelo.vo.ProductoVo;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogDetalle extends DialogFragment {
	private List<ProductoVo> productosList;
	private int indice;

	public DialogDetalle() {
	}

	public DialogDetalle(List<ProductoVo> pList, int i) {
		productosList = pList;
		indice = i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.detalle, container);
		getDialog().setTitle("Producto Detalle");
		TextView texto = (TextView) view.findViewById(R.id.editNombre);
		ImageView imagenProducto = (ImageView) view
				.findViewById(R.id.imageViewProducto);
		CalendarView fechaCaducidad = (CalendarView) view
				.findViewById(R.id.calendarVencimiento);
		Button buttonGuardar = (Button) view.findViewById(R.id.buttonGuardar);

		texto.setText(productosList.get(indice).getNombre());
		fechaCaducidad.setDate(productosList.get(indice).getFechaCaducidad());
		File imgFile = new File(productosList.get(indice).getImagen());
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			imagenProducto.setImageBitmap(myBitmap);
		}

		// Acciones Botones
		buttonGuardar.setOnClickListener(onClickListener);
		return view;
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			// Funcionamiento para Boton Aceptar
			case R.id.buttonGuardar:
				TextView nombre = (TextView) getView().findViewById(
						R.id.editNombre);
				ImageView imagenProducto = (ImageView) getView().findViewById(
						R.id.imageViewProducto);
				CalendarView fechaCaducidad = (CalendarView) getView()
						.findViewById(R.id.calendarVencimiento);

				ProductoVo producto = new ProductoVo();
				producto.setNombre(nombre.getText().toString());
				producto.setFechaCaducidad(fechaCaducidad.getDate());
				producto.setImagen(productosList.get(indice).getImagen());
				producto.setIdProducto(productosList.get(indice).getIdProducto());
				File imgFile = new File(productosList.get(indice).getImagen());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					imagenProducto.setImageBitmap(myBitmap);
				}
				ProductoDao productoDao = new ProductoDao();
				try {
					productoDao.modificarProducto(producto);
					Toast.makeText(
							view.getContext(),
							"Se ha modificado " + producto.getNombre()
									+ " correctamente", Toast.LENGTH_SHORT)
							.show();
				} catch (ExcepcionesGenerales e1) {
					Toast.makeText(view.getContext(), e1.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				break;
			}
		}
	};
}