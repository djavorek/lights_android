package doublevv.lights.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.madrapps.eyedropper.EyeDropper;

import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.services.led.LedDeviceState;
import doublevv.lights.viewmodels.LedDeviceModel;


public class ColorFragment extends Fragment {
    private static final String COLOR_PARAM = "color";

    LedDeviceModel ledModel;

    @BindView(R.id.colorView)
    View colorView;

    @BindView(R.id.intensity)
    SeekBar intensityBar;

    @ColorInt
    int currentColor;

    int alpha = 255;

    public ColorFragment() {
    }

    public static ColorFragment newInstance() {
        ColorFragment fragment = new ColorFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledModel = ViewModelProviders.of(getActivity()).get(LedDeviceModel.class);
        ledModel.getState().observe(this, new Observer<LedDeviceState>() {
            @Override
            public void onChanged(@Nullable LedDeviceState ledDeviceState) {
                if(ledDeviceState.getStatus() == LedDeviceState.Status.COLOR && !ledDeviceState.getColor().equals("0:0:0"))
                {
                    String[] colorComponents = ledDeviceState.getColor().split(":");
                    int red = Integer.valueOf(colorComponents[0]);
                    int green = Integer.valueOf(colorComponents[1]);
                    int blue = Integer.valueOf(colorComponents[2]);
                    intensityBar.setProgress(alpha);
                    currentColor = Color.argb(alpha, red, green, blue);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.bind(this, view);

        new EyeDropper(colorView, new EyeDropper.ColorSelectionListener() {
            @Override
            public void onColorSelected(@ColorInt int color) {
                if (Color.red(color) + Color.green(color) + Color.blue(color) != 0) {
                    intensityBar.setProgress(intensityBar.getMax());
                    currentColor = color;
                    changeColor(currentColor);
                }
            }
        });

        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = progress;
                changeColor(currentColor);
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

    public void changeColor(@ColorInt int color) {
        String colorString = String.valueOf(Color.red(color)) + ":" + String.valueOf(Color.green(color)) + ":" + String.valueOf(Color.blue(color) + ":" + String.valueOf(alpha));
        ledModel.sendCommandAndRefreshState(colorString);
    }
}