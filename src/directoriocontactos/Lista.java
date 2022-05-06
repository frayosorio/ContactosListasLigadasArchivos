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

    //eliminar el nodo de la lista
    public void eliminar(Nodo n) {
        //si hay lista y nodo a eliminar
        if (cabeza != null && n != null) {
            //buscar el nodo
            Nodo apuntador = cabeza;
            Nodo anterior = null;
            boolean encontrado = false;
            while (apuntador != null && !encontrado) {
                //se halló el nodo
                if (apuntador == n) {
                    encontrado = true;
                } else {
                    anterior = apuntador;
                    apuntador = apuntador.siguiente;
                }
            }
            //eliminar el nodo
            if (encontrado) {
                //eliminado la cabeza?
                if (anterior == null) {
                    cabeza = apuntador.siguiente;
                } else {
                    anterior.siguiente = apuntador.siguiente;
                }
                n = null;
            }
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
                DefaultTableModel dtm = (DefaultTableModel) e.getSource();
                actualizar(p,
                        (String) dtm.getValueAt(p, 0),
                        (String) dtm.getValueAt(p, 1),
                        (String) dtm.getValueAt(p, 2),
                        (String) dtm.getValueAt(p, 3),
                        (String) dtm.getValueAt(p, 4)
                );
            }
        });

        tbl.setModel(dtm);
    }

    public boolean guardar(String nombreArchivo) {
        int tn = obtenerLongitud();
        if (tn > 0) {
            //Convertir de lista ligada a vector de cadenas de texto
            String[] lineas = new String[tn];
            Nodo apuntador = cabeza;
            int i = 0;
            while (apuntador != null) {
                lineas[i] = (apuntador.nombre != null ? apuntador.nombre : " ") + "\t"
                        + (apuntador.telefono != null ? apuntador.telefono : " ") + "\t"
                        + (apuntador.celular != null ? apuntador.celular : " ") + "\t"
                        + (apuntador.direccion != null ? apuntador.direccion : " ") + "\t"
                        + (apuntador.correo != null ? apuntador.correo : " ");
                apuntador = apuntador.siguiente;
                i++;
            }

            return Archivo.guardar(nombreArchivo, lineas);
        } else {
            return false;
        }
    }

    public Nodo obtenerAntecesor(Nodo n) {
        Nodo apuntador = null;
        if (n != cabeza && cabeza != null) {
            apuntador = cabeza;
            while (apuntador != null && apuntador.siguiente != n) {
                apuntador = apuntador.siguiente;
            }
        }
        return apuntador;
    }

    public void intercambiar(Nodo n1, Nodo n2, Nodo a1, Nodo a2) {
        if (n1 != null && n2 != null && cabeza != null && n1 != n2) {
            //Nodo a1 = obtenerAntecesor(n1);
            //Nodo a2 = obtenerAntecesor(n2);

            /*
             System.out.println((a1 != null ? a1.nombre : "*** | ")
             + n1.nombre + " | "
             + a2.nombre + " | "
             + n2.nombre);
             */
            if (a1 != null) {
                a1.siguiente = n2;
            } else {
                cabeza = n1;
            }

            //Se guarda temporalmente el apuntador siguiente del segundo nodo
            Nodo t = n2.siguiente;
            if (n1 != a2) {
                n2.siguiente = n1.siguiente;
                a2.siguiente = n1;
            } else {
                n2.siguiente = n1;
            }
            n1.siguiente = t;

        }
    }

    public void ordenar() {
        Nodo ni = cabeza;
        Nodo ai = null;
        while (ni.siguiente != null) {
            Nodo aj = ni;
            Nodo nj = ni.siguiente;
            while (nj != null) {

                System.out.println(ni.nombre + " | " + nj.nombre);

                if (ni.nombre.compareTo(nj.nombre) > 0) {
                    intercambiar(ni, nj, ai, aj);
                    Nodo t = ni;
                    ni = nj;
                    nj = t;
                }
                aj = nj;
                nj = nj.siguiente;
            }
            ai = ni;
            ni = ni.siguiente;
        }
    }

}
