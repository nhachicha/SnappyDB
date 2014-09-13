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

public class ArrayOperationsSnippetActivity extends AbsBaseSnippetActivity {

    @Override
    public Fragment getExecutionFragment() {
        return new PrimitiveOperationSnippetFragment ();
    }

    public static class PrimitiveOperationSnippetFragment extends BaseExecutionFragment implements View.OnClickListener {
        private WeakReference<EditText> mEmp1ZipCode,  mEmp2ZipCode, mEmp1Name, mEmp2Name, mKey;
        private Button mBtnPut, mBtnGet, mBtnDel;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view  = inflater.inflate(R.layout.array_operations, container, false);
            mEmp1ZipCode = new WeakReference<>((EditText) view.findViewById(R.id.emp1ZipCode));
            mEmp2ZipCode = new WeakReference<>((EditText) view.findViewById(R.id.emp2ZipCode));
            mEmp1Name = new WeakReference<>((EditText) view.findViewById(R.id.emp1Name));
            mEmp2Name = new WeakReference<>((EditText) view.findViewById(R.id.emp2Name));
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
                        String emp1Name = mEmp1Name.get().getText().toString();
                        String emp1ZipCode = mEmp1ZipCode.get().getText().toString();
                        String emp2Name = mEmp2Name.get().getText().toString();
                        String emp2ZipCode = mEmp2ZipCode.get().getText().toString();

                        Employee employee1 = new Employee();
                        Employee employee2 = new Employee();

                        employee1.setName(emp1Name);
                        employee1.setAddress(new Address(emp1ZipCode));

                        employee2.setName(emp2Name);
                        employee2.setAddress(new Address(emp2ZipCode));

                        Employee[] employees = new Employee[2];
                        employees[0] = employee1;
                        employees[1] = employee2;

                        mSnappyDB.put(key, employees);

                        mKey.get().post(new Runnable() {
                            @Override
                            public void run() {
                                // Clear the inserted values, as a visual indicator that the operation succeeded
                                mEmp1Name.get().setText("");
                                mEmp1ZipCode.get().setText("");
                                mEmp2Name.get().setText("");
                                mEmp2ZipCode.get().setText("");
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
                        final Employee[] employees = mSnappyDB.getObjectArray(key, Employee.class);

                        mKey.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mEmp1Name.get().setText(employees[0].getName());
                                mEmp1ZipCode.get().setText(employees[0].getAddress().getZipCode());
                                mEmp2Name.get().setText(employees[1].getName());
                                mEmp2ZipCode.get().setText(employees[1].getAddress().getZipCode());

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
                                mEmp1Name.get().setText("");
                                mEmp1ZipCode.get().setText("");
                                mEmp2Name.get().setText("");
                                mEmp2ZipCode.get().setText("");
                                mKey.get().setText("");
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
