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
package me.brokenearthdev.fusionyaml.configurations;

import me.brokenearthdev.fusionyaml.YamlException;
import me.brokenearthdev.fusionyaml.DefaultParser;
import me.brokenearthdev.fusionyaml.YamlParser;
import me.brokenearthdev.fusionyaml.object.YamlObject;
import me.brokenearthdev.fusionyaml.utils.URLUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Reads data from a given {@link URL}, copies it into a {@link YamlObject}, and allows you to
 * modify data. However, the modified data will do no effect on the file the given {@link URL}
 * leads to.
 * <p>
 * For example, you created a new instance. You then called {@link #set(String, Object)}
 * and added data. If you checked the file on the {@link URL}, there won't be a change.
 * Instead, you would notice a change in the {@link YamlObject} object in the class, retrievable
 * by calling {@link #getContents()}
 */
public class WebConfiguration extends YamlConfiguration {

    /**
     * The {@link URL} to the file
     */
    private URL url;

    /**
     * This constructor requires a {@link URL} object. The {@link URL} should lead to a raw file
     * containing data expressed in a {@code yaml} syntax.
     * <p>
     * When a {@link URL} object is passed, the contents will be copied into a {@link Map}, which
     * will then be used to convert the data to a {@link YamlObject} object.
     *
     * @param url A {@link URL} that leads to a raw file
     * @throws IOException If any IO error occurred
     * @throws YamlException If there is an error while converting the data into a map,
     * which will then be used to convert it to a {@link YamlObject} instance.
     * @see #WebConfiguration(String)
     */
    public WebConfiguration(URL url) throws IOException, YamlException {
        this.url = url;
        synchronize();
    }

    /**
     * This constructor requires a URL. The URL should lead to a raw file containing data expressed in
     * a {@code yaml} syntax.
     * <p>
     * When a URL is passed, the contents will be copied into a {@link Map}, which will then be used to
     * convert the data to a {@link YamlObject} object.
     *
     * @param url A URL that leads to a raw file
     * @throws IOException If any IO error occurred
     * @throws YamlException If there is an error while converting the data into a map,
     * which will then be used to convert it to a {@link YamlObject} instance.
     * @see #WebConfiguration(String)
     */
    public WebConfiguration(String url) throws IOException, YamlException {
        this(new URL(url));
    }

    /**
     * Synchronizes, or updates, data by re-downloading and re-parsing data in the {@link URL}
     * initially passed in.
     *
     * @throws IOException If any IO error occurred
     * @throws YamlException If there is an error while converting the data into a map,
     * which will then be used to convert it to a {@link YamlObject} instance.
     */
    public void synchronize() throws IOException, YamlException {
        String data = URLUtils.readURLToString(url);
        YamlParser parser = new DefaultParser(data);
        Map<String, Object> parsed = parser.map();
        if (parsed == null)
            throw new YamlException("parsed map is null");
        object = new YamlObject(YamlUtils.toMap(parsed));
    }

    /**
     * @return The {@link URL} initially passed into the constructor
     */
    public URL getURL() {
        return url;
    }

}
