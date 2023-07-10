package edu.ptit.ql_fresher.model;

public class Center {
    private int id;
    private String name, address;
    private int totalFresher;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalFresher() {
        return totalFresher;
    }

    public Center(String name, String address, int totalFresher) {
        this.name = name;
        this.address = address;
        this.totalFresher = totalFresher;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalFresher(int totalFresher) {
        this.totalFresher = totalFresher;
    }
}