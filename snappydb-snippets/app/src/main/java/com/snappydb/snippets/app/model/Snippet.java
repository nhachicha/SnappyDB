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


package com.snappydb.snippets.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.snappydb.snippets.app.activity.AbsBaseSnippetActivity;

public class Snippet implements Parcelable {
    public static final Creator<Snippet> CREATOR = new Creator<Snippet>() {
        @Override
        public Snippet createFromParcel(Parcel parcel) {
            return new Snippet(parcel);
        }

        @Override
        public Snippet[] newArray(int i) {
            return new Snippet[i];
        }
    };
    private String name;
    private String html;
    private Class<? extends AbsBaseSnippetActivity> activityClazz;

    public Snippet(String name, String html, Class<? extends AbsBaseSnippetActivity> activityClazz) {
        this.name = name;
        this.html = html;
        this.activityClazz = activityClazz;
    }

    private Snippet(Parcel in) {
        name = in.readString();
        html = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(html);
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public String getHtml() {
        return html;
    }

    public Class<? extends AbsBaseSnippetActivity> getActivity() {
        return activityClazz;
    }

}
