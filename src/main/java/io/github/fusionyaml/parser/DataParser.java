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
package io.github.fusionyaml.parser;

import com.google.gson.Gson;
import io.github.fusionyaml.exceptions.YamlParseFailedException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.utils.FileUtils;
import io.github.fusionyaml.utils.StorageUtils;
import io.github.fusionyaml.utils.URLUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Retrieves data from the mapped data
 */
public abstract class DataParser extends YamlParser {

    /**
     * This constructor requires a raw YAML data which will be used
     * while mapping and parsing.
     *
     * @param raw The raw YAML data
     */
    public DataParser(@NotNull String raw) {
        super(raw);
    }

    /**
     * This constructor requires a {@link File} which will get its
     * raw YAML contents extracted from. The contents will then by used
     * while mapping and parsing.
     *
     * @param file The file to get its raw YAML contents extracted from
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public DataParser(@NotNull File file) throws IOException {
        this(FileUtils.readToString(file));
    }

    /**
     * This constructor requires a {@link URL} which will get its
     * raw YAML contents extracted from. The contents will then by used
     * while mapping and parsing.
     *
     * @param url The {@link URL} to get its raw YAML contents extracted from
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public DataParser(@NotNull URL url) throws IOException {
        this(URLUtils.readURLToString(url));
    }

    /**
     * This constructor requires a {@link Map} and a {@link YamlType}. Since the
     * value passed in is a map, the contents won't be mapped. Hence, calling {@link #map()}
     * will return the {@link Map} you passed in.
     *
     * @param map  The map that houses YAML contents
     * @param type The type of the YAML
     */
    public DataParser(@NotNull Map<String, Object> map, YamlType type) {
        super(map, type);
    }

    /**
     * This constructor requires a {@link Map} that houses the YAML contents. Since the
     * value passed in is a map, the contents won't be mapped. Hence, calling {@link #map()}
     * will return the {@link Map} you passed in.
     *
     * @param map The map that houses YAML contents
     */
    public DataParser(@NotNull Map<String, Object> map) {
        super(map);
    }

    /**
     * Maps the raw (yaml) content into a form of a {@link Map}. If you initialized this class
     * using {@link #DataParser(Map)}, you don't need to call this method. Calling this method
     * will return the map passed in.
     * <p>
     * {@link YamlParseFailedException} will be thrown if an error occurred
     * while mapping. Invalid yaml may be the cause of the exception
     *
     * @return A map containing yaml data ({@link #raw})
     * @throws YamlParseFailedException If an error occurred while mapping
     * @see #reload(File)
     * @see #reload(URL)
     */
    @Override
    public abstract Map<String, Object> map() throws YamlParseFailedException;

    /**
     * Converts the data written in yaml syntax into json.
     *
     * @return Data written in json's syntax
     */
    @Override
    public @NotNull String toJson() {
        Gson gson = new Gson();
        return gson.toJson(getMap());
    }

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
    @Override
    public @Nullable Object getObject(@NotNull String path, char dirSeparator) {
        if (data == null)
            return null;
        if (path.startsWith(".") || path.endsWith("."))
            return null;
        List<String> paths = StorageUtils.toList(path, dirSeparator);
        return getObject(paths);
    }

    /**
     * Retrieves an object from a given path. The method requires a {@link List}. Every
     * index in a list indicates a descent.
     * <p>
     *
     * @param path The path where the object is located
     * @return The {@link Object} found, or {@code null} otherwise.
     */
    @Override
    public @Nullable Object getObject(@NotNull List<String> path) {
        if (data == null)
            return null;
        if (path.size() == 0)
            return null;
        return YamlUtils.getObject(data, path, new HashMap(), path.get(0), true, 0);
    }

    /**
     * Retrieves an object from a given path. The method requires an array of strings.
     * Every index in the array indicates a descent.
     *
     * @param path The path where the object is located
     * @return The {@link Object} found, or null otherwise.
     */
    @Override
    public @Nullable Object getObject(@NotNull String[] path) {
        if (data == null)
            return null;
        return getObject(new LinkedList<>(Arrays.asList(path)));
    }

    /**
     * Retrieves a {@link YamlElement} from a given path. The method requires a {@code character} which
     * serves as a separator when used in the {@code path}. Using a separator targets elements
     * in nested paths. For example, the path player.games.tetris using a dot as a separator:
     * <pre>
     *     player:
     *       games:
     *         tetris: 3
     * </pre>
     *
     * @param path The path where the element is found
     * @param dirSeparator The path separator. When applied, the value will be searched deeper
     *                     into the path.
     * @return The {@link YamlElement} found, or {@code null} otherwise
     */
    @Override
    public @Nullable YamlElement getElement(@NotNull String path, char dirSeparator) {
        return getElement(StorageUtils.toList(path, dirSeparator));
    }

    /**
     * Retrieves a {@link YamlElement} from a given path. Every index of the {@link List}
     * required in the parameter is a child under the string mentioned before the
     * index in the {@link List} excluding {@code 0}.
     *
     * @param path The path where the object is located
     * @return The {@link YamlElement} found, or {@code null} otherwise
     */
    @Override
    public @Nullable YamlElement getElement(@NotNull List<String> path) {
        if (path.size() == 0) return null;
        Object o = getObject(path);
        return YamlUtils.toElement(o);
    }

    /**
     * Retrieves a {@link YamlElement} from a given path. Every index of the
     * {@code String} array is a child under the string mentioned before the
     * index excluding {@code 0}.
     *
     * @param path The path where the object is found
     * @return The {@link YamlElement} found, or {@code null} otherwise
     */
    @Override
    public @Nullable YamlElement getElement(@NotNull String[] path) {
        return getElement(new LinkedList<>(Arrays.asList(path)));
    }

}