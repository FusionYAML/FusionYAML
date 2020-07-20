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
package io.github.fusionyaml.object;

import com.google.gson.internal.LazilyParsedNumber;

/**
 * An object of this class represents a primitive or {@link String}
 * datum in {@code YAML}. A primitive value can be one of the following:
 * <ul>
 * <li>boolean</li>
 * <li>char</li>
 * <li>byte</li>
 * <li>short</li>
 * <li>float</li>
 * <li>double</li>
 * <li>int</li>
 * <li>long</li>
 * </ul>
 */
public class YamlPrimitive implements YamlElement {

    /**
     * The primitive or {@link String} value
     */
    private Object value;

    /**
     * This constructor requires a {@link Boolean} to be passed into the
     * parameter.
     *
     * @param bool The {@link Boolean}
     */
    public YamlPrimitive(Boolean bool) {
        this.value = bool;
    }

    /**
     * This constructor requires a {@link Number} to be passed into the
     * parameter.
     *
     * @param number The {@link Number}
     */
    public YamlPrimitive(Number number) {
        this.value = number;
    }

    /**
     * This constructor requires a {@link String} to be passed into the
     * parameter.
     *
     * @param string The {@link String}
     */
    public YamlPrimitive(String string) {
        this.value = string;
    }

    /**
     * This constructor requires an {@link Object} to be passed into the
     * parameter.
     * <p>
     * The {@link Object} should be of primitive or {@link String} type, or
     * an exception will be thrown.
     *
     * @param primitive The {@link Object} which should be of primitive or
     *                  {@link String} type
     * @throws IllegalArgumentException If the {@link Object} passed in is
     * not of primitive or {@link String} type.
     */
    public YamlPrimitive(Object primitive) {
        if (!(primitive instanceof Number) && !(primitive instanceof String) && !(primitive instanceof Character) && !(primitive instanceof Boolean))
            throw new IllegalArgumentException(primitive.getClass().getName() + " is not a primitive type");
        value = primitive;
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code boolean}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code boolean} value found in this {@link YamlPrimitive} object
     * @see #isBoolean()
     */
    @Override
    public boolean getAsBoolean() {
        return Boolean.parseBoolean(getAsString());
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Boolean} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Boolean}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    @Override
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code byte}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code byte} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code short}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code short} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to an {@code int}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code int} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code float}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code float} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code double}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code double} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code long}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code long} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    /**
     * Casts {@link Number} to the value found in this {@link YamlPrimitive} object or
     * converts the value to {@link Number} if the value is not an instance of a {@link Number}
     * object.
     *
     * @return The {@link Number} converted from the value in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException if this class is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
    @Override
    public Number getAsNumber() {
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Number} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Number}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    @Override
    public boolean isNumber() {
        return value instanceof Number;
    }

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code char}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code char} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isCharacter()
     */
    @Override
    public char getAsChar() {
        return getAsString().charAt(0);
    }


    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Character} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Character}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    @Override
    public boolean isCharacter() {
        return value instanceof Character;
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link String} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link String}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    @Override
    public boolean isString() {
        return value instanceof String;
    }

    /**
     * Casts {@link String} to the value found in this {@link YamlPrimitive} object or
     * converts the value to {@link String} if the value is not an instance of a {@link String}
     * object.
     *
     * @return The {@link String} converted from the value in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException if this class is not an instance of {@link YamlPrimitive}
     * @see #isNumber()
     */
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

    public Object getValue() {
        return value;
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
