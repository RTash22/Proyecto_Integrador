package Modelo;
import java.sql.*;
import java.util.*;

public class BaseDatos {
    
    private Connection conexion;
    private static BaseDatos instance;

    public BaseDatos(Connection conexion) {
        this.conexion = conexion;
    }
    public static BaseDatos getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BaseDatos no ha sido inicializada. Llama a initialize() primero.");
        }
        return instance;
    }

    public static void initialize(Connection conexion) throws SQLException {
        if (instance == null) {
            instance = new BaseDatos(conexion);
        } else {
            throw new SQLException("BaseDatos ya ha sido inicializada.");
        }
    }

    
    public ArrayList<String[]> consultar(String tabla, String campos, String condicion) {
        ArrayList<String[]> resultados = new ArrayList<String[]>();
       String sql="";
        if (campos==null){
            
            sql = "SELECT * FROM " + tabla;
        }
             	else
         sql = "SELECT " + campos + " FROM " + tabla;
        if (condicion != null && !condicion.isEmpty()) {
            sql += " WHERE " + condicion;
        }
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumnas = rsmd.getColumnCount();
            while (rs.next()) {
                String[] fila = new String[numColumnas];
                for (int i = 1; i <= numColumnas; i++) {
                    fila[i-1] = rs.getString(i);
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los registros: " + e.getMessage());
        }
        return resultados;
    }

 
    public ArrayList<String[]> consultar(String tabla1, String tabla2, String campos, String condicion) {
        ArrayList<String[]> resultados = new ArrayList<String[]>();
        String sql = "";

        if (campos == null)
            sql = "SELECT * FROM " + tabla1 + " INNER JOIN " + tabla2 + " ON "+ condicion+" ;";
        else
            sql = "SELECT " + campos + " FROM " + tabla1 + " INNER JOIN " + tabla2 + " ON "+condicion+" ;" ;
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumnas = rsmd.getColumnCount();

            while (rs.next()) {
                String[] fila = new String[numColumnas];
                for (int i = 1; i <= numColumnas; i++) {
                    fila[i - 1] = rs.getString(i);
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los registros: " + e.getMessage());
        }

        return resultados;
    }

    
    public boolean existe(String tabla, String campo,String valor) {
        boolean enc=false;
    	String sql = "SELECT * FROM " + tabla;
            sql += " WHERE " + campo +" = "+valor;
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            enc= rs.next();
        } catch (SQLException e) {
            System.err.println("Error al consultar los registros: " + e.getMessage());
        }
        return enc;
    } 

    
    
    public void insertar(String tabla, String campos, String[] valores) {
        String sql = "INSERT INTO " + tabla + "";
        
            sql = sql+" ";
            if (campos != null) {
                sql += campos;
            }
            

        sql = sql.substring(0, sql.length()) + " VALUES (";
        for (int i = 0; i < valores.length; i++) {
        	if (i< (valores.length-1))
            sql += "\""+valores[i]+"\""+",";
        	else
        		sql += "\""+valores[i]+"\")";
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        System.out.println(sql);
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Registro insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar el registro: " + e.getMessage());
        }
    }

    
    public void eliminar(String tabla, String campo, String valor) {
        String sql = "DELETE FROM " + tabla + " WHERE " + campo + " = ?";
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, valor);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                System.out.println("No se encontraron registros para eliminar.");
            } else {
                System.out.println("Registros eliminados correctamente: " + resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar los registros: " + e.getMessage());
        }
    }

    public void eliminar(String tabla, String campocondicion1, String valorCampo1, 
                     String campocondicion2, String valorCampo2) {
        String sql = "DELETE FROM " + tabla + " WHERE " 
                    + campocondicion1 + " = ? AND " 
                    + campocondicion2 + " = ?";
        try {
            System.out.println(sql); // Para depuración
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, valorCampo1); // Primera condición
            ps.setString(2, valorCampo2); // Segunda condición
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                System.out.println("Registro(s) eliminado(s) correctamente.");
            } else {
                System.out.println("No se encontraron registros para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar registros: " + e.getMessage());
        }
    }

    
    public void modificar(String tabla, String campocondicion, String valorCampo, String condicion, String campoActualizar, String valorActualizar) {
        String sql = "UPDATE " + tabla + " SET " + campoActualizar + " = ? WHERE " + campocondicion + " " + condicion + " ?";
        try {
            System.out.println(sql);
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, valorActualizar); // Asignar el nuevo valor del campo
            ps.setString(2, valorCampo); // Asignar el valor de la condición
            System.out.println(ps);
            ps.executeUpdate();
            System.out.println("Registro(s) actualizado(s) correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el registro: " + e.getMessage());
        }
    }

    public void modificar(String tabla, String campocondicion1, String valorCampo1, String condicion1,
                      String campocondicion2, String valorCampo2, String condicion2, 
                      String campoActualizar, String valorActualizar) {
    String sql = "UPDATE " + tabla + " SET " + campoActualizar + " = ? WHERE " 
                 + campocondicion1 + " " + condicion1 + " ? AND " 
                 + campocondicion2 + " " + condicion2 + " ?";
    try {
        System.out.println(sql);
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, valorActualizar); // Nuevo valor para actualizar
        ps.setString(2, valorCampo1);     // Valor de la primera condición
        ps.setString(3, valorCampo2);     // Valor de la segunda condición
        ps.executeUpdate();
        System.out.println("Registro(s) actualizado(s) correctamente.");
    } catch (SQLException e) {
        System.err.println("Error al actualizar el registro: " + e.getMessage());
    }
}


    

    
}
