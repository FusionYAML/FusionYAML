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
package org.fusionyaml.library.configurations;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlException;
import org.fusionyaml.library.io.DocumentReader;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.io.IOException;
import java.io.InputStreamReader;
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
 * by calling {@link #toYamlObject()}
 */
public class WebConfiguration extends YamlConfiguration {

    /**
     * The {@link URL} to the file
     */
    private final URL url;

    /**
     * This constructor requires a {@link URL} object. The {@link URL} should lead to a raw file
     * containing data expressed in a {@code yaml} syntax.
     * <p>
     * When a {@link URL} object is passed, the contents will be copied into a {@link Map}, which
     * will then be used to convert the data to a {@link YamlObject} object.
     *
     * @param url  A {@link URL} that leads to a raw file
     * @param yaml A {@link FusionYAML} object
     * @throws YamlException If there is an error while converting the data into a map,
     *                       which will then be used to convert it to a {@link YamlObject} instance.
     * @see #WebConfiguration(String)
     */
    public WebConfiguration(URL url, FusionYAML yaml) throws YamlException, IOException {
        super(yaml);
        this.url = url;
        reload();
    }
    
    public WebConfiguration(URL url) throws YamlException, IOException {
        this(url, new FusionYAML());
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

    public WebConfiguration(String url, FusionYAML yaml) throws IOException {
        this(new URL(url), yaml);
    }

    public void reload() throws IOException {
        try (DocumentReader reader = new DocumentReader(new InputStreamReader(url.openStream()))) {
            YamlElement element = reader.readDocument();
            if (element.isYamlNull())
                object = new YamlObject();
            else this.object = element.getAsYamlObject();
        }
    }

    /**
     * @return The {@link URL} initially passed into the constructor
     */
    public URL getURL() {
        return url;
    }

}
