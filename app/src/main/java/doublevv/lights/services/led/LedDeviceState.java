package doublevv.lights.services.led;

public class LedDeviceState {
    public enum Status {
        UNAVAILABLE, OFF, COLOR, FADE;
    }

    private Status status;
    private int fadeSpeed;
    private String color;


    public LedDeviceState() {}

    public void updateState(String statusString) {
        if(statusString.contains("Fade")) {
            setStatus(Status.FADE);
            setFadeSpeed(Integer.parseInt(statusString.substring(statusString.indexOf(":") + 1)));
        }
        else if(statusString.matches("(.*:.*:.*:.*)")) {
            setStatus(Status.COLOR);
            setColor(statusString);
        }
        else if(statusString.equals("Off")) {
            setStatus(Status.OFF);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getFadeSpeed() {
        return fadeSpeed;
    }

    public void setFadeSpeed(int fadeSpeed) {
        this.fadeSpeed = fadeSpeed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
