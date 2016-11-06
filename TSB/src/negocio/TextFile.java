/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soporte.HashTable;

/**
 *
 * @author marcela.tartabini
 */
public class TextFile {

    private final File file;
    private final HashTable<Palabra> palabras;

    public TextFile(File file) {
        this.file = new File(file.getPath());
        this.palabras = new HashTable<>(101);
    }

    public HashTable<Palabra> getPalabras() {
        return palabras;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public void readFile() {
        String patron = "([^a-záéíóúüñ0-9]*)([a-záéíóúüñ]+)([^a-záéíóúüñ0-9]*)";
        Pattern pat = Pattern.compile(patron);
        Matcher mat;

        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {
            String linea = br.readLine();
            while (linea != null) {
                String[] lista_palabras = linea.split(" ");
                for (String palabra : lista_palabras) {
                    palabra = palabra.toLowerCase();
                    mat = pat.matcher(palabra);
                    if (mat.matches()) {
                        String[] temp = palabra.split("([^a-záéíóúüñ]+)");
                        if (temp.length == 1) {
                            palabra = temp[0];
                        } else {
                            palabra = temp[1];
                        }
                        Palabra p = new Palabra(palabra);
                        if (!palabras.isEmpty()) {
                            Palabra x = palabras.get(p);
                            if (x != null) {
                                x.sumar();
                                continue;
                            }
                        }
                        palabras.put(p);
                        //System.out.println(palabra);
                    }
                }
                linea = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error de apertura");
        } catch (IOException ex) {
            System.out.println("Error al cerrar archivo");
        }

    }

    @Override
    public String toString() {
        return "TextFile{" + "file=" + file + '}';
    }

}
