package doublevv.lights.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.controllers.LedController;
import doublevv.lights.controllers.Status;

public class StatusFragment extends Fragment implements LedController.LedInfoView {
    private StatusChangeListener listener;

    @BindView(R.id.statusProgressbar)
    ProgressBar statusBar;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.task)
    TextView task;

    LedController ledController = LedController.getInstance();
    CountDownTimer statusTimer;

    public StatusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatusFragment.StatusChangeListener) {
            listener = (StatusFragment.StatusChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnColorChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void refreshStatus() {
        startStatusTimer();
        ledController.refreshDeviceInfo(this);
    }

    @Override
    public void refreshLedFeedbackInfo() {

        switch (ledController.getStatus()) {
            case UNAVAILABLE: {
                status.setText(getResources().getString(R.string.unavailable));
                task.setText("");
                listener.onUnavailable();
                break;
            }
            case OFF: {
                status.setText(getResources().getString(R.string.available));
                task.setText(getResources().getString(R.string.off));
                listener.onOff();
                break;
            }
            case COLOR: {
                status.setText(getResources().getString(R.string.available));
                task.setText(getResources().getString(R.string.color));
                listener.onColor();
                break;
            }
            case FADE: {
                status.setText(getResources().getString(R.string.available));
                task.setText(getResources().getString(R.string.fade));
                listener.onFade();
                break;
            }
        }

        if(ledController.getStatus() != Status.UNAVAILABLE)
        {
            statusTimer.onFinish();
        }
    }

    private void startStatusTimer()
    {
        statusTimer = new CountDownTimer(2100, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((2100 - millisUntilFinished) / 20);
                statusBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                if(ledController.getUdpAddress() == LedController.BROADCAST_IP)
                {
                    refreshStatus();
                }
                else {
                    statusBar.setVisibility(View.INVISIBLE);
                    statusTimer.cancel();
                }
            }
        };
        statusTimer.start();
    }

    public interface StatusChangeListener {
        void onUnavailable();
        void onOff();
        void onColor();
        void onFade();
    }
}
