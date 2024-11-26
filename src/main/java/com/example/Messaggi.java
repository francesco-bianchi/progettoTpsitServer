package com.example;


public class Messaggi {
    String mitt;
    String dest;
    String messaggio;
    public Messaggi(String mitt, String dest, String messaggio){
        this.mitt = mitt;
        this.dest = dest;
        this.messaggio = messaggio;
    }
    public Messaggi(){}
    
    public String getMitt() {
        return mitt;
    }
    public void setMitt(String mitt) {
        this.mitt = mitt;
    }
    public String getDest() {
        return dest;
    }
    public void setDest(String dest) {
        this.dest = dest;
    }
    public String getMessaggio() {
        return messaggio;
    }
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }


    
    
}
