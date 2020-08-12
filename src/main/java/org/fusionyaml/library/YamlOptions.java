package org.fusionyaml.library;

import org.fusionyaml.library.serialization.Expose;
import org.yaml.snakeyaml.DumperOptions;

import java.util.TimeZone;

/**
 * A wrapper for SnakeYAML's {@link org.yaml.snakeyaml.DumperOptions} class, containing
 * a few more options than the {@link org.yaml.snakeyaml.DumperOptions} class
 */
public class YamlOptions {

    private final DumperOptions options;
    private boolean onlyExposed = false;
    private boolean mentionEnumName = false;
    private boolean excludeNullValues = false;
    private boolean canonical = false;
    private boolean allowUnicode = true;
    private boolean prettyFlow = false;
    private boolean splitLines = true;
    private int indent = 2;
    private int width = 80;
    private int maxKeyLength = 128;
    private TimeZone timeZone = TimeZone.getDefault();
    private DumperOptions.ScalarStyle scalarStyle = DumperOptions.ScalarStyle.PLAIN;
    private DumperOptions.Version version = null;
    private DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.UNIX;
    private DumperOptions.NonPrintableStyle nonPrintableStyle = DumperOptions.NonPrintableStyle.BINARY;
    private DumperOptions.FlowStyle flowStyle = DumperOptions.FlowStyle.BLOCK;

    public YamlOptions() {
        options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }


    public boolean isOnlyExposed() {
        return onlyExposed;
    }

    public boolean onlyEnumNameMentioned() {
        return mentionEnumName;
    }

    public boolean isCanonical() {
        return canonical;
    }

    public boolean isAllowUnicode() {
        return allowUnicode;
    }

    public boolean isPrettyFlow() {
        return prettyFlow;
    }

    public boolean isSplitLines() {
        return splitLines;
    }

    public boolean isExcludeNullVals() {
        return excludeNullValues;
    }

    public int getIndent() {
        return indent;
    }

    public int getWidth() {
        return width;
    }

    public int getMaxKeyLength() {
        return maxKeyLength;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public DumperOptions.ScalarStyle getScalarStyle() {
        return scalarStyle;
    }

    public DumperOptions.Version getVersion() {
        return version;
    }

    public DumperOptions.LineBreak getLineBreak() {
        return lineBreak;
    }

    public DumperOptions.NonPrintableStyle getNonPrintableStyle() {
        return nonPrintableStyle;
    }

    public DumperOptions.FlowStyle getFlowStyle() {
        return flowStyle;
    }

    DumperOptions dumperOptions() {
        return options;
    }

    public static class Builder {

        private final YamlOptions options = new YamlOptions();

        /**
         * @param onlyExposed If set to true, FusionYAML will serialize or deserialize only with
         *                    fields with the {@link Expose} annotation
         * @return this object
         */
        public Builder setOnlyExposed(boolean onlyExposed) {
            options.onlyExposed = onlyExposed;
            return this;
        }

        /**
         * @param mentionEnumName If set to true, the EnumTypeAdapter will only mention enum's name and
         *                        not the path if otherwise.
         * @return this object
         */
        public Builder setMentionEnumName(boolean mentionEnumName) {
            options.mentionEnumName = mentionEnumName;
            return this;
        }

        /**
         * @param excludeNullValues If set to true, {@code null} values will be included.
         * @return this object
         */
        public Builder setExcludeNullValues(boolean excludeNullValues) {
            options.excludeNullValues = excludeNullValues;
            return this;
        }

        /**
         * @param canonical If set to true, a canonical YAML document will be produced instead
         * @return this object
         */
        public Builder setCanonical(boolean canonical) {
            options.canonical = canonical;
            options.options.setCanonical(canonical);
            return this;
        }

        /**
         * @param allowUnicode If set to true, unicode characters will be allowed
         * @return this object
         */
        public Builder setAllowUnicode(boolean allowUnicode) {
            options.allowUnicode = allowUnicode;
            options.options.setAllowUnicode(allowUnicode);
            return this;
        }

        /**
         * @param prettyFlow If set to true, the YAML dumper will be forced to produce a pretty
         *                   YAML document. This only works when the FlowStyle is set to FLOW
         * @return this object
         */
        public Builder setPrettyFlow(boolean prettyFlow) {
            options.prettyFlow = prettyFlow;
            options.options.setPrettyFlow(prettyFlow);
            return this;
        }

        /**
         * @param splitLines If set to true, lines will be split if they exceed the preferred width
         * @return this object
         */
        public Builder setSplitLines(boolean splitLines) {
            options.splitLines = splitLines;
            options.options.setSplitLines(splitLines);
            return this;
        }

        /**
         * @param indent Indentation from the margin
         * @return this object
         */
        public Builder setIndent(int indent) {
            options.indent = indent;
            options.options.setIndent(indent);
            return this;
        }

        /**
         * @param width The maximum preferred width
         * @return this object
         */
        public Builder setWidth(int width) {
            options.width = width;
            options.options.setWidth(width);
            return this;
        }

        /**
         * @param maxKeyLength The maximum simple key length
         * @return This object
         */
        public Builder setMaxKeyLength(int maxKeyLength) {
            options.maxKeyLength = maxKeyLength;
            options.options.setMaxSimpleKeyLength(maxKeyLength);
            return this;
        }

        /**
         * @param timezone The preferred {@link TimeZone}, which will be
         *                 utilized when serializing and deserializing dates
         * @return This object
         */
        public Builder setTimezone(TimeZone timezone) {
            options.timeZone = timezone;
            options.options.setTimeZone(timezone);
            return this;
        }

        /**
         * @param style The preferred {@link org.yaml.snakeyaml.DumperOptions.ScalarStyle}
         * @return This object
         */
        public Builder setScalarStyle(DumperOptions.ScalarStyle style) {
            options.scalarStyle = style;
            options.options.setDefaultScalarStyle(style);
            return this;
        }

        /**
         * @param version The YAML {@link org.yaml.snakeyaml.DumperOptions.Version}, which
         *                is supported up to v1.1
         * @return This object
         */
        public Builder setVersion(DumperOptions.Version version) {
            options.version = version;
            options.options.setVersion(version);
            return this;
        }

        /**
         * @param lineBreak The {@link org.yaml.snakeyaml.DumperOptions.LineBreak} which
         *                  may possibly vary depending on the operating system.
         * @return This object
         */
        public Builder setLineBreak(DumperOptions.LineBreak lineBreak) {
            options.lineBreak = lineBreak;
            options.options.setLineBreak(lineBreak);
            return this;
        }

        /**
         * @param nonPrintableStyle The preferred {@link org.yaml.snakeyaml.DumperOptions.NonPrintableStyle}
         * @return This object
         */
        public Builder setNonPrintableStyle(DumperOptions.NonPrintableStyle nonPrintableStyle) {
            options.nonPrintableStyle = nonPrintableStyle;
            options.options.setNonPrintableStyle(nonPrintableStyle);
            return this;
        }

        /**
         * @param flowStyle The preferred {@link org.yaml.snakeyaml.DumperOptions.FlowStyle}
         * @return This object
         */
        public Builder setFlowStyle(DumperOptions.FlowStyle flowStyle) {
            options.flowStyle = flowStyle;
            options.options.setDefaultFlowStyle(flowStyle);
            return this;
        }

        public YamlOptions build() {
            return options;
        }

    }

}
