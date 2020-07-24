package io.github.fusionyaml;

import org.yaml.snakeyaml.DumperOptions;

import java.util.TimeZone;

/**
 * A wrapper for SnakeYAML's {@link org.yaml.snakeyaml.DumperOptions} class, containing
 * a few more options than the {@link org.yaml.snakeyaml.DumperOptions} class
 */
public class YamlOptions {

    private DumperOptions options;
    private boolean onlyExposed = false;
    private boolean mentionEnumName = false;
    private boolean includeInterfacesWithNoAdapters = false;
    private boolean canonical = false;
    private boolean preserveComments = false;
    private boolean allowUnicode = true;
    private boolean prettyFlow = false;
    private boolean splitLines = true;
    private int indent = 2;
    private int width = 80;
    private int maxKeyLength = 128;
    private TimeZone timeZone = TimeZone.getDefault();
    private DumperOptions.ScalarStyle scalarStyle = DumperOptions.ScalarStyle.DOUBLE_QUOTED;
    private DumperOptions.Version version = DumperOptions.Version.V1_1;
    private DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.UNIX;
    private DumperOptions.NonPrintableStyle nonPrintableStyle = DumperOptions.NonPrintableStyle.BINARY;
    private DumperOptions.FlowStyle flowStyle = DumperOptions.FlowStyle.FLOW;

    public YamlOptions() {
        options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
    }

    /**
     * @param onlyExposed If set to true, FusionYAML will serialize or deserialize only with
     *                    fields with the {@link io.github.fusionyaml.serialization.Expose} annotation
     * @return this object
     */
    public YamlOptions setOnlyExposed(boolean onlyExposed) {
        this.onlyExposed = onlyExposed;
        return this;
    }

    /**
     * @param mentionEnumName If set to true, the EnumTypeAdapter will only mention enum's name and
     *                        not the path if otherwise.
     * @return this object
     */
    public YamlOptions setMentionEnumName(boolean mentionEnumName) {
        this.mentionEnumName = mentionEnumName;
        return this;
    }

    /**
     * @param includeInterfacesWithNoAdapters If set to true, interfaces with no type adapters will be exempt from
     *                                        serialization and deserialization
     * @return this object
     */
    public YamlOptions setIncludeInterfacesWithNoAdapters(boolean includeInterfacesWithNoAdapters) {
        this.includeInterfacesWithNoAdapters = includeInterfacesWithNoAdapters;
        return this;
    }

    /**
     * @param canonical If set to true, a canonical YAML document will be produced instead
     * @return this object
     */
    public YamlOptions setCanonical(boolean canonical) {
        this.canonical = canonical;
        options.setCanonical(canonical);
        return this;
    }

    /**
     * @param preserveComments If set to true, comments are preserved and saved. Please note that this
     *                         may affect performance as it would force FusionYAML to manually insert
     *                         them since snakeyaml doesn't preserve them
     * @return This object
     */
    public YamlOptions setPreserveComments(boolean preserveComments) {
        this.preserveComments = preserveComments;
        return this;
    }

    /**
     * @param allowUnicode If set to true, unicode characters will be allowed
     * @return this object
     */
    public YamlOptions setAllowUnicode(boolean allowUnicode) {
        this.allowUnicode = allowUnicode;
        options.setAllowUnicode(allowUnicode);
        return this;
    }

    /**
     * @param prettyFlow If set to true, the YAML dumper will be forced to produce a pretty
     *                   YAML document. This only works when the FlowStyle is set to FLOW
     * @return this object
     */
    public YamlOptions setPrettyFlow(boolean prettyFlow) {
        this.prettyFlow = prettyFlow;
        options.setPrettyFlow(prettyFlow);
        return this;
    }

    /**
     * @param splitLines If set to true, lines will be split if they exceed the preferred width
     * @return this object
     */
    public YamlOptions setSplitLines(boolean splitLines) {
        this.splitLines = splitLines;
        options.setSplitLines(splitLines);
        return this;
    }

    /**
     * @param indent Indentation from the margin
     * @return this object
     */
    public YamlOptions setIndent(int indent) {
        this.indent = indent;
        options.setIndent(indent);
        return this;
    }

    /**
     * @param width The maximum preferred width
     * @return this object
     */
    public YamlOptions setWidth(int width) {
        this.width = width;
        options.setWidth(width);
        return this;
    }

    /**
     * @param maxKeyLength The maximum simple key length
     * @return This object
     */
    public YamlOptions setMaxKeyLength(int maxKeyLength) {
        this.maxKeyLength = maxKeyLength;
        options.setMaxSimpleKeyLength(maxKeyLength);
        return this;
    }

    /**
     * @param timezone The preferred {@link TimeZone}, which will be
     *                 utilized when serializing and deserializing dates
     * @return This object
     */
    public YamlOptions setTimezone(TimeZone timezone) {
        this.timeZone = timezone;
        options.setTimeZone(timezone);
        return this;
    }

    /**
     * @param style The preferred {@link org.yaml.snakeyaml.DumperOptions.ScalarStyle}
     * @return This object
     */
    public YamlOptions setScalarStyle(DumperOptions.ScalarStyle style) {
        this.scalarStyle = style;
        options.setDefaultScalarStyle(style);
        return this;
    }

    /**
     * @param version The YAML {@link org.yaml.snakeyaml.DumperOptions.Version}, which
     *                is supported up to v1.1
     * @return This object
     */
    public YamlOptions setVersion(DumperOptions.Version version) {
        this.version = version;
        options.setVersion(version);
        return this;
    }

    /**
     * @param lineBreak The {@link org.yaml.snakeyaml.DumperOptions.LineBreak} which
     *                  may possibly vary depending on the operating system.
     * @return This object
     */
    public YamlOptions setLineBreak(DumperOptions.LineBreak lineBreak) {
        this.lineBreak = lineBreak;
        options.setLineBreak(lineBreak);
        return this;
    }

    /**
     * @param nonPrintableStyle The preferred {@link org.yaml.snakeyaml.DumperOptions.NonPrintableStyle}
     * @return This object
     */
    public YamlOptions setNonPrintableStyle(DumperOptions.NonPrintableStyle nonPrintableStyle) {
        this.nonPrintableStyle = nonPrintableStyle;
        options.setNonPrintableStyle(nonPrintableStyle);
        return this;
    }

    /**
     * @param flowStyle The preferred {@link org.yaml.snakeyaml.DumperOptions.FlowStyle}
     * @return This object
     */
    public YamlOptions setFlowStyle(DumperOptions.FlowStyle flowStyle) {
        this.flowStyle = flowStyle;
        options.setDefaultFlowStyle(flowStyle);
        return this;
    }

    /**
     * @return Whether
     */
    public boolean isOnlyExposed() {
        return onlyExposed;
    }

    public boolean onlyEnumNameMentioned() {
        return mentionEnumName;
    }

    public boolean isIncludeInterfacesWithNoAdapters() {
        return includeInterfacesWithNoAdapters;
    }

    public boolean isCanonical() {
        return canonical;
    }

    public boolean isPreserveComments() {
        return preserveComments;
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


}
