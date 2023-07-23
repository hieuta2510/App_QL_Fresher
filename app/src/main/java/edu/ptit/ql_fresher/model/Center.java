package edu.ptit.ql_fresher.model;

import java.io.Serializable;

public class Center implements Serializable {
    private int id;
    private String acronym;
    private String name;
    private String address;
    private int totalFresher;

    public Center() {
    }

    public Center(int id, String acronym, String name, String address, int totalFresher) {
        this.id = id;
        this.acronym = acronym;
        this.name = name;
        this.address = address;
        this.totalFresher = totalFresher;
    }

    public Center(String acronym, String name, String address, int totalFresher) {
        this.acronym = acronym;
        this.name = name;
        this.address = address;
        this.totalFresher = totalFresher;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalFresher() {
        return totalFresher;
    }

    public void setTotalFresher(int totalFresher) {
        this.totalFresher = totalFresher;
    }

    @Override
    public String toString() {
        return "Center{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", address='" + address + '\'' +
                ", totalFresher=" + totalFresher +
                '}';
    }
}