package com.intuisis.binarygame.view.items;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intuisis.binarygame.R;
import com.intuisis.binarygame.entities.LineEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallant on 02/03/15.
 */
public class LineItem extends LinearLayout implements View.OnClickListener {

    private static final String TAG = LineItem.class.getSimpleName();

    public static final int RESULT_CLICK = 8;

    LinearLayout llLineItem;
    TextView tvLineResult;
    LineEntity lineEntity;
    Button[] btnLines = new Button[8];
    int[] lines;

    int position;

    OnLineItemClickListener listener;

    public LineItem(Context context) {
        super(context);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.item_line, this);

        llLineItem = (LinearLayout) findViewById(R.id.ll_line_item);
        tvLineResult = (TextView) findViewById(R.id.tv_line_result);
        btnLines[0] = (Button) findViewById(R.id.btn_line_0);
        btnLines[1] = (Button) findViewById(R.id.btn_line_1);
        btnLines[2] = (Button) findViewById(R.id.btn_line_2);
        btnLines[3] = (Button) findViewById(R.id.btn_line_3);
        btnLines[4] = (Button) findViewById(R.id.btn_line_4);
        btnLines[5] = (Button) findViewById(R.id.btn_line_5);
        btnLines[6] = (Button) findViewById(R.id.btn_line_6);
        btnLines[7] = (Button) findViewById(R.id.btn_line_7);

        tvLineResult.setOnClickListener(this);
        for(Button btnLine : btnLines) {
            btnLine.setOnClickListener(this);
        }
    }

    public void bind(LineEntity entity, int lineHeight, int position) {
        LayoutParams params = (LayoutParams) llLineItem.getLayoutParams();
        params.height = lineHeight;

        this.lineEntity = entity;
        this.position = position;
        this.lines = entity.getLines();

        for(int i=0; i<entity.getLines().length; i++) {
            setBtnBackground(btnLines[i], lines[i]);
        }

        switch (entity.getType()) {
            case LineEntity.GAME_MODE_ONE : {
                for (Button btnLine : btnLines) {
                    btnLine.setEnabled(true);
                    tvLineResult.setEnabled(false);
                    tvLineResult.setText("" + entity.getResult());
                }
                break;
            }
            case LineEntity.GAME_MODE_TWO : {
                for (Button btnLine : btnLines) {
                    btnLine.setEnabled(false);
                    tvLineResult.setEnabled(true);
                    if (entity.getResult() == 0)
                        tvLineResult.setText("");
                    else
                        tvLineResult.setText("" + entity.getResult());
                }
                break;
            }
            default: break;
        }
    }

    private void setBtnBackground(Button button, int value) {
        if (value == 0) {
            button.setText("" + value);
            button.setBackgroundResource(R.color.primary);
        } else if (value == 1) {
            button.setText("" + value);
            button.setBackgroundResource(R.color.primary_dark);
        }
    }

    @Override
    public void onClick(View v) {
        int id =  v.getId();
        int itemPosition = 8;
        if (id == tvLineResult.getId()) {
            itemPosition = RESULT_CLICK;
        } else {
            int index = 0;
            for(Button btnLine : btnLines) {
                if (id == btnLine.getId()) {
                    if (lineEntity.getLines()[index] == 0) {
                        lineEntity.getLines()[index] = 1;
                        setBtnBackground(btnLine, 1);
                    } else if (lineEntity.getLines()[index] == 1) {
                        lineEntity.getLines()[index] = 0;
                        setBtnBackground(btnLine, 0);
                    }
                    itemPosition = index;
                }
                index++;
            }
        }
        listener.onLineItemClick(v, position, itemPosition, lineEntity);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            listener = (OnLineItemClickListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().getClass().toString()
                    + " must implement OnLineItemClickListener");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
    }

    public TextView getTvResult() {
        return this.tvLineResult;
    }

    public interface OnLineItemClickListener {
        public void onLineItemClick(View v, int parentPosition, int itemPosition, LineEntity lineEntity);
    }
}
