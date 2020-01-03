package doublevv.lights.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import doublevv.lights.services.led.LedRepository;
import doublevv.lights.services.led.LedRepositoryImpl;
import doublevv.lights.services.led.LedDeviceState;

public class LedDeviceModel extends ViewModel {
    private MutableLiveData<LedDeviceState> state = new MutableLiveData<>();
    private LedRepository ledRepository = LedRepositoryImpl.getInstance();

    public LiveData<LedDeviceState> getState() {
        return state;
    }

    public void refreshState() {
        ledRepository.getState(state);
    }

    public void sendCommandAndRefreshState(String command) {
        ledRepository.sendCommandAndGetState(command, state);
    }
}
