package com.bigscreen.binarygame.common.component;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bigscreen.binarygame.R;


public class Keyboard extends Fragment implements View.OnClickListener {

    private static final String TAG = Keyboard.class.getSimpleName();

    private Button[] buttons = new Button[12];

    private OnKeyboardItemClickListener listener;

    public static final int KEY_DELETE = 10;
    public static final int KEY_OK = 11;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard, container, false);

        inflateView(view);

        return view;
    }
    
    private void inflateView(View view) {
        buttons[0] = (Button) view.findViewById(R.id.button_keyboard_0);
        buttons[1] = (Button) view.findViewById(R.id.button_keyboard_1);
        buttons[2] = (Button) view.findViewById(R.id.button_keyboard_2);
        buttons[3] = (Button) view.findViewById(R.id.button_keyboard_3);
        buttons[4] = (Button) view.findViewById(R.id.button_keyboard_4);
        buttons[5] = (Button) view.findViewById(R.id.button_keyboard_5);
        buttons[6] = (Button) view.findViewById(R.id.button_keyboard_6);
        buttons[7] = (Button) view.findViewById(R.id.button_keyboard_7);
        buttons[8] = (Button) view.findViewById(R.id.button_keyboard_8);
        buttons[9] = (Button) view.findViewById(R.id.button_keyboard_9);
        buttons[10] = (Button) view.findViewById(R.id.button_keyboard_del);
        buttons[11] = (Button) view.findViewById(R.id.button_keyboard_ok);

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id =  v.getId();
        int index = 0;
        for(Button button : buttons) {
            if (id == button.getId()) {
                listener.onKeyboardItemClick(v, index);
            }
            index++;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnKeyboardItemClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                    + " must implement OnKeyboardItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnKeyboardItemClickListener {
        void onKeyboardItemClick(View v, int key);
    }
}
