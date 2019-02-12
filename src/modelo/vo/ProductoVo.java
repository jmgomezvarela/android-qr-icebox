package modelo.vo;

public class ProductoVo {
	
	private Integer idProducto;
	private String nombre;
	private Integer descripcion;
	private String fechaRegistro;
	private long fechaCaducidad;
	private String imagen;
	private Integer idUsuario;
	
	public Integer getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(Integer descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public long getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(long fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
}
