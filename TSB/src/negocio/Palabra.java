package negocio;

import java.io.*;

public class Palabra implements Comparable<Palabra>, Serializable {

    private final String palabra;
    private int cantidad;

    public Palabra(String p) {
        palabra = p;
        cantidad = 1;
    }

    public String getPalabra() {
        return palabra;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public void sumar() {
        cantidad += 1;
    }

    @Override
    public String toString() {
        return palabra + ": " + cantidad;
    }

    @Override
    public int hashCode() {
        return Math.abs(palabra.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Palabra)) {
            return false;
        }

        Palabra x = (Palabra) obj;
        return palabra.equals(x.palabra);
    }

    @Override
    public int compareTo(Palabra o) {
        return palabra.compareTo(o.palabra);
    }
}
