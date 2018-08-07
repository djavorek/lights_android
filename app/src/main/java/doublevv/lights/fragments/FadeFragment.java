package doublevv.lights.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.viewmodels.LedDeviceModel;


public class FadeFragment extends Fragment {
    LedDeviceModel ledModel;

    @BindView(R.id.speed)
    SeekBar speedBar;

    int speed = 50;


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
        View view = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.bind(this, view);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
                ledModel.sendCommandAndRefreshState("Fade:" + speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

}
