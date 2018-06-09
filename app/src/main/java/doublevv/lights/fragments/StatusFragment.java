package doublevv.lights.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.services.udp.DeviceService;
import doublevv.lights.services.udp.DeviceService.Status;

public class StatusFragment extends Fragment implements DeviceService.LedInfoView {
    private DeviceService deviceService = DeviceService.getInstance();
    private StatusChangeListener statusChangeListener;
    private CountDownTimer initializationTimer;
    private Handler pollingHandler = new Handler();

    @BindView(R.id.statusProgressbar)
    ProgressBar initializationBar;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.task)
    TextView task;

    @BindView(R.id.task_color)
    View taskColor;

    public StatusFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatusFragment.StatusChangeListener) {
            statusChangeListener = (StatusFragment.StatusChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnColorChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        pollingHandler.removeCallbacks(refreshStatus);
        initializationTimer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        statusChangeListener = null;
    }

    public void initializeStatus() {
        startInitializationTimer();
        deviceService.refreshDeviceStatus(this);
    }

    private Runnable refreshStatus = new Runnable() {
        @Override
        public void run() {
            deviceService.refreshDeviceStatus(StatusFragment.this);
            pollingHandler.postDelayed(this, 5000);
        }
    };

    //When refreshDeviceStatus finished
    @Override
    public void refreshLedFeedbackInfo() {
        Status currentStatus = deviceService.getStatus();

        switch (currentStatus) {
            case UNAVAILABLE: {
                task.setText("");
                statusChangeListener.onUnavailable();
                status.setText(getResources().getString(R.string.unavailable));
                break;
            }
            case OFF: {
                task.setText(getResources().getString(R.string.off));
                statusChangeListener.onOff();
                break;
            }
            case COLOR: {
                task.setText(getResources().getString(R.string.color));
                statusChangeListener.onColor();
                setTaskColor(deviceService.getColor());
                break;
            }
            case FADE: {
                task.setText(getResources().getString(R.string.fade));
                statusChangeListener.onFade();
                break;
            }
        }

        if(currentStatus != Status.UNAVAILABLE)
        {
            status.setText(getResources().getString(R.string.available));
            initializationBar.setVisibility(View.INVISIBLE);
        }

        if(currentStatus != Status.COLOR)
        {
            resetTaskColor();
        }
    }

    private void setTaskColor(String color) {
        String[] colorComponents = color.split(":");
        int red = Integer.valueOf(colorComponents[0]);
        int green = Integer.valueOf(colorComponents[1]);
        int blue = Integer.valueOf(colorComponents[2]);

        int background = Color.rgb(red, green, blue);
        taskColor.setBackgroundColor(background);
        taskColor.setVisibility(View.VISIBLE);

    }

    private void resetTaskColor() {
        taskColor.setVisibility(View.GONE);
    }

    private void startInitializationTimer()
    {
        initializationTimer = new CountDownTimer(2100, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((2100 - millisUntilFinished) / 20);
                initializationBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                if(deviceService.getStatus() == Status.UNAVAILABLE)
                {
                    this.start();
                    deviceService.refreshDeviceStatus(StatusFragment.this);
                }
                else {
                    pollingHandler.postDelayed(refreshStatus, 4000);
                }
            }
        };
        initializationTimer.start();
    }

    public interface StatusChangeListener {
        void onUnavailable();
        void onOff();
        void onColor();
        void onFade();
    }
}
