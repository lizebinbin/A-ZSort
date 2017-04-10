package com.moore.sortdemo;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MooreLi on 2017/4/10.
 */

public class ExpandAdapter implements ExpandableListAdapter {
    private Context mContext;
    private List<List<City>> datas;

    public ExpandAdapter(Context context, List<List<City>> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder parentHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_parent, null);
            parentHolder = new ParentHolder();
            parentHolder.tvParentInfo = (TextView) convertView.findViewById(R.id.tv_parentInfo);
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        parentHolder.tvParentInfo.setText(datas.get(groupPosition).get(0).getFirstLetter());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_child, null);
            childHolder = new ChildHolder();
            childHolder.tvChildInfo = (TextView) convertView.findViewById(R.id.tv_childInfo);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.tvChildInfo.setText(datas.get(groupPosition).get(childPosition).getCityName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class ChildHolder {
        TextView tvChildInfo;
    }

    class ParentHolder {
        TextView tvParentInfo;
    }
}
