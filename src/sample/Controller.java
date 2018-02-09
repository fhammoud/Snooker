package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Controller implements EventHandler<ActionEvent> {

    private Player player1;
    private Player player2;
    private Player player;
    private ArrayList<Ball> reds;
    private ArrayList<Ball> colors;
    private ArrayList<Shot> shots;
    private int pointsLeft;
    private boolean lastColor;

    public Button red;
    public Button yellow;
    public Button green;
    public Button brown;
    public Button blue;
    public Button pink;
    public Button black;
    public Button miss;
    public Button safety;
    public Button undo;
    public Button switchP;

    public RadioButton shortPot;
    public RadioButton longPot;
    public RadioButton foul;
    public RadioButton free;
    public ToggleGroup potType;

    public Label score1;
    public Label score2;

    private Label scoreLabel;

    public Label remaining;
    public Label break1;
    public Label break2;

    private Label breakLabel;

    public Label potSuccess1;
    public Label potSuccess2;
    public Label longPotSuccess1;
    public Label longPotSuccess2;
    public Label highestBreak1;
    public Label highestBreak2;
    public Label difference;

    private Label potSuccessLabel;
    private Label longPotSuccessLabel;
    private Label highestBreakLabel;


    public void initialize() {

        // Set initial state
        load();
        player = player1;
        scoreLabel = score1;
        breakLabel = break1;
        potSuccessLabel = potSuccess1;
        longPotSuccessLabel = longPotSuccess1;
        highestBreakLabel = highestBreak1;
        lastColor = false;

        // Initialize shots array
        shots = new ArrayList<>();

        // Create array of colors
        colors = new ArrayList<>(6);
        colors.add(new Ball(7));
        colors.add(new Ball(6));
        colors.add(new Ball(5));
        colors.add(new Ball(4));
        colors.add(new Ball(3));
        colors.add(new Ball(2));

        // Create array of reds
        reds = new ArrayList<>(15);
        for (int i = 0; i < 5; i++)
            reds.add(new Ball(1));

        // Set initial remaining points
        pointsLeft = getPointsLeft();
        remaining.setText("" + pointsLeft);
    }

    @Override
    public void handle(ActionEvent event) {

        int score = 0;
        String type = "";
        boolean success = true;
        Shot shot = null;

        // Check radio buttons
        if (shortPot.isSelected()) {
            type = "short";
        } else if (longPot.isSelected()) {
            type = "long";
        } else if (foul.isSelected()) {
            type = "foul";
            success = false;
        } else if (free.isSelected()) {
            type = "free";
        }

        // Handle buttons
        if (event.getSource().equals(red)) {
            score = 1;
        } else if (event.getSource().equals(yellow)) {
            score = 2;
        } else if (event.getSource().equals(green)) {
            score = 3;
        } else if (event.getSource().equals(brown)) {
            score = 4;
        } else if (event.getSource().equals(blue)) {
            score = 5;
        } else if (event.getSource().equals(pink)) {
            score = 6;
        } else if (event.getSource().equals(black)) {
            score = 7;
        } else if (event.getSource().equals(miss)) {
            success = false;
//            switchPlayer();
        } else if (event.getSource().equals(safety)) {
            type = "safety";
//            switchPlayer();
        } else if (event.getSource().equals(switchP)) {
            switchPlayer();
            return;
        } else if (event.getSource().equals(undo)) {
            popShot();
            return;
        }

        // Update scores
        if (score == 1) {
            if (!type.equals("free"))
                popBall(score);
            pointsLeft -= score;
        } else {
            if (lastColor)
                popBall(score);

            if (reds.size() == 0)
                lastColor = true;

            pointsLeft = getPointsLeft();
        }
        addShot(new Shot(player, type, new Ball(score), success));

        if (score > 0) {
//            addScore(score);
//            addBreak(score);
        } else
            switchPlayer();

        updateStats();
        updateTable();
        remaining.setText("" + pointsLeft);
    }

    /*private void addScore(int score) {
        Shot shot = null;
        if (foul.isSelected()) {
            shot = new Shot("foul", false);
            addShot(shot);
            switchPlayer();
            if (score < 4) score = 4;
        } else {
            if (score == 1 || pointsLeft <= 27) {
                if (score == 1) {
                    reds -= 1;
                    if (reds == 0) {
                        lastColor = true;
                        red.setDisable(true);
                    }
                }
                pointsLeft -= score;
                if (pointsLeft <= 27)
                    disableColor(score, true);
            }
            else
                pointsLeft = getPointsLeft();

            if (shortPot.isSelected())
                shot = new Shot("short", true);
            else if (longPot.isSelected())
                shot = new Shot("long", true);

            addBreak(score);
            addShot(shot);
        }

        addScoreTotal(score);

        // Re-spot black
        if (pointsLeft == 0 && player1.getScore() == player2.getScore())
            pointsLeft = 7;

        remaining.setText("" + pointsLeft);
    }*/

    private void switchPlayer() {

        player.setBreakScore(0);
        scoreLabel.setOpacity(0.5);
        breakLabel.setText("0");

        if (player.equals(player1)) {
            player = player2;
            scoreLabel = score2;
            breakLabel = break2;
            potSuccessLabel = potSuccess2;
            longPotSuccessLabel = longPotSuccess2;
            highestBreakLabel = highestBreak2;
            score2.setOpacity(1.0);
        } else {
            player = player1;
            scoreLabel = score1;
            breakLabel = break1;
            potSuccessLabel = potSuccess1;
            longPotSuccessLabel = longPotSuccess1;
            highestBreakLabel = highestBreak1;
            score1.setOpacity(1.0);
        }
    }

    private void disableColors(boolean bool) {
        if (bool) {
            red.setDisable(false);
            yellow.setDisable(true);
            green.setDisable(true);
            brown.setDisable(true);
            blue.setDisable(true);
            pink.setDisable(true);
            black.setDisable(true);
        } else {
            red.setDisable(true);
            yellow.setDisable(false);
            green.setDisable(false);
            brown.setDisable(false);
            blue.setDisable(false);
            pink.setDisable(false);
            black.setDisable(false);
        }
    }

    private void disableColor(int score, boolean bool) {
        switch (score) {
            case 1: red.setDisable(bool); break;
            case 2: yellow.setDisable(bool); break;
            case 3: green.setDisable(bool); break;
            case 4: brown.setDisable(bool); break;
            case 5: blue.setDisable(bool); break;
            case 6: pink.setDisable(bool); break;
            case 7: black.setDisable(bool); break;
        }
    }

    private int getPointsLeft() {

        int total = reds.size() * 8;
        for (Ball ball : colors) {
            total += ball.getValue();
        }

        return total;
    }

    private void addScore(int score) {
        player.addScore(score);
//        scoreLabel.setText("" + player.getScore());
    }

    private void addBreak(int score) {
        player.addBreakScore(score);
//        breakLabel.setText("" + player.getBreakScore());
    }

    private void addShot(Shot shot) {
        shots.add(shot);
        player.addShot(shot);
        /*updateStats();
        updateTable();*/

//        save();
    }

    private void updateStats() {
        DecimalFormat df = new DecimalFormat("##0.0");
        potSuccessLabel.setText("" + df.format(player.getPotSuccess()) + "%");
        longPotSuccessLabel.setText("" + df.format(player.getLongPotSuccess()) + "%");
        highestBreakLabel.setText("" + player.getHighestBreak());
    }

    private void updateTable() {
        if (reds.size() == 0)
            red.setDisable(true);

        switch (colors.size()) {
            case 0: black.setDisable(true); break;
            case 1: pink.setDisable(true); break;
            case 2: blue.setDisable(true); break;
            case 3: brown.setDisable(true); break;
            case 4: green.setDisable(true); break;
            case 5: yellow.setDisable(true); break;
        }

        if (shots.size() == 0)
            undo.setDisable(true);
        else
            undo.setDisable(false);

        red.setText("" + reds.size());
        remaining.setText("" + pointsLeft);
        shortPot.setSelected(true);
        difference.setText("" + Math.abs(player1.getScore() - player2.getScore()));
        scoreLabel.setText("" + player.getScore());
        breakLabel.setText("" + player.getBreakScore());
    }

    private void popBall(int score) {
        if (score == 1)
            reds.remove(reds.size() - 1);
        else
            colors.remove(colors.size() - 1);
    }

    private void popShot() {
        Shot shot = shots.remove(shots.size() - 1);
        Ball ball = shot.getBall();
        if (ball.getValue() == 1) {
            reds.add(ball);
            pointsLeft += 1;
        } else if (ball.getValue() > 1) {
            if (colors.size() == 0) {
                colors.add(ball);
                pointsLeft += ball.getValue();
            } else {
                pointsLeft += 7;
            }
        }

        Player tempPlayer = shot.getOwner();
        tempPlayer.removeShot();
        tempPlayer.addBreakScore(-ball.getValue());
        updateStats();
        updateTable();
    }

    private void save() {
        try {
            FileOutputStream fileOut1 = new FileOutputStream("quan.ser");
            FileOutputStream fileOut2 = new FileOutputStream("faris.ser");
            ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
            ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
            out1.writeObject(player1);
            out2.writeObject(player2);
            out1.close();
            out2.close();
            fileOut1.close();
            fileOut2.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private void load() {
        try {
            DecimalFormat df = new DecimalFormat("##0.0");
            FileInputStream fileOut1 = new FileInputStream("quan.ser");
            FileInputStream fileOut2 = new FileInputStream("faris.ser");
            ObjectInputStream in1 = new ObjectInputStream(fileOut1);
            ObjectInputStream in2 = new ObjectInputStream(fileOut2);
            player1 =  (Player) in1.readObject();
            player2 =  (Player) in2.readObject();
            in1.close();
            in2.close();
            fileOut1.close();
            fileOut2.close();
            potSuccess1.setText("" + df.format(player1.getPotSuccess()) + "%");
            longPotSuccess1.setText("" + df.format(player1.getLongPotSuccess()) + "%");
            highestBreak1.setText("" + player1.getHighestBreak());
            potSuccess2.setText("" + df.format(player2.getPotSuccess()) + "%");
            longPotSuccess2.setText("" + df.format(player2.getLongPotSuccess()) + "%");
            highestBreak2.setText("" + player2.getHighestBreak());
        } catch (IOException i) {
            player1 = new Player("Quan");
            player2 = new Player("Faris");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        player1.setBreakScore(0);
        player1.setScore(0);
        player2.setBreakScore(0);
        player2.setScore(0);
    }
}
