package org.fusionyaml.library.reference;

import com.google.common.base.Splitter;
import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.configurations.Configuration;
import org.fusionyaml.library.configurations.YamlConfiguration;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlNull;
import org.fusionyaml.library.object.YamlObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

/**
 * A {@link Configuration} capable of identifying references.
 */
public class ReferenceConfiguration extends YamlConfiguration {
    
    private final Configuration configuration;
    
    /**
     * Creates an instance of this object given a {@link Configuration}
     * and a {@link FusionYAML} object
     *
     * @param configuration The configuration
     * @param yaml          A {@link FusionYAML} object
     */
    public ReferenceConfiguration(Configuration configuration, FusionYAML yaml) {
        super(configuration.toYamlObject(), yaml);
        this.configuration = configuration;
    }
    
    /**
     * Creates an instance of this object given a {@link Configuration}
     * object.
     *
     * @param configuration The configuration
     */
    public ReferenceConfiguration(Configuration configuration) {
        this(configuration, new FusionYAML());
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then the default value will be returned.
     *
     * @param path         The path
     * @param defaultValue The default value
     * @return The reference found, or the default value if the reference isn't found
     */
    public Reference getReference(List<String> path, Reference defaultValue) {
        Object object = getObject(path);
        return object instanceof Reference ? (Reference) object : defaultValue;
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then {@code null} will be returned.
     *
     * @param path The path
     * @return The reference found, or {@code null} if the reference isn't found
     */
    public Reference getReference(List<String> path) {
        return getReference(path, null);
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then the default value will be returned.
     *
     * @param path         The path
     * @param separator    The path separator, where each usage indicates a descent
     * @param defaultValue The default value
     * @return The reference found, or the default value if the reference isn't found
     */
    public Reference getReference(String path, char separator, Reference defaultValue) {
        return getReference(Splitter.on(separator).splitToList(path), defaultValue);
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then {@code null} will be returned.
     *
     * @param path      The path
     * @param separator The path separator, where each usage indicates a descent
     * @return The reference found, or {@code null} if the reference isn't found
     */
    public Reference getReference(String path, char separator) {
        return getReference(path, separator, null);
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then the default value will be returned.
     *
     * @param path         The path
     * @param defaultValue The default value
     * @return The reference found, or the default value if the reference isn't found
     */
    public Reference getReference(String path, Reference defaultValue) {
        return getReference(Collections.singletonList(path), defaultValue);
    }
    
    /**
     * Gets the {@link Reference} found in the given path. If no such reference exists,
     * then {@code null} will be returned.
     *
     * @param path The path
     * @return The reference found, or {@code null} if the reference isn't found
     */
    public Reference getReference(String path) {
        return getReference(path, null);
    }
    
    /**
     * Gets the {@link YamlObject} that represents this configuration.
     *
     * @param substituteAndCopy Whether a copy of the {@link YamlObject} in the class is
     *                          returned with the {@link Reference}s substituted
     * @return A {@link YamlObject} representing this configuration
     */
    public YamlObject toYamlObject(boolean substituteAndCopy) {
        if (substituteAndCopy) {
            return References.copyAndSubstituteReferences(object);
        }
        return toYamlObject();
    }
    
    /**
     * Saves the {@link Writer} using the configuration passed in when this
     * object was initialized.
     *
     * @param writer The writer
     * @param buffer The buffer size
     * @throws IOException If an IO error occurred
     */
    @Override
    public void save(@NotNull Writer writer, int buffer) throws IOException {
        configuration.save(writer, buffer);
    }
    
    /**
     * Saves the {@link File} using the configuration passed in when this object
     * was initialized.
     *
     * @param file The file that'll be saved to. If the file doesn't exist, the file will
     *             be created with the data saved to it.
     * @throws IOException If an IO error occurred
     */
    @Override
    public void save(@NotNull File file) throws IOException {
        configuration.save(file);
    }
    
    /**
     * Saves the {@link Writer} using the configuration passed in when this
     * object was initialized.
     *
     * @param writer The writer
     * @throws IOException If an IO error occurred
     */
    @Override
    public void save(@NotNull Writer writer) throws IOException {
        configuration.save(writer);
    }
    
    @Override
    public void set(@NotNull String path, Object value) {
        if (value instanceof Reference)
            value = value.toString();
        super.set(path, value);
    }
    
    @Override
    public void set(@NotNull String path, char separator, Object value) {
        if (value instanceof Reference)
            value = value.toString();
        super.set(path, separator, value);
    }
    
    @Override
    public YamlElement getElement(@NotNull List<String> path, YamlElement defValue) {
        YamlElement found = super.getElement(path, defValue);
        Reference reference = getReference(path);
        if (found != null && reference != null)
            return reference.getReferenced();
        else if (found == null && defValue.isYamlPrimitive()) {
            if (References.isReference(defValue.getAsString()))
                return References.parseReference(defValue.getAsString(), object).getReferenced();
        }
        return found == null ? defValue : found;
    }
    
    @Override
    public YamlElement getElement(@NotNull List<String> path) {
        return getElement(path, YamlNull.NULL);
    }
    
    @Override
    public YamlElement getElement(@NotNull String path, char separator, YamlElement defValue) {
        return getElement(Splitter.on(separator).splitToList(path), defValue);
    }
    
    @Override
    public YamlElement getElement(@NotNull String path, char separator) {
        return getElement(path, separator, YamlNull.NULL);
    }
    
    @Override
    public YamlElement getElement(@NotNull String path, YamlElement defValue) {
        return getElement(Collections.singletonList(path), defValue);
    }
    
    @Override
    public YamlElement getElement(@NotNull String path) {
        return getElement(path, YamlNull.NULL);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull List<String> path, Object value) {
        if (value instanceof Reference)
            value = value.toString();
        super.set(path, value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull List<String> path, Object defValue) {
        return checkReference(super.getObject(path, checkReference(defValue)));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull List<String> path) {
        return checkReference(super.getObject(path));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull String path, char separator, Object defValue) {
        return checkReference(super.getObject(path, separator, defValue));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull String path, char separator) {
        return checkReference(super.getObject(path, separator));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull String path, Object defValue) {
        return checkReference(super.getObject(path, checkReference(defValue)));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(@NotNull String path) {
        return checkReference(super.getObject(path));
    }
    
    private Object checkReference(Object obj) {
        if (obj == null) return null;
        if (References.isReference(obj.toString()))
            return References.parseReference(obj.toString(), object);
        return obj;
    }
    
}
