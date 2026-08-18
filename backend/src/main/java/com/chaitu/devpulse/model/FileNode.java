package com.chaitu.devpulse.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("File")
public class FileNode {

    @Id
    private String id;
    private String path;
    private String extension;
    private int linesOfCode;

    public FileNode() {}

    public FileNode(String id, String path, String extension, int linesOfCode) {
        this.id = id;
        this.path = path;
        this.extension = extension;
        this.linesOfCode = linesOfCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }
}
