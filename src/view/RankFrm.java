package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.User;

/**
 *
 * @author admin
 */
public class RankFrm extends JFrame {

    JScrollPane scrollPane;
    JLabel lb;
    JComboBox<String> comboBox;
    DefaultComboBoxModel<String> model2;
    JPanel panel;
    ArrayList<User> list;
    JTable rankTable;
    DefaultTableModel model;
    Vector vtData, vtHeader;

    public RankFrm(ArrayList<User> listt) throws HeadlessException {
        super();
        setTitle("Bảng xếp hạng");
        list = new ArrayList<>();
        list = listt;
        vtHeader = new Vector();
        vtData = new Vector();
        vtHeader.add("STT");
        vtHeader.add("Tên");
        vtHeader.add("Điểm");
        vtHeader.add("Thời gian thắng trung bình");
        model = new DefaultTableModel(vtData, vtHeader);
        rankTable = new JTable();
        rankTable.setModel(model);
        scrollPane = new JScrollPane(rankTable);
        rankTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        rankTable.getColumnModel().getColumn(0).setMaxWidth(30);
        lb = new JLabel("Sắp xếp theo: ");
        lb.setFont(new Font("arial", Font.BOLD, 16));
        rankTable.setFont(new Font("times new roman", Font.PLAIN, 20));
        rankTable.setRowHeight(23);
        model2 = new DefaultComboBoxModel<>();
        model2.addElement("Tổng số điểm giảm dần");
        model2.addElement("Trung bình thời gian kết thúc trận thắng tăng dần");
        model2.addElement("Trung bình điểm của các đối thủ đã gặp giảm dần");
        comboBox = new JComboBox<>(model2);
        comboBox.setFont(new Font("arial", Font.BOLD, 16));
        sortByPoint();
        panel = new JPanel(new FlowLayout());
        panel.add(lb);
        panel.add(comboBox);
        panel.setPreferredSize(new Dimension(600, 50));
        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane);
        this.setSize(600, 800);
        this.setLocationRelativeTo(null);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == 0) {
                    model.setRowCount(0);
                    sortByPoint();
                } else if (comboBox.getSelectedIndex() == 1) {
                    model.setRowCount(0);
                    sortByTime();
                } else {
                    sortByRivalPoint();
                }
            }
        });
    }

    public void sortByPoint() {
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getWinGames() == 0 && o2.getWinGames() != 0) {
                    double time2;
                    String s2[] = o2.getTime().toString().split(":");
                    time2 = (Integer.parseInt(s2[0]) * 3600 + Integer.parseInt(s2[1]) * 60
                            + Integer.parseInt(s2[2])) / o2.getWinGames();
                    int time22 = (int) time2;
                    String s22;
                    s22 = time22 / 3600 + ":" + (time22 - time22 / 3600 * 3600) / 60 + ":"
                            + (time22 - time22 / 3600 * 3600 - (time22 - time22 / 3600 * 3600) / 60 * 60);
                    o2.setAverageTime(s22);
                    o1.setAverageTime("0:0:0");
                } else if (o2.getWinGames() == 0) {
                    if (o1.getWinGames() == 0) {
                        o1.setAverageTime("0:0:0");
                    } else {
                        double time1;
                        String s1[] = o1.getTime().toString().split(":");
                        time1 = (Integer.parseInt(s1[0]) * 3600 + Integer.parseInt(s1[1]) * 60
                                + Integer.parseInt(s1[2])) / o1.getWinGames();
                        int time11 = (int) time1;
                        String s11;
                        s11 = time11 / 3600 + ":" + (time11 - time11 / 3600 * 3600) / 60 + ":"
                                + (time11 - time11 / 3600 * 3600 - (time11 - time11 / 3600 * 3600) / 60 * 60);
                        o1.setAverageTime(s11);
                    }
                    o2.setAverageTime("0:0:0");
                } else if (o1.getWinGames() != 0 && o2.getWinGames() != 0) {
                    double time1, time2;
                    String s1[] = o1.getTime().toString().split(":");
                    String s2[] = o2.getTime().toString().split(":");
                    time1 = (Integer.parseInt(s1[0]) * 3600 + Integer.parseInt(s1[1]) * 60
                            + Integer.parseInt(s1[2])) / o1.getWinGames();
                    time2 = (Integer.parseInt(s2[0]) * 3600 + Integer.parseInt(s2[1]) * 60
                            + Integer.parseInt(s2[2])) / o2.getWinGames();
                    int time11 = (int) time1;
                    int time22 = (int) time2;
                    String s11, s22;
                    s11 = time11 / 3600 + ":" + (time11 - time11 / 3600 * 3600) / 60 + ":"
                            + (time11 - time11 / 3600 * 3600 - (time11 - time11 / 3600 * 3600) / 60 * 60);
                    s22 = time22 / 3600 + ":" + (time22 - time22 / 3600 * 3600) / 60 + ":"
                            + (time22 - time22 / 3600 * 3600 - (time22 - time22 / 3600 * 3600) / 60 * 60);
                    o1.setAverageTime(s11);
                    o2.setAverageTime(s22);
                }
                return o1.getPoint() > o2.getPoint() ? -1 : 1;
            }
        });
        int i=1;
        for (User user : list) {
            Vector vtRow = new Vector();
            vtRow.add(i);
            vtRow.add(user.getName());
            vtRow.add(user.getPoint());
            vtRow.add(user.getAverageTime());
//            vtData.add(vtRow);
            model.addRow(vtRow);
            i++;
        }
    }

    public void sortByTime() {
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getWinGames() == 0 && o2.getWinGames() != 0) {
                    double time2;
                    String s2[] = o2.getTime().toString().split(":");
                    time2 = (Integer.parseInt(s2[0]) * 3600 + Integer.parseInt(s2[1]) * 60
                            + Integer.parseInt(s2[2])) / o2.getWinGames();
                    int time22 = (int) time2;
                    String s22;
                    s22 = time22 / 3600 + ":" + (time22 - time22 / 3600 * 3600) / 60 + ":"
                            + (time22 - time22 / 3600 * 3600 - (time22 - time22 / 3600 * 3600) / 60 * 60);
                    o2.setAverageTime(s22);
                    o1.setAverageTime("0:0:0");
                    return 1;
                } else if (o2.getWinGames() == 0) {
                    if (o1.getWinGames() == 0) {
                        o1.setAverageTime("0:0:0");
                    } else {
                        double time1;
                        String s1[] = o1.getTime().toString().split(":");
                        time1 = (Integer.parseInt(s1[0]) * 3600 + Integer.parseInt(s1[1]) * 60
                                + Integer.parseInt(s1[2])) / o1.getWinGames();
                        int time11 = (int) time1;
                        String s11;
                        s11 = time11 / 3600 + ":" + (time11 - time11 / 3600 * 3600) / 60 + ":"
                                + (time11 - time11 / 3600 * 3600 - (time11 - time11 / 3600 * 3600) / 60 * 60);
                        o1.setAverageTime(s11);
                    }
                    o2.setAverageTime("0:0:0");
                    return -1;
                } else {
                    double time1, time2;
                    String s1[] = o1.getTime().toString().split(":");
                    String s2[] = o2.getTime().toString().split(":");
                    time1 = (Integer.parseInt(s1[0]) * 3600 + Integer.parseInt(s1[1]) * 60
                            + Integer.parseInt(s1[2])) / o1.getWinGames();
                    time2 = (Integer.parseInt(s2[0]) * 3600 + Integer.parseInt(s2[1]) * 60
                            + Integer.parseInt(s2[2])) / o2.getWinGames();
                    int time11 = (int) time1;
                    int time22 = (int) time2;
                    String s11, s22;
                    s11 = time11 / 3600 + ":" + (time11 - time11 / 3600 * 3600) / 60 + ":"
                            + (time11 - time11 / 3600 * 3600 - (time11 - time11 / 3600 * 3600) / 60 * 60);
                    s22 = time22 / 3600 + ":" + (time22 - time22 / 3600 * 3600) / 60 + ":"
                            + (time22 - time22 / 3600 * 3600 - (time22 - time22 / 3600 * 3600) / 60 * 60);
                    o1.setAverageTime(s11);
                    o2.setAverageTime(s22);
                    return time1 < time2 ? -1 : 1;
                }
            }
        });
        int i=1;
        for (User user : list) {
            Vector vtRow = new Vector();
            vtRow.add(i);
            vtRow.add(user.getName());
            vtRow.add(user.getPoint());
            vtRow.add(user.getAverageTime());
//            vtData.add(vtRow);
            model.addRow(vtRow);
            i++;
        }
    }

    public void sortByRivalPoint() {

    }
}
