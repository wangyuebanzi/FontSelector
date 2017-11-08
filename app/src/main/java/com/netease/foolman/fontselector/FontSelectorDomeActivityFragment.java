package com.netease.foolman.fontselector;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class FontSelectorDomeActivityFragment extends Fragment {

    private static final String TAG = "FontSelectorDome";

    private FontSelector fontSelector;

    public FontSelectorDomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font_selector_dome, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        fontSelector = view.findViewById(R.id.fontSelector);
        fontSelector.setOnPositionChangedListener(new FontSelector.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                Log.i(TAG,"position = "+position);
            }
        });
    }
}
