package org.fusionyaml.library;

import org.fusionyaml.library.utils.custom.ConfigurationCustomization;
import org.fusionyaml.library.utils.custom.ConfigurationCustomizationImpl;
import org.fusionyaml.library.utils.custom.FusionYAMLCustomization;
import org.fusionyaml.library.utils.custom.FusionYAMLCustomizationImpl;
import org.yaml.snakeyaml.DumperOptions;

/**
 * A public utility class for the FusionYAML library
 */
public final class Utilities {
    
    private Utilities() {
    }
    
    public static final class FusionYAMLs {
        
        /**
         * A customization of the default {@link FusionYAML} object, with no customizations in
         * {@link FusionYAML.Builder}
         */
        public static final FusionYAMLCustomization DEFAULT_YAML = new FusionYAMLCustomizationImpl(new FusionYAML.Builder());
        
        /**
         * A customization of a {@link FusionYAML} object with pretty options enabled
         */
        public static final FusionYAMLCustomization PRETTY_YAML = new FusionYAMLCustomizationImpl(
                new FusionYAML.Builder().flowStyle(DumperOptions.FlowStyle.FLOW).prettyFlow(true)
        );
        
        /**
         * A customization of a {@link FusionYAML} that only serializes and deserializes
         * fields annotated with {@link org.fusionyaml.library.serialization.Expose}
         */
        public static final FusionYAMLCustomization ONLY_EXPOSED = new FusionYAMLCustomizationImpl(
                new FusionYAML.Builder().onlyExposed(true)
        );
        
        /**
         * A customization of a {@link FusionYAML} object that only identifies an enum with their
         * name during serialization and deserialization
         */
        public static final FusionYAMLCustomization ONLY_ENUM_NAME_MENTIONED = new FusionYAMLCustomizationImpl(
                new FusionYAML.Builder().enumNameMentioned(true)
        );
        
        /**
         * A customization of a {@link FusionYAML} object that encloses scalars with double
         * quotes
         */
        public static final FusionYAMLCustomization DOUBLE_QUOTED_SCALAR_STYLE = new FusionYAMLCustomizationImpl(
                new FusionYAML.Builder().scalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED)
        );
        
    }
    
    public static final class Configurations {
        
        public static final ConfigurationCustomization CONFIGURATION_CUSTOMIZATION = new ConfigurationCustomizationImpl();
        
    }
    
}
