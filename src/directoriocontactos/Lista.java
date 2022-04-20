package directoriocontactos;

import java.io.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Lista {

    private Nodo cabeza;

    public void agregar(Nodo n) {
        //La lista esta vacia
        if (cabeza == null) {
            cabeza = n;
        } else {
            //Buscar el ultimo nodo de la lista
            Nodo sig = cabeza;
            while (sig.siguiente != null) {
                sig = sig.siguiente;
            }
            //Apuntar desde el ultimo nodo al nuevo nodo
            sig.siguiente = n;
        }
        n.siguiente = null;
    }

    //devuelve el nodo que se encuentre en una poisición
    public Nodo obtenerNodo(int posicion) {
        int p = 0;
        Nodo apuntador = cabeza;
        while (apuntador != null && p != posicion) {
            apuntador = apuntador.siguiente;
            p++;
        }
        return apuntador;
    }

    //cambiar los valores de un nodo, dada la posición
    public void actualizar(int posicion,
            String nombre,
            String telefono,
            String celular,
            String direccion,
            String correo) {
        Nodo n = obtenerNodo(posicion);
        if (n != null) {
            n.actualizar(nombre, telefono, celular, direccion, correo);
        }
    }

    public void desdeArchivo(String nombreArchivo) {
        BufferedReader bf = Archivo.Abrir(nombreArchivo);
        try {
            String linea = bf.readLine();
            while (linea != null) {
                String[] datos = linea.split("\t");
                if (datos.length >= 5) {
                    Nodo n = new Nodo();
                    n.actualizar(datos[0],
                            datos[1],
                            datos[2],
                            datos[3],
                            datos[4]
                    );
                    agregar(n);
                }
                linea = bf.readLine();
            }
        } catch (Exception ex) {
            cabeza = null;
        }
    }

    public int obtenerLongitud() {
        int l = 0;

        Nodo sig = cabeza;
        while (sig != null) {
            l++;
            sig = sig.siguiente;
        }
        return l;
    }

    public void mostrar(JTable tbl) {
        //Crear matriz de datos para la rejilla a partir de la lista ligada
        String[][] datos = new String[obtenerLongitud()][5];

        //Pasar los datos de la lista ligada a la matriz
        Nodo sig = cabeza;
        int f = 0;
        while (sig != null) {
            datos[f][0] = sig.nombre;
            datos[f][1] = sig.telefono;
            datos[f][2] = sig.celular;
            datos[f][3] = sig.direccion;
            datos[f][4] = sig.correo;
            f++;
            sig = sig.siguiente;
        }
        //Crear los encabezados
        String[] encabezados = new String[]{"Nombre", "Teléfono", "Móvil", "Dirección", "Correo"};

        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);

        //programar evento cuando cambien los datos
        dtm.addTableModelListener(new TableModelListener() {
            
            @Override
            public void tableChanged(TableModelEvent e) {
                int p = e.getFirstRow();
                DefaultTableModel dtm=(DefaultTableModel)e.getSource();
                actualizar(p,
                        (String)dtm.getValueAt(p, 0),
                        (String)dtm.getValueAt(p, 1),
                        (String)dtm.getValueAt(p, 2),
                        (String)dtm.getValueAt(p, 3),
                        (String)dtm.getValueAt(p, 4)
                        );
            }
        });

        tbl.setModel(dtm);
    }

}
