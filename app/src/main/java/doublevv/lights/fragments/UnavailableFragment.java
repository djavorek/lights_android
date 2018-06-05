package doublevv.lights.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import doublevv.lights.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnavailableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnavailableFragment extends Fragment {
    public UnavailableFragment() {
    }


    public static UnavailableFragment newInstance() {
        UnavailableFragment fragment = new UnavailableFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_unavailable, container, false);
        return view;
    }

}
