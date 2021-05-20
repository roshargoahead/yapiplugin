package com.inheritech.it.ui;

import com.inheritech.it.entity.UserVO;
import com.inheritech.it.util.HttpUtil;
import com.intellij.openapi.ui.Messages;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.apache.commons.lang.StringUtils;

public class LoginWindow extends JDialog {
    private JPanel contentPane;

    private JButton buttonOK;

    private JButton buttonCancel;

    private JTextField username;

    private JCheckBox rememberPassword;

    private JPasswordField password;

    private String isConfirm;

    public LoginWindow() {
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setTitle("请登录");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        username.setText(UserVO.getInstance().getUserAccount());
        password.setText(UserVO.getInstance().getPassword());
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
        isConfirm = "1";
        String userNameText = username.getText();
        String passwordText = new String(password.getPassword());
        UserVO.getInstance().setRememberPassword(rememberPassword.isSelected());
        if (StringUtils.isEmpty(userNameText) || StringUtils.isEmpty(passwordText)) {
            Messages.showInfoMessage("请输入账号密码", "Tips");
            return;
        }
        if (!HttpUtil.doLogin(userNameText, passwordText)) {
            // 登录失败
            return;
        }
        UserVO.getInstance().setUserAccount(userNameText);
        // 登录成功
        if (rememberPassword.isSelected()) {
            UserVO.getInstance().setPassword(passwordText);
        }
        // add your code here
        dispose();
    }

    private void onCancel() {
        isConfirm = "0";
        // add your code here if necessary
        dispose();
    }

    public String getIsConfirm() {
        return isConfirm;
    }
}
