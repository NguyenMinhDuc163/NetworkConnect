package controller;

import view.ServerView;

/**
 *
 * @author admin
 */
public class ServerRun {

    public static void main(String[] args) {
        ServerView view = new ServerView();
        int port = 8888;
        ServerController server = new ServerController(view, port);
        server.start();
    }
}
