package doublevv.lights.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import doublevv.lights.services.led.LedDeviceState;
import doublevv.lights.viewmodels.LedDeviceModel;

public class StatusFragment extends Fragment {
    private LedDeviceModel ledModel;
    private StatusChangeListener statusChangeListener;
    private CountDownTimer initializationTimer;
    private Handler pollingHandler = new Handler();
    private LedDeviceState.Status persistedStatus;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledModel = ViewModelProviders.of(getActivity()).get(LedDeviceModel.class);
        ledModel.getState().observe(this, new Observer<LedDeviceState>() {
            @Override
            public void onChanged(@Nullable LedDeviceState ledDeviceState) {
                updateUI(ledDeviceState);
            }
        });
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
        ledModel.refreshState();
        startInitializationTimer();
    }

    private Runnable refreshStatus = new Runnable() {
        @Override
        public void run() {
            ledModel.refreshState();
            pollingHandler.postDelayed(this, 5000);
        }
    };


    public void updateUI(LedDeviceState state) {
        LedDeviceState.Status currentStatus = state.getStatus();

        if(currentStatus != null && (currentStatus != LedDeviceState.Status.UNAVAILABLE || !currentStatus.equals(persistedStatus))) {

            switch (currentStatus) {
                case UNAVAILABLE: {
                    task.setText("");
                    statusChangeListener.onUnavailable();
                    status.setText(getResources().getString(R.string.unavailable));
                    break;
                }
                case OFF: {
                    task.setText(getResources().getString(R.string.off));
                    break;
                }
                case COLOR: {
                    task.setText(getResources().getString(R.string.color));
                    setTaskColor(state.getColor());
                    break;
                }
                case FADE: {
                    task.setText(getResources().getString(R.string.fade));
                    break;
                }
            }

            if(currentStatus != LedDeviceState.Status.COLOR)
            {
                hideTaskColor();
            }

            if (persistedStatus == LedDeviceState.Status.UNAVAILABLE && currentStatus != LedDeviceState.Status.UNAVAILABLE)
            {
                status.setText(getResources().getString(R.string.available));
                initializationBar.setVisibility(View.INVISIBLE);
                statusChangeListener.onAvailable();
            }

            persistedStatus = currentStatus;
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

    private void hideTaskColor() {
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
                initializationTimer.cancel();

                if(ledModel.getState().getValue().getStatus() == null || ledModel.getState().getValue().getStatus() == LedDeviceState.Status.UNAVAILABLE) {
                    this.start();
                    initializeStatus();
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
        void onAvailable();
    }
}