/*
Copyright 2019 BrokenEarthDev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package me.brokenearthdev.simpleyaml.entities;

import com.google.common.collect.ImmutableList;

public interface Configuration {

    void set(String key, Object value);
    void removeKey(String key);

    byte getByte(String path);
    short getShort(String path);
    int getInt(String path);
    float getFloat(String path);
    double getDouble(String path);
    long getLong(String path);
    String getList();


    ImmutableList<YamlElement> getAllElements();

}
