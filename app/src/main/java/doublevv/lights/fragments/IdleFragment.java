package doublevv.lights.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import doublevv.lights.R;


public class IdleFragment extends Fragment {


    public IdleFragment() {
        // Required empty public constructor
    }
    public static IdleFragment newInstance() {
        IdleFragment fragment = new IdleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_idle, container, false);
        return view;
    }
}
