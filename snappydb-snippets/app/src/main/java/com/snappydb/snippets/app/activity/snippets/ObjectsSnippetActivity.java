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

import com.snappydb.SnappydbException;
import com.snappydb.snippets.app.R;
import com.snappydb.snippets.app.activity.AbsBaseSnippetActivity;
import com.snappydb.snippets.app.fragment.BaseExecutionFragment;
import com.snappydb.snippets.app.model.objects.Address;
import com.snappydb.snippets.app.model.objects.Employee;

import java.lang.ref.WeakReference;

public class ObjectsSnippetActivity extends AbsBaseSnippetActivity {

    @Override
    public Fragment getExecutionFragment() {
        return new PrimitiveOperationSnippetFragment();
    }

    public static class PrimitiveOperationSnippetFragment extends BaseExecutionFragment implements View.OnClickListener {
        private WeakReference<EditText> mZipCode, mName, mKey;
        private Button mBtnPut, mBtnGet, mBtnDel;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.object_operations, container, false);
            mZipCode = new WeakReference<>((EditText) view.findViewById(R.id.zipCode));
            mName = new WeakReference<>((EditText) view.findViewById(R.id.name));
            mKey = new WeakReference<>((EditText) view.findViewById(R.id.key));

            mBtnPut = (Button) view.findViewById(R.id.btnPut);
            mBtnGet = (Button) view.findViewById(R.id.btnGet);
            mBtnDel = (Button) view.findViewById(R.id.btnDel);

            mBtnPut.setOnClickListener(this);
            mBtnGet.setOnClickListener(this);
            mBtnDel.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPut: {
                    put();
                    break;
                }
                case R.id.btnGet: {
                    get();
                    break;
                }
                case R.id.btnDel: {
                    del();
                    break;
                }
            }
        }

        // DB operations //

        private void put() {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = mKey.get().getText().toString();
                        String name = mName.get().getText().toString();
                        String zipCode = mZipCode.get().getText().toString();

                        Employee employee = new Employee();
                        employee.setName(name);
                        employee.setAddress(new Address(zipCode));

                        mSnappyDB.put(key, employee);

                        mZipCode.get().post(new Runnable() {
                            @Override
                            public void run() {
                                // Clear the inserted value, as a visual indicator that the operation succeeded
                                mZipCode.get().setText("");
                                mName.get().setText("");
                                mKey.get().setText("");
                            }
                        });

                    } catch (NullPointerException | SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void get() {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = mKey.get().getText().toString();

                        final Employee employee = mSnappyDB.getObject(key, Employee.class);

                        mZipCode.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mZipCode.get().setText(employee.getAddress().getZipCode());
                                mName.get().setText(employee.getName());
                            }
                        });

                    } catch (NullPointerException | SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void del() {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = mKey.get().getText().toString();

                        mSnappyDB.del(key);

                        mKey.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mKey.get().setText("");
                                mZipCode.get().setText("");
                                mName.get().setText("");
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
