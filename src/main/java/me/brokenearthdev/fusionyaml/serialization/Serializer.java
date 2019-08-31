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
package me.brokenearthdev.fusionyaml.serialization;

import java.util.*;

public abstract class Serializer {

    public abstract Object serialize(Object o) throws IllegalAccessException;

    public Map<String, Object> serialize(String varName, Object o) throws IllegalAccessException {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(varName, serialize(o));
        return linkedHashMap;
    }

    public List<Object> serializeAll(List<Object> objects) throws IllegalAccessException {
        return serializeAll((Collection<Object>) objects);
    }

    public List<Object> serializeAll(Collection<Object> objects) throws IllegalAccessException {
        List<Object> serialized = new LinkedList<>();
        for (Object object : objects) {
            serialize(object);
        }
        return serialized;
    }

    public List<Object> serializeAll(Object[] objects) throws IllegalAccessException {
        LinkedList<Object> serialized = new LinkedList<>();
        for (Object o : objects)
            serialized.add(serialize(o));
        return serialized;
    }

    public Map<String, Object> serializeAll(LinkedList<String> varNames, LinkedList<Object> objects) throws IllegalAccessException {
        if (varNames.size() != objects.size())
            throw new IllegalArgumentException("The lists' sizes passed into the parameter is not equal");
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        LinkedList<Object> serialized = (LinkedList<Object>) serializeAll(objects);
        for (int i = 0; i < varNames.size(); i++)
            map.put(varNames.get(i), serialized.get(i));
        return map;
    }

    public Map<String, Object> serializeAll(String[] varNames, Object[] objects) throws IllegalAccessException {
        if (varNames.length != objects.length)
            throw new IllegalArgumentException("The arrays' lengths aren't equal");
        LinkedList<String> vars = new LinkedList<>();
        LinkedList<Object> obj = new LinkedList<>();
        for (int i = 0; i < objects.length; i++) {
            vars.add(varNames[i]);
            obj.add(objects[i]);
        }
        return serializeAll(vars, obj);
    }


}
