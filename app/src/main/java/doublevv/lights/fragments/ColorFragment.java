package doublevv.lights.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madrapps.eyedropper.EyeDropper;

import butterknife.BindView;
import butterknife.ButterKnife;
import doublevv.lights.R;


public class ColorFragment extends Fragment {
    private OnColorChangeListener listener;

    @BindView(R.id.colorView)
    View colorView;

    public ColorFragment() {
        // Required empty public constructor
    }


    public static ColorFragment newInstance() {
        ColorFragment fragment = new ColorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.bind(this, view);

        new EyeDropper(colorView, new EyeDropper.ColorSelectionListener() {
            @Override
            public void onColorSelected(@ColorInt int color) {
                if(Color.red(color) + Color.green(color) + Color.blue(color) != 0) {
                    colorChanging(color);
                }
            }
        });

        return view;
    }

    public void colorChanging(@ColorInt int color) {
        if (listener != null) {
            listener.onColorChange(color);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnColorChangeListener) {
            listener = (OnColorChangeListener) context;
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

    public interface OnColorChangeListener {
        void onColorChange(@ColorInt int color);
    }
}
