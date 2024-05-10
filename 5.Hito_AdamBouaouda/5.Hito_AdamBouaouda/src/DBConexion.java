
import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;

public class DBConexion {
    static final String IP_SERVIDOR = "localhost";
    static final String DB_NOMBRE = "hito4";
    static final String USUARIO = "root";
    static final String PASSWORD = "zubiri";
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://" + IP_SERVIDOR + ":3306/" + DB_NOMBRE;
    private Connection conn;

    public void conectarBaseDatos() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void cargarFotografos() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Fotografos");

            while (rs.next()) {
                int id = rs.getInt("IdFotografo");
                String nombre = rs.getString("Nombre");
                boolean premiado = rs.getBoolean("Premiado");
                JComboBox<Fotografo> comboBoxFotografos = new JComboBox<>();
                comboBoxFotografos.addItem(new Fotografo(id, nombre, premiado));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los fotógrafos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HashMap<Integer, Integer> createVisitsMap() {
        HashMap<Integer, Integer> visitsMap = new HashMap<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT IdFotografo, SUM(Visitas) AS TotalVisitas FROM Fotografias GROUP BY IdFotografo");

            while (rs.next()) {
                int idFotografo = rs.getInt("IdFotografo");
                int totalVisitas = rs.getInt("TotalVisitas");
                visitsMap.put(idFotografo, totalVisitas);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visitsMap;
    }

    public void cargarFotos(Object selectedItem, Date fechaSeleccionada, JList<Fotografia> listFotos) {
    }
}
