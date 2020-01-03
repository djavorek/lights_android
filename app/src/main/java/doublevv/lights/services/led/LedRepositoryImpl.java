package doublevv.lights.services.led;

import android.arch.lifecycle.MutableLiveData;

import javax.inject.Singleton;

import doublevv.lights.services.udp.ResponseMessage;
import doublevv.lights.services.udp.UdpClientHandler;
import doublevv.lights.services.udp.UdpClientThread;

@Singleton
public class LedRepositoryImpl implements LedRepository {
    private static final LedRepositoryImpl instance = new LedRepositoryImpl();

    private static final String BROADCAST_IP = "255.255.255.255";
    private String udpAddress;

    private static int retriesBeforeUnavailable = 2;
    private int failedResponses = 0;

    {
        udpAddress = BROADCAST_IP;
    }

    private LedRepositoryImpl(){}

    public static LedRepositoryImpl getInstance() {
        return instance;
    }

    public MutableLiveData<LedDeviceState> getState(MutableLiveData<LedDeviceState> data) {
        LedDeviceState state = new LedDeviceState();
        refreshStatus(data, state);

        return data;
    }

    public MutableLiveData<LedDeviceState> sendCommandAndGetState(String message, MutableLiveData<LedDeviceState> data) {
        LedDeviceState state = new LedDeviceState();

        if(message != null) {
            new UdpClientThread(this.getUdpAddress(), message, new UdpClientHandler(getOperator(data, state))).start();
        }
        else {
            getState(data);
        }

        return data;
    }

    private void refreshStatus(final MutableLiveData<LedDeviceState> data, LedDeviceState state) {
        //TODO: Wire device name here
        new UdpClientThread(getUdpAddress(), "Status:alpha", new UdpClientHandler(getOperator(data, state))).start();
    }

    private UdpClientHandler.UdpOperator getOperator(final MutableLiveData<LedDeviceState> responseData, final LedDeviceState state) {
        return new UdpClientHandler.UdpOperator() {
            @Override
            public void noResponse() {
                failedResponses++;
                setUdpAddress(BROADCAST_IP);
            }

            @Override
            public void update(ResponseMessage message) {
                failedResponses = 0;
                setUdpAddress(message.getIp());
                state.updateState(message.getResponse());
            }

            @Override
            public void done() {
                if(failedResponses >= retriesBeforeUnavailable) {
                    state.setStatus(LedDeviceState.Status.UNAVAILABLE);
                }
                responseData.postValue(state);
            }
        };
    }

    private String getUdpAddress() {
        return udpAddress;
    }

    private void setUdpAddress(String udpAddress) {
        this.udpAddress = udpAddress;
    }
}