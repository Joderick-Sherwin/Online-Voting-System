package com.onlinevotingsystem.result;

public class Result {
    private int id;
    private String electionName;
    private String winnerName;
    private int votesWinner;

    public Result(String electionName, String winnerName, int votesWinner) {
        this.electionName = electionName;
        this.winnerName = winnerName;
        this.votesWinner = votesWinner;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getVotesWinner() {
        return votesWinner;
    }

    public void setVotesWinner(int votesWinner) {
        this.votesWinner = votesWinner;
    }

@Override
public String toString() {
    return "Result{" +
            "id=" + id +
            ", electionName='" + electionName + '\'' +
            ", winnerName='" + winnerName + '\'' +
            ", votesWinner=" + votesWinner +
            '}';
}
}