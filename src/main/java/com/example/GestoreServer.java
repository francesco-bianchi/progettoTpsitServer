package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class GestoreServer extends Thread {
    Socket socket;
    Socket socket2;
    ArrayList<String> user;
    Chat chat;
    BufferedReader in;
    DataOutputStream out;

    public GestoreServer(Socket socket, ArrayList<String> user, Chat chat) {
        this.socket = socket;
        this.user = user;
        this.chat = chat;
    }


    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            boolean presente = false;
            String fraseRic = "";
            String username = "";
            String[] fraseSplit;

            do {
                presente = false;
                username = in.readLine();

                if (user.contains(username)) {
                    out.writeBytes("KOS" + "\n");
                    presente = true;
                } else {
                    System.out.println("Utente " + username + " si è connesso");
                    user.add(username);
                    this.setName(username);
                    chat.getThreads().add(this);
                    out.writeBytes("OKS" + "\n");
                }

            } while (presente);

            do { // fai switch
                String lista;
                String listaMess = "";
                fraseRic = in.readLine();
                fraseSplit = fraseRic.split(":"); // Si divide la stringa per controllare ciò che l'utente passa
                
                switch (fraseSplit[0]) {
                    case "C":

                        lista = "";
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            lista += chat.getThreads().get(i).getName() + ";";
                        }

                        out.writeBytes(lista + "\n");

                        break;
                    case "PRIV":
                        boolean inviato = false;
                        String destinatario = fraseSplit[1];
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            if (chat.getThreads().get(i).getName().equals(destinatario)) {
                                if (fraseSplit[2].equals("CR")) {
                                    if (!chat.getCronologia(this.getName(), destinatario).isEmpty()) {
                                        for(int j=0;j< chat.getCronologia(this.getName(), destinatario).size();j++){
                                            listaMess += chat.getCronologia(this.getName(), destinatario).get(j) + ";";
                                        }                           
                                        out.writeBytes("CR:" + listaMess + "\n");
                                        System.out.println(fraseSplit[0]);
                                    } else {
                                        out.writeBytes("NO\n");
                                    }

                                } else{
                                    System.out.println("Frase aggiunta a:" + destinatario);
                                    chat.aggiungiMessaggio(this.getName(), destinatario, fraseSplit[2]);
                                    chat.getThreads().get(i).inviaClient(fraseSplit[0]+":" +this.getName() + ": " + fraseSplit[2]);
                                    System.out.println(fraseSplit[0]);
                                }
                                inviato = true;
                            }
                        }
                        if (!inviato) {
                            out.writeBytes("KO\n");
                        }
                        break;
                    case "ALL":
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            if(!chat.getThreads().get(i).getName().equals(this.getName())){
                                chat.getThreads().get(i).inviaClient("ALL: " + fraseSplit[1] + ":" + this.getName());
                            }
                        }

                        break;

                    default:
                        System.out.println("Client disconnesso");
                        user.remove(this.getName());
                        chat.getThreads().remove(this);
                        break;
                }

            } while (!fraseSplit[0].equals("EXT"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void inviaClient(String msg) {

        try {
            out.writeBytes(msg + "\n");

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
