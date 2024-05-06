package com.onlinevotingsystem.candidate;

public class Candidate {
    private int id;
    private String name;
    private String party;
    private String platform;

    public Candidate(String name, String party, String platform) {
        this.name = name;
        this.party = party;
        this.platform = platform;
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

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
