package org.fusionyaml.library.object;

public class YamlNull implements YamlElement {

    public static final YamlNull NULL = new YamlNull();

    private YamlNull() {
    }

    /**
     * Copies all of the elements and the children contained
     * herein
     *
     * @return A copy of this {@link YamlElement}
     */
    @Override
    public YamlNull deepCopy() {
        // null values aren't deep copied
        return NULL;
    }
}
