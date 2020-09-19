package org.fusionyaml.library.utils.custom;


import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.Utilities;
import org.fusionyaml.library.configurations.*;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ConfigurationCustomizationImpl implements ConfigurationCustomization {
    
    private static YamlObject yamlObjectCheck(YamlElement element) {
        if (!element.isYamlObject())
            return new YamlObject();
        return element.getAsYamlObject();
    }
    
    @Override
    public SpecificCustomization withFusionYAML(FusionYAML fusionYAML) {
        return new SpecificCustomizationImpl(fusionYAML);
    }
    
    @Override
    public SpecificCustomization withDefaultFusionYAML() {
        return withFusionYAML(Utilities.FusionYAMLs.DEFAULT_YAML.get());
    }
    
    @Override
    public SpecificCustomization withPrettyYAML() {
        return withFusionYAML(Utilities.FusionYAMLs.PRETTY_YAML.get());
    }
    
    private static class SpecificCustomizationImpl implements SpecificCustomization {
        
        private final FusionYAML fusionYAML;
        
        public SpecificCustomizationImpl(FusionYAML fusionYAML) {
            this.fusionYAML = fusionYAML;
        }
        
        @Override
        public BulkCustomizable<YamlObject> fromObject(YamlObject object) {
            return new BulkCustomizableImpl<>(fusionYAML, object);
        }
        
        @Override
        public SingleContentCustomization<InputStream> fromInputStream(InputStream stream) {
            return new SingleContentCustomizationImpl<>(fusionYAML, stream);
        }
        
        @Override
        public SingleContentCustomization<URL> fromURL(URL url) {
            return new SingleContentCustomizationImpl<>(fusionYAML, url);
        }
        
        @Override
        public BulkCustomizable<File> fromFile(File file) {
            return new BulkCustomizableImpl<>(fusionYAML, file);
        }
        
        @Override
        public BulkCustomizable<String> fromString(String string) {
            return new BulkCustomizableImpl<>(fusionYAML, string);
        }
        
        @Override
        public Configuration toConfiguration() {
            return new YamlConfiguration(new YamlObject(), fusionYAML);
        }
    }
    
    private static class SingleContentCustomizationImpl<T> implements SingleContentCustomization<T> {
        
        private final T object;
        private final FusionYAML fusionYAML;
        
        public SingleContentCustomizationImpl(FusionYAML fusionYAML, T obj) {
            this.fusionYAML = fusionYAML;
            object = obj;
        }
        
        @Override
        public Configuration toConfiguration() {
            try {
                if (object instanceof YamlObject)
                    return new YamlConfiguration((YamlObject) object, fusionYAML);
                else if (object instanceof InputStream)
                    return new InputStreamConfiguration((InputStream) object, fusionYAML);
                else if (object instanceof URL)
                    return new WebConfiguration((URL) object, fusionYAML);
                else if (object instanceof File)
                    return new FileConfiguration((File) object, fusionYAML);
                else throw new UnsupportedOperationException(object.getClass().toString());
            } catch (IOException e) {
                return null;
            }
        }
        
    }
    
    private static class BulkCustomizableImpl<T> implements BulkCustomizable<T> {
        
        private final FusionYAML fusionYAML;
        private final T t;
        
        public BulkCustomizableImpl(FusionYAML fusionYAML, T t) {
            this.fusionYAML = fusionYAML;
            this.t = t;
        }
        
        @SafeVarargs
        @Override
        public final BulkContentCustomization<T> bulkCustomizationOf(T... t) {
            BulkContentCustomization<T> customization = toBulkCustomization();
            for (T obj : t) customization.and(obj);
            return customization;
        }
        
        @Override
        public BulkContentCustomization<T> toBulkCustomization() {
            YamlObject object;
            if (t instanceof YamlObject) object = (YamlObject) t;
            else if (t instanceof File) object = yamlObjectCheck(fusionYAML.fromYAML((File) t));
            else if (t instanceof String) object = yamlObjectCheck(fusionYAML.fromYAML(t.toString()));
            else throw new UnsupportedOperationException(t.getClass().toString());
            return new BulkContentCustomizationImpl<>(fusionYAML, object);
        }
        
        @Override
        public Configuration toConfiguration() {
            return new SingleContentCustomizationImpl<>(fusionYAML, t).toConfiguration();
        }
    }
    
    private static class BulkContentCustomizationImpl<T> implements BulkContentCustomization<T> {
        
        private final YamlObject object;
        private final FusionYAML fusionYAML;
        
        public BulkContentCustomizationImpl(FusionYAML fusionYAML, YamlObject object) {
            this.object = object;
            this.fusionYAML = fusionYAML;
        }
        
        @Override
        public BulkContentCustomization<T> and(T t) {
            YamlObject toAdd;
            if (t instanceof YamlObject) toAdd = (YamlObject) t;
            else if (t instanceof File) toAdd = yamlObjectCheck(fusionYAML.fromYAML((File) t));
            else if (t instanceof String) toAdd = yamlObjectCheck(fusionYAML.fromYAML(t.toString()));
            else throw new UnsupportedOperationException(t.getClass().toString());
            toAdd.forEach((k, v) -> this.object.set(k, v.deepCopy()));
            return this;
        }
        
        @Override
        public Configuration toConfiguration() {
            return new YamlConfiguration(object, fusionYAML);
        }
        
    }
    
    
}
