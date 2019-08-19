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
package me.brokenearthdev.simpleyaml.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.brokenearthdev.simpleyaml.entities.YamlElement;

import java.util.List;
import java.util.Map;

public interface Configuration {

    byte getByte(String path, byte defaultValue);
    byte getByte(String path);
    short getShort(String path, short defaultValue);
    short getShort(String path);
    int getInt(String path, int defaultValue);
    int getInt(String path);
    float getFloat(String path, float defaultValue);
    float getFloat(String path);
    double getDouble(String path, double defaultValue);
    double getDouble(String path);
    long getLong(String path, long defaultValue);
    long getLong(String path);

    Object getObject(String path, Object defaultValue);
    Object getObject(String path);
    String getString(String path, String defaultValue);
    String getString(String path);
    List getList(String path, List defaultValue);
    List getList(String path);
    Map getMap(String path, Map defaultValue);
    Map getMap(String path);

    ImmutableList<YamlElement> getAllElements();
    ImmutableMap<Object, Object> getAsMap();

    @Override
    String toString();

}
