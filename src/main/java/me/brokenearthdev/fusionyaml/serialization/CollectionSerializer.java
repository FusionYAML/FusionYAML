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

import me.brokenearthdev.fusionyaml.utils.StorageUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CollectionSerializer extends ObjectSerializer {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> serialize(Object o) throws IllegalAccessException {
        if (!(o instanceof Collection))
            throw new IllegalArgumentException("The value passed in is not an instance of " + Collection.class.getName());
        Collection collection = (Collection) o;
        Collection serialized = new LinkedList();
        for (Object e : collection) {
            serialized.add(new ObjectSerializer().serialize(e));
        }
        return serialized;
    }

}
