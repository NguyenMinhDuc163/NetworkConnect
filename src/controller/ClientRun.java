package controller;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import view.LoginFrm;

/**
 *
 * @author admin
 */
public class ClientRun extends JFrame {
    public static void main(String[] args) throws BadLocationException, IOException {
        LoginFrm login = new LoginFrm();
        login.setVisible(true);
        ClientController client = new ClientController(login);
    }
}
