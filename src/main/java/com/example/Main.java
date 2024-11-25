package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            ArrayList <String> arrUser = new ArrayList<String>();
            Chat chat = new Chat();
            System.out.println("Server partito");
            while(true){
                Socket socket = serverSocket.accept();
                GestoreServer gs = new GestoreServer(socket,  arrUser, chat);
                gs.start();
            }
        }
    }
}