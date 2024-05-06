package com.onlinevotingsystem.poll;

public class Poll {
    private int id;
    private String question;
    private String option1;
    private String option2;
    private int votesOption1;
    private int votesOption2;

    public Poll(String question, String option1, String option2) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.votesOption1 = 0;
        this.votesOption2 = 0;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public int getVotesOption1() {
        return votesOption1;
    }

    public void setVotesOption1(int votesOption1) {
        this.votesOption1 = votesOption1;
    }

    public int getVotesOption2() {
        return votesOption2;
    }

    public void setVotesOption2(int votesOption2) {
        this.votesOption2 = votesOption2;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", votesOption1=" + votesOption1 +
                ", votesOption2=" + votesOption2 +
                '}';
    }
}
