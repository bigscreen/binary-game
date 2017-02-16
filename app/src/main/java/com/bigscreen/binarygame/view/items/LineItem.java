package com.bigscreen.binarygame.view.items;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigscreen.binarygame.models.Line;
import com.bigscreen.binarygame.R;


public class LineItem extends LinearLayout implements View.OnClickListener {

    private static final String TAG = LineItem.class.getSimpleName();

    public static final int RESULT_CLICK = 8;

    LinearLayout layoutLineItem;
    TextView textLineResult;
    Line line;
    Button[] buttonLines = new Button[8];
    int[] lines;

    int position;

    OnLineItemClickListener listener;

    public LineItem(Context context) {
        super(context);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.item_line, this);

        layoutLineItem = (LinearLayout) findViewById(R.id.layout_line_item);
        textLineResult = (TextView) findViewById(R.id.text_line_result);
        buttonLines[0] = (Button) findViewById(R.id.button_line_0);
        buttonLines[1] = (Button) findViewById(R.id.button_line_1);
        buttonLines[2] = (Button) findViewById(R.id.button_line_2);
        buttonLines[3] = (Button) findViewById(R.id.button_line_3);
        buttonLines[4] = (Button) findViewById(R.id.button_line_4);
        buttonLines[5] = (Button) findViewById(R.id.button_line_5);
        buttonLines[6] = (Button) findViewById(R.id.button_line_6);
        buttonLines[7] = (Button) findViewById(R.id.button_line_7);

        textLineResult.setOnClickListener(this);
        for(Button btnLine : buttonLines) {
            btnLine.setOnClickListener(this);
        }
    }

    public void bind(Line entity, int lineHeight, int position) {
        LayoutParams params = (LayoutParams) layoutLineItem.getLayoutParams();
        params.height = lineHeight;

        this.line = entity;
        this.position = position;
        this.lines = entity.getLines();

        for(int i=0; i<entity.getLines().length; i++) {
            setBtnBackground(buttonLines[i], lines[i]);
        }

        switch (entity.getType()) {
            case Line.GAME_MODE_ONE : {
                for (Button btnLine : buttonLines) {
                    btnLine.setEnabled(true);
                    textLineResult.setEnabled(false);
                    textLineResult.setText("" + entity.getResult());
                }
                break;
            }
            case Line.GAME_MODE_TWO : {
                for (Button btnLine : buttonLines) {
                    btnLine.setEnabled(false);
                    textLineResult.setEnabled(true);
                    if (entity.getResult() == 0)
                        textLineResult.setText("");
                    else
                        textLineResult.setText("" + entity.getResult());
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
        if (id == textLineResult.getId()) {
            itemPosition = RESULT_CLICK;
        } else {
            int index = 0;
            for(Button btnLine : buttonLines) {
                if (id == btnLine.getId()) {
                    if (line.getLines()[index] == 0) {
                        line.getLines()[index] = 1;
                        setBtnBackground(btnLine, 1);
                    } else if (line.getLines()[index] == 1) {
                        line.getLines()[index] = 0;
                        setBtnBackground(btnLine, 0);
                    }
                    itemPosition = index;
                }
                index++;
            }
        }
        listener.onLineItemClick(v, position, itemPosition, line);
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
        return this.textLineResult;
    }

    public interface OnLineItemClickListener {
        public void onLineItemClick(View v, int parentPosition, int itemPosition, Line line);
    }
}
