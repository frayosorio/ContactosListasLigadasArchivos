package directoriocontactos;

import java.io.*;

public class Archivo {

    public static BufferedReader Abrir(String nombreArchivo) {

        //verificar la existencia del archicvo
        File f = new File(nombreArchivo);
        if (f.exists()) {
            try {
                FileReader fr = new FileReader(f);
                return new BufferedReader(fr);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }

    }

    public static boolean guardar(String nombreArchivo, String[] lineas) {
        if (lineas != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo));
                for(int i=0;i<lineas.length;i++){
                    //guardar la linea
                    bw.write(lineas[i]);
                    bw.newLine();
                }
                bw.close();
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        else{
            return false;
        }
    }

}
