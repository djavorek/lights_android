package doublevv.lights.udp;

import android.os.Handler;
import android.os.Message;

public class UdpClientHandler extends Handler {
    public static final int NO_RESPONSE = -1;
    public static final int UPDATE = 0;
    public static final int DONE = 1;
    private UdpOperator operator;

    public UdpClientHandler(UdpOperator operator) {
        super();
        this.operator = operator;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what){
            case NO_RESPONSE:
                operator.noResponse();
                break;
            case UPDATE:
                operator.update((ResponseMessage)msg.obj);
                break;
            case DONE:
                operator.done();
                break;
            default:
                super.handleMessage(msg);
        }
    }

    public interface UdpOperator {
        public void noResponse();
        public void update(ResponseMessage message);
        public void done();
    }
}