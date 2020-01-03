package doublevv.lights.services.led;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public interface LedRepository {
    public MutableLiveData<LedDeviceState> getState(MutableLiveData<LedDeviceState> data);
    public MutableLiveData<LedDeviceState> sendCommandAndGetState(String message, MutableLiveData<LedDeviceState> data);
}
