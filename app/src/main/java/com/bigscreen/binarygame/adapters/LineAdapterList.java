package com.bigscreen.binarygame.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bigscreen.binarygame.models.Line;
import com.bigscreen.binarygame.view.items.LineItem;

import java.util.ArrayList;
import java.util.List;


public class LineAdapterList extends BaseAdapter {

    private static final String TAG = LineAdapterList.class.getSimpleName();

    private List<Line> entities;
    private int lineHeight;
    private Context context;

    public LineAdapterList(Context context, int lineHeight) {
        this.context = context;
        this.lineHeight = lineHeight;
        entities = new ArrayList<>();
    }

    public void processData(List<Line> ts) {
        if (entities == null) {
            entities = ts;
        } else {
            entities.clear();
            entities.addAll(ts);
        }
        notifyDataSetChanged();
    }

    public void setData(List<Line> lineEntities) {
        entities.clear();
        appendData(lineEntities);
    }

    public void appendData(List<Line> lineEntities) {
        entities.addAll(lineEntities);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (entities != null) {
            entities.remove(position);
            notifyDataSetChanged();
        } else {
            Log.e(TAG, "Data is null, nothing to remove!");
        }
    }

    public void clearData() {
        if (entities != null) {
            entities.clear();
            notifyDataSetChanged();
        } else {
            Log.e(TAG, "Data is null, nothing to remove!");
        }
    }

    public void appendSingleData(Line line) {
        this.entities.add(line);
        notifyDataSetChanged();
    }

    public void appendSingleData(Line line, int index) {
        this.entities.add(index, line);
        notifyDataSetChanged();
    }

    public List<Line> getList() {
        return entities;
    }

    @Override
    public int getCount() {
        if (entities != null)
            return entities.size();
        else
            return 0;
    }

    @Override
    public Line getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LineItem item;
        if (convertView == null) {
            item = new LineItem(context);
        } else {
            item = (LineItem) convertView;
        }

        item.bind(getItem(position), lineHeight, position);

        return item;
    }
}
