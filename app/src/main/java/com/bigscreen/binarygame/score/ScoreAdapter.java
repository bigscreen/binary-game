package com.bigscreen.binarygame.score;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class ScoreAdapter extends BaseAdapter {

    private static final String TAG = ScoreAdapter.class.getSimpleName();

    private List<Score> entities;
    private Context context;

    public ScoreAdapter(Context context) {
        this.context = context;
        entities = new ArrayList<>();
    }

    public void setData(List<Score> scoreEntities) {
        if (entities == null) {
            entities = scoreEntities;
        } else {
            entities.clear();
            entities.addAll(scoreEntities);
        }
        notifyDataSetChanged();
    }

    public void appendData(List<Score> scoreEntities) {
        entities.addAll(scoreEntities);
        notifyDataSetChanged();
    }

    public List<Score> getList() {
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
    public Score getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoreItem item;
        if (convertView == null) {
            item = new ScoreItem(context);
        } else {
            item = (ScoreItem) convertView;
        }

        item.bind(getItem(position), position);

        return item;
    }
}
