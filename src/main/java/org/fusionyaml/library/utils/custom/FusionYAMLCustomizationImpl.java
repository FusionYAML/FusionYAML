package org.fusionyaml.library.utils.custom;

import org.fusionyaml.library.FusionYAML;
import org.yaml.snakeyaml.DumperOptions;

import static org.fusionyaml.library.Utilities.FusionYAMLs;

public class FusionYAMLCustomizationImpl implements FusionYAMLCustomization {
    
    private final FusionYAML.Builder builder;
    
    public FusionYAMLCustomizationImpl(FusionYAML.Builder builder) {
        this.builder = builder;
    }
    
    @Override
    public FusionYAMLCustomization and(FusionYAMLCustomization another) {
        FusionYAMLCustomizationImpl newCustom = new FusionYAMLCustomizationImpl(builder);
        if (another.equals(FusionYAMLs.DEFAULT_YAML))
            return this; // do nothing
        else if (another.equals(FusionYAMLs.PRETTY_YAML)) {
            newCustom.builder.flowStyle(DumperOptions.FlowStyle.FLOW);
            newCustom.builder.prettyFlow(true);
        } else if (another.equals(FusionYAMLs.ONLY_EXPOSED)) {
            newCustom.builder.onlyExposed(true);
        } else if (another.equals(FusionYAMLs.ONLY_ENUM_NAME_MENTIONED))
            newCustom.builder.enumNameMentioned(true);
        else
            throw new IllegalArgumentException("Only customization objects from " + FusionYAMLs.class + " are allowed");
        return newCustom;
    }
    
    @Override
    public FusionYAMLCustomization not(FusionYAMLCustomization not) {
        FusionYAMLCustomizationImpl customization = new FusionYAMLCustomizationImpl(builder);
        if (not.equals(FusionYAMLs.DEFAULT_YAML))
            return this; // do nothing
        else if (not.equals(FusionYAMLs.PRETTY_YAML)) {
            customization.builder.flowStyle(DumperOptions.FlowStyle.BLOCK);
            customization.builder.prettyFlow(false);
        } else if (not.equals(FusionYAMLs.ONLY_EXPOSED)) {
            customization.builder.onlyExposed(false);
        } else if (not.equals(FusionYAMLs.ONLY_ENUM_NAME_MENTIONED)) {
            customization.builder.enumNameMentioned(false);
        } else
            throw new IllegalArgumentException("Only customization objects from " + FusionYAMLs.class + " are allowed");
        return customization;
    }
    
    @Override
    public FusionYAMLCustomization allExcept(FusionYAMLCustomization except) {
        return this.and(FusionYAMLs.DEFAULT_YAML).and(FusionYAMLs.ONLY_ENUM_NAME_MENTIONED).and(FusionYAMLs.ONLY_EXPOSED)
                .and(FusionYAMLs.PRETTY_YAML).not(except);
    }
    
    @Override
    public FusionYAML get() {
        return builder.build();
    }
    
}
