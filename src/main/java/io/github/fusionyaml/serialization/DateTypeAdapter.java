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
package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);;

    private final FusionYAML yaml;
    private final Calendar CALENDAR = Calendar.getInstance();

    public DateTypeAdapter(FusionYAML yaml) {
        this.yaml = yaml;
    }

    public DateTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public Date deserialize(@NotNull YamlElement serialized) {
        if (!serialized.isYamlPrimitive())
            throw new YamlDeserializationException("The " + YamlElement.class + "passed in is not a serialized form of " + TimeZone.class);
        String val = serialized.getAsYamlPrimitive().getAsString();
        TimeZone zone = yaml.getDumperOptions().getTimeZone();
        CALENDAR.setTimeZone(zone);
        try {
            return sdf.parse(val);
        } catch (ParseException e) {
            throw new YamlSerializationException(e);
        }
    }

    @Override
    public YamlElement serialize(@NotNull Date obj) {
        TimeZone zone = yaml.getDumperOptions().getTimeZone();
        CALENDAR.setTimeZone(zone);
        return new YamlPrimitive(CALENDAR.getTime().toString());
    }
}
