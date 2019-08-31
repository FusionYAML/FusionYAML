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

import me.brokenearthdev.fusionyaml.utils.YamlUtils;

public class PrimitiveSerializer extends ObjectSerializer {

    @Override
    public Object serialize(Object o) {
        if (!YamlUtils.isPrimitive(o))
            throw new IllegalArgumentException("The value passed in is not a primitive");
        return o;
    }

}
