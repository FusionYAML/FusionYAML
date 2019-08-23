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
package me.brokenearthdev.fusionyaml.object;

import com.google.gson.internal.LazilyParsedNumber;

public class YamlPrimitive implements YamlElement {

    private Object value;

    public YamlPrimitive(Boolean bool) {
        this.value = bool;
    }


    public YamlPrimitive(Number number) {
        this.value = number;
    }

    public YamlPrimitive(String string) {
        this.value = string;
    }

    public YamlPrimitive(Object primitive) {
        if (!(primitive instanceof Number) && !(primitive instanceof String) && !(primitive instanceof Character) && !(primitive instanceof Boolean))
            throw new IllegalArgumentException(primitive.getClass().getName() + " is not a primitive type");
        value = primitive;
    }

    @Override
    public boolean getAsBoolean() {

        return Boolean.parseBoolean(getAsString());
    }

    @Override
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    @Override
    public Number getAsNumber() {
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
    }

    @Override
    public boolean isNumber() {
        return value instanceof Number;
    }

    @Override
    public char getAsChar() {
        return getAsString().charAt(0);
    }

    @Override
    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        } else if (isBoolean()) {
            return ((Boolean) value).toString();
        } else {
            return (String) value;
        }
    }

    @Override
    public boolean isCharacter() {
        return value instanceof Character;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof YamlPrimitive))
            return false;
        YamlPrimitive other = (YamlPrimitive) o;
        if (other.value instanceof Number && value instanceof Number) {
            double a = getAsNumber().doubleValue();
            double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return value.equals(other.value);
    }

}
