package doublevv.lights.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.madrapps.eyedropper.EyeDropper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;
import doublevv.lights.misc.ColorSlot;
import doublevv.lights.services.led.LedDeviceState;
import doublevv.lights.viewmodels.LedDeviceModel;


    public class ColorFragment extends Fragment {

        LedDeviceModel ledModel;
        SharedPreferences colorPref;
        SharedPreferences.Editor prefEditor;

        @BindView(R.id.colorView) View colorView;
        @BindView(R.id.intensity) SeekBar intensityBar;

        @BindView(R.id.colorSlot1) View colorSlot1;
        @BindView(R.id.colorSlot2) View colorSlot2;
        @BindView(R.id.colorSlot3) View colorSlot3;
        @BindView(R.id.colorSlot4) View colorSlot4;
        @BindView(R.id.colorSlot5) View colorSlot5;
        @BindView(R.id.colorSlot6) View colorSlot6;

        List<ColorSlot> slots = new ArrayList<>();

        @ColorInt int currentColor;
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

        Context context = getActivity();
        colorPref = context.getSharedPreferences(getResources().getString(R.string.savedColor_pref), context.MODE_PRIVATE);
        prefEditor = colorPref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.bind(this, view);

        ledModel.getState().observe(this, new Observer<LedDeviceState>() {
            @Override
            public void onChanged(@Nullable LedDeviceState ledDeviceState) {
                if(ledDeviceState.getStatus() == LedDeviceState.Status.COLOR && !ledDeviceState.getColor().equals("0:0:0"))
                {
                    String[] colorComponents = ledDeviceState.getColor().split(":");
                    int red = Integer.valueOf(colorComponents[0]);
                    int green = Integer.valueOf(colorComponents[1]);
                    int blue = Integer.valueOf(colorComponents[2]);
                    alpha = Integer.valueOf(colorComponents[3]);
                    intensityBar.setProgress(alpha);
                    currentColor = Color.rgb(red, green, blue);
                }
            }
        });

        initalizeColorPicker();
        initializeIntensityBar();
        initializeColorSlots();

        return view;
    }

    private void initalizeColorPicker()
    {
        new EyeDropper(colorView, new EyeDropper.ColorSelectionListener() {
            @Override
            public void onColorSelected(@ColorInt int color) {
                if (Color.red(color) + Color.green(color) + Color.blue(color) != 0) {
                    currentColor = color;
                    changeColor(currentColor);
                }
            }
        });
    }

    private void initializeIntensityBar()
    {
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
    }

    private void initializeColorSlots()
    {
        @ColorInt int fallback = Color.WHITE;
        String slotname = "slot1";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot1));
        slotname = "slot2";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot2));
        slotname = "slot3";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot3));
        slotname = "slot4";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot4));
        slotname = "slot5";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot5));
        slotname = "slot6";
        slots.add(new ColorSlot(slotname,colorPref.getInt(slotname, fallback), colorSlot6));

        for(ColorSlot slot : slots)
        {
            setupColorSlot(slot);
        }
    }

    private void setupColorSlot(final ColorSlot slot)
    {
        slot.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(slot.getColor());
            }
        });

        slot.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                slot.setColor(currentColor);
                prefEditor.putInt(slot.getName(), currentColor);
                prefEditor.apply();
                return true;
            }
        });
    }

    public void changeColor(@ColorInt int color)
    {
        String colorString = String.valueOf(Color.red(color)) + ":" + String.valueOf(Color.green(color)) + ":" + String.valueOf(Color.blue(color) + ":" + String.valueOf(alpha));
        ledModel.sendCommandAndRefreshState(colorString);
    }
}