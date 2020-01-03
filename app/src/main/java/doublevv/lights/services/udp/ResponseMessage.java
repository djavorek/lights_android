package doublevv.lights.services.udp;

public class ResponseMessage {
    private String ip;
    private String status;

    public ResponseMessage(String ip, String response) {
        this.ip = ip;
        this.status = response;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResponse() {
        return status;
    }

    public void setResponse(String status) {
        this.status = status;
    }
}
