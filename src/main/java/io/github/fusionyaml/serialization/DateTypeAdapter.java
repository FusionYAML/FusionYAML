package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);;

    private final Calendar CALENDAR = Calendar.getInstance();

    /**
     * @param yaml The {@link FusionYAML} object, which will be used to retrieve the {@link org.yaml.snakeyaml.DumperOptions}
     */
    public DateTypeAdapter(FusionYAML yaml) {
        super(yaml);
    }

    public DateTypeAdapter() {
        this(new FusionYAML());
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
