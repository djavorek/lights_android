package doublevv.lights.services.udp;

public class ResponseMessage {
    private String ip;
    private String status;

    public ResponseMessage(String ip, String status) {
        this.ip = ip;
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
