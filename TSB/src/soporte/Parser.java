/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporte;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cundo
 */
public class Parser {
    HashTable<String> hash;
    String texto;

    public Parser(String texto) {
        this.texto = texto;
        this.hash = new HashTable(101);
        process();
    }

    public HashTable<String> getHash() {
        return hash;
    }
    
    private void process(){
        String patron = "([^a-záéíóúüñ0-9]*)([a-záéíóúüñ]+)([^a-záéíóúüñ0-9]*)";
        Pattern pat = Pattern.compile(patron);
        String[] lineas = texto.split("\n");
        for (String linea : lineas) {
            String[] palabras = linea.split(" ");
            for (String palabra : palabras){
                palabra = palabra.toLowerCase();
                Matcher mat = pat.matcher(palabra);
                if (mat.matches()){
                    String[] temp = palabra.split("([^a-záéíóúüñ]+)");
                    if (temp.length == 1)
                        palabra = temp[0];
                    else
                        palabra = temp[1];
                    System.out.println(palabra);
                }
            }
        }
    }
}
