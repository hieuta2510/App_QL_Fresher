package edu.ptit.ql_fresher.model;

public class Fresher {
    private int id;
    private String name, language, email;
    private float score;
    private String phone, dateOfBirth;
    private String center;

    public Fresher(int id, String name, String language, String email, float score, String phone, String dateOfBirth, String center) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.email = email;
        this.score = score;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.center = center;
    }

    public Fresher(String name, String language, String email, float score, String phone, String dateOfBirth, String center) {
        this.name = name;
        this.language = language;
        this.email = email;
        this.score = score;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.center = center;
    }

    public Fresher(String name, String language, String email, float score, String phone, String dateOfBirth) {
        this.name = name;
        this.language = language;
        this.email = email;
        this.score = score;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }
}