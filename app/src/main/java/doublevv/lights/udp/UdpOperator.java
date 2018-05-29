package doublevv.lights.udp;

import doublevv.lights.activities.interfaces.LedInfoView;

public interface UdpOperator {
    public void noResponse();
    public void update(ResponseMessage message);
    public void done();
}
