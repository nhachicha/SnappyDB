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


package com.snappydb.snippets.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Nabil on 06/09/14.
 */
public class SnippetWebViewFragment extends Fragment {
    public static final String TAG = "com.snappydb.snippets.app.fragment.SnippetWebViewFragment.TAG";

    private static final String URL_ARG = "com.snappydb.snippets.app.fragment.SnippetWebViewFragment.URL_ARG";
    private WebView mWebView;
    private String mUrl;

    public static SnippetWebViewFragment newInstance(String url) {
        SnippetWebViewFragment fragment = new SnippetWebViewFragment();

        Bundle args = new Bundle();
        args.putString(URL_ARG, url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(URL_ARG);

        } else {
            mUrl = getArguments().getString(URL_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mWebView) {
            mWebView = new WebView(getActivity());
            configureWebSettings();
        }

        mWebView.loadUrl(mUrl);
        return mWebView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL_ARG, mUrl);
    }

    private void configureWebSettings() {
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setInitialScale(125);
    }
}
