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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.snappydb.snippets.app.R;
import com.snappydb.snippets.app.fragment.SnippetWebViewFragment;
import com.snappydb.snippets.app.model.Snippet;

public abstract class AbsBaseSnippetActivity extends ActionBarActivity {
    public final static String SNIPPET_ARG = "com.snappydb.snippets.app.activity.basic.OpenCloseSnippetActivity.SNIPPET_ARG";

    private Snippet mSnippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            mSnippet = savedInstanceState.getParcelable(SNIPPET_ARG);

        } else if (getIntent().hasExtra(SNIPPET_ARG)) {
            mSnippet = getIntent().getParcelableExtra(SNIPPET_ARG);

        } else {
            throw new IllegalArgumentException("Need a Snippet instance to work");
        }

        setContentView(R.layout.basic_snippet_layout);

        setTitle(mSnippet.getName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.webview_fragment,
                                    SnippetWebViewFragment.newInstance(mSnippet.getHtml()),
                                    SnippetWebViewFragment.TAG);

        Fragment execFrag = getExecutionFragment();
        if (null != execFrag) {
            fragmentTransaction.replace(R.id.execution_fragment, execFrag, null);
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SNIPPET_ARG, mSnippet);
    }

    protected abstract Fragment getExecutionFragment();

}
