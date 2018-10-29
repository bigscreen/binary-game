package com.bigscreen.binarygame.line;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LineAdapter {

    private static final String TAG = LineAdapter.class.getSimpleName();

    private Context context;
    private LinearLayout lView;
    private int lineHeight;
    private List<Line> lineEntities;

    public LineAdapter(Context context, LinearLayout lView, int lineHeight) {
        this.context = context;
        this.lView = lView;
        this.lineHeight = lineHeight;
        lineEntities = new ArrayList<>();
    }

    public void setData(List<Line> lineEntities) {
        this.lineEntities.clear();
        appendData(lineEntities);
    }

    public void appendData(List<Line> lineEntities) {
        this.lineEntities.addAll(lineEntities);
        notifyDataSetChanged();
    }

    public void appendSingleData(Line line) {
        this.lineEntities.add(line);
        notifyDataSetChanged();
    }

    public void appendSingleData(int index, Line line) {
        this.lineEntities.add(index, line);
        notifyDataSetChanged();
    }

    public void removeData(int index) {
        lineEntities.remove(index);
        notifyDataSetChanged();
    }

    public void clearData() {
        lineEntities.clear();
        notifyDataSetChanged();
    }

    public void updateItem(int index, Line line) {
        this.lineEntities.set(index, line);
    }

    public int getCount() {
        return lineEntities.size();
    }

    public Line getItem(int position) {
        return lineEntities.get(position);
    }

    public long getItemId(int id) {
        return id;
    }

    public List<Line> getList() {
        return lineEntities;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        LineItem item;
        if (convertView == null) {
            item = new LineItem(context);
        } else {
            item = (LineItem) convertView;
        }

        item.bind(getItem(position), lineHeight, position);

        return item;
    }

    public void recreateView() {
        for(int position=0; position<lineEntities.size(); position++) {
            View itemView = getView(position, lView.getChildAt(position), lView);
            itemView.setTag(position);
            if (lView.getChildCount() < position) {
                lView.addView(itemView);
            } else {
                lView.addView(itemView, position);
            }
        }
    }

    public void notifyDataSetChanged() {
        lView.removeAllViews();
        recreateView();
    }

    public TextView getTvResult(int position) {
        LinearLayout llTemp = (LinearLayout) ((LinearLayout) lView.getChildAt(position)).getChildAt(0);
        return (TextView) llTemp.getChildAt(9);
    }

}
