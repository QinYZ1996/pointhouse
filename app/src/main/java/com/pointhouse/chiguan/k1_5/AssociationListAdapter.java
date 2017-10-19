package com.pointhouse.chiguan.k1_5;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;

import java.util.Vector;

/**
 * Created by ljj on 2017/7/24.
 */

public class AssociationListAdapter extends BaseAdapter {

    public class AssociationItem {
        public String associationName;
        public String associationKey;
    }

    private LayoutInflater mInflater;
    private Vector<AssociationItem> mModels = new Vector<AssociationItem>();

    public AssociationListAdapter(Context context, ListView listView) {
        mInflater = LayoutInflater.from(context);
    }

    public void addAssociation(String associationName,String associationKey) {
        AssociationItem model = new AssociationItem();
        model.associationName = associationName;
        model.associationKey = associationKey;
        mModels.add(model);
    }

    public void clean() {
        mModels.clear();
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= getCount()) {
            return null;
        }
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.k1_5_adapter_associationlist,
                    null);
        AssociationItem model = mModels.get(position);
        TextView searchTxt = (TextView) convertView.findViewById(R.id.searchTxt);
        String associationKey = "<font color='#3c5fc5'>"+model.associationKey+"</font>";

        searchTxt.setText(Html.fromHtml(model.associationName.replace(model.associationKey,associationKey)));
        return convertView;
    }
}
