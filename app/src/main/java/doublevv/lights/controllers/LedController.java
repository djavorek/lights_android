package doublevv.lights.controllers;

import doublevv.lights.activities.interfaces.LedInfoView;
import doublevv.lights.udp.ResponseMessage;
import doublevv.lights.udp.UdpClientHandler;
import doublevv.lights.udp.UdpClientThread;
import doublevv.lights.udp.UdpOperator;

public class LedController {
    private static final LedController instance = new LedController();

    public static final String BROADCAST_IP = "255.255.255.255";

    private String udpAddress;
    private Status status;
    private int fadeSpeed;
    private String color;

    {
        udpAddress = BROADCAST_IP;
        status = Status.UNAVAILABLE;
    }

    private LedController(){}

    public static LedController getInstance() {
        return instance;
    }

    public void refreshDeviceInfo(final LedInfoView infoGui) {
        UdpOperator operator = new UdpOperator() {
            @Override
            public void noResponse() {
                LedController.this.setUdpAddress(BROADCAST_IP);
                status = Status.UNAVAILABLE;
            }

            @Override
            public void update(ResponseMessage message) {
                setUdpAddress(message.getIp());
                parseStatus(message.getStatus());
            }

            @Override
            public void done() {
                infoGui.refreshLedFeedbackInfo();
            }
        };

        new UdpClientThread(this.getUdpAddress(), "Status", new UdpClientHandler(operator)).start();
    }

    public void sendCommand(String message, final LedInfoView infoGui) {
        if(message != null) {
            UdpOperator operator = new UdpOperator() {
                @Override
                public void noResponse() {
                    LedController.this.setUdpAddress(BROADCAST_IP);
                    status = Status.UNAVAILABLE;
                }

                @Override
                public void update(ResponseMessage message) {
                    setUdpAddress(message.getIp());
                    parseStatus(message.getStatus());
                }

                @Override
                public void done() {
                    infoGui.refreshLedFeedbackInfo();
                }
            };

            new UdpClientThread(this.getUdpAddress(), message, new UdpClientHandler(operator)).start();
        }
    }

    private void parseStatus(String status) {
        if(status.contains("Fade")) {
            setStatus(Status.FADE);
            setFadeSpeed(Integer.parseInt(status.substring(status.indexOf(":") + 1)));
        }
        else if(status.matches("(.*:.*:.*)")) {
            setStatus(Status.COLOR);
            setColor(status);
        }
        else if(status.equals("Off")) {
            setStatus(Status.OFF);
        }
    }

    public String getUdpAddress() {
        return udpAddress;
    }

    private void setUdpAddress(String udpAddress) {
        this.udpAddress = udpAddress;
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    public int getFadeSpeed() {
        return fadeSpeed;
    }

    private void setFadeSpeed(int fadeSpeed) {
        this.fadeSpeed = fadeSpeed;
    }

    public String getColor() {
        return color;
    }

    private void setColor(String color) {
        this.color = color;
    }
}
