package controller;

import DAO.GetDB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import model.User;

/**
 *
 * @author admin
 */
public class ServerHandle extends Thread {

    public Socket clientSocket;
    public ServerController server;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    public User u;
    public ArrayList<User> listUserOnline;
    public ArrayList<String> list;
    public boolean stop = false;
    public GetDB getDB = new GetDB();

    public ServerHandle() {
    }

    public ServerHandle(ServerController server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop) {
            handle();
        }
    }

    private void handle() {
        String received;
        try {
            received = (String) ois.readObject();
            if (received.equalsIgnoreCase("login")) {
                login();
            } else if (received.equalsIgnoreCase("regis")) {
                regis();
            } else if (received.equalsIgnoreCase("invite")) {
                invite();
            } else if (received.equalsIgnoreCase("agree")) {
                agree();
            } else if (received.equalsIgnoreCase("reject")) {
                reject();
            } else if (received.equalsIgnoreCase("thoat")) {
                User u4 = (User) this.ois.readObject();
                ArrayList<ServerHandle> handleList = server.getHandleList();
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(u4.getUsername())) {
                        sh.sendObject("thoat");
                    }
                }
                this.sendObject("thoat");
            } else if (received.equalsIgnoreCase("finish")) {
                User u1 = new User();
                u1 = (User) this.ois.readObject();
                String s1 = (String) this.ois.readObject();
                server.addPair(u1, s1);
                if (server.getPair().size() == 2) {
                    resultHandle(server.getPair());
                }
                
            } else if (received.equalsIgnoreCase("replay")) {
                replay();
            } else if (received.equalsIgnoreCase("noReplay")) {
                User user = (User) ois.readObject();
                ArrayList<ServerHandle> handleList = server.getHandleList();
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(user.getUsername())) {
                        sh.sendObject("noReplay");
                    }
                }
            } else if (received.equalsIgnoreCase("outgame")) {
                User user = (User) ois.readObject();
                ArrayList<ServerHandle> handleList = server.getHandleList();
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(user.getUsername())) {
                        sh.sendObject("outgame");
                    }
                }
                this.sendObject("ioutgame");
            } else if (received.equalsIgnoreCase("offline")) {
                offline();
            } else if (received.equalsIgnoreCase("bxh")) {
                bxh();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resultHandle(HashMap<User, String> pair) throws SQLException {
        Set<User> keySet = pair.keySet();
        ArrayList<User> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<ServerHandle> handleList = server.getHandleList();
        for (User u : keySet) {
            list1.add(u);
            list2.add(pair.get(u));
        }
        User u1 = new User();
        u1 = list1.get(0);
        User u2 = new User();
        u2 = list1.get(1);
        String s1 = list2.get(0);
        String s2 = list2.get(1);
        if (s1.equalsIgnoreCase("hoanthanh") && s2.equalsIgnoreCase("hoanthanh")) {
            int i = u1.getTime().compareTo(u2.getTime());
            if (i < 0) {
                getDB.updatePoint(u1, 1.0);
                getDB.updateTime(u1, u1.getTime());
                getDB.addGameWin(u1);
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Thắng");
                        sh.sendObject(1.0);
                    }
                    if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Thua");
                        sh.sendObject(0.0);
                    }
                }
            } else if (i == 0) {
                getDB.updatePoint(u1, 0.5);
                getDB.updatePoint(u2, 0.5);
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Hòa");
                        sh.sendObject(0.5);
                    }
                    if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Hòa");
                        sh.sendObject(0.5);
                    }
                }
            } else {
                getDB.updatePoint(u2, 1.0);
                getDB.updateTime(u2, u2.getTime());
                getDB.addGameWin(u2);
                for (ServerHandle sh : handleList) {
                    if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Thắng");
                        sh.sendObject(1.0);
                    }
                    if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                        sh.sendObject("finish");
                        sh.sendObject("Thua");
                        sh.sendObject(0.0);
                    }
                }
            }
        } else if (s1.equalsIgnoreCase("hoanthanh") && s2.equalsIgnoreCase("khonghoanthanh")) {
            getDB.updatePoint(u1, 1.0);
            getDB.updateTime(u1, u1.getTime());
            getDB.addGameWin(u1);
            for (ServerHandle sh : handleList) {
                if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Thắng");
                    sh.sendObject(1.0);
                }
                if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Thua");
                    sh.sendObject(0.0);
                }
            }
        } else if (s2.equalsIgnoreCase("hoanthanh") && s1.equalsIgnoreCase("khonghoanthanh")) {
            getDB.updatePoint(u2, 1.0);
            getDB.updateTime(u2, u2.getTime());
            getDB.addGameWin(u1);
            for (ServerHandle sh : handleList) {
                if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Thắng");
                    sh.sendObject(1.0);
                }
                if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Thua");
                    sh.sendObject(0.0);
                }
            }
        } else if (s1.equalsIgnoreCase("khonghoanthanh") && s2.equalsIgnoreCase("khonghoanthanh")) {
            getDB.updatePoint(u1, 0.5);
            getDB.updatePoint(u2, 0.5);
            for (ServerHandle sh : handleList) {
                if (sh.getU().getUsername().equalsIgnoreCase(u1.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Hòa");
                    sh.sendObject(0.5);
                }
                if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                    sh.sendObject("finish");
                    sh.sendObject("Hòa");
                    sh.sendObject(0.5);
                }
            }
        }
        server.getPair().clear();
    }



    public User getU() {
        return this.u;
    }


    public void sendObject(Object o) {
        try {
            this.oos.writeObject(o);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMsg(int i) throws IOException {
        this.oos.write(i);
    }

    public void guiDeBai(ArrayList<String> listt, User user1) {
        this.sendObject("debai");
        this.sendObject(listt);
        this.sendObject(user1);
    }

    public ArrayList<String> taoDeBai() {
        list = new ArrayList<>();
        int k = 1;
        for (int i = 1; i <= 16; i++) {
            list.add("" + k++);
        }
        Collections.shuffle(list);
        return list;
    }

    private void login() throws Exception {
        Object o = ois.readObject();
        if (o instanceof User) {
            User user = (User) o;
            if (getDB.checkUser(user)) {
                oos.writeObject("true");
                u = getDB.getUserDB(user);
                System.out.println(u.getName()+" đã đăng nhập");
                u.setStatus("online");
                listUserOnline = new ArrayList<>();
                ArrayList<ServerHandle> handleList = server.getHandleList();
                //lay danh sach cac user online 
                if (handleList.size() > 1) {
                    for (ServerHandle handle : handleList) {
                        if (handle != this) {
                            listUserOnline.add(handle.getU());
                        }
                    }
                    //them user hien tai vao cac client khac
                    for (ServerHandle handle : handleList) {
                        if (handle != this) {
                            handle.listUserOnline.add(u);
                            handle.sendObject("newuser");
                            handle.sendObject(u);
                        }
                    }
                }

                listUserOnline.add(u);
                oos.writeObject(listUserOnline);
            } else {
                oos.writeObject("false");
            }
        }
    }

    private void regis() throws IOException, ClassNotFoundException {
        User user = (User) ois.readObject();
        if (getDB.addUserDB(user)) {
            oos.writeObject("true");
        } else {
            oos.writeObject("false");
        }
    }

    private void invite() throws IOException, ClassNotFoundException {
        User u2 = (User) ois.readObject();
        User u3 = (User) ois.readObject();
        ArrayList<ServerHandle> handleList = server.getHandleList();
        for (ServerHandle sh : handleList) {
            if (sh.getU().getUsername().equalsIgnoreCase(u2.getUsername())) {
                sh.sendObject("invite");
                sh.sendObject(u3);//người dc mời
            }
        }
    }

    private void agree() throws IOException, ClassNotFoundException {
        User u3 = (User) this.ois.readObject();//người mời
        User u4 = (User) this.ois.readObject();//người được mời
        ArrayList<String> listt = taoDeBai();
        this.guiDeBai(listt, u3);
        ArrayList<ServerHandle> handleList = server.getHandleList();
        for (ServerHandle sh : handleList) {
            if (sh.getU().getUsername().equalsIgnoreCase(u3.getUsername())) {
                sh.guiDeBai(listt, u4);
            }
        }
    }

    private void reject() throws IOException, ClassNotFoundException {
        User user = (User) ois.readObject();
        ArrayList<ServerHandle> handleList = server.getHandleList();
        for (ServerHandle sh : handleList) {
            if (sh.getU().getUsername().equalsIgnoreCase(user.getUsername())) {
                sh.sendObject("reject");
            }
        }
    }

    private void replay() throws IOException, ClassNotFoundException {
        User user = (User) ois.readObject();
        server.replay.add(user);
        if (server.replay.size() == 2) {
            User user1 = server.replay.get(0);
            User user2 = server.replay.get(1);
            ArrayList<String> listt = taoDeBai();
            ArrayList<ServerHandle> handleList = server.getHandleList();
            for (ServerHandle sh : handleList) {
                if (sh.getU().getUsername().equalsIgnoreCase(user1.getUsername())) {
                    sh.guiDeBai(listt, user2);
                }
                if (sh.getU().getUsername().equalsIgnoreCase(user2.getUsername())) {
                    sh.guiDeBai(listt, user1);
                }
            }
            server.replay.clear();
        }
    }

    private void offline() {
//      this.ois.close();
//      this.oos.close();
//      this.con.close();
//      this.clientSocket.close();
        server.getHandleList().remove(this);
        ArrayList<ServerHandle> handleList = server.getHandleList();
        System.out.println(getU().getName()+" đã thoát!");
        this.listUserOnline.remove(getU());
        if (handleList.size() > 0) {
            for (ServerHandle sh : handleList) {
                sh.listUserOnline.remove(this.getU());
                sh.sendObject("offline");
                sh.sendObject(listUserOnline);
            }
        }
        this.stop = true;
    }

    private void bxh() throws SQLException, IOException {
        ArrayList<User> listUser = getDB.getUserListDB();
        oos.writeObject("bxh");
        oos.writeObject(listUser);
    }
    
     private void listInvite() throws SQLException, IOException {
        ArrayList<User> listUser = getDB.getUserListDB();
        oos.writeObject("listInvite");
        oos.writeObject(listUser);
    }

}
