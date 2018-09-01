package doublevv.lights.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import doublevv.lights.R;
import doublevv.lights.fragments.ColorFragment;
import doublevv.lights.fragments.FadeFragment;
import doublevv.lights.fragments.FunctionFragment;
import doublevv.lights.fragments.IdleFragment;
import doublevv.lights.fragments.SleepFragment;
import doublevv.lights.fragments.StatusFragment;
import doublevv.lights.fragments.UnavailableFragment;
import doublevv.lights.services.led.LedDeviceState;
import doublevv.lights.viewmodels.LedDeviceModel;


public class MainActivity extends AppCompatActivity implements StatusFragment.StatusChangeListener {
    LedDeviceModel ledModel;
    FunctionFragment function;
    StatusFragment statusFragment;

    @BindView(R.id.offButton)
    Button offButton;

    @BindView(R.id.onButton)
    Button onButton;

    @BindView(R.id.colorButton)
    Button colorButton;

    @BindView(R.id.fadeButton)
    Button fadeButton;

    @BindView(R.id.sleepButton)
    Button sleepButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        statusFragment = (StatusFragment)getSupportFragmentManager().findFragmentById(R.id.statusFragment);
        replaceFunctionFragment(FunctionFragment.IDLE);

        ledModel = ViewModelProviders.of(this).get(LedDeviceModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({ R.id.offButton, R.id.onButton})
    public void basicFunctionSelect(View button) {
        String command = null;

        switch(button.getId()) {
            case R.id.offButton: {
                command = "0:0:0:255";
                function = FunctionFragment.IDLE;
                break;
            }
            case R.id.onButton: {
                command = "255:255:255:255";
                function = FunctionFragment.COLOR;
                break;
            }
        }

        ledModel.sendCommandAndRefreshState(command);
        replaceFunctionFragment(function);
    }

    @OnClick({R.id.colorButton, R.id.fadeButton, R.id.sleepButton})
    public void normalFunctionSelect(View button) {
        switch(button.getId()) {
            case R.id.colorButton: {
                function = FunctionFragment.COLOR;
                break;
            }
            case R.id.fadeButton: {
                function = FunctionFragment.FADE;
                break;
            }
            case R.id.sleepButton: {
                function = FunctionFragment.SLEEP;
                break;
            }
        }

        replaceFunctionFragment(function);
    }

    @Override
    public void onUnavailable() {
        replaceFunctionFragment(FunctionFragment.UNAVAILABLE);
        enableControlButtons(false);
    }

    @Override
    public void onAvailable()
    {
        replaceFunctionFragment(FunctionFragment.IDLE);
        enableControlButtons(true);
    }

    public void replaceFunctionFragment(FunctionFragment functionFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment requestedFragment = fragmentManager.findFragmentByTag(functionFragment.getTag());

        if(requestedFragment == null)
        {
            switch (functionFragment)
            {
                case IDLE: {
                    requestedFragment = IdleFragment.newInstance();
                    break;
                }
                case UNAVAILABLE: {
                    requestedFragment = UnavailableFragment.newInstance();
                    break;
                }
                case COLOR: {
                    requestedFragment = ColorFragment.newInstance();
                    break;
                }
                case FADE: {
                    requestedFragment = FadeFragment.newInstance();
                    break;
                }
                case SLEEP: {
                    requestedFragment = SleepFragment.newInstance();
                    break;
                }
            }
        }

        if (!requestedFragment.isVisible())
        {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left)
                    .replace(R.id.functionFragment, requestedFragment, functionFragment.getTag())
                    .commit();
        }
    }

    private void enableButton(boolean enabled, Button button)
    {
        button.setEnabled(enabled);

        if(!enabled)
        {
            button.setVisibility(View.INVISIBLE);
        }
        else
        {
            button.setVisibility(View.VISIBLE);
        }
    }

    public void enableControlButtons(boolean enabled)
    {
        enableButton(enabled, offButton);
        enableButton(enabled, onButton);
        enableButton(enabled, colorButton);
        enableButton(enabled, fadeButton);
        enableButton(enabled, sleepButton);
    }
}
