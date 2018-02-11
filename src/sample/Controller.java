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
        int redsCount = 15;

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
        reds = new ArrayList<>(redsCount);
        for (int i = 0; i < redsCount; i++)
            reds.add(new Ball(1));
        red.setText("" + redsCount);

        // Set initial remaining points
        pointsLeft = getPointsLeft();
        remaining.setText("" + pointsLeft);
    }

    @Override
    public void handle(ActionEvent event) {

        int score = 0;
        String type = "";
        boolean success = true;

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
        } else if (event.getSource().equals(safety)) {
            type = "safety";
        } else if (event.getSource().equals(switchP)) {
            switchPlayer();
            return;
        } else if (event.getSource().equals(undo)) {
            popShot();
            return;
        }

        // Check radio buttons
        if (shortPot.isSelected() && !type.equals("safety")) {
            type = "short";
        } else if (longPot.isSelected()) {
            type = "long";
        } else if (foul.isSelected()) {
            type = "foul";
            success = false;
            if (score < 4)
                score = 4;
        } else if (free.isSelected()) {
            type = "free";
        }

        // Update scores
        if (score == 1) {
            if (type.equals("free")) {
                reds.add(new Ball(1));
                pointsLeft = getPointsLeft();
            }
            popBall(score);
            pointsLeft -= score;
        } else {
            if (lastColor && !type.equals("free"))
                popBall(score);

            if (reds.size() == 0)
                lastColor = true;

            pointsLeft = getPointsLeft();
        }
        addShot(new Shot(player, type, new Ball(score), success));

        if (score == 0)
            switchPlayer();
        else if (type.equals("foul")) {
            switchPlayer();
            player.addScore(score);
        }

        updateTable();
        updateStats();
        remaining.setText("" + pointsLeft);
    }

    private void switchPlayer() {
        scoreLabel.setOpacity(0.5);
        breakLabel.setOpacity(0.5);
        breakLabel.setText("0");

        if (player.equals(player1)) {
            player = player2;
            scoreLabel = score2;
            breakLabel = break2;
            potSuccessLabel = potSuccess2;
            longPotSuccessLabel = longPotSuccess2;
            highestBreakLabel = highestBreak2;
            score2.setOpacity(1.0);
            break2.setOpacity(1.0);
        } else {
            player = player1;
            scoreLabel = score1;
            breakLabel = break1;
            potSuccessLabel = potSuccess1;
            longPotSuccessLabel = longPotSuccess1;
            highestBreakLabel = highestBreak1;
            score1.setOpacity(1.0);
            break1.setOpacity(1.0);
        }
    }

    private int getPointsLeft() {
        int total = reds.size() * 8;
        for (Ball ball : colors) {
            total += ball.getValue();
        }
        return total;
    }

    private void addShot(Shot shot) {
        shots.add(shot);
        player.addShot(shot);
        save();
    }

    private void updateStats() {
        DecimalFormat df = new DecimalFormat("##0.0");
        potSuccessLabel.setText("" + df.format(player.getPotSuccess()) + "%");
        longPotSuccessLabel.setText("" + df.format(player.getLongPotSuccess()) + "%");
        highestBreakLabel.setText("" + player.getHighestBreak());
    }

    private void updateTable() {
        if (shots.size() == 0)
            undo.setDisable(true);
        else
            undo.setDisable(false);

        disableBalls(true);
        miss.setDisable(true);
        if (potType.getSelectedToggle() != null)
            potType.getSelectedToggle().setSelected(false);

        red.setText("" + reds.size());
        remaining.setText("" + pointsLeft);
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
        Player shotOwner = shot.getOwner();

        boolean ownerIsPlayer = shotOwner.equals(player);

        Ball ball = shot.getBall();
        String type = shot.getType();
        int value = ball.getValue();

        if (value == 1) {
            if (!type.equals("free"))
                reds.add(ball);
            pointsLeft = getPointsLeft();
        } else if (value > 1) {
            if (colors.size() < 6) {
                if (!type.equals("free"))
                    colors.add(ball);
                pointsLeft += value;
            } else if (!type.equals("foul")) {
                pointsLeft += 7;
            }
        }

        if (type.equals("foul")) {
            if (shotOwner.equals(player))
                switchPlayer();
            player.addScore(-value);
            scoreLabel.setText("" + player.getScore());
        }

        if (!shotOwner.equals(player)) {
            switchPlayer();
        }

        shotOwner.removeShot();
        updateTable();
        updateStats();
        save();
    }

    private void disableBalls(boolean bool) {
        red.setDisable(bool);
        yellow.setDisable(bool);
        green.setDisable(bool);
        brown.setDisable(bool);
        blue.setDisable(bool);
        pink.setDisable(bool);
        black.setDisable(bool);
    }

    public void disableBalls(ActionEvent actionEvent) {
        miss.setDisable(false);
        if (reds.size() == 0)
            red.setDisable(true);
        else
            red.setDisable(false);

        switch (colors.size()) {
            case 6: yellow.setDisable(false);
            case 5: green.setDisable(false);
            case 4: brown.setDisable(false);
            case 3: blue.setDisable(false);
            case 2: pink.setDisable(false);
            case 1: black.setDisable(false);
        }
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

        player1.setScore(0);
        player2.setScore(0);
    }
}
