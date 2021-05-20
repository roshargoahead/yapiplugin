import java.awt.BorderLayout;

import java.awt.Component;

import java.awt.Dimension;

import javax.swing.JComboBox;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.ListCellRenderer;

public class Main extends JFrame {
    JComboBox cbo = new JComboBox<>(new String[]{"Hello"});

    public Main() {
        cbo.setRenderer(new CustomComboBox());

        add(cbo, BorderLayout.SOUTH);

        add(new JLabel("Hello"), BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();

        setVisible(true);

    }

    public static void main(String[] args) {
        new Main();

    }

}

class CustomComboBox extends JLabel implements ListCellRenderer {
    @Override

    public Component getListCellRendererComponent(JList list, Object value,

                                                  int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = new JLabel() {
            public Dimension getPreferredSize() {
                return new Dimension(200, 100);

            }

        };

        label.setText(String.valueOf(value));

        return label;

    }

}
