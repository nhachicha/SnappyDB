/*
 * Copyright (C) 2014 Nabil HACHICHA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.snappydb.snippets.app.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.snappydb.snippets.app.model.Snippet;

import java.util.AbstractMap;
import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
    private final ArrayList<String> mHeader;
    private final AbstractMap<String, ArrayList<Snippet>> mChildren;
    private final LayoutInflater mInflater;
    private final OnSnippetClicked mSnippetClickListener;

    public ExpandableListAdapter(LayoutInflater inflater, ArrayList<String> headers,
                                 AbstractMap<String, ArrayList<Snippet>> children,
                                 OnSnippetClicked snippetClickListener) {
        mHeader = headers;
        mChildren = children;
        mInflater = inflater;
        mSnippetClickListener = snippetClickListener;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mChildren.get(this.mHeader.get(groupPosition)).get(childPosition).getName();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }

        TextView tvChild = (TextView) convertView.findViewById(android.R.id.text1);
        tvChild.setText(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mChildren.get(this.mHeader.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return this.mHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(android.R.id.text1);
        tvHeader.setTypeface(null, Typeface.BOLD_ITALIC);
        tvHeader.setText(getGroup(groupPosition));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        mSnippetClickListener.onClick(mChildren.get(this.mHeader.get(groupPosition)).get(childPosition));
        return false;
    }

    public static interface OnSnippetClicked {
        void onClick(Snippet snippet);
    }
}