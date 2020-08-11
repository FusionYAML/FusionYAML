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
package org.fusionyaml.library.utils;

import org.fusionyaml.library.object.YamlObject;

import java.util.List;
import java.util.Map;

/**
 * Class not intended for public usage
 */
public class YamlUtils {

    public static Object getObject(Map<?, ?> init, List<String> paths, Map newMap, String currentPath, boolean first, int loops) {
        if (paths.size() == 1)
            return init.get(paths.get(0));
        Map object = (first) ? init : newMap;
        if (object == null) return null;
        if (currentPath.equals(paths.get(paths.size() - 1))) {
            Object o = object.get(currentPath);
            return o;
        }
        for (Object o : object.keySet()) {
            if (!o.equals(currentPath)) continue;
            if (object.get(o) instanceof Map) {
                Map objMap = (Map) object.get(o);
                return getObject(init, paths, objMap, paths.get(loops + 1), false, loops + 1);
            }
        }
        return null;
    }

    public static Object getObjectInYamlObject(YamlObject init, List<String> paths, YamlObject newMap, String currentPath, boolean first, int loops) {
        if (paths.size() == 1)
            return init.get(paths.get(0));
        YamlObject object = (first) ? init : newMap;
        if (object == null) return null;
        if (currentPath.equals(paths.get(paths.size() - 1))) {
            Object o = object.get(currentPath);
            return o;
        }
        for (Object o : object.keySet()) {
            if (!o.equals(currentPath)) continue;
            if (object.get(o.toString()) instanceof YamlObject) {
                YamlObject objMap = (YamlObject) object.get(o.toString());
                return getObjectInYamlObject(init, paths, objMap, paths.get(loops + 1), false, loops + 1);
            }
        }
        return null;
    }

}
