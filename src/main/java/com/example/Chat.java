package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void aggiungiMessaggio(String dest, String testo){
        Messaggi messaggio = new Messaggi(dest, testo);
        cronologia.add(messaggio);
    }

    public ArrayList<String> getCronologia(String dest) {
        ArrayList<String> risultati = new ArrayList<>();
        for (Messaggi messaggio : cronologia) {
            if ((messaggio.getDest().equals(dest))){
                risultati.add(messaggio.getMessaggio());
            }
        }
        return risultati;
    }
    public void removeCrono(){
        cronologia.clear();
    }
    
    
}
