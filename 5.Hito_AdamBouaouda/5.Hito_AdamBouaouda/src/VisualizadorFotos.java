import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

public class VisualizadorFotos extends JFrame {
    private JComboBox<Fotografo> comboBoxFotografos;
    private JLabel labelFotografo;
    private JFormattedTextField txtFecha;
    private JLabel labelFecha;
    private JList<Fotografia> listFotos;
    private JLabel labelImagen;
    private JButton btnPremio;
    private JButton btnEliminar;
    private DBConexion dbConexion;
    private Connection conn;

    public VisualizadorFotos() {
        setTitle("Visualizador de Fotos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        comboBoxFotografos = new JComboBox<>();
        labelFotografo = new JLabel("Photographer");
        txtFecha = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        labelFecha = new JLabel("Photos after");
        listFotos = new JList<>();
        labelImagen = new JLabel();
        btnPremio = new JButton("PREMIO");
        btnEliminar = new JButton("ELIMINAR");
        dbConexion = new DBConexion();

        dbConexion.conectarBaseDatos();

        comboBoxFotografos.setPreferredSize(labelFotografo.getPreferredSize());

        JPanel panelComboBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelDatePicker = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelBotones = new JPanel(new GridLayout(1, 2));

        panelComboBox.add(labelFotografo);
        panelComboBox.add(comboBoxFotografos);
        panelDatePicker.add(labelFecha);
        panelDatePicker.add(txtFecha);
        panelBotones.add(btnPremio);
        panelBotones.add(btnEliminar);

        add(panelComboBox);
        add(panelDatePicker);
        add(new JScrollPane(listFotos));
        add(labelImagen);
        add(panelBotones);

        btnPremio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Ingrese el mínimo de visitas para otorgar el premio:");
                if (input != null) {
                    try {
                        int minVisitas = Integer.parseInt(input);
                        HashMap<Integer, Integer> visitsMap = dbConexion.createVisitsMap();
                        for (Map.Entry<Integer, Integer> entry : visitsMap.entrySet()) {
                            if (entry.getValue() >= minVisitas) {
                                marcarComoPremiado(entry.getKey());
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar las imágenes no mostradas de fotógrafos no premiados?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement stmt = conn.prepareStatement("SELECT IdFoto, IdFotografo FROM Fotografias WHERE Visitas = 0");
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            int idFoto = rs.getInt("IdFoto");
                            int idFotografo = rs.getInt("IdFotografo");
                            if (!esPremiado(idFotografo)) {
                                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la imagen con ID " + idFoto + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                                if (opcion == JOptionPane.YES_OPTION) {
                                    eliminarFoto(idFoto);
                                }
                            }
                        }
                        rs.close();
                        stmt.close();
                        eliminarFotografosSinFotos();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar las imágenes no mostradas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cargarFotografos();
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
            JOptionPane.showMessageDialog(null, "Error al cargar los fotógrafos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarComoPremiado(int idFotografo) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Fotografos SET Premiado = true WHERE IdFotografo = ?");
            stmt.setInt(1, idFotografo);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al marcar como premiado al fotógrafo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean esPremiado(int idFotografo) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT Premiado FROM Fotografos WHERE IdFotografo = ?");
            stmt.setInt(1, idFotografo);
            ResultSet rs = stmt.executeQuery();
            boolean premiado = false;
            if (rs.next()) {
                premiado = rs.getBoolean("Premiado");
            }
            rs.close();
            stmt.close();
            return premiado;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar si el fotógrafo es premiado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void eliminarFoto(int idFoto) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Fotografias WHERE IdFoto = ?");
            stmt.setInt(1, idFoto);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar la foto con ID " + idFoto, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFotografosSinFotos() {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Fotografos WHERE IdFotografo NOT IN (SELECT DISTINCT IdFotografo FROM Fotografias)");
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar los fotógrafos sin fotos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VisualizadorFotos().setVisible(true);
            }
        });
    }
}
