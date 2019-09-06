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
package me.brokenearthdev.fusionyaml.parser;

import me.brokenearthdev.fusionyaml.exceptions.YamlParseFailedException;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Built-in parser that parses both normal YAML and list YAML
 */
public class DefaultParser extends DataParser {

    /**
     * This constructor requires a raw YAML data which will be used
     * while mapping and parsing.
     *
     * @param raw The raw YAML data
     */
    public DefaultParser(@NotNull String raw) {
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
    public DefaultParser(@NotNull File file) throws IOException {
        super(file);
    }

    /**
     * This constructor requires a {@link URL} which will get its
     * raw YAML contents extracted from. The contents will then by used
     * while mapping and parsing.
     *
     * @param url The {@link URL} to get its raw YAML contents extracted from
     * @throws IOException Any IO errors will cause an {@link IOException} to be thrown
     */
    public DefaultParser(@NotNull URL url) throws IOException {
        super(url);
    }

    /**
     * This constructor requires a {@link Map} and a {@link YamlType}. Since the
     * value passed in is a map, the contents won't be mapped. Hence, calling {@link #map()}
     * will return the {@link Map} you passed in.
     *
     * @param map  The map that houses YAML contents
     * @param type The type of the YAML
     */
    public DefaultParser(@NotNull Map<String, Object> map, YamlType type) {
        super(map, type);
    }

    /**
     * This constructor requires a {@link Map} that houses the YAML contents. Since the
     * value passed in is a map, the contents won't be mapped. Hence, calling {@link #map()}
     * will return the {@link Map} you passed in.
     *
     * @param map The map that houses YAML contents
     */
    public DefaultParser(@NotNull Map<String, Object> map) {
        super(map);
    }

    /**
     * Maps the raw (yaml) content into a form of a {@link Map}. If you initialized this class
     * using {@link #DefaultParser(Map)}, you don't need to call this method. Calling this method
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
    public Map<String, Object> map() throws YamlParseFailedException {
        if (raw == null)
            return data;
        try {
            MapParser parser = new MapParser(raw);
            Map<String, Object> map = parser.map();
            type = YamlType.MAP;
            return map;
        } catch (Exception e) {
            if (YamlUtils.mapConstructorException(e)) {
                Map<String, Object> map = new ListParser(raw).map();
                type = YamlType.LIST;
                return map;
            }
            throw new YamlParseFailedException(e);
        }
    }

}
