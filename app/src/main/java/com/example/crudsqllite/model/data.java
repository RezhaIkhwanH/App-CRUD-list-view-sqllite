package com.example.crudsqllite.model;

public class data {
    private String id, name, address,phat;

    public data(){

    }

    public data(String id, String name, String address,String paht){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phat = paht;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhat() {
        return phat;
    }

    public void setPhat(String phat) {
        this.phat = phat;
    }
}
