package org.fusionyaml.library.utils;

/**
 * Represents a choice in the utility classes. All choices are
 * immutable, so methods returning {@link Choice} are in fact returning
 * new objects.
 *
 * @param <T> The type represented
 */
public interface Choice<T> {
    
    Choice<T> and(Choice<T> another);
    
    Choice<T> not(Choice<T> not);
    
    Choice<T> allExcept(Choice<T> choice);
    
    T get();
    
    
}
