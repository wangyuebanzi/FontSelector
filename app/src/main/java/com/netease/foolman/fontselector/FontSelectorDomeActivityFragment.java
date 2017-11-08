package com.netease.foolman.fontselector;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FontSelectorDomeActivityFragment extends Fragment {

    private static final String TAG = "FontSelectorDome";

    private FontSelector fontSelector;
    private TextView textView;
    private int[] textSize;

    public FontSelectorDomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font_selector_dome, container, false);
        textSize = getResources().getIntArray(R.array.text_size);
        initView(view);
        return view;
    }

    private void initView(View view) {
        fontSelector = view.findViewById(R.id.fontSelector);
        textView = view.findViewById(R.id.text);

        fontSelector.setOnPositionChangedListener(new FontSelector.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                Log.i(TAG,"position = "+position);
                textView.setTextSize(textSize[position]);
            }
        });
    }
}
