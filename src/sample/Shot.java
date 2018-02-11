package sample;

import java.io.Serializable;

public class Shot implements Serializable {

    private Player owner;
    private String type;
    private Ball ball;
    private boolean success;

    public Shot(Player owner, String type, Ball ball, boolean success) {
        this.owner = owner;
        this.type = type;
        this.ball = ball;
        this.success = success;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
