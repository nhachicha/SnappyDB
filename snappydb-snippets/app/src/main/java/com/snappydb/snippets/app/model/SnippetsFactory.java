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

import com.snappydb.snippets.app.activity.snippets.ArrayOperationsSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.CustomKryoSerializationSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.ObjectsSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.OpenCloseSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.PrimitiveOperationsSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.RangeOperationsSnippetActivity;
import com.snappydb.snippets.app.activity.snippets.SerializableSnippetActivity;

import java.util.ArrayList;
import java.util.TreeMap;

public enum SnippetsFactory {
    INSTANCE;
    private final static String HTML_BASE_DIR = "file:///android_asset/";
    private final TreeMap<String, ArrayList<Snippet>> snippets;

    private SnippetsFactory() {
        snippets = new TreeMap<>();

        ArrayList<Snippet> children = new ArrayList<>();

        children.add(new
                Snippet("[Open/Create]",
                HTML_BASE_DIR + "basics.html",
                OpenCloseSnippetActivity.class));

        children.add(new
                Snippet("[Primitive]",
                HTML_BASE_DIR + "primitive.html",
                PrimitiveOperationsSnippetActivity.class));

        snippets.put("01. Basics", children);


        children = new ArrayList<>();
        children.add(new
                Snippet("[Serializable]",
                HTML_BASE_DIR + "serializable.html",
                SerializableSnippetActivity.class));

        children.add(new
                Snippet("[Objects]",
                HTML_BASE_DIR + "object.html",
                ObjectsSnippetActivity.class));

        children.add(new
                Snippet("[Arrays]",
                HTML_BASE_DIR + "array.html",
                ArrayOperationsSnippetActivity.class));

        children.add(new
                Snippet("[Custom Kryo Serializer]",
                HTML_BASE_DIR + "kryo_serilizer.html",
                CustomKryoSerializationSnippetActivity.class));

        snippets.put("02. Objects/Serializable", children);

        children = new ArrayList<>();
        children.add(new
                Snippet("[Prefixes]",
                HTML_BASE_DIR + "range.html",
                RangeOperationsSnippetActivity.class));

        snippets.put("03. Keys Search", children);
    }

    public TreeMap<String, ArrayList<Snippet>> getSnippets() {
        return snippets;
    }
}
