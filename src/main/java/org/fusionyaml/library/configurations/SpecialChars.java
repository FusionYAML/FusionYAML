package org.fusionyaml.library.configurations;

public enum SpecialChars {

    SPLIT("\n#"),
    NEW_LINE("\n\n#");

    private final String str;

    SpecialChars(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
