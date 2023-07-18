package edu.ptit.ql_fresher.model;

public class Center {
    private int id;
    private String name;
    private String acronym;
    private String address;
    private String totalFresher;

    public Center() {
    }

    public Center(int id, String name, String acronym, String address, String totalFresher) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.address = address;
        this.totalFresher = totalFresher;
    }

    public Center(String name, String acronym, String address, String totalFresher) {
        this.name = name;
        this.acronym = acronym;
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

    public String getTotalFresher() {
        return totalFresher;
    }

    public void setTotalFresher(String totalFresher) {
        this.totalFresher = totalFresher;
    }
}