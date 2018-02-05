package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String name;
    private int score;
    private int breakScore;
    private int highestBreak;

    private ArrayList<Shot> shots;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.highestBreak = 0;
        shots = new ArrayList<>();
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
        return breakScore;
    }

    public void setBreakScore(int breakScore) {
        this.breakScore = breakScore;
    }

    public void addBreakScore(int breakScore) {
        this.breakScore += breakScore;
        setHighestBreak(this.breakScore);
    }

    public int getHighestBreak() {
        return highestBreak;
    }

    public void setHighestBreak(int highestBreak) {
        if (highestBreak > this.highestBreak)
            this.highestBreak = highestBreak;
    }

    public void addShot(Shot shot) {
        this.shots.add(shot);
    }

    public double getPotSuccess() {
        int count = 0;
        int size = shots.size();
        for (Shot shot : shots)
            if (shot.isSuccess())
                count++;
        if (size == 0) size++;
        return (double)count / size * 100;
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
