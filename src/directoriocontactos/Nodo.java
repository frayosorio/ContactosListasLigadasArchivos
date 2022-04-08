package directoriocontactos;

public class Nodo {
    
    public String nombre;
    public String telefono;
    public String celular;
    public String correo;
    public String direccion;
    
    public Nodo siguiente;
    
    public void actualizar(String nombre,
                            String telefono,
                            String celular,
                            String direccion,
                            String correo){
        this.nombre=nombre;
        this.telefono=telefono;
        this.celular=celular;
        this.correo=correo;
        this.direccion=direccion;
    }
    
}
