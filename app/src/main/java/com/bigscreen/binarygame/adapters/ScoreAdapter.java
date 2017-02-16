package com.bigscreen.binarygame.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bigscreen.binarygame.entities.ScoreEntity;
import com.bigscreen.binarygame.view.items.ScoreItem;

import java.util.ArrayList;
import java.util.List;


public class ScoreAdapter extends BaseAdapter {

    private static final String TAG = ScoreAdapter.class.getSimpleName();

    private List<ScoreEntity> entities;
    private Context context;

    public ScoreAdapter(Context context) {
        this.context = context;
        entities = new ArrayList<>();
    }

    public void setData(List<ScoreEntity> scoreEntities) {
        if (entities == null) {
            entities = scoreEntities;
        } else {
            entities.clear();
            entities.addAll(scoreEntities);
        }
        notifyDataSetChanged();
    }

    public void appendData(List<ScoreEntity> scoreEntities) {
        entities.addAll(scoreEntities);
        notifyDataSetChanged();
    }

    public List<ScoreEntity> getList() {
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
    public ScoreEntity getItem(int position) {
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
