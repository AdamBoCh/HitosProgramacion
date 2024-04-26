import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import org.jdesktop.swingx.JXDatePicker;

public class VisualizadorFotos extends JFrame {
    private JComboBox<Fotografo> comboBoxFotografos;
    private JLabel labelFotografo;
    private JXDatePicker datePicker;
    private JLabel labelFecha;
    private JList<Fotografia> listFotos;
    private JLabel labelImagen;

    static final String IP_SERVIDOR = "localhost";
    static final String DB_NOMBRE = "hito4";
    static final String USUARIO = "root";
    static final String PASSWORD = "zubiri";
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://" + IP_SERVIDOR + ":3306/" + DB_NOMBRE;
    private Connection conn;

    public VisualizadorFotos() {
        setTitle("Visualizador de Fotos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        comboBoxFotografos = new JComboBox<>();
        labelFotografo = new JLabel("Photographer");
        datePicker = new JXDatePicker();
        labelFecha = new JLabel("Photos after");
        listFotos = new JList<>();
        labelImagen = new JLabel();

        conectarBaseDatos();

        comboBoxFotografos.setPreferredSize(labelFotografo.getPreferredSize());

        datePicker.setFormats("dd/MM/yyyy");
        datePicker.setLightWeightPopupEnabled(true);

        JPanel panelComboBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelDatePicker = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelComboBox.add(labelFotografo);
        panelComboBox.add(comboBoxFotografos);
        panelDatePicker.add(labelFecha);
        panelDatePicker.add(datePicker);

        add(panelComboBox);
        add(panelDatePicker);
        add(new JScrollPane(listFotos));
        add(labelImagen);


        comboBoxFotografos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarFotos();
            }
        });
        cargarFotografos();
    }

    private void conectarBaseDatos() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void cargarFotografos() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Fotografos");

            while (rs.next()) {
                int id = rs.getInt("IdFotografo");
                String nombre = rs.getString("Nombre");
                boolean premiado = rs.getBoolean("Premiado");
                comboBoxFotografos.addItem(new Fotografo(id, nombre, premiado));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los fotógrafos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFotos() {
        Fotografo fotografoSeleccionado = (Fotografo) comboBoxFotografos.getSelectedItem();
        Date fechaSeleccionada = datePicker.getDate();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Fotografias WHERE IdFotografo = ? AND Fecha >= ?");
            stmt.setInt(1, fotografoSeleccionado.getId());
            stmt.setDate(2, new java.sql.Date(fechaSeleccionada.getTime()));
            ResultSet rs = stmt.executeQuery();

            DefaultListModel<Fotografia> model = new DefaultListModel<>();
            while (rs.next()) {
                int idFoto = rs.getInt("IdFoto");
                String titulo = rs.getString("Titulo");
                String archivo = rs.getString("Archivo");
                int visitas = rs.getInt("Visitas");
                int idFotografo = rs.getInt("IdFotografo");
                model.addElement(new Fotografia(idFoto, titulo, archivo, visitas, idFotografo));
            }

            listFotos.setModel(model);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las fotos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}