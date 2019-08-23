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
package me.brokenearthdev.simpleyaml.object;


public interface YamlElement {

    // primitives

    default boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default short getAsShort() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default int getAsInt() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default long getAsLong() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default char getAsChar() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    // CHECKS

    default boolean isYamlPrimitive() {
        return this instanceof YamlPrimitive;
    }

    default boolean isYamlList() {
        return this instanceof YamlList;
    }

    default boolean isYamlObject() {
        return this instanceof YamlObject;
    }


    default boolean isNumber() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default boolean isBoolean() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default boolean isYamlNode() {
        return this instanceof YamlNode;
    }

    default boolean isCharacter() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    // NON-PRIMITIVE

    default YamlList getAsYamlList() {
        if (isYamlList())
            return (YamlList) this;
        else throw new IllegalStateException("Not a YAML list: " + this);
    }

    default YamlNode getAsYamlNode() {
        if (isYamlNode())
            return (YamlNode) this;
        else throw new IllegalStateException("Not a YAML node: " + this);
    }

    default YamlObject getAsYamlObject() {
        if (isYamlObject())
            return (YamlObject) this;
        else throw new IllegalStateException("Not a YAML object: " + this);
    }

    default YamlPrimitive getAsYamlPrimitive() {
        if (isYamlPrimitive())
            return (YamlPrimitive) this;
        else throw new IllegalStateException("Not a YAML primitive: " + this);
    }

    default Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default String getAsString() {
        throw new UnsupportedOperationException(getClass().getName());
    }

}
