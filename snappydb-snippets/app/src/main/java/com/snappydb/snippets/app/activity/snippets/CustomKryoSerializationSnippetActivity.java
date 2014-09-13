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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.snappydb.SnappydbException;
import com.snappydb.snippets.app.R;
import com.snappydb.snippets.app.activity.AbsBaseSnippetActivity;
import com.snappydb.snippets.app.fragment.BaseExecutionFragment;
import com.snappydb.snippets.app.model.custom.TimestampedEmployee;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomKryoSerializationSnippetActivity extends AbsBaseSnippetActivity implements Serializable {

    @Override
    public Fragment getExecutionFragment() {
        return new PrimitiveOperationSnippetFragment();
    }

    public static class PrimitiveOperationSnippetFragment extends BaseExecutionFragment implements View.OnClickListener, Serializable {
        private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm:ss");
        private WeakReference<EditText> mName, mKey;
        private WeakReference<TextView> mResult;
        private Button mBtnPut, mBtnGet, mBtnDel;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.kryo_serializer, container, false);
            mName = new WeakReference<>((EditText) view.findViewById(R.id.name));
            mKey = new WeakReference<>((EditText) view.findViewById(R.id.key));
            mResult = new WeakReference<>((TextView) view.findViewById(R.id.result));

            mBtnPut = (Button) view.findViewById(R.id.btnPut);
            mBtnGet = (Button) view.findViewById(R.id.btnGet);
            mBtnDel = (Button) view.findViewById(R.id.btnDel);

            mBtnPut.setOnClickListener(this);
            mBtnGet.setOnClickListener(this);
            mBtnDel.setOnClickListener(this);

            return view;
        }

        @Override
        public void onStart() {
            super.onStart();
            //Register a custom Kryo Serializer (instead of the default FiledSerializer)
            mSnappyDB.getKryoInstance().addDefaultSerializer(TimestampedEmployee.class, new Serializer<TimestampedEmployee>() {

                @Override
                public void write(Kryo kryo, Output output, TimestampedEmployee employee) {
                    output.writeString(employee.getName());
                    output.writeLong(System.currentTimeMillis());//set the new serialization time
                }

                @Override
                public TimestampedEmployee read(Kryo kryo, Input input, Class<TimestampedEmployee> type) {
                    TimestampedEmployee timestampedEmployee = new TimestampedEmployee();

                    timestampedEmployee.setName(input.readString());
                    timestampedEmployee.setSerializationDate(input.readLong()); // use previous serialization time
                    timestampedEmployee.setDeserializationDate(System.currentTimeMillis());// set deserialization time

                    return timestampedEmployee;
                }
            });
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
                        String name = mName.get().getText().toString();
                        String key = mKey.get().getText().toString();
                        TimestampedEmployee timestampedEmployee = new TimestampedEmployee();
                        timestampedEmployee.setName(name);

                        mSnappyDB.put(key, timestampedEmployee);

                        mName.get().post(new Runnable() {
                            @Override
                            public void run() {
                                // Clear the inserted values, as a visual indicator that the operation succeeded
                                mName.get().setText("");
                                mKey.get().setText("");
                                mResult.get().setText("");
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

                        final TimestampedEmployee timestampedEmployee = mSnappyDB.getObject(key, TimestampedEmployee.class);

                        mResult.get().post(new Runnable() {
                            @Override
                            public void run() {
                                mName.get().setText(timestampedEmployee.getName());
                                mResult.get().setText("Serialized at: "
                                        + DATE_FORMAT.format(new Date(timestampedEmployee.getSerializationDate()))
                                        + "\nDeserialized at: "
                                        + DATE_FORMAT.format(new Date(timestampedEmployee.getDeserializationDate())));

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
