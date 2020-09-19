package org.fusionyaml.library.utils.custom;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.configurations.Configuration;
import org.fusionyaml.library.object.YamlObject;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Allows you to customize {@link org.fusionyaml.library.configurations.Configuration}s.
 * All instances of this interfaces are immutable.
 */
public interface ConfigurationCustomization {
    
    SpecificCustomization withFusionYAML(FusionYAML fusionYAML);
    
    SpecificCustomization withDefaultFusionYAML();
    
    SpecificCustomization withPrettyYAML();
    
    /**
     * Allows you to customize the contents of a {@link org.fusionyaml.library.configurations.Configuration}.
     * This can be accessed by calling the methods in {@link ConfigurationCustomization}
     */
    interface SpecificCustomization {
        
        BulkCustomizable<YamlObject> fromObject(YamlObject object);
        
        SingleContentCustomization<InputStream> fromInputStream(InputStream stream);
        
        SingleContentCustomization<URL> fromURL(URL url);
        
        BulkCustomizable<File> fromFile(File file);
        
        BulkCustomizable<String> fromString(String string);
        
        Configuration toConfiguration();
        
    }
    
    
    /**
     * Doesn't allow you to add multiple instances of {@link T}
     *
     * @param <T> The type
     */
    interface SingleContentCustomization<T> {
        
        /**
         * Converts this customization to a {@link Configuration}. Depending the type
         * of {@link T}, different types of {@link Configuration}s may be returned.
         *
         * @return A {@link Configuration}
         */
        Configuration toConfiguration();
    }
    
    /**
     * Allows you to customize contents and add them.
     *
     * @param <T>
     */
    interface BulkContentCustomization<T> {
        
        /**
         * Adds the data found and returns {@code this} object
         *
         * @param t The {@link T} to add
         * @return {@code this} object
         */
        BulkContentCustomization<T> and(T t);
        
        /**
         * Converts the customization to a {@link Configuration}. Please note that
         * only {@link org.fusionyaml.library.configurations.YamlConfiguration}s will
         * be returned.
         *
         * @return The configuration
         */
        Configuration toConfiguration();
        
    }
    
    /**
     * A content customization that can be bulk customized.
     *
     * @param <T> The type
     */
    interface BulkCustomizable<T> {
        
        BulkContentCustomization<T> bulkCustomizationOf(T... t);
        
        BulkContentCustomization<T> toBulkCustomization();
        
        Configuration toConfiguration();
        
    }
    
}
