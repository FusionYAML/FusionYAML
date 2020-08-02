package com.github.fusionyaml.configurations;

public enum SpecialChars {

    SPLIT("\n#"),
    NEW_LINE("\n\n#");

    private String str;

    SpecialChars(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
