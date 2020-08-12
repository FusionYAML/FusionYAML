package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlDeserializationException;
import org.fusionyaml.library.exceptions.YamlSerializationException;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    private final Calendar CALENDAR = Calendar.getInstance();
    private final FusionYAML fusionYAML;


    /**
     * @param yaml The {@link FusionYAML} object, which will be used to retrieve the {@link org.yaml.snakeyaml.DumperOptions}
     */
    public DateTypeAdapter(FusionYAML yaml) {
        fusionYAML = yaml;
    }


    @Override
    public YamlElement serialize(@NotNull Date obj, Type type) {
        TimeZone zone = fusionYAML.getYamlOptions().getTimeZone();
        CALENDAR.setTimeZone(zone);
        return new YamlPrimitive(CALENDAR.getTime().toString());
    }

    @Override
    public Date deserialize(@NotNull YamlElement serialized, Type type) {
        if (!serialized.isYamlPrimitive())
            throw new YamlDeserializationException("The " + YamlElement.class + "passed in is not a serialized form of " + TimeZone.class);
        String val = serialized.getAsYamlPrimitive().getAsString();
        TimeZone zone = fusionYAML.getYamlOptions().getTimeZone();
        CALENDAR.setTimeZone(zone);
        try {
            return sdf.parse(val);
        } catch (ParseException e) {
            throw new YamlSerializationException(e);
        }
    }

}
