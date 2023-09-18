package view;

import controller.ClientController;
import controller.HourThread;
import controller.MinuteThread;
import controller.SecondThread;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.User;

/**
 *
 * @author admin
 */
public class GameFrm extends JFrame implements ActionListener {

    private JPanel p, p2;
    private int row, col, mul;
    private JButton bt[][] = new JButton[100][100];
    JLabel lbTime;
    private int t;
    HourThread htd;
    MinuteThread mtd;
    SecondThread std;
    private int hour = 0, minute = 0, second = 0;

    /**
     * Creates new form ClientSide
     */
    public GameFrm(ArrayList<String> list, User rival) {
        super();
        setTitle("DÒ SỐ - " + ClientController.u1.getUsername().toUpperCase());
        row = 4;
        col = 4;
        mul = 16;
        p = new JPanel();
        p.setLayout(new GridLayout(row, col));
        int k = 1;
        k = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bt[i][j] = new JButton(list.get(k++));
                bt[i][j].setFont(new Font("arial", Font.PLAIN, 20));
                bt[i][j].addActionListener(this);
                p.add(bt[i][j]);
            }
        }
        p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        lbTime = new JLabel();
        JButton bt2 = new JButton("Thoát");
        bt2.setPreferredSize(new Dimension(100, 35));
        bt2.setFont(new Font("Arial", Font.PLAIN, 20));
        lbTime.setFont(new Font("Arial", Font.PLAIN, 20));
        lbTime.setText("20:00:00");
        lbTime.setPreferredSize(new Dimension(100, 50));
        p2.add(lbTime);
        p2.add(bt2);
        this.add(p, BorderLayout.CENTER);
        this.add(p2, BorderLayout.SOUTH);
        this.setVisible(true);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
//        this.pack();
        t = 0;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ClientController.writeObject("outgame");
                ClientController.writeObject(rival);
            }

        });
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select = JOptionPane.showConfirmDialog(rootPane, "Thoát?");
                if (select == JOptionPane.YES_OPTION) {
                    ClientController.writeObject("thoat");
                    ClientController.writeObject(rival);
                }
            }
        });

    }

    public void setSecond(int second) {
        this.second = second;
        lbTime.setText(hour + ":" + minute + ":" + second);
    }

    public void setMinute(int minute) {
        this.minute = minute;
        lbTime.setText(hour + ":" + minute + ":" + second);
    }

    public void setHour(int hour) {
        this.hour = hour;
        lbTime.setText(hour + ":" + minute + ":" + second);
    }

    public void runClock() {
        htd = new HourThread(this);
        mtd = new MinuteThread(this, htd);
        std = new SecondThread(this, mtd);
        htd.start();
        mtd.start();
        std.start();
    }

    public void mess(String s, double d) {
        JOptionPane.showMessageDialog(rootPane, "Bạn " + s + " được: " + d + " điểm");
    }

    public int confirm(String s) {
        return JOptionPane.showConfirmDialog(rootPane, s);
    }

    public void mess2(String s) {
        JOptionPane.showMessageDialog(rootPane, s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                if (e.getSource().equals(bt[i][j])) {
                    if (t == Integer.parseInt(bt[i][j].getText()) - 1) {
                        t = Integer.parseInt(bt[i][j].getText());
                        bt[i][j].setVisible(false);
                        if (t == mul) {
                            std.stopp();
                            JOptionPane.showMessageDialog(rootPane, "Chúc mừng bạn hoàn thành trong: " + lbTime.getText() + "s");
                            String[] ss = lbTime.getText().split(":");
                            int x, y, z;
                            x = Integer.parseInt(ss[0]);
                            y = Integer.parseInt(ss[1]);
                            z = Integer.parseInt(ss[2]);
                            Time time = new Time(x, y, z);
                            User uu = new User(null, ClientController.u1.getUsername(), ClientController.u1.getPassword(),
                                    null, 0.0, null, time, 0);
                            ClientController.writeObject("finish");
                            ClientController.writeObject(uu);
                            ClientController.writeObject("hoanthanh");
                        }
                    } else {
                        std.stopp();
                        for (int m = 1; m <= row; m++) {
                            for (int n = 1; n <= col; n++) {
                                bt[m][n].setEnabled(false);
                            }
                        }
                                JOptionPane.showMessageDialog(rootPane, "Bạn chọn sai. Không hoàn thành!");
                                ClientController.writeObject("finish");
//                        ClientController.writeObject(ClientController.u1);
                                Time time = new Time(00, 00, 00);
                                User uu = new User(null, ClientController.u1.getUsername(), ClientController.u1.getPassword(),
                                        null, 0.0, "online", time, 0);
                                ClientController.writeObject(uu);
                                ClientController.writeObject("khonghoanthanh");
                            }
                        }
                    }
                }
            }
        }
