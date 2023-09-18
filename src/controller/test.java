package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Xuan Loc
 */
public class test {

    public static void main(String[] args) throws IOException {
        MulticastSocket socket = new MulticastSocket(1107);
        InetAddress address = InetAddress.getByName("224.2.2.3");
        socket.joinGroup(address);
        byte[] buf = new byte[256];
        DatagramPacket packet;
        while (true) {
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData());
            System.out.println("Message from server: " + received);
            byte[] data=new byte[1024];
//            String s="Xin chao toi la client 1!";
//            data=s.getBytes();
//            DatagramPacket dpsend=new DatagramPacket(data, data.length,address,1107); 
//            socket.send(dpsend);
        }
    }
}
