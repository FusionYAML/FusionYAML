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
package org.fusionyaml.library.object;

/**
 * A {@link YamlElement} is a data contained in a {@link java.util.Map} that
 * is ready to be converted into a {@code YAML} {@link String}.
 */
public interface YamlElement {

    /**
     * Copies all of the elements and the children contained
     * herein
     *
     * @return A copy of this {@link YamlElement}
     */
    YamlElement deepCopy();

    /**
     * Converts the value in this {@link YamlPrimitive} object to a {@code boolean}.
     * If this method is invoked and this class isn't an instance of {@link YamlPrimitive},
     * {@link UnsupportedOperationException} will be thrown.
     *
     * @return The {@code boolean} value found in this {@link YamlPrimitive} object
     * @throws UnsupportedOperationException If the object is not an instance of {@link YamlPrimitive}
     * @see #isBoolean()
     */
    default boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default short getAsShort() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default int getAsInt() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default long getAsLong() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default char getAsChar() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * Checks whether if this class is an instance of {@link YamlPrimitive} or
     * not. This method returns a value equivalent to using the {@code instanceof}
     * keyword.
     *
     * @return Whether this class is an instance of {@link YamlPrimitive} or not.
     * @see #getAsYamlPrimitive()
     */
    default boolean isYamlPrimitive() {
        return this instanceof YamlPrimitive;
    }

    /**
     * Checks whether if this class is an instance of {@link YamlArray} or
     * not. This method returns a value equivalent to using the {@code instanceof}
     * keyword.
     *
     * @return Whether this class is an instance of {@link YamlArray} or not.
     * @see #getAsYamlArray()
     */
    default boolean isYamlArray() {
        return this instanceof YamlArray;
    }

    /**
     * Checks whether if this class is an instance of {@link YamlObject} or
     * not. This method returns a value equivalent to using the {@code instanceof}
     * keyword.
     *
     * @return Whether this class is an instance of {@link YamlObject} or not.
     * @see #getAsYamlObject()
     */
    default boolean isYamlObject() {
        return this instanceof YamlObject;
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Number} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Number}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    default boolean isNumber() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Boolean} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Boolean}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    default boolean isBoolean() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link Character} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link Character}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    default boolean isCharacter() {
        throw new UnsupportedOperationException(getClass().getName());
    }


    /**
     * Checks whether if the value in {@link YamlPrimitive} is a {@link String} or not.
     * Please note this this method will only work if this class is an instance of
     * {@link YamlPrimitive}.
     *
     * @return Whether if the value in {@link YamlPrimitive} is a {@link String}
     * @throws UnsupportedOperationException If this method is not an instance of {@link YamlPrimitive}
     */
    default boolean isString() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * Checks whether this object is an instance of {@link YamlNull} and returs treue
     * if it does
     *
     * @return Whether this object is an instance of {@link YamlNull} or not
     */
    default boolean isYamlNull() {
        return this instanceof YamlNull;
    }

    /**
     * Casts {@link YamlArray} to this class's instance after checking if this class
     * is indeed an instance of {@link YamlArray}.
     *
     * @return A {@link YamlArray} casted to this class
     * @throws IllegalStateException If this class is not an instance of {@link YamlArray}
     * @see #isYamlArray()
     */
    default YamlArray getAsYamlArray() {
        if (isYamlArray())
            return (YamlArray) this;
        else throw new IllegalStateException("Not a YAML array: " + this);
    }

    /**
     * Casts {@link YamlObject} to this class's instance after checking if this class
     * is indeed an instance of {@link YamlObject}.
     *
     * @return A {@link YamlObject} casted to this class
     * @throws IllegalStateException if this class is not an instance of {@link YamlObject}
     * @see #isYamlObject()
     */
    default YamlObject getAsYamlObject() {
        if (isYamlObject())
            return (YamlObject) this;
        else throw new IllegalStateException("Not a YAML object: " + this);
    }

    /**
     * Casts {@link YamlPrimitive} to this class's instance after checking if this class is
     * indeed an instance of {@link YamlPrimitive}
     *
     * @return A {@link YamlPrimitive} casted to this class
     * @throws IllegalStateException if this class is not an instance of {@link YamlPrimitive}
     * @see #isYamlPrimitive()
     */
    default YamlPrimitive getAsYamlPrimitive() {
        if (isYamlPrimitive())
            return (YamlPrimitive) this;
        else throw new IllegalStateException("Not a YAML primitive: " + this);
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
    default Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getName());
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
    default String getAsString() {
        throw new UnsupportedOperationException(getClass().getName());
    }
    
    @Override
    boolean equals(Object o);
    
}