import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setVisible(true);

        frame.getContentPane().setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add(new JLabel());
        frame.getContentPane().setLayout(new BorderLayout());
        JCheckBox checkbox1 = new JCheckBox("Bugatti");
        JCheckBox checkBox2 = new JCheckBox("Bugatti Veyron");
        northPanel.add(checkbox1);
        northPanel.add(checkBox2);
        frame.getContentPane().add(northPanel, BorderLayout.NORTH);

        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(250, eastPanel.getHeight()));
        frame.getContentPane().add(eastPanel, BorderLayout.EAST);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        southPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        southPanel.add(new JButton("Botón 1"));
        southPanel.add(new JButton("Botón 2"));
        frame.getContentPane().add(southPanel, BorderLayout.SOUTH);

        JPanel westPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JRadioButton button1 = new JRadioButton("Opción 1");
        JRadioButton button2 = new JRadioButton("Opcion 2");
        JRadioButton button3 = new JRadioButton("Opcion 3");
        radioButtonGroup.add(button1);
        radioButtonGroup.add(button2);
        radioButtonGroup.add(button3);
        westPanel.add(button1);
        westPanel.add(button2);
        westPanel.add(button3);
        button1.setSelected(true);
        frame.getContentPane().add(westPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        ImageIcon imageIcon = new ImageIcon("img/img1.jpg");
        JLabel label1 = new JLabel(imageIcon);
        JLabel label2 = new JLabel(imageIcon);
        JLabel label3 = new JLabel(imageIcon);
        JLabel label4 = new JLabel(imageIcon);
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        frame.add(panel, BorderLayout.WEST);

        frame.pack();
    }
}