package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/clothes_shop",
                    "root",    // tu usuario MySQL
                    "user"// tu contraseña (probablemente vacía)
            );
            System.out.println("✅ Conexión exitosa!");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No se ha podido encontrar el driver de conexión con la base de datos");
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            System.out.println("No se ha podido conectar con la base de datos. Comprueba que está funcionando correctamente");
            sqle.printStackTrace();
        }
    }
}