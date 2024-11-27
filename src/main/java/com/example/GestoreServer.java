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

            do { //si controlla la validità dell'utente
                presente = false;
                username = in.readLine();

                if (user.contains(username) || username.isEmpty()) {
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

            do {
                String lista;
                String listaMess = "";
                String listaMitt = "";
                fraseRic = in.readLine();
                fraseSplit = fraseRic.split(":"); // Si divide la stringa per controllare ciò che l'utente passa
                
                switch (fraseSplit[0]) {
                    case "C":

                        lista = "";
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            lista += chat.getThreads().get(i).getName() + ";";
                        }

                        out.writeBytes(lista + "\n"); // si manda la lista di tutti gli utenti nella chat

                        break;
                    case "PRIV":
                        boolean inviato = false;
                        String destinatario = fraseSplit[1];
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            if (chat.getThreads().get(i).getName().equals(destinatario)) { // si controlla se il destinatario è presente nella chat
                                if (fraseSplit[2].equals("CR")) { //se bisogna far vedere la cronologia
                                    if (!chat.getCronologia(this.getName(), destinatario).isEmpty()) { //se non è vuota si scorre e si mette in una stringa che verra inviata al thread del client
                                        for(int j=0;j< chat.getCronologia(this.getName(), destinatario).size();j++){
                                            listaMess += chat.getCronologia(this.getName(), destinatario).get(j) + ";";
                                            listaMitt += chat.getMitt(this.getName(), destinatario).get(j) + ";";
                                        }                           
                                        out.writeBytes("CR:" + listaMess + ":" + listaMitt +"\n");
                                    } else {
                                        out.writeBytes("NO\n");
                                    }

                                } else{ //se si manda un messaggio si invia al suo destinatario aggiungendolo nella cronologia di quella chat privata
                                    System.out.println("Frase aggiunta a:" + destinatario);
                                    chat.aggiungiMessaggio(this.getName(), destinatario, fraseSplit[2]);
                                    chat.getThreads().get(i).inviaClient(fraseSplit[0]+":" +this.getName() + ": " + fraseSplit[2]);
                                }
                                inviato = true;
                            }
                        }
                        if (!inviato) {
                            out.writeBytes("KO\n"); // nessun destinatario trovato
                        }
                        break;
                    case "ALL":
                        for (int i = 0; i < chat.getThreads().size(); i++) {
                            if(!chat.getThreads().get(i).getName().equals(this.getName())){
                                chat.getThreads().get(i).inviaClient("ALL: " + fraseSplit[1] + ":" + this.getName()); //si manda a tutti gli utenti tranne a quello che sta inviando il messaggio
                            }
                        }

                        break;

                    case "EXT": //uscita e rimozione dell'utente
                        System.out.println("Client disconnesso");
                        user.remove(this.getName());
                        chat.getThreads().remove(this);
                        break;
                    default:
                        
                        break;
                }

            } while (!fraseSplit[0].equals("EXT"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inviaClient(String msg) {

        try {
            out.writeBytes(msg + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
