package doublevv.lights.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import doublevv.lights.R;
import doublevv.lights.controllers.LedController;


public class MainActivity extends AppCompatActivity implements ColorFragment.OnColorChangeListener {

    LedController ledController = LedController.getInstance();

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

        statusFragment = (StatusFragment)getFragmentManager().findFragmentById(R.id.statusFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        statusFragment.refreshStatus();
    }

    @OnClick({ R.id.offButton, R.id.onButton})
    public void basicFunctionSelect(View button) {
        String command = null;

        switch(button.getId()) {
            case R.id.offButton: {
                command = "0:0:0";
                break;
            }
            case R.id.onButton: {
                command = "255:255:255";
                break;
            }
        }

        ledController.sendCommand(command, statusFragment);
    }

    @OnClick({R.id.colorButton, R.id.fadeButton, R.id.sleepButton})
    public void normalFunctionSelect(View button) {
        Fragment FunctionFragment = null;

        switch(button.getId()) {
            case R.id.colorButton: {
                FunctionFragment = ColorFragment.newInstance(ledController.getColor());
                break;
            }
            case R.id.fadeButton: {
                break;
            }
            case R.id.sleepButton: {
                break;
            }
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.functionFragment, FunctionFragment);
    }


    @Override
    public void onColorChange(@ColorInt int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        String colorString = String.valueOf(red) + ":" + String.valueOf(green) + ":" + String.valueOf(blue);

        ledController.sendCommand(colorString, statusFragment);
    }
}
