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


package com.snappydb.snippets.app.activity.snippets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappydb.SnappydbException;
import com.snappydb.snippets.app.R;
import com.snappydb.snippets.app.activity.AbsBaseSnippetActivity;
import com.snappydb.snippets.app.fragment.BaseExecutionFragment;

import java.lang.ref.WeakReference;

public class RangeOperationsSnippetActivity extends AbsBaseSnippetActivity {

    @Override
    public Fragment getExecutionFragment() {
        return new PrimitiveOperationSnippetFragment();
    }

    public static class PrimitiveOperationSnippetFragment extends BaseExecutionFragment implements View.OnClickListener {
        private WeakReference<EditText> mPrefix, mRangeFrom, mRangeTo;
        private Button mBtnPrefix, mBtnRange;
        private WeakReference<TextView> mResult;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.range_operations, container, false);
            mPrefix = new WeakReference<>((EditText) view.findViewById(R.id.prefix));
            mRangeFrom = new WeakReference<>((EditText) view.findViewById(R.id.rangeFrom));
            mRangeTo = new WeakReference<>((EditText) view.findViewById(R.id.rangeTo));
            mResult = new WeakReference<>((TextView) view.findViewById(R.id.result));

            mBtnPrefix = (Button) view.findViewById(R.id.btnPrefix);
            mBtnRange = (Button) view.findViewById(R.id.btnRange);
            mBtnPrefix.setOnClickListener(this);
            mBtnRange.setOnClickListener(this);

            return view;
        }

        @Override
        public void onStart() {
            super.onStart();
            populateSampleData();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPrefix: {
                    searchByPrefix();
                    break;
                }

                case R.id.btnRange: {
                    searchByRange();
                    break;
                }
            }
        }

        private void populateSampleData() {
            // populate db with the example data
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSnappyDB.put("android:03", "Cupcake");
                        mSnappyDB.put("android:04", "Donut");
                        mSnappyDB.put("android:05", "Eclair");
                        mSnappyDB.put("android:08", "Froyo");
                        mSnappyDB.put("android:09", "Gingerbread");
                        mSnappyDB.put("android:11", "Honeycomb");
                        mSnappyDB.put("android:14", "Ice Cream Sandwich");
                        mSnappyDB.put("android:16", "Jelly Bean");
                        mSnappyDB.put("android:19", "KitKat");

                    } catch (NullPointerException | SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // database operations //

        private void searchByPrefix() {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String prefix = mPrefix.get().getText().toString();

                        final StringBuilder result = new StringBuilder();

                        String[] keys = mSnappyDB.findKeys(prefix);

                        if (null != keys && keys.length > 0) {
                            for (String key : keys) {
                                result.append(mSnappyDB.get(key)).append("\n");
                            }
                        }

                        mResult.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mResult.get().setText(result.toString());
                            }
                        });
                    } catch (NullPointerException | SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void searchByRange() {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String from = mRangeFrom.get().getText().toString();
                        String to = mRangeTo.get().getText().toString();

                        String[] keys = mSnappyDB.findKeysBetween(from, to);

                        final StringBuilder result = new StringBuilder();

                        if (null != keys && keys.length > 0) {
                            for (String key : keys) {
                                result.append(mSnappyDB.get(key)).append("\n");
                            }
                        }

                        mResult.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mResult.get().setText(result.toString());
                            }
                        });

                    } catch (NullPointerException | SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
