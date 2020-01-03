package doublevv.lights.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.services.led.LedDeviceState;
import doublevv.lights.viewmodels.LedDeviceModel;


public class FadeFragment extends Fragment {
    LedDeviceModel ledModel;

    @BindView(R.id.speed)
    SeekBar speedBar;

    @BindView(R.id.intensity)
    SeekBar intensityBar;

    @BindView(R.id.modeSelector)
    RadioGroup modeSelector;

    @BindView(R.id.modeTransition)
    RadioButton modeTransition;

    @BindView(R.id.modeFade)
    RadioButton modeFade;

    int mode;
    int speed;
    int intensity;

    public FadeFragment() {
    }

    public static FadeFragment newInstance() {
        FadeFragment fragment = new FadeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledModel = ViewModelProviders.of(getActivity()).get(LedDeviceModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fade, container, false);
        ButterKnife.bind(this, view);
        initializeUI();

        speed = speedBar.getProgress();
        intensity = intensityBar.getProgress();

        return view;
    }

    public void initializeUI()
    {
        ledModel.getState().observe(this, new Observer<LedDeviceState>() {
            @Override
            public void onChanged(@Nullable LedDeviceState ledDeviceState) {
                if(ledDeviceState.getStatus() != LedDeviceState.Status.FADE)
                {
                    speedBar.setEnabled(false);
                    intensityBar.setEnabled(false);
                }
                else
                {
                    speedBar.setProgress(ledDeviceState.getFadeSpeed());
                    intensityBar.setProgress(ledDeviceState.getFadeAlpha());
                }
            }
        });
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startFade();
            }
        });

        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensity = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                startFade();
            }
        });

        modeSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                speedBar.setEnabled(true);
                intensityBar.setEnabled(true);

                if(checkedId == modeTransition.getId()){
                    mode = 0;
                }
                if(checkedId == modeFade.getId()){
                    mode = 1;
                }

                startFade();
            }
        });
    }

    private void startFade()
    {
        ledModel.sendCommandAndRefreshState("Fade:" + mode + ":" + speed + ":" + intensity);
    }
}
