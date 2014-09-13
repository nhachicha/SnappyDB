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

package com.snappydb.snippets.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ExpandableListView;

import com.snappydb.snippets.app.R;
import com.snappydb.snippets.app.adapter.ExpandableListAdapter;
import com.snappydb.snippets.app.model.Snippet;
import com.snappydb.snippets.app.model.SnippetsFactory;

import java.util.ArrayList;
import java.util.TreeMap;

public class MainActivity extends ActionBarActivity implements ExpandableListAdapter.OnSnippetClicked {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TreeMap<String, ArrayList<Snippet>> snippets = SnippetsFactory.INSTANCE.getSnippets();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.categories);
        ExpandableListAdapter adapter = new ExpandableListAdapter(LayoutInflater.from(this),//
                                            new ArrayList<>(snippets.keySet()),
                                            snippets, this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                openAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Snippet snippet) {
        Intent intent = new Intent(this, snippet.getActivity());
        intent.putExtra(AbsBaseSnippetActivity.SNIPPET_ARG, snippet);
        startActivity(intent);
    }

    private void openAbout () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.about);

        WebView wv = new WebView(this);
        wv.loadUrl("file:///android_asset/about.html");
        alert.setView(wv);
        alert.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}