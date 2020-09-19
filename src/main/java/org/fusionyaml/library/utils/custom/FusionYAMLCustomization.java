package org.fusionyaml.library.utils.custom;

import org.fusionyaml.library.FusionYAML;

/**
 * Allows you to customize {@link org.fusionyaml.library.FusionYAML}s. All customizations
 * are immutable. Calling any of its method will return a new customization
 * object
 */
public interface FusionYAMLCustomization {
    
    /**
     * Includes the other customization as well as this customization
     * in the returned {@link FusionYAMLCustomization} object
     *
     * @param another The other customization
     * @return A new customization with the customizations included
     */
    FusionYAMLCustomization and(FusionYAMLCustomization another);
    
    /**
     * Excludes the customization in the return {@link FusionYAMLCustomization}
     * object
     *
     * @param not The other customization
     * @return A new customization with the other customizations
     * excluded
     */
    FusionYAMLCustomization not(FusionYAMLCustomization not);
    
    FusionYAMLCustomization allExcept(FusionYAMLCustomization except);
    
    FusionYAML get();
    
}
