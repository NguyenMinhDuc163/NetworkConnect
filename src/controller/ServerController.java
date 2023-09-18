package controller;

import DAO.GetDB;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import model.User;
import view.ServerView;

/**
 *
 * @author admin
 */
public class ServerController extends Thread {

    private ServerView sView;
    private ServerSocket myServer;
    private Socket clientSocket;
    public static ArrayList<ServerHandle> handleList = new ArrayList<>();
    public static HashMap<User, String> pair = new HashMap<>();
    public static ArrayList<User> replay = new ArrayList<>();

    public ServerController(ServerView sView, int serverPort) {
        this.sView = sView;
        openServer(serverPort);
        sView.showMessage("TCP server is running...");
    }

    public ArrayList<ServerHandle> getHandleList() {
        return this.handleList;
    }

    public HashMap<User, String> getPair() {
        return this.pair;
    }

    public void addPair(User u, String s) {
        this.pair.put(u, s);
    }

    private void openServer(int portNumber) {
        try {
            myServer = new ServerSocket(portNumber);
        } catch (IOException e) {
            sView.showMessage(e.toString());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                clientSocket = myServer.accept();
                ServerHandle sh = new ServerHandle(this, clientSocket);
                this.handleList.add(sh);
                sh.start();
            }
        } catch (Exception e) {
            sView.showMessage(e.toString());
        }
    }


}
