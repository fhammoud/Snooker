package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String name;
    private int score;
    private int breakScore;
    private int highestBreak;

    private ArrayList<Shot> currentGameShots;
    private ArrayList<Shot> shots;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.highestBreak = 0;
        shots = new ArrayList<>();
        currentGameShots = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getBreakScore() {
        int br = 0;
        for (int i = currentGameShots.size() - 1; i >= 0; i--) {
            Shot shot = currentGameShots.get(i);
            if (shot.isSuccess() && !shot.getType().equals("safety")) {
                br += shot.getBall().getValue();
            } else
                break;
        }

        return br;
    }

    /*public void setBreakScore(int breakScore) {
        this.breakScore = breakScore;
    }

    public void addBreakScore(int breakScore) {
        this.breakScore += breakScore;
        setHighestBreak(this.breakScore);
    }

    public void setHighestBreak(int highestBreak) {
        if (highestBreak > this.highestBreak)
            this.highestBreak = highestBreak;
    }*/

    public int getHighestBreak() {
        int highest = 0;
        int score = 0;
        for (Shot shot : currentGameShots) {
            if (shot.isSuccess() && !shot.getType().equals("safety")) {
                score += shot.getBall().getValue();
            }

            if (score > highest)
                highest = score;
            score = 0;
        }

        return highest;
    }

    public void addShot(Shot shot) {
        this.shots.add(shot);
        this.currentGameShots.add(shot);
        addScore(shot.getBall().getValue());
    }

    public void removeShot() {
        this.currentGameShots.remove(this.currentGameShots.size() - 1);
        Shot shot = this.shots.remove(this.shots.size() - 1);
        this.score -= shot.getBall().getValue();
    }

    public double getPotSuccess() {
        int potCount = 0;
        int successCount = 0;
        for (Shot shot : shots)
            if (!shot.getType().equals("safety")) {
                potCount++;
                if (shot.isSuccess())
                    successCount++;
            }
        if (potCount == 0) potCount++;
        return (double)successCount / potCount * 100;
    }

    public double getLongPotSuccess() {
        int longCount = 0;
        int successCount = 0;
        for (Shot shot : shots) {
            if (shot.getType().equals("long")) {
                longCount++;
                if (shot.isSuccess())
                    successCount++;
            }
        }
        if (longCount == 0) longCount++;
        return (double)successCount / longCount * 100;
    }
}
