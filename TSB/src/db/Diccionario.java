/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.*;
import java.util.Iterator;
import negocio.Palabra;

/**
 *
 * @author cundo
 */
public class Diccionario {

    private final String path = "db/diccionario.sqlite";
    private Connection conn;

    private void conectar() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        if (conn != null) {
            System.out.println("Conectado");
        }
    }

    private void cerrar() throws SQLException {
        conn.close();
        System.out.println("Cerrado");
    }

    public void crearTablas() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ";
        String palabras = "\"main\".\"PALABRAS\" (\"id\" INTEGER PRIMARY KEY  "
                + "NOT NULL , \"palabra\" VARCHAR NOT NULL  UNIQUE )";
        String archivos = "\"main\".\"ARCHIVOS\" (\"id\" INTEGER PRIMARY KEY  "
                + "NOT NULL , \"archivo\" VARCHAR NOT NULL  UNIQUE )";
        String palabraXArchivo = "\"main\".\"PALABRAXARCHIVO\" (\"id_palabra\" "
                + "INTEGER NOT NULL REFERENCES PALABRAS(id) ON DELETE CASCADE ,"
                + " \"id_archivo\" INTEGER NOT NULL REFERENCES ARCHIVOS(id) "
                + "ON DELETE CASCADE , \"cantidad\" INTEGER NOT NULL , "
                + "PRIMARY KEY (\"id_palabra\", \"id_archivo\"))";

        conectar();
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.execute(sql + archivos);
            st.execute(sql + palabras);
            st.execute(sql + palabraXArchivo);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
        conn.setAutoCommit(true);
        cerrar();
    }

    public void vaciarDiccionario() throws SQLException {
        String sql = "DROP TABLE IF EXISTS ";
        String palabras = "\"main\".\"PALABRAS\"";
        String archivos = "\"main\".\"ARCHIVOS\"";
        String palabraXArchivo = "\"main\".\"PALABRAXARCHIVO\"";

        conectar();
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.execute(sql + palabraXArchivo);
            st.execute(sql + palabras);
            st.execute(sql + archivos);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
        conn.setAutoCommit(true);
        cerrar();

        crearTablas();
    }

    public void procesarArchivo(String archivo, Iterator<Palabra> it)
            throws SQLException {
        int idArchivo, idPalabra, cantidad;
        String palabra;
        Palabra p;
        ResultSet rs;

        conectar();
        conn.setAutoCommit(false);
        try (PreparedStatement consArchivo = conn.prepareStatement(
                "SELECT id FROM ARCHIVOS WHERE archivo=?");
                PreparedStatement consPalabra = conn.prepareStatement(
                        "SELECT id FROM PALABRAS WHERE palabra=?");
                PreparedStatement insArchivo = conn.prepareStatement(
                        "INSERT INTO ARCHIVOS(archivo) VALUES(?)");
                PreparedStatement insPalabra = conn.prepareStatement(
                        "INSERT INTO PALABRAS(palabra) VALUES(?)");
                PreparedStatement insPalabraXArchivo = conn.prepareStatement(
                        "INSERT INTO PALABRAXARCHIVO(id_palabra,id_archivo,"
                        + "cantidad) VALUES(?,?,?)")) {
            
            consArchivo.setString(1, archivo);
            rs = consArchivo.executeQuery();
            if (rs.next()) {
                conn.setAutoCommit(true);
                return;
            }
            
            insArchivo.setString(1, archivo);
            insArchivo.executeUpdate();
            rs = consArchivo.executeQuery();
            rs.next();
            idArchivo = rs.getInt("id");
            
            while (it.hasNext()) {
                p = it.next();
                palabra = p.getPalabra();
                cantidad = p.getCantidad();
                consPalabra.setString(1, palabra);
                rs = consPalabra.executeQuery();
                if (!rs.next()) {
                    insPalabra.setString(1, palabra);
                    insPalabra.executeUpdate();
                    rs = consPalabra.executeQuery();
                    rs.next();
                }
                idPalabra = rs.getInt("id");
                insPalabraXArchivo.setInt(1, idPalabra);
                insPalabraXArchivo.setInt(2, idArchivo);
                insPalabraXArchivo.setInt(3, cantidad);
                insPalabraXArchivo.executeUpdate();
            }
            
            conn.commit();
            rs.close();

        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
        conn.setAutoCommit(true);
        cerrar();
    }

    public static void main(String[] args)
            throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");

        Diccionario dic = new Diccionario();
    }
}
