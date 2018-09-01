package doublevv.lights.services.led;

public class LedDeviceState {
    public enum Status {
        UNAVAILABLE, OFF, COLOR, FADE, SLEEP;
    }
    public enum FadeMode {
        TRANSITION , FADE;
    }

    private Status status;

    private String color;

    private FadeMode fadeMode;
    private int fadeSpeed;
    private int fadeAlpha;


    public LedDeviceState() {}

    public void updateState(String statusString) {
        if(statusString.contains("Fade")) {
            setStatus(Status.FADE);
            String[] fadeProperties = statusString.split(":");

            switch (Integer.parseInt(fadeProperties[1])) {
                case 1:
                    setFadeMode(FadeMode.TRANSITION);
                    break;
                case 2:
                    setFadeMode(FadeMode.FADE);
                    break;
            }

            setFadeSpeed(Integer.parseInt(fadeProperties[2]));
            setFadeAlpha(Integer.parseInt(fadeProperties[3]));
        }
        else if(statusString.contains("Sleep")) {
            setStatus(Status.SLEEP);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public FadeMode getFadeMode() {
        return fadeMode;
    }

    public void setFadeMode(FadeMode fadeMode) {
        this.fadeMode = fadeMode;
    }

    public int getFadeSpeed() {
        return fadeSpeed;
    }

    public void setFadeSpeed(int fadeSpeed) {
        this.fadeSpeed = fadeSpeed;
    }

    public int getFadeAlpha() {
        return fadeAlpha;
    }

    public void setFadeAlpha(int fadeAlpha) {
        this.fadeAlpha = fadeAlpha;
    }

}
