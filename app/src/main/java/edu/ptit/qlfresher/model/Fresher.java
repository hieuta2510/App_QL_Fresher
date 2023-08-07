package edu.ptit.qlfresher.model;

public class Fresher implements Comparable<Fresher>{
    private String key;
    private String name;
    private String email;
    private String language;
    private String center;
    private String dateOfBirth;
    private String image;
    private String score;

    public Fresher() {
    }

    public Fresher(String key, String name, String email, String language, String center, String dateOfBirth, String image, String score) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.language = language;
        this.center = center;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    // sx theo ten
    @Override
    public int compareTo(Fresher fresher2) {
        String[] strFresher1 = this.name.replaceAll("\\p{M}", "").split("\\s+");
        String[] strFresher2 = fresher2.getName().replaceAll("\\p{M}", "").split("\\s+");

        // So sánh firstName
        String firstName1 = strFresher1[strFresher1.length - 1];
        String firstName2 = strFresher2[strFresher2.length - 1];

        return firstName1.compareTo(firstName2);
    }

}