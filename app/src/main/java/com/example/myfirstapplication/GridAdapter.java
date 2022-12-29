package com.example.myfirstapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    String [] GridTitles;
    String [] GridInfo;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] gridTitles, String[] gridInfo) {
        this.context = context;
        GridTitles = gridTitles;
        GridInfo = gridInfo;
    }

    @Override
    public int getCount() {
        return GridTitles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.gird_item, null);

        TextView tv_gridTitle = view.findViewById(R.id.tv_gridTitle);
        TextView tv_gridInfo = view.findViewById(R.id.tv_gridInfo);


        tv_gridTitle.setText(GridTitles[i]);
        tv_gridInfo.setText(GridInfo[i]);

        return view;
    }
}
