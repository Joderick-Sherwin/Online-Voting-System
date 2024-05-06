package com.onlinevotingsystem.registration;

public class Voter {
    private int id;
    private String name;
    private String email;
    private String voterID;
    private String address;
    private String dateOfBirth;
    private String securityQuestion;
    private String securityAnswer;

    public Voter(String name, String email, String voterID, String address, String dateOfBirth,
                 String securityQuestion, String securityAnswer) {
        this.name = name;
        this.email = email;
        this.voterID = voterID;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    // Getters and setters for all fields

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", voterID='" + voterID + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                '}';
    }
}
