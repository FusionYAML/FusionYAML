package org.fusionyaml.library.utils;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.YamlOptions;
import org.yaml.snakeyaml.DumperOptions;

import static org.fusionyaml.library.utils.ChoiceConstants.*;

public class FusionYAMLs {
    
    /**
     * The default {@link FusionYAML} object
     */
    public static final Choice<FusionYAML> DEFAULT_YAML = new Choice$FusionYAML(F_DEFAULT_YAML.build(), F_DEFAULT_YAML);
    
    /**
     * A {@link FusionYAML} object for printing pretty yaml
     */
    public static final Choice<FusionYAML> PRETTY_YAML = new Choice$FusionYAML(F_PRETTY_YAML.build(), F_PRETTY_YAML);
    
    /**
     * A {@link FusionYAML} object that only allows for the serialization and
     * deserialization of the fields annotated with {@link org.fusionyaml.library.serialization.Expose}
     */
    public static final Choice<FusionYAML> ONLY_EXPOSED = new Choice$FusionYAML(F_ONLY_EXPOSED.build(), F_ONLY_EXPOSED);
    
    /**
     * A {@link FusionYAML} that excludes key-value pairs that are {@code null} or
     * {@link org.fusionyaml.library.object.YamlNull}
     */
    public static final Choice<FusionYAML> EXCLUDE_NULL = new Choice$FusionYAML(F_EXCLUDE_NULL.build(), F_EXCLUDE_NULL);
    
    /**
     * A {@link FusionYAML} that only considers an enum's name during serialization and
     * deserialization
     */
    public static final Choice<FusionYAML> ONLY_ENUM_NAME_MENTIONED = new Choice$FusionYAML(
            F_ONLY_ENUM_NAME_MENTIONED.build(),
            F_ONLY_ENUM_NAME_MENTIONED);
    
    
    public static FusionYAML fusionYAMLWithOptions(YamlOptions options) {
        return new FusionYAML.Builder()
                .indent(options.getIndent())
                .flowStyle(options.getFlowStyle())
                .excludeNullValues(options.isExcludeNullVals())
                .enumNameMentioned(options.onlyEnumNameMentioned())
                .canonical(options.isCanonical())
                .allowUnicode(options.isAllowUnicode())
                .linebreak(options.getLineBreak())
                .maxKeyLength(options.getMaxKeyLength())
                .nonPrintableStyle(options.getNonPrintableStyle())
                .onlyExposed(options.isOnlyExposed())
                .prettyFlow(options.isPrettyFlow())
                .scalarStyle(options.getScalarStyle())
                .timezone(options.getTimeZone())
                .splitLinesOverWidth(options.isSplitLines())
                .version(options.getVersion())
                .width(options.getWidth())
                .build();
    }
    
    private static class Choice$FusionYAML implements Choice<FusionYAML> {
        
        private final FusionYAML fusionYAML;
        private final FusionYAML.Builder builder;
        
        Choice$FusionYAML(FusionYAML fusionYAML, FusionYAML.Builder builder) {
            this.fusionYAML = fusionYAML;
            this.builder = builder;
        }
        
        @Override
        public Choice<FusionYAML> and(Choice<FusionYAML> another) {
            Choice$FusionYAML newChoice = new Choice$FusionYAML(fusionYAML, builder);
            FusionYAML.Builder builder = newChoice.builder;
            if (this.equals(DEFAULT_YAML))
                return this; // do nothing
            else if (this.equals(PRETTY_YAML)) {
                builder.flowStyle(DumperOptions.FlowStyle.FLOW);
                builder.prettyFlow(true);
            } else if (this.equals(ONLY_EXPOSED)) {
                builder.onlyExposed(true);
            } else if (this.equals(EXCLUDE_NULL))
                builder.excludeNullValues(true);
            else if (another.equals(ONLY_ENUM_NAME_MENTIONED))
                builder.enumNameMentioned(true);
            else
                throw new IllegalArgumentException("Argument should contain a choice of FusionYAML present in this object");
            return new Choice$FusionYAML(fusionYAML, builder);
        }
        
        @Override
        public Choice<FusionYAML> not(Choice<FusionYAML> not) {
            Choice$FusionYAML choice$FusionYAML = new Choice$FusionYAML(fusionYAML, builder);
            FusionYAML.Builder builder = choice$FusionYAML.builder;
            if (this.equals(DEFAULT_YAML))
                return this; // do nothing
            else if (this.equals(PRETTY_YAML)) {
                builder.flowStyle(DumperOptions.FlowStyle.BLOCK);
                builder.prettyFlow(false);
            } else if (this.equals(ONLY_EXPOSED))
                builder.onlyExposed(false);
            else if (this.equals(EXCLUDE_NULL))
                builder.excludeNullValues(false);
            else if (this.equals(ONLY_ENUM_NAME_MENTIONED))
                builder.enumNameMentioned(false);
            else
                throw new IllegalArgumentException("Argument should contain a choice of FusionYAML present in this object");
            return new Choice$FusionYAML(fusionYAML, builder);
        }
        
        @Override
        public Choice<FusionYAML> allExcept(Choice<FusionYAML> choice) {
            return this.and(DEFAULT_YAML).and(PRETTY_YAML).and(ONLY_EXPOSED)
                    .and(EXCLUDE_NULL).and(ONLY_ENUM_NAME_MENTIONED).not(choice);
        }
        
        @Override
        public FusionYAML get() {
            return builder.build();
        }
    }
    
}
