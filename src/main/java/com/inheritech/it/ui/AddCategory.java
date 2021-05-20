package com.inheritech.it.ui;

import com.inheritech.it.settings.Settings;
import com.intellij.openapi.ui.Messages;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.apache.commons.lang.StringUtils;

public class AddCategory extends JDialog {
    private JPanel contentPane;

    private JButton buttonOK;

    private JButton buttonCancel;

    private JTextField categoryField;

    private JTextField remarkField;

    public AddCategory() {
        setContentPane(contentPane);
        // 居中
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String category = categoryField.getText();
        if (StringUtils.isEmpty(category)) {
            Messages.showInfoMessage("分类不能为空", "Title Info");
            return;
        }
        String remark = remarkField.getText();
        Settings instance = Settings.getInstance();
        instance.getUrl();
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        AddCategory dialog = new AddCategory();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
