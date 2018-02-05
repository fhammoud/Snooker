package sample;

import java.io.Serializable;

public class Shot implements Serializable {
    private String type;
    private boolean success;

    public Shot(String type, boolean success) {
        this.type = type;
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
