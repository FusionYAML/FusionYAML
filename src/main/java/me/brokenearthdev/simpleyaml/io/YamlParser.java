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
package me.brokenearthdev.simpleyaml.io;

import me.brokenearthdev.simpleyaml.object.*;
import me.brokenearthdev.simpleyaml.utils.URLUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * This class is responsible for parsing YAML and converting raw YAML data
 * into a map which can be also used to convert the raw YAML data to JSON using
 * {@link #toJson()}
 */
public abstract class YamlParser {

    /**
     * The raw yaml content
     */
    protected String raw;

    /**
     * The yaml raw contents as a {@link Map}
     */
    protected Map<String, Object> data;

    /**
     * This constructor requires a raw YAML data which will be used
     * while mapping and parsing.
     *
     * @param raw The raw YAML data
     */
    public YamlParser(@NotNull String raw) {
        this.raw = raw;
    }

    /**
     * This constructor requires a {@link File} which will get its
     * raw YAML contents extracted from. The contents will then by used
     * while mapping and parsing.
     *
     * @param file The file to get its raw YAML contents extracted from
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public YamlParser(@NotNull File file) throws IOException {
        this(FileUtils.readFileToString(file, Charset.defaultCharset()));
    }

    /**
     * This constructor requires a {@link URL} which will get its
     * raw YAML contents extracted from. The contents will then by used
     * while mapping and parsing.
     *
     * @param url The {@link URL} to get its raw YAML contents extracted from
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public YamlParser(@NotNull URL url) throws IOException {
        this(URLUtils.readURLToString(url));
    }

    /**
     * This constructor requires a {@link Map} that houses the YAML contents. Since the
     * value passed in is a map, the contents won't be mapped. Hence, calling {@link #map()}
     * will return the {@link Map} you passed in.
     *
     * @param map The map that houses YAML contents
     */
    public YamlParser(@NotNull Map<String, Object> map) {
        data = map;
    }

    /**
     * Maps the raw (yaml) content into a form of an object. If you initialized this class
     * using {@link #YamlParser(Map)}, you don't need to call this method. Calling this method
     * will return the map passed in.
     *
     * {@link org.yaml.snakeyaml.error.YAMLException} will be thrown if an error occurred
     * while mapping. Invalid yaml may be the cause of the exception
     *
     * @return A map containing yaml data ({@link #raw})
     * @throws org.yaml.snakeyaml.error.YAMLException If an error occurred while mapping
     * @see #reload(File)
     * @see #reload(URL)
     */
    @Nullable
    public abstract Map<String, Object> map();

    /**
     * Converts the data written in yaml syntax into json.
     *
     * @return Data written in json's syntax
     */
    @NotNull
    public abstract String toJson();

    /**
     * Retrieves an object from a given path. The method requires a {@code char} which
     * serves as a separator if there are nested paths. For example, if the character
     * is set to a dot (.), then to retrieve the following, the path will be human.gender
     * <pre>
     *     human:
     *       gender: 'male'
     * </pre>
     * <p>
     *
     * @param path The path where the object is located
     * @param dirSeparator The path separator. Applying it will signal a descent.
     * @return The {@link Object} found, or {@code null} otherwise.
     */
    @Nullable
    public abstract Object getObject(@NotNull String path, char dirSeparator);

    /**
     * Retrieves an object from a given path. The method requires a {@link List}. Every
     * index in a list signals a descent.
     * <p>
     *
     * @param path The path where the object is located
     * @return The {@link Object} found, or {@code null} otherwise.
     */
    @Nullable
    public abstract Object getObject(@NotNull List<String> path);

    /**
     * Retrieves an object from a given path. The method requires an array of strings.
     * Every index in the array signals a descent.
     *
     * @param path The path where the object is located
     * @return The {@link Object} found, or null otherwise.
     */
    @Nullable
    public abstract Object getObject(@NotNull String[] path);

    @Nullable
    public abstract YamlElement getElement(@NotNull String path, char dirSeparator);

    @Nullable
    public abstract YamlElement getElement(@NotNull List<String> path);

    @Nullable
    public abstract YamlElement getElement(@NotNull String[] path);

    /**
     * Reloads by changing the raw contents set up during initialization to
     * the new contents found in the {@link File} in the parameter.
     *
     * @param file The {@link File} that will get its contents retrieved
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public void reload(@NotNull File file) throws IOException {
        raw = FileUtils.readFileToString(file, Charset.defaultCharset());
        map();
    }

    /**
     * Reloads by changing the raw contents set up during initialization to
     * the new contents found in the {@link URL} in the parameter.
     * <p>
     * Any changes
     *
     * @param url The {@link URL} that will get its contents retrieved
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public void reload(@NotNull URL url) throws IOException {
        raw = URLUtils.readURLToString(url);
        map();
    }

    public void setDataMap(Map<String, Object> map) {
        this.data = map;
    }

    /**
     * @return The yaml raw contents
     */
    public String getRawContents() {
        return raw;
    }

    /**
     * @return The yaml raw contents as a {@link Map}.
     * <p>
     * Please note that this method returns {@link #data}, which is a
     * map of objects and not a map of String with values of type {@link YamlElement}
     */
    public Map<?, ?> getMap() {
        return data;
    }

}