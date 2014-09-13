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

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BaseExecutionFragment extends Fragment {
    protected ExecutorService mExecutor;
    protected DB mSnappyDB;

    @Override
    public void onStart() {
        super.onStart();
        mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                return thread;
            }
        });

        try {
            mSnappyDB = new SnappyDB.Builder(getActivity())
                    .name(getClass().getSimpleName())
                    .build();

        } catch (SnappydbException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't create database");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mExecutor) {
            mExecutor.shutdownNow();
        }

        if (null != mSnappyDB) {
            try {
                mSnappyDB.close();
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
        }
    }
}
