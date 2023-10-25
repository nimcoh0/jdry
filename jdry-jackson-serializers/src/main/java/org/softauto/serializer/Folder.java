package org.softauto.serializer;


import java.util.ArrayList;
import java.util.List;

public class Folder {

    private Long id;
    private String name;
    private String owner = "asdfrg";
    private List<File> files = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getOwner() {
        return owner;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
