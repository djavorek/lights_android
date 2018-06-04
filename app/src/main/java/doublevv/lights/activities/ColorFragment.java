package doublevv.lights.activities;

import android.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnColorChangeListener} interface
 * to handle interaction events.
 * Use the {@link ColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorFragment extends Fragment {
    private static final String colorToInitializeWith = "255:255:255";

    private String actualColor;
    private OnColorChangeListener listener;

    @BindView(R.id.colorView)
    View colorView;

    public ColorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param initColor Initializing color.
     * @return A new instance of fragment ColorFragment.
     */
    public static ColorFragment newInstance(String initColor) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putString(colorToInitializeWith, initColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actualColor = getArguments().getString(colorToInitializeWith);
        }
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
