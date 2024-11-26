package com.example;

import java.util.ArrayList;

public class Chat {
    ArrayList<GestoreServer> threads;
    ArrayList<Messaggi> cronologia = new ArrayList<>();

    public Chat(){
        threads = new ArrayList<GestoreServer>();
        cronologia = new ArrayList<Messaggi>();
    }

    public ArrayList<GestoreServer> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<GestoreServer> threads) {
        this.threads = threads;
    }

    public void aggiungiMessaggio(String mitt, String dest, String testo){
        Messaggi messaggio = new Messaggi(mitt, dest, testo); 
        cronologia.add(messaggio); // aggiunge il messaggio alla cronologia
    }

    public ArrayList<String> getCronologia(String mittente, String destinatario) {
        ArrayList<String> risultati = new ArrayList<>();
        for (Messaggi messaggio : cronologia) { // si va a prendere la chat di cui si vuole ottenere la cronologia controllando il mittente e il destinatario 
            if ((messaggio.getDest().equals(mittente) && messaggio.getMitt().equals(destinatario)) || (messaggio.getDest().equals(destinatario) && messaggio.getMitt().equals(mittente))){
                risultati.add(messaggio.getMessaggio()); // si fa tornare una lista di tutti i messaggi della chat fra i due utenti
            }
        }
        return risultati;
    }
    /*public void removeCrono(String mittente, String destinatario) {
         for (Messaggi messaggio : cronologia) {
            if ((messaggio.getDest().equals(mittente) && messaggio.getMitt().equals(destinatario)) || (messaggio.getDest().equals(destinatario) && messaggio.getMitt().equals(mittente))){
                cronologia.remove(messaggio.getMessaggio());
            }
        }
    }*/
    
    
}
