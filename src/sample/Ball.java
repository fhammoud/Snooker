package sample;

import java.io.Serializable;

public class Ball implements Serializable {
    private int value;

    public Ball(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
