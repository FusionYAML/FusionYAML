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
package com.github.fusionyaml.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Class not intended for public usage
 */
public class URLUtils {

    public static String readURLToString(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();
        return readInputStreamToString(stream);
    }

    public static String readInputStreamToString(InputStream stream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder contents = new StringBuilder();
        boolean first = true;
        while ((line = reader.readLine()) != null) {
            if (first)
                first = false;
            else contents.append("\n");
            contents.append(line);
        }
        reader.close();
        return contents.toString();
    }

    public static void readURLToFile(URL url, File target) throws IOException {
        FileUtils.writeStringToFile(target, readURLToString(url), Charset.defaultCharset());
    }

}
