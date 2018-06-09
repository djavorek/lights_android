package doublevv.lights.services.udp;

public class DeviceService {
    private static final DeviceService instance = new DeviceService();
    private static int retriesBeforeUnavailable = 2;

    private int failedResponses = 0;

    public enum Status {
        UNAVAILABLE, OFF, COLOR, FADE;
    }

    public static final String BROADCAST_IP = "255.255.255.255";

    private String udpAddress;
    private Status status;
    private int fadeSpeed;
    private String color;

    {
        udpAddress = BROADCAST_IP;
        status = Status.UNAVAILABLE;
    }

    private DeviceService(){}

    public static DeviceService getInstance() {
        return instance;
    }

    public void refreshDeviceStatus(final LedInfoView infoGui) {
        UdpClientHandler.UdpOperator operator = new UdpClientHandler.UdpOperator() {

            @Override
            public void noResponse() {
                failedResponses++;
                DeviceService.this.setUdpAddress(BROADCAST_IP);
            }

            @Override
            public void update(ResponseMessage message) {
                failedResponses = 0;
                setUdpAddress(message.getIp());
                parseStatus(message.getStatus());
            }

            @Override
            public void done() {
                if(failedResponses >= retriesBeforeUnavailable)
                {
                    status = Status.UNAVAILABLE;
                }

                infoGui.refreshLedFeedbackInfo();
            }
        };

        new UdpClientThread(this.getUdpAddress(), "Status", new UdpClientHandler(operator)).start();
    }

    public void sendCommand(String message, final LedInfoView infoGui) {
        if(message != null) {
            UdpClientHandler.UdpOperator operator = new UdpClientHandler.UdpOperator() {
                @Override
                public void noResponse() {
                    DeviceService.this.setUdpAddress(BROADCAST_IP);
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

            if(UdpClientThread.isReady)
            {
                new UdpClientThread(this.getUdpAddress(), message, new UdpClientHandler(operator)).start();
            }
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

    public interface LedInfoView {
        public void refreshLedFeedbackInfo();
    }
}
