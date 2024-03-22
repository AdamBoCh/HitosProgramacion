import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;

public class FileViewer extends JFrame {
    private JTextArea textArea;
    private JComboBox<String> fileComboBox;

    public FileViewer() {
        setTitle("File Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fileComboBox = new JComboBox<>(new String[]{"python.txt", "c.txt", "java.txt"});
        fileComboBox.setPreferredSize(new Dimension(300, fileComboBox.getPreferredSize().height));
        fileComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFileContent((String) fileComboBox.getSelectedItem());
            }
        });

        displayFileContent("python.txt");

        JButton clearButton = new JButton("Borrar");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                displayFileContent("python.txt");
            }
        });

        comboBoxPanel.add(new JLabel());
        comboBoxPanel.add(fileComboBox);
        comboBoxPanel.add(clearButton);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        mainPanel.add(comboBoxPanel);
        mainPanel.add(scrollPane);

        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        closeButtonPanel.add(closeButton);

        add(mainPanel, BorderLayout.CENTER);
        add(closeButtonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void displayFileContent(String file) {
        String selectedFile = "Recursos/" + file;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(selectedFile)))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textArea.setText(content.toString());
        } catch (NullPointerException | IOException e) {
            JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileViewer();
            }
        });
    }
}
