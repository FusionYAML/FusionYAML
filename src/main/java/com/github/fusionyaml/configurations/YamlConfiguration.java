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
package com.github.fusionyaml.configurations;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.document.YamlComment;
import com.github.fusionyaml.events.ConfigurationChangeListener;
import com.github.fusionyaml.events.FileSaveListener;
import com.github.fusionyaml.events.Listener;
import com.github.fusionyaml.exceptions.UnsupportedYamlException;
import com.github.fusionyaml.exceptions.YamlDeserializationException;
import com.github.fusionyaml.io.DocumentWriter;
import com.github.fusionyaml.io.YamlWriter;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlNull;
import com.github.fusionyaml.object.YamlObject;
import com.github.fusionyaml.object.YamlPrimitive;
import com.github.fusionyaml.parser.YamlParser;
import com.github.fusionyaml.serialization.TypeAdapter;
import com.github.fusionyaml.utils.FileUtils;
import com.github.fusionyaml.utils.YamlUtils;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class allows you to save {@link YamlObject}s to a file. This is a super class
 * for most configurations. For example, {@link FileConfiguration} extends this class.
 */
public class YamlConfiguration implements Configuration {

    /**
     * The {@link ConfigurationChangeListener} for this object
     */
    private ConfigurationChangeListener changeListener;

    /**
     * The {@link FileSaveListener} for this object
     */
    private FileSaveListener saveListener;

    /**
     * The {@link FusionYAML} instance, which is used for deserialization
     */
    protected FusionYAML fusionYAML;

    /**
     * The local {@link YamlObject} that contains class data
     */
    protected YamlObject object;

    /**
     * A list of comments
     */
    protected List<YamlComment> comments = new ArrayList<>();

    /**
     * The default {@link DumperOptions}. If {@link #save(File)} is called, this object will
     * be used to call {@link #save(DumperOptions, File)}
     */
    private static final DumperOptions defOptions = defOptions();

    /**
     * Deserializes the whole configuration into an {@link Object}. If the configuration contains
     * keys the {@link Object} doesn't have, {@link YamlDeserializationException}
     * will be thrown.
     *
     * @param clazz The class to deserialize into
     * @return The deserialized class
     * @throws YamlDeserializationException If an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(Class<T> clazz) {
        return toObject((Type) clazz);
    }

    /**
     * Deserializes the configuration section present in the given path into an {@link Object}. If the
     * value is a primitive, {@link String}, or {@link Collection},
     * {@link YamlDeserializationException}.
     * <p>
     * Usually, the value in the path is a {@link Map} because {@link Object}s are sometimes
     * serialized into a {@link Map}
     *
     * @param path  The path to the serialized {@link Object}
     * @param clazz The class to deserialize into
     * @return The deserialized class
     * @throws YamlDeserializationException If an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(String path, Class<T> clazz) {
        return toObject(Collections.singletonList(path), (Type) clazz);
    }

    /**
     * Deserializes the configuration section present in the given path into an {@link Object}. If the
     * value is a primitive, {@link String}, or {@link Collection},
     * {@link YamlDeserializationException}.
     * <p>
     * Usually, the value in the path is a {@link Map} because {@link Object}s are sometimes
     * serialized into a {@link Map}
     *
     * @param path      The path to the serialized {@link Object}
     * @param separator The path separator. Using the path separator in the path will cause the method to
     *                  look for the serialized {@link Object} under the previous path section
     * @param clazz     The class to deserialize into
     * @return The deserialized class
     * @throws YamlDeserializationException If an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(String path, char separator, Class<T> clazz) {
        return toObject(Splitter.on(separator).splitToList(path), (Type) clazz);
    }

    /**
     * Deserializes the configuration section present in the given path into an {@link Object}. If the
     * value is a primitive, {@link String}, or {@link Collection},
     * {@link YamlDeserializationException}.
     * <p>
     * Usually, the value in the path is a {@link Map} because {@link Object}s are sometimes
     * serialized into a {@link Map}
     * serialized into a {@link Map}
     * <p>
     * Every index in the {@link List} is a child of the {@link String} at the previous index except at
     * index {@code 0}, where it is the uppermost parent.
     *
     * @param path  The path to the serialized {@link Object}
     * @param clazz The class to deserialize into
     * @return The deserialized class
     * @throws YamlDeserializationException If an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(List<String> path, Class<T> clazz) {
        return toObject(path, (Type) clazz);
    }

    /**
     * Deserializes the configuration section present in the given path into an {@link Object}. If the
     * value is a primitive, {@link String}, or {@link Collection},
     * {@link YamlDeserializationException}.
     * <p>
     * Usually, the value in the path is a {@link Map} because {@link Object}s are sometimes
     * serialized into a {@link Map}
     * <p>
     * Every index in the array is a child of the {@link String} at the previous index except at
     * index {@code 0}, where it is the uppermost parent.
     *
     * @param path  The path to the serialized {@link Object}
     * @param clazz The class to deserialize into
     * @return The deserialized class
     * @throws YamlDeserializationException If an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(String[] path, Class<T> clazz) {
        return toObject(new LinkedList<>(Arrays.asList(path)), (Type) clazz);
    }

    /**
     * This method requires a {@link FileSaveListener} object to be passed into the method's
     * parameter. {@link FileSaveListener} is called when {@code this} {@link Configuration}
     * is saved to a {@link File}.
     * *
     *
     * @param listener The {@link Listener} that will be
     *                 called when the {@link Configuration} is saved to a {@link File}
     */
    @Override
    public void setOnFileSave(FileSaveListener listener) {
        this.saveListener = listener;
    }

    /**
     * This method requires a {@link ConfigurationChangeListener} object to be passed into the method's
     * parameter. {@link ConfigurationChangeListener} is called when an entry in {@code this}
     * {@link Configuration} is modified. For example, calling setters and adding, removing, or
     * modifying the value calls this listener.
     *
     *
     * @param listener The {@link ConfigurationChangeListener}
     */
    @Override
    public void setOnConfigChange(ConfigurationChangeListener listener) {
        this.changeListener = listener;
    }


    /**
     * This constructor requires a {@link YamlObject} instance to initialize this
     * class. The {@link YamlObject} passed in will then be modified depending on the method
     * calling in this object.
     *
     * @param obj The {@link YamlObject} instance
     */
    public YamlConfiguration(YamlObject obj, FusionYAML yaml) {
        object = obj;
        this.fusionYAML = yaml;
    }

    protected YamlConfiguration(FusionYAML yaml) {
        this(new YamlObject(), yaml);
    }

    public YamlConfiguration(YamlObject obj) {
        this(obj, new FusionYAML());
    }

    /**
     * This method saves the contents into a {@link File} specified.
     *
     * @param options The {@link DumperOptions}, which contains convenient options to fit your needs
     * @param file    The file that'll be saved to. If the file doesn't exist, the file will
     * @throws IOException Thrown if any IO error occurred.
     * @deprecated This is an outdated function. Use {@link #save(File)}. The latter function is
     * more efficient; moreover, you can use {@link FusionYAML.Builder} to build a customizable
     * {@link FusionYAML} as well as a {@link com.github.fusionyaml.YamlOptions}. This function is
     * also not updated, so there may be issues with it.
     */
    @Override
    public void save(DumperOptions options, @NotNull File file) throws IOException {
        Map<String, Object> map = YamlUtils.toMap0(object);
        Yaml yaml = new Yaml((options != null) ? options : defOptions);
        String data;
        if (object.getYamlType() == YamlParser.YamlType.MAP)
            data = yaml.dump(map);
        else if (object.getYamlType() == YamlParser.YamlType.LIST)
            data = yaml.dump(YamlUtils.toList(map));
        else throw new UnsupportedYamlException("The YAML type is unsupported");
        FileUtils.writeToFile(data, new FileWriter(file), 8192);
        if (saveListener != null)
            saveListener.onSave(this, file);
    }

    /**
     * Writes the contents into the target using a given {@link Writer}
     *
     * @param writer The writer
     * @param buffer The buffer size
     * @throws IOException If an IO error occurs
     */
    @Override
    public void save(@NotNull Writer writer, int buffer) throws IOException {
        try (YamlWriter docWriter = new DocumentWriter(writer, buffer)) {
            docWriter.write(object, fusionYAML, new HashSet<>(comments));
        }
    }

    /**
     * This method saves the contents into a {@link File} specified.
     *
     * @param file The file that'll be saved to. If the file doesn't exist, the file will
     *             be created with the data saved to it.
     * @throws IOException Thrown if any IO error occurred.
     */
    @Override
    public void save(@NotNull File file) throws IOException {
        save(new FileWriter(file), nearestBuff(file.length()));
    }

    /**
     * Writes the contents into the target using a given {@link Writer}
     *
     * @param writer The writer
     * @throws IOException If an IO error occurs
     */
    @Override
    public void save(@NotNull Writer writer) throws IOException {
        save(writer, 65536);
    }

    private static int nearestBuff(long num) {
        if (num > 524288) return 524288;
        int buff = 4096;
        while (num > buff) {
            buff *= 2;
        }
        return Math.min(buff, 524288);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     * <p>
     * The object passed in will be serialized using the appropriate
     * {@link TypeAdapter}
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull String path, Object value) {
        set(Collections.singletonList(path), value);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * The separator is a character that when used, the value will be set under the parent path, which
     * is the part of the path used before the separator. Calling this method while not using a separator
     * in the path is equivalent to calling {@link #set(String, Object)}.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     * <p>
     * The object passed in will be serialized using the appropriate
     * {@link TypeAdapter}
     *
     * @param path      The path to the value
     * @param separator The path separator. When used, the value will be set under the parent, which is
     *                  the section before the separator.
     * @param value     The value the path contains
     */
    @Override
    public void set(@NotNull String path, char separator, Object value) {
        set(Splitter.on(separator).splitToList(path), value);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * The method requires a {@link List} to be passed in. In an ascending order, every index in the
     * {@link List} is the child of the previous parent except the first index, which is the uppermost
     * parent.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     * <p>
     * The object passed in will be serialized using the appropriate
     * {@link TypeAdapter}
     *
     * @param path The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull List<String> path, Object value) {
        if (value instanceof YamlElement) {
            set(path, (YamlElement) value);
            return;
        }
        if (value == null) {
            object.set(path, null);
            return;
        }
        if (!YamlUtils.isPrimitive(value) && !(value instanceof Map) && !(value instanceof Collection)) {
            object.set(path, fusionYAML.serialize(value, value.getClass()));
            return;
        }
        YamlElement converted = YamlUtils.toElement(value);
        YamlElement data = (converted != null) ? converted : new YamlPrimitive(value.toString());
        object.set(path, data);
        if (changeListener != null)
            changeListener.onChange(this, path, value);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     *
     * @param path The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull String path, YamlElement value) {
        set(Collections.singletonList(path), value);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * The separator is a character that when used, the value will be set under the parent path, which
     * is the part of the path used before the separator. Calling this method while not using a separator
     * in the path is equivalent to calling {@link #set(String, Object)}.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     *
     * @param path The path to the value
     * @param separator The path separator. When used, the value will be set under the parent, which is
     *                  the section before the separator.
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull String path, char separator, YamlElement value) {
        set(Splitter.on(separator).splitToList(path), value);
    }

    /**
     * Sets the value in the path. If the path didn't exist, a new path will be created with the
     * value set to it. If the path does exist, the value in the path will be changed into the new
     * value provided.
     * <p>
     * The method requires a {@link List} to be passed in. In an ascending order, every index in the
     * {@link List} is the child of the previous parent except the first index, which is the uppermost
     * parent.
     * <p>
     * Depending on the options set in the {@link FusionYAML.Builder}, if {@code null} is passed
     * in, the path-value pair may (or may not) be removed. Calling {@link #removePath(String)} (or
     * any of its overloaded methods) guarantees removal of the key-value pair.
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull List<String> path, YamlElement value) {
        set(path, (Object) value);
    }

    /**
     * Adds a {@link YamlComment} to the configuration. The comment will be
     * visible when dumped to a string or any {@link Writer}.
     * <p>
     * Please note that info about the line number and columns are not final,
     * since comments are relative to one another. For example, if - for any
     * reason - a comment is split into two, every succeeding comment will be
     * shifted one line down.
     *
     * @param comment A {@link YamlComment} object
     */
    @Override
    public void addComment(YamlComment comment) {
        comments.add(comment);
    }

    /**
     * Adds {@link YamlComment}s to the configuration. The comments will be
     * visible when dumped to a string, file, or any {@link Writer}.
     * <p>
     * Please note that info about the line number and columns are not final,
     * since comments are relative to one another. For example, if - for any
     * reason - a comment is split into two, every comment that appears after
     * it will be shifted one line down.
     *
     * @param comments {@link YamlComment} objects
     */
    @Override
    public void addComments(YamlComment... comments) {
        this.comments.addAll(Arrays.asList(comments));
    }

    /**
     * Removes a comment from this {@link Configuration}. The comment won't appear
     * when dumped to a string, file, or any {@link Writer}.
     *
     * @param comment A {@link YamlComment} object
     */
    @Override
    public void removeComment(YamlComment comment) {
        comments.remove(comment);
    }

    /**
     * Removes comment(s) from this {@link Configuration}. The comment(s) won't appear
     * when dumped to a string, file, or any {@link Writer}.
     *
     * @param comments {@link YamlComment} objects
     */
    @Override
    public void removeComments(YamlComment... comments) {
        this.comments.removeAll(Arrays.asList(comments));
    }

    /**
     * Removes all comments from this {@link Configuration}, causing them to not
     * appear when dumped to a string, file, or any {@link Writer}
     */
    @Override
    public void removeAllComments() {
        comments.clear();
    }

    /**
     * @return The registered comments in this object.
     */
    @Override
    public List<YamlComment> getComments() {
        return comments;
    }


    /**
     * Removes the key-value pair found in the path. A path is essentially a key.
     *
     * @param path The path to the key-value pair.
     */
    @Override
    public void removePath(@NotNull String path) {
        set(path, null);
    }

    /**
     * Removes the key-value pair found in the path. A path is essentially a key.
     * <p>
     * The separator is a character that when used, the value will be set under the parent path, which
     * is the part of the path used before the separator. Calling this method while not using a separator
     * in the path is equivalent to calling {@link #set(String, Object)}.
     *
     * @param path      The path to the key-value pair.
     * @param separator The path separator. When used, the value will be set under the parent, which is
     */
    @Override
    public void removePath(@NotNull String path, char separator) {
        set(Splitter.on(separator)
                .splitToList(path), null);
    }

    /**
     * Removes the key-value pair found in the path. A path is essentially a key.
     * <p>
     * The method requires a {@link List} to be passed in. In an ascending order, every index in the
     * {@link List} is the child of the previous parent except the first index, which is the uppermost
     * parent.
     *
     * @param path The path to the key-value pair. Every index in the {@link List} is a descent.
     */
    @Override
    public void removePath(@NotNull List<String> path) {
        set(path, null);
    }

    /**
     * @return Gets the {@link YamlObject} for the configuration
     */
    @Override
    public YamlObject getContents() {
        return object;
    }

    /**
     * This method retrieves the {@link Object} in a {@link List} of paths with the specified
     * default value if the {@link Object} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use
     * {@link #getElement(List, YamlElement)} instead
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@link Object} found in the given path or the default value if not
     */
    @Override
    public Object getObject(@NotNull List<String> path, Object defValue) {
        Object obj = YamlUtils.getObject(object.getMap(), path, new HashMap(), path.get(0), true, 0);
        if (obj == null) return null;
        Object found = fusionYAML.deserialize((YamlElement) obj, obj.getClass());
        return (found != null) ? found : defValue;
    }

    /**
     * This method retrieves the {@link Object} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@link Object} in the given path is not found, {@code null} is returned.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use
     * {@link #getElement(List)} instead
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@link Object} found in the given path or {@code null} if the {@link Object}
     * in the given path is not found
     */
    @Override
    public Object getObject(@NotNull List<String> path) {
        return getObject(path, null);
    }

    /**
     * This method retrieves the {@link Object} in a given path with a given default value if the {@link Object}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@link Object} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use
     * {@link #getElement(String, char, YamlElement)} instead
     *
     * @param path      The path where the {@link Object} is found
     * @param separator The path separator. When used, the method will look for the {@link Object}
     *                  under the parent.
     * @param defValue  If the {@link Object} is not found, the method will return this value.
     * @return The {@link Object} found in the given path or the default value if not
     */
    @Override
    public Object getObject(@NotNull String path, char separator, Object defValue) {
        return getObject(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@link Object} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@link Object} under the path before
     * the separator is used. If an {@link Object} is not found in the given path, {@code null} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use {@link #getElement(String, char)}
     * instead
     *
     * @param path      The path where the {@link Object} is found
     * @param separator The path separator. When used, the method will look for the {@link Object}
     *                  under the parent.
     * @return The {@link Object} found in the given path or {@code null} if otherwise
     */
    @Override
    public Object getObject(@NotNull String path, char separator) {
        return getObject(Splitter.on(separator).splitToList(path));
    }

    /**
     * This method retrieves the {@link Object} in a given path with a given default value if the {@link Object}
     * isn't found. The method only works on retrieving the {@link Object} in the uppermost key.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use {@link #getElement(String, YamlElement)}
     * instead
     *
     * @param path     The path where the {@link Object} is found
     * @param defValue If the {@link Object} is not found, the method will return this value.
     * @return The {@link Object} found in the given path or the default value if not
     */
    @Override
    public Object getObject(@NotNull String path, Object defValue) {
        return getObject(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@link Object} in a given path or {@code null} if otherwise.
     * The method only works on retrieving the {@link Object} in the uppermost key.
     * <p>
     * Calling this method will never return an instance of {@link YamlElement}. Use {@link #getElement(String)}
     * instead
     *
     * @param path The path where the {@link Object} is found
     * @return The {@link Object} found in the given path or {@code null} if otherwise
     */
    @Override
    public Object getObject(@NotNull String path) {
        return getObject(path, null);
    }

    /**
     * This method retrieves the {@link String} in a {@link List} of paths with the specified
     * default value if the {@link String} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@link String} found in the given path or the default value if not
     */
    @Override
    public String getString(@NotNull List<String> path, String defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof String) ? (String) found : defValue;
    }

    /**
     * This method retrieves the {@link String} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@link String} in the given path is not found, {@code null} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@link String} found in the given path or {@code null} if the {@link String}
     * in the given path is not found
     */
    @Override
    public String getString(@NotNull List<String> path) {
        return getString(path, null);
    }

    /**
     * This method retrieves the {@link String} in a given path with a given default value if the {@link String}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@link String} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link String} is found
     * @param separator The path separator. When used, the method will look for the {@link String}
     *                  under the parent.
     * @param defValue  If the {@link String} is not found, the method will return this value.
     * @return The {@link String} found in the given path or the default value if not
     */
    @Override
    public String getString(@NotNull String path, char separator, String defValue) {
        return getString(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@link String} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@link String} under the path before
     * the separator is used. If a {@link String} is not found in the given path, {@code null} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link String} is found
     * @param separator The path separator. When used, the method will look for the {@link String}
     *                  under the parent.
     * @return The {@link String} found in the given path or {@code null} if otherwise
     */
    @Override
    public String getString(@NotNull String path, char separator) {
        return getString(path, separator, null);
    }

    /**
     * This method retrieves the {@link String} in a given path with a given default value if the {@link String}
     * isn't found. The method only works on retrieving the {@link String} in the uppermost key.
     *
     * @param path     The path where the {@link String} is found
     * @param defValue If the {@link String} is not found, the method will return this value.
     * @return The {@link String} found in the given path or the default value if not
     */
    @Override
    public String getString(@NotNull String path, String defValue) {
        return getString(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@link String} in a given path or {@code null} if the {@link String}
     * isn't found. The method only works on retrieving the {@link String} in the uppermost key.
     *
     * @param path The path where the {@link String} is found
     * @return The {@link String} found in the given path or {@code null} if otherwise
     */
    @Override
    public String getString(@NotNull String path) {
        return getString(Collections.singletonList(path));
    }

    /**
     * This method retrieves the {@link YamlElement} in a {@link List} of paths with the specified
     * default value if the {@link YamlElement} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@link YamlElement} found in the given path or the default value if not
     */
    @Override
    public YamlElement getElement(@NotNull List<String> path, YamlElement defValue) {
        Object obj = getObject(path, defValue);
        return $DataBridge.toElement(obj);
    }

    /**
     * This method retrieves the {@link YamlElement} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@link YamlElement} in the given path is not found, {@code null} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@link YamlElement} found in the given path or {@code null} if the {@link YamlElement}
     * in the given path is not found
     */
    @Override
    public YamlElement getElement(@NotNull List<String> path) {
        return getElement(path, YamlNull.NULL);
    }

    /**
     * This method retrieves the {@link YamlElement} in a given path with a given default value if the {@link YamlElement}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@link YamlElement} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getElement(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link YamlElement} is found
     * @param separator The path separator. When used, the method will look for the {@link YamlElement}
     *                  under the parent.
     * @param defValue  If the {@link YamlElement} is not found, the method will return this value.
     * @return The {@link YamlElement} found in the given path or the default value if not
     */
    @Override
    public YamlElement getElement(@NotNull String path, char separator, YamlElement defValue) {
        return getElement(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@link YamlElement} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@link YamlElement} under the path before
     * the separator is used. If a {@link YamlElement} is not found in the given path, {@code null} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getElement(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link YamlElement} is found
     * @param separator The path separator. When used, the method will look for the {@link YamlElement}
     *                  under the parent.
     * @return The {@link YamlElement} found in the given path or {@code null} if otherwise
     */
    @Override
    public YamlElement getElement(@NotNull String path, char separator) {
        return getElement(Splitter.on(separator).splitToList(path), YamlNull.NULL);
    }

    /**
     * This method retrieves the {@link YamlElement} in a given path with a given default value if the {@link YamlElement}
     * isn't found. The method only works on retrieving the {@link YamlElement} in the uppermost key.
     *
     * @param path     The path where the {@link YamlElement} is found
     * @param defValue If the {@link YamlElement} is not found, the method will return this value.
     * @return The {@link YamlElement} found in the given path or the default value if not
     */
    @Override
    public YamlElement getElement(@NotNull String path, YamlElement defValue) {
        YamlElement element = getElement(path);
        return element == null ? defValue : element;
    }

    /**
     * This method retrieves the {@link YamlElement} in a given path or {@code null} if the {@link YamlElement}
     * isn't found. The method only works on retrieving the {@link YamlElement} in the uppermost key.
     *
     * @param path The path where the {@link YamlElement} is found
     * @return The {@link YamlElement} found in the given path or {@code null} if otherwise
     */
    @Override
    public YamlElement getElement(@NotNull String path) {
        return getElement(Collections.singletonList(path));
    }

    /**
     * This method retrieves the {@code boolean} in a {@link List} of paths with the specified
     * default value if the {@code boolean} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code boolean} found in the given path or the default value if not
     */
    @Override
    public boolean getBoolean(@NotNull List<String> path, boolean defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Boolean) ? (boolean) found : defValue;
    }

    /**
     * This method retrieves the {@code boolean} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code boolean} in the given path is not found, {@code false} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code boolean} found in the given path or {@code false} if the {@code boolean}
     * in the given path is not found
     */
    @Override
    public boolean getBoolean(@NotNull List<String> path) {
        return getBoolean(path, false);
    }

    /**
     * This method retrieves the {@code boolean} in a given path with a given default value if the {@code boolean}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code boolean} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code boolean} is found
     * @param separator The path separator. When used, the method will look for the {@code boolean}
     *                  under the parent.
     * @param defValue  If the {@code boolean} is not found, the method will return this value.
     * @return The {@code boolean} found in the given path or the default value if not
     */
    @Override
    public boolean getBoolean(@NotNull String path, char separator, boolean defValue) {
        return getBoolean(Splitter.on(separator).splitToList(path), false);
    }

    /**
     * This method retrieves the {@code boolean} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code boolean} under the path before
     * the separator is used. If a {@code boolean} is not found in the given path, {@code false} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code boolean} is found
     * @param separator The path separator. When used, the method will look for the {@code boolean}
     *                  under the parent.
     * @return The {@code boolean} found in the given path or {@code false} if otherwise
     */
    @Override
    public boolean getBoolean(@NotNull String path, char separator) {
        return getBoolean(path, separator, false);
    }

    /**
     * This method retrieves the {@code boolean} in a given path with a given default value if the {@code boolean}
     * isn't found. The method only works on retrieving the {@code boolean} in the uppermost key.
     *
     * @param path     The path where the {@code boolean} is found
     * @param defValue If the {@code boolean} is not found, the method will return this value.
     * @return The {@code boolean} found in the given path or the default value if not
     */
    @Override
    public boolean getBoolean(@NotNull String path, boolean defValue) {
        return getBoolean(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code boolean} in a given path with a given default value if the {@code boolean}
     * isn't found. The method only works on retrieving the {@code boolean} in the uppermost key.
     *
     * @param path The path where the {@code boolean} is found
     * @return The {@code byte} found in the given path or {@code false} if otherwise
     */
    @Override
    public boolean getBoolean(@NotNull String path) {
        return getBoolean(Collections.singletonList(path));
    }

    /**
     * This method retrieves the {@code byte} in a {@link List} of paths with the specified
     * default value if the {@code byte} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code byte} found in the given path or the default value if not
     */
    @Override
    public byte getByte(@NotNull List<String> path, byte defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Byte) ? (byte) found : defValue;
    }

    /**
     * This method retrieves the {@code byte} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code byte} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code byte} found in the given path or {@code 0} if the {@code byte}
     * in the given path is not found
     */
    @Override
    public byte getByte(@NotNull List<String> path) {
        return getByte(path, (byte) 0);
    }

    /**
     * This method retrieves the {@code byte} in a given path with a given default value if the {@code byte}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code byte} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code byte} is found
     * @param separator The path separator. When used, the method will look for the {@code byte}
     *                  under the parent.
     * @param defValue  If the {@code byte} is not found, the method will return this value.
     * @return The {@code byte} found in the given path or the default value if not
     */
    @Override
    public byte getByte(@NotNull String path, char separator, byte defValue) {
        return getByte(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code byte} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code byte} under the path before
     * the separator is used. If a {@code byte} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code byte} is found
     * @param separator The path separator. When used, the method will look for the {@code byte}
     *                  under the parent.
     * @return The {@code byte} found in the given path or {@code 0} if otherwise
     */
    @Override
    public byte getByte(@NotNull String path, char separator) {
        return getByte(Splitter.on(separator).splitToList(path), (byte) 0);
    }

    /**
     * This method retrieves the {@code byte} in a given path with a given default value if the {@code byte}
     * isn't found. The method only works on retrieving the {@code byte} in the uppermost key.
     *
     * @param path     The path where the {@code byte} is found
     * @param defValue If the {@code byte} is not found, the method will return this value.
     * @return The {@code byte} found in the given path or the default value if not
     */
    @Override
    public byte getByte(@NotNull String path, byte defValue) {
        return getByte(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code byte} in a given path with a given default value if the {@code byte}
     * isn't found. The method only works on retrieving the {@code byte} in the uppermost key.
     *
     * @param path The path where the {@code byte} is found
     * @return The {@code byte} found in the given path or {@code 0} if otherwise
     */
    @Override
    public byte getByte(@NotNull String path) {
        return getByte(Collections.singletonList(path), (byte) 0);
    }

    /**
     * This method retrieves the {@code short} in a {@link List} of paths with the specified
     * default value if the {@code short} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code short} found in the given path or the default value if not
     */
    @Override
    public short getShort(@NotNull List<String> path, short defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Short) ? (short) found : defValue;
    }

    /**
     * This method retrieves the {@code short} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code short} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code short} found in the given path or {@code 0} if the {@code short}
     * in the given path is not found
     */
    @Override
    public short getShort(@NotNull List<String> path) {
        return getShort(path, (short) 0);
    }

    /**
     * This method retrieves the {@code short} in a given path with a given default value if the {@code short}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code short} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code short} is found
     * @param separator The path separator. When used, the method will look for the {@code short}
     *                  under the parent.
     * @param defValue  If the {@code short} is not found, the method will return this value.
     * @return The {@code short} found in the given path or the default value if not
     */
    @Override
    public short getShort(@NotNull String path, char separator, short defValue) {
        return getShort(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code short} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code short} under the path before
     * the separator is used. If a {@code short} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code short} is found
     * @param separator The path separator. When used, the method will look for the {@code short}
     *                  under the parent.
     * @return The {@code short} found in the given path or {@code 0} if otherwise
     */
    @Override
    public short getShort(@NotNull String path, char separator) {
        return getShort(Splitter.on(separator).splitToList(path), (short) 0);
    }

    /**
     * This method retrieves the {@code short} in a given path with a given default value if the {@code short}
     * isn't found. The method only works on retrieving the {@code short} in the uppermost key.
     *
     * @param path     The path where the {@code short} is found
     * @param defValue If the {@code short} is not found, the method will return this value.
     * @return The {@code short} found in the given path or the default value if not
     */
    @Override
    public short getShort(@NotNull String path, short defValue) {
        return getShort(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code short} in a given path with a given default value if the {@code short}
     * isn't found. The method only works on retrieving the {@code short} in the uppermost key.
     *
     * @param path The path where the {@code short} is found
     * @return The {@code short} found in the given path or {@code 0} if otherwise
     */
    @Override
    public short getShort(@NotNull String path) {
        return getShort(path, (short) 0);
    }

    /**
     * This method retrieves the {@code float} in a {@link List} of paths with the specified
     * default value if the {@code float} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code float} found in the given path or the default value if not
     */
    @Override
    public float getFloat(@NotNull List<String> path, float defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Float) ? (float) found : defValue;
    }

    /**
     * This method retrieves the {@code float} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code float} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code float} found in the given path or {@code 0} if the {@code float}
     * in the given path is not found
     */
    @Override
    public float getFloat(@NotNull List<String> path) {
        return getFloat(path, 0f);
    }

    /**
     * This method retrieves the {@code float} in a given path with a given default value if the {@code float}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code float} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code float} is found
     * @param separator The path separator. When used, the method will look for the {@code float}
     *                  under the parent.
     * @param defValue  If the {@code float} is not found, the method will return this value.
     * @return The {@code float} found in the given path or the default value if not
     */
    @Override
    public float getFloat(@NotNull String path, char separator, float defValue) {
        return getFloat(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code float} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code float} under the path before
     * the separator is used. If a {@code float} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code float} is found
     * @param separator The path separator. When used, the method will look for the {@code float}
     *                  under the parent.
     * @return The {@code float} found in the given path or {@code 0} if otherwise
     */
    @Override
    public float getFloat(@NotNull String path, char separator) {
        return getFloat(Splitter.on(separator).splitToList(path));
    }

    /**
     * This method retrieves the {@code float} in a given path with a given default value if the {@code float}
     * isn't found. The method only works on retrieving the {@code float} in the uppermost key.
     *
     * @param path     The path where the {@code float} is found
     * @param defValue If the {@code float} is not found, the method will return this value.
     * @return The {@code float} found in the given path or the default value if not
     */
    @Override
    public float getFloat(@NotNull String path, float defValue) {
        return getFloat(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code float} in a given path with a given default value if the {@code float}
     * isn't found. The method only works on retrieving the {@code float} in the uppermost key.
     *
     * @param path The path where the {@code float} is found
     * @return The {@code float} found in the given path or {@code 0} if otherwise
     */
    @Override
    public float getFloat(@NotNull String path) {
        return getFloat(path, 0f);
    }

    /**
     * This method retrieves the {@code double} in a {@link List} of paths with the specified
     * default value if the {@code double} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code double} found in the given path or the default value if not
     */
    @Override
    public double getDouble(@NotNull List<String> path, double defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Double) ? (double) found : defValue;
    }

    /**
     * This method retrieves the {@code double} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code double} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code double} found in the given path or {@code 0} if the {@code double}
     * in the given path is not found
     */
    @Override
    public double getDouble(@NotNull List<String> path) {
        return getDouble(path, 0d);
    }

    /**
     * This method retrieves the {@code double} in a given path with a given default value if the {@code double}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code double} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code double} is found
     * @param separator The path separator. When used, the method will look for the {@code double}
     *                  under the parent.
     * @param defValue  If the {@code double} is not found, the method will return this value.
     * @return The {@code double} found in the given path or the default value if not
     */
    @Override
    public double getDouble(@NotNull String path, char separator, double defValue) {
        return getDouble(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code double} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code double} under the path before
     * the separator is used. If a {@code double} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code double} is found
     * @param separator The path separator. When used, the method will look for the {@code double}
     *                  under the parent.
     * @return The {@code double} found in the given path or {@code 0} if otherwise
     */
    @Override
    public double getDouble(@NotNull String path, char separator) {
        return getDouble(path, separator, 0d);
    }

    /**
     * This method retrieves the {@code double} in a given path with a given default value if the {@code double}
     * isn't found. The method only works on retrieving the {@code double} in the uppermost key.
     *
     * @param path     The path where the {@code double} is found
     * @param defValue If the {@code double} is not found, the method will return this value.
     * @return The {@code double} found in the given path or the default value if not
     */
    @Override
    public double getDouble(@NotNull String path, double defValue) {
        return getDouble(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code double} in a given path with a given default value if the {@code double}
     * isn't found. The method only works on retrieving the {@code double} in the uppermost key.
     *
     * @param path The path where the {@code double} is found
     * @return The {@code double} found in the given path or {@code 0} if otherwise
     */
    @Override
    public double getDouble(@NotNull String path) {
        return getDouble(path, 0d);
    }

    /**
     * This method retrieves the {@code int} in a {@link List} of paths with the specified
     * default value if the {@code int} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code int} found in the given path or the default value if not
     */
    @Override
    public int getInt(@NotNull List<String> path, int defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Integer) ? (int) found : defValue;
    }

    /**
     * This method retrieves the {@code int} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code int} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code int} found in the given path or {@code 0} if the {@code int}
     * in the given path is not found
     */
    @Override
    public int getInt(@NotNull List<String> path) {
        return getInt(path, 0);
    }

    /**
     * This method retrieves the {@code int} in a given path with a given default value if the {@code int}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code int} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code int} is found
     * @param separator The path separator. When used, the method will look for the {@code int}
     *                  under the parent.
     * @param defValue  If the {@code int} is not found, the method will return this value.
     * @return The {@code int} found in the given path or the default value if not
     */
    @Override
    public int getInt(@NotNull String path, char separator, int defValue) {
        return getInt(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code int} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code int} under the path before
     * the separator is used. If a {@code int} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code int} is found
     * @param separator The path separator. When used, the method will look for the {@code int}
     *                  under the parent.
     * @return The {@code int} found in the given path or {@code 0} if otherwise
     */
    @Override
    public int getInt(@NotNull String path, char separator) {
        return getInt(path, separator, 0);
    }

    /**
     * This method retrieves the {@code int} in a given path with a given default value if the {@code int}
     * isn't found. The method only works on retrieving the {@code int} in the uppermost key.
     *
     * @param path     The path where the {@code int} is found
     * @param defValue If the {@code int} is not found, the method will return this value.
     * @return The {@code int} found in the given path or the default value if not
     */
    @Override
    public int getInt(@NotNull String path, int defValue) {
        return getInt(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code int} in a given path with a given default value if the {@code int}
     * isn't found. The method only works on retrieving the {@code int} in the uppermost key.
     *
     * @param path The path where the {@code int} is found
     * @return The {@code int} found in the given path or {@code 0} if otherwise
     */
    @Override
    public int getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    /**
     * This method retrieves the {@code long} in a {@link List} of paths with the specified
     * default value if the {@code long} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@code long} found in the given path or the default value if not
     */
    @Override
    public long getLong(@NotNull List<String> path, long defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof Long) ? (long) found : defValue;
    }

    /**
     * This method retrieves the {@code long} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@code long} in the given path is not found, {@code 0} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@code long} found in the given path or {@code 0} if the {@code long}
     * in the given path is not found
     */
    @Override
    public long getLong(@NotNull List<String> path) {
        return getLong(path, 0);
    }

    /**
     * This method retrieves the {@code long} in a given path with a given default value if the {@code long}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@code long} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code long} is found
     * @param separator The path separator. When used, the method will look for the {@code long}
     *                  under the parent.
     * @param defValue  If the {@code long} is not found, the method will return this value.
     * @return The {@code long} found in the given path or the default value if not
     */
    @Override
    public long getLong(@NotNull String path, char separator, long defValue) {
        return getLong(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@code long} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@code long} under the path before
     * the separator is used. If a {@code long} is not found in the given path, {@code 0} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@code long} is found
     * @param separator The path separator. When used, the method will look for the {@code long}
     *                  under the parent.
     * @return The {@code long} found in the given path or {@code 0} if otherwise
     */
    @Override
    public long getLong(@NotNull String path, char separator) {
        return getLong(path, separator, 0);
    }

    /**
     * This method retrieves the {@code long} in a given path with a given default value if the {@code long}
     * isn't found. The method only works on retrieving the {@code long} in the uppermost key.
     *
     * @param path     The path where the {@code long} is found
     * @param defValue If the {@code long} is not found, the method will return this value.
     * @return The {@code long} found in the given path or the default value if not
     */
    @Override
    public long getLong(@NotNull String path, long defValue) {
        return getLong(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@code long} in a given path with a given default value if the {@code long}
     * isn't found. The method only works on retrieving the {@code long} in the uppermost key.
     *
     * @param path The path where the {@code long} is found
     * @return The {@code long} found in the given path or {@code 0} if otherwise
     */
    @Override
    public long getLong(@NotNull String path) {
        return getLong(path, 0);
    }

    /**
     * This method retrieves the {@link List} in a {@link List} of paths with the specified
     * default value if the {@link List} in the path didn't exist. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     *
     * @param path     The path to the value. Every index is a child of the previous index
     *                 except at index {@code 0}
     * @param defValue The default value if the value in the given path doesn't exist
     * @return The {@link List} found in the given path or the default value if not
     */
    @Override
    public List getList(@NotNull List<String> path, List defValue) {
        Object found = getObject(path, defValue);
        return (found instanceof List) ? (List) found : defValue;
    }

    /**
     * This method retrieves the {@link List} in a {@link List} of paths. Every index in the
     * list represents a parent that has one or more children excluding the last index.
     * If the {@link List} in the given path is not found, {@code null} is returned.
     *
     * @param path The path to the value. Every index is a child of the previous index
     *             except at index {@code 0}
     * @return The {@link List} found in the given path or {@code null} if the {@link List}
     * in the given path is not found
     */
    @Override
    public List getList(@NotNull List<String> path) {
        return getList(path, null);
    }

    /**
     * This method retrieves the {@link List} in a given path with a given default value if the {@link List}
     * isn't found. The separator is a {@code char} when used, it is a descent, and therefore, the method
     * will look for the {@link List} under the path before the separator is used.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link List} is found
     * @param separator The path separator. When used, the method will look for the {@link List}
     *                  under the parent.
     * @param defValue  If the {@link List} is not found, the method will return this value.
     * @return The {@link List} found in the given path or the default value if not
     */
    @Override
    public List getList(@NotNull String path, char separator, List defValue) {
        return getList(Splitter.on(separator).splitToList(path), defValue);
    }

    /**
     * This method retrieves the {@link List} in a given path. The separator is a {@code char} when used,
     * it is a descent, and therefore, the method will look for the {@link List} under the path before
     * the separator is used. If an {@link List} is not found in the given path, {@code null} is returned.
     * <p>
     * For example, trying to retrieve the value of "player.stats.wins" is equal to calling
     * {@link #getObject(List)} with a {@link List} with 3 indexes, namely player, stats, and wins,
     * in its parameter.
     *
     * @param path      The path where the {@link List} is found
     * @param separator The path separator. When used, the method will look for the {@link List}
     *                  under the parent.
     * @return The {@link List} found in the given path or {@code null} if otherwise
     */
    @Override
    public List getList(@NotNull String path, char separator) {
        return getList(path, separator, null);
    }

    /**
     * This method retrieves the {@link List} in a given path with a given default value if the {@link List}
     * isn't found. The method only works on retrieving the {@link List} in the uppermost key.
     *
     * @param path     The path where the {@link List} is found
     * @param defValue If the {@link List} is not found, the method will return this value.
     * @return The {@link List} found in the given path or the default value if not
     */
    @Override
    public List getList(@NotNull String path, List defValue) {
        return getList(Collections.singletonList(path), defValue);
    }

    /**
     * This method retrieves the {@link List} in a given path or {@code null} if otherwise.
     * The method only works on retrieving the {@link List} in the uppermost key.
     *
     * @param path The path where the {@link List} is found
     * @return The {@link List} found in the given path or {@code null} if otherwise
     */
    @Override
    public List getList(@NotNull String path) {
        return getList(path, null);
    }

    /**
     * Deserializes the whole {@link Configuration} into an object of a similar type
     * as to what has been passed in. Calling this method is equivalent to calling
     * {@link FusionYAML#deserialize(YamlElement, Type)}
     *
     * @param type The type
     * @return The deserialized object of type {@link T}
     * @throws YamlDeserializationException Thrown if an error occurred while deserializing
     */
    @Override
    public <T> T toObject(Type type) {
        return fusionYAML.deserialize(object, type);
    }

    /**
     * Deserializes the value found in the path passed in. The value will be extracted
     * and then an appropriate deserializer will be fetched.
     *
     * @param path The path to the object
     * @param type The type of the object
     * @return The deserialized object of type {@link T}
     * @throws YamlDeserializationException Thrown if an error occurred while deserializing
     */
    @Override
    public <T> T toObject(String path, Type type) {
        return toObject(Collections.singletonList(path), type);
    }

    /**
     * Deserializes the value found in the path. The seperator serves as the splitter
     * where each occurrence will cause the configuration to search for the value nested
     * under the path before the occurrence.
     * <p>
     * For example:
     * {@code
     * key:
     * nested1:
     * nested2: 8
     * }
     * To retrieve the value in nested2:
     * {@code
     * Integer integer = toObject("key.nested1.nested2", '.', Integer.class);
     * }
     *
     * @param path      The path
     * @param seperator The seperator
     * @param type      The type of the object
     * @return The deserialized object of type {@link T}
     * @throws YamlDeserializationException Thrown if an error occurred while deserializing
     */
    @Override
    public <T> T toObject(String path, char seperator, Type type) {
        return toObject(Splitter.on(seperator).splitToList(path), type);
    }

    /**
     * Deserializes the values found in a {@link List} of paths where each index
     * represents a shift to the value of the nested path under the previous index.
     *
     * @param path The path
     * @param type The type of the object
     * @return The deserialized object of type {@link T}
     * @throws YamlDeserializationException Thrown if an error occurred while
     *                                      deserializing
     */
    @Override
    public <T> T toObject(List<String> path, Type type) {
        Object obj =
                YamlUtils.getObject(object.getMap(), path, new HashMap(), path.get(0), true, 0);
        return fusionYAML.deserialize((YamlElement) obj, type);
    }

    /**
     * Deserializes the values found in an array of strings where each index represents
     * a shift to the value of the nested path under the previous index.
     *
     * @param path The path
     * @param type The type of the object
     * @return The deserialized object of type {@link T}
     * @throws YamlDeserializationException Thrown if an error occurred while
     *                                      deserializign
     */
    @Override
    public <T> T toObject(String[] path, Type type) {
        return toObject(Arrays.asList(path), type);
    }

    /**
     * @return The default {@link DumperOptions} with tailored options
     */
    private static DumperOptions defOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    @Override
    public String toString() {
        return object.toString();
    }

}