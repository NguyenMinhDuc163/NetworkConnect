package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import view.HomeFrm;
import view.LoginFrm;
import view.RegisterFrm;

/**
 *
 * @author admin
 */
public class ClientController {

    private LoginFrm login;
    private RegisterFrm regis;
    private String ServerHost = "localhost";
    private int serverPort = 8888;
    static Socket mySocket;
    static ObjectOutputStream oos;
    static ObjectInputStream ois;
    HomeFrm home;
    public static User u1;
    public static Thread t;

    public ClientController(LoginFrm login) throws IOException {
        this.login = login;
        this.login.addLoginListener(new LoginListener());
        this.login.addRegisterListener(new RegisterListener());
        mySocket = new Socket(ServerHost, serverPort);
        oos = new ObjectOutputStream(mySocket.getOutputStream());
        ois = new ObjectInputStream(mySocket.getInputStream());
    }

    public static int read() throws IOException, ClassNotFoundException {
        return ois.read();
    }

    public static Object readObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public static void writeObject(Object o) {
        try {
            oos.writeObject(o);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void close() {
        try {
            oos.close();
            ois.close();
            mySocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                User user = login.getUser();
                oos.writeObject("login");
                oos.writeObject(user);
                Object o = ois.readObject();
                if (o instanceof String) {
                    String result = (String) o;
                    if (result.equals("true")) {
                        u1 = user;
                        login.dispose();
                        LoginFrm.list = (ArrayList<User>) ois.readObject();
                        home = new HomeFrm();
                        home.addMouseListener(new ClickListener());
                        home.setVisible(true);
                        t = new Thread(home);
                        t.start();
                    } else {
                        login.showMessage("Username or Password Incorrect!");
                        login.dispose();
                        login = new LoginFrm();
                        login.setVisible(true);
                        login.addLoginListener(new LoginListener());
                        login.addRegisterListener(new RegisterListener());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class RegisterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            regis = new RegisterFrm();
            regis.addRegisterListener(new Register2Listener());
            regis.setVisible(true);
            login.setVisible(false);
            User user1 = login.getUser();
            regis.setForm(user1);
        }

        class Register2Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    User user = regis.getUser();
                    if (user.getPassword().equalsIgnoreCase(user.getRePass())) {
                        oos.writeObject("regis");
                        oos.writeObject(user);
                        if (ois.read() != 0) {
                            String result = (String) ois.readObject();
                            if (result.equalsIgnoreCase("true")) {
                                regis.showMess("Register Succes!");
                                regis.dispose();
                                login.setVisible(true);
                            } else {
                                regis.showMess("Username already exists!");
                            }
                        }
                    } else {
                        regis.showMess("password do not overlap!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    class ClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            User user = (User) home.getSelectedObject(home.getSelectedIndex());
            if (e.getClickCount() == 2 && !user.getUsername().equalsIgnoreCase(u1.getUsername())) {
                try {
                    oos.writeObject("Invite");
                    oos.writeObject(user);//đối thủ 
                    oos.writeObject(u1);//người mời
                    home.mess("Chờ đối thủ đồng ý...");
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

}
