package com.example.listview_sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonAdapter extends BaseAdapter {

    Context context;
    ArrayList<PersonInfo> arrayList;

    public PersonAdapter(Context context, ArrayList<PersonInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.display_layout, null);

        TextView idv = view.findViewById(R.id.idView);
        TextView namev = view.findViewById(R.id.nameView);
        TextView agev = view.findViewById(R.id.ageView);

        PersonInfo pi = arrayList.get(position);
        int id = pi.getId();;
        String name = pi.getName();
        int age = pi.getAge();

        idv.setText(String.valueOf(id));
        namev.setText(name);
        agev.setText(String.valueOf(age));

        return view;
    }
}
