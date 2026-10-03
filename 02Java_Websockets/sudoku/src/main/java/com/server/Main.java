package com.server;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        int puerto = 3000;
        InetSocketAddress address = new InetSocketAddress(puerto);
        Server server = new Server(address);
        server.start();
    }

}
