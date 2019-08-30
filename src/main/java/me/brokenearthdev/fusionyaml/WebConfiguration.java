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
package me.brokenearthdev.fusionyaml;

import me.brokenearthdev.fusionyaml.YamlConfiguration;
import me.brokenearthdev.fusionyaml.error.YamlException;
import me.brokenearthdev.fusionyaml.io.DefaultParser;
import me.brokenearthdev.fusionyaml.io.YamlParser;
import me.brokenearthdev.fusionyaml.object.YamlObject;
import me.brokenearthdev.fusionyaml.utils.URLUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class WebConfiguration extends YamlConfiguration {

    private URL url;

    public WebConfiguration(URL url) throws IOException, YamlException {
        synchronize();
    }

    public void synchronize() throws IOException, YamlException {
        object = getYamlObj(url);
    }

    public URL getURL() {
        return url;
    }

    private static YamlObject getYamlObj(URL url) throws IOException, YamlException {
        String data = URLUtils.readURLToString(url);
        YamlParser parser = new DefaultParser(data);
        Map<String, Object> parsed = parser.map();
        if (parsed == null)
            throw new YamlException("parsed map is null");
        return new YamlObject(YamlUtils.toMap(parsed));
    }

}
