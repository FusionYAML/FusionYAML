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
package me.brokenearthdev.fusionyaml;

import me.brokenearthdev.fusionyaml.object.YamlElement;
import me.brokenearthdev.fusionyaml.object.YamlObject;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.util.List;

public interface Configuration {

    void save(DumperOptions options, File file);
    void save(File file);
    String saveToString();
    YamlObject getYamlObject();
    Object getObject(List<String> path, Object defValue);
    Object getObject(List<String> path);
    Object getObject(String path, char separator, Object defValue);
    Object getObject(String path, char separator);
    Object getObject(String path, Object defValue);
    Object getObject(String path);
    String getString(List<String> path, String defValue);
    String getString(List<String> path);
    String getString(String path, char separator, String defValue);
    String getString(String path, char separator);
    String getString(String path, String defValue);
    String getString(String path);
    YamlElement getElement(List<String> path, YamlElement defValue);
    YamlElement getElement(List<String> path);
    YamlElement getElement(String path, char separator, YamlElement defValue);
    YamlElement getElement(String path, char separator);
    YamlElement getElement(String path, YamlElement defValue);
    YamlElement getElement(String path);
    boolean getBoolean(List<String> path, boolean defValue);
    boolean getBoolean(List<String> path);
    boolean getBoolean(String path, char separator, boolean defValue);
    boolean getBoolean(String path, char separator);
    boolean getBoolean(String path, boolean defValue);
    boolean getBoolean(String path);
    byte getByte(List<String> path, byte defValue);
    byte getByte(List<String> path);
    byte getByte(String path, char separator, byte defValue);
    byte getByte(String path, char separator);
    byte getByte(String path, byte defValue);
    byte getByte(String path);
    short getShort(List<String> path, short defValue);
    short getShort(List<String> path);
    short getShort(String path, char separator, short defValue);
    short getShort(String path, char separator);
    short getShort(String path, short defValue);
    short getShort(String path);
    float getFloat(List<String> path, float defValue);
    float getFloat(List<String> path);
    float getFloat(String path, char separator, float defValue);
    float getFloat(String path, char separator);
    float getFloat(String path, float defValue);
    float getFloat(String path);
    double getDouble(List<String> path, double defValue);
    double getDouble(List<String> path);
    double getDouble(String path, char separator, double defValue);
    double getDouble(String path, char separator);
    double getDouble(String path, double defValue);
    double getDouble(String path);
    int getInt(List<String> path, int defValue);
    int getInt(List<String> path);
    int getInt(String path, char separator, int defValue);
    int getInt(String path, char separator);
    int getInt(String path, int defValue);
    int getInt(String path);
    long getLong(List<String> path, long defValue);
    long getLong(List<String> path);
    long getLong(String path, char separator, long defValue);
    long getLong(String path, char separator);
    long getLong(String path, long defValue);
    long getLong(String path);
    List getList(List<String> path, List defValue);
    List getList(List<String> path);
    List getList(String path, char separator, List defValue);
    List getList(String path, char separator);
    List getList(String path, List defValue);
    List getList(String path);

}
