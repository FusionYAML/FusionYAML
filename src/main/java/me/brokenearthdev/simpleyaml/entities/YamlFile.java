package me.brokenearthdev.simpleyaml.entities;

import java.io.File;

public class YamlFile {

    private File file;

    public YamlFile(File file) {
        this.file = file;
    }

    public YamlFile(String path) {
        this(new File(path));
    }

    public File getFile() {
        return file;
    }

}
