import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ImageWindow extends JFrame {
    private JLabel imageLabel;
    private JComboBox<String> imageComboBox;
    private JCheckBox commentCheckBox;
    private JTextField commentTextField;
    private JComboBox<String> fileComboBox;

    public ImageWindow() {
        super("Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        imageComboBox = new JComboBox<>();
        loadCombo();
        topPanel.add(imageComboBox, BorderLayout.NORTH);
        imageLabel = new JLabel();
        topPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        commentCheckBox = new JCheckBox("Guardar tu comentario");
        commentTextField = new JTextField(20);
        centerPanel.add(commentCheckBox, BorderLayout.EAST);
        centerPanel.add(commentTextField, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton saveButton = new JButton("GUARDAR");
        saveButton.addActionListener(new SaveButtonListener());
        bottomPanel.add(saveButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        imageComboBox.addActionListener(new ComboListener());

        fileComboBox = new JComboBox<>(new String[]{"img1.png", "img2.png", "img3.png"});
        fileComboBox.setPreferredSize(new Dimension(100, fileComboBox.getPreferredSize().height));
        fileComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedImage = (String) fileComboBox.getSelectedItem();
                    ImageIcon icon = new ImageIcon("Recursos/" + selectedImage);
                    imageLabel.setIcon(icon);
                }
            }
        });
        getContentPane().add(fileComboBox, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(ImageWindow.this, "¡Adiós!");
            }
        });
    }

    private void loadCombo() {
    }

    private class ComboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedImage = (String) imageComboBox.getSelectedItem();
            ImageIcon icon = new ImageIcon("Recursos/" + selectedImage);
            imageLabel.setIcon(icon);
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedImage = (String) imageComboBox.getSelectedItem();
            String comment = commentTextField.getText();
            boolean addComment = commentCheckBox.isSelected();
            if (addComment) {
                try {
                    String filePath = "Recursos" + File.separator + selectedImage + ".txt";
                    FileWriter writer = new FileWriter(filePath, true);
                    writer.write(selectedImage + ": " + comment + "\n");
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean contrasena() {
        JPanel contrasenaPanel = new JPanel();
        contrasenaPanel.setLayout(new BoxLayout(contrasenaPanel, BoxLayout.Y_AXIS)); // Cambiado a Y_AXIS para que los componentes estén uno encima del otro

        JLabel inputLabel = new JLabel("Introduzca la contraseña:");
        JPasswordField contrasenaField = new JPasswordField();

        contrasenaPanel.add(inputLabel);
        contrasenaPanel.add(contrasenaField);

        int result = JOptionPane.showConfirmDialog(null, contrasenaPanel, "Contraseña", JOptionPane.OK_CANCEL_OPTION);
        String inputContrasena = new String(contrasenaField.getPassword());

        return inputContrasena.equals("damocles");
    }

    public static void main(String[] args) {
        ImageWindow window = new ImageWindow();
        if (window.contrasena()) {
            SwingUtilities.invokeLater(() -> {
                window.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Saliendo del programa.");
            System.exit(0);
        }
    }
}
