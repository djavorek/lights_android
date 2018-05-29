package doublevv.lights.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import doublevv.lights.R;
import doublevv.lights.controllers.LedController;

public class MainActivity extends AppCompatActivity {

    LedController ledController = LedController.getInstance();

    StatusFragment statusFragment;

    @BindView(R.id.offButton)
    Button offButton;

    @BindView(R.id.fullBrightnessButton)
    Button fullBrightnessButton;

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

    @OnClick({ R.id.offButton, R.id.fullBrightnessButton})
    public void basicFunctionSelect(View button) {
        String command = null;

        if(button.getId() == R.id.offButton) {
            command = "0:0:0";
        }
        else if (button.getId() == R.id.fullBrightnessButton) {
            command = "255:255:255";
        }

        ledController.sendCommand(command, statusFragment);

    }
}
