package view;

import controller.ClientController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import model.User;

/**
 *
 * @author admin
 */
public class HomeFrm extends JFrame implements Runnable {

    JList list;
    DefaultListModel model;
    JScrollPane listScrollPane;
    private boolean stop = false;
    public static User rival;
    GameFrm game;
    JButton bxh, listInvite;
    JLabel lb;

    public HomeFrm() {
        super();
        setTitle("Home");
        setSize(400, 500);
        model = new DefaultListModel();
        lb = new JLabel("Người chơi đang online:");
        lb.setFont(new Font("arial", Font.BOLD, 20));
        lb.setPreferredSize(new Dimension(600, 40));
        list = new JList(model);
        listScrollPane = new JScrollPane(list);
        add(lb, BorderLayout.NORTH);
        add(listScrollPane);
        bxh = new JButton("Bảng xếp hạng");
        bxh.setFont(new Font("arial", Font.BOLD, 18));
        bxh.setPreferredSize(new Dimension(40, 33));
        add(bxh, BorderLayout.SOUTH);
        
        listInvite = new JButton("Danh sách lời mời");
        listInvite.setFont(new Font("arial", Font.BOLD, 16));
        listInvite.setPreferredSize(new Dimension(40, 33));
        add(listInvite, BorderLayout.NORTH);
        
        for (User u : LoginFrm.list) {
            model.addElement(u);
        }

        list.setFont(new Font("arial", Font.PLAIN, 18));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ClientController.writeObject("offline");
                stop = true;
            }

        });
        bxh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientController.writeObject("bxh");
            }
        });
        
           listInvite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientController.writeObject("listInvite");
            }
        });
        
    }

    @Override
    public void run() {
        try {
            while (stop == false) {
                if (ClientController.read() != 0) {
                    Object o = ClientController.readObject();
                    if (o instanceof String) {
                        String request = (String) o;
                        if (request.equals("newuser")) {
                            newUser();
                        } else if (request.equalsIgnoreCase("invite")) {
                            invite();
                        } else if (request.equalsIgnoreCase("debai")) {
                            play();
                        } else if (request.equalsIgnoreCase("thoat")) {
                            game.dispose();
                            this.setVisible(true);
                        } else if (request.equalsIgnoreCase("finish")) {
                            finish();
                        } else if (request.equalsIgnoreCase("reject")) {
                            mess("Đối thủ từ chối");
                        } else if (request.equalsIgnoreCase("noReplay")) {
                            game.mess2("Đối thủ không muốn chơi lại");
                            game.dispose();
                            this.setVisible(true);
                        } else if (request.equalsIgnoreCase("outgame")) {
                            game.mess2("Đối thủ đã rời game!");
                            game.dispose();
                            this.setVisible(true);
                        } else if (request.equalsIgnoreCase("offline")) {
                            ArrayList<User> listt = (ArrayList<User>) ClientController.readObject();
                            model.clear();
                            for (User user : listt) {
                                model.addElement(user);
                            }
                        } else if (request.equalsIgnoreCase("ioutgame")) {
                            game.dispose();
                            this.setVisible(true);
                        } else if (request.equalsIgnoreCase("bxh")) {
                            bxh();
                        } else if (request.equalsIgnoreCase("listInvite")) {
                            listInvite();
                    }
                }
                }  }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addMouseListener(MouseListener evt) {
        list.addMouseListener(evt);
    }

    public void mess(String s) {
        JOptionPane.showMessageDialog(rootPane, s);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public Object getSelectedObject(int i) {
        return model.getElementAt(i);
    }

    public void play() throws IOException, ClassNotFoundException {
        this.setVisible(false);
        ArrayList<String> list = (ArrayList<String>) ClientController.readObject();
        rival = (User) ClientController.readObject();// đối thủ
        game = new GameFrm(list, rival);
        game.runClock();
        game.setVisible(true);
    }

    public void newUser() throws IOException, ClassNotFoundException {
        User u = (User) ClientController.readObject();;
        model.addElement(u);
    }

    private void invite() throws IOException, ClassNotFoundException {
        User u2 = (User) ClientController.readObject();
        int select = JOptionPane.showConfirmDialog(rootPane, u2.getUsername().toUpperCase() + " muốn chơi với bạn");
        if (select == JOptionPane.YES_OPTION) {
            ClientController.writeObject("agree");
            ClientController.writeObject(u2);//người mời
            ClientController.writeObject(ClientController.u1);//người được mời
        } else {
            ClientController.writeObject("reject");
            ClientController.writeObject(u2);//người mời
        }
    }

    private void finish() throws IOException, ClassNotFoundException {
        String s = (String) ClientController.readObject();
        double d = (double) ClientController.readObject();
        game.mess(s, d);
        int confirm = game.confirm("Bạn có muốn tiếp tục không?");
        if (confirm == 0) {// = 0 là có chơi lại
            ClientController.writeObject("replay");
            ClientController.writeObject(ClientController.u1);
            game.mess2("Chờ đối thủ...");
            game.dispose();
            this.setVisible(true);
        } else {
            ClientController.writeObject("noReplay");
            ClientController.writeObject(rival);
            game.dispose();
            this.setVisible(true);
        }
    }

    private void bxh() throws IOException, ClassNotFoundException {
        ArrayList<User> listUser = (ArrayList<User>) ClientController.readObject();
        RankFrm rankFrm = new RankFrm(listUser);
        rankFrm.setVisible(true);
    }
    private void listInvite() throws IOException, ClassNotFoundException  {
        ArrayList<User> listInvite = (ArrayList<User>) ClientController.readObject();
        ListInviteFrm listInviteFrm = new ListInviteFrm();
        listInviteFrm.setVisible(true);
        
    }
}
