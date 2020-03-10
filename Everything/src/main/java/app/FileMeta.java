package app;

import util.Util;

import java.io.File;
import java.util.Objects;

public class FileMeta {
    private String name;
    private String path;
    private String sizeText;
    private String lastModifiedText;
    private long size;
    private long lastModified;
    private Boolean isDirectory;

    public FileMeta(String name, String path, long size, long lastModified, Boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.isDirectory = isDirectory;
        this.sizeText = Util.parseSize(size);
        this.lastModifiedText = Util.parseDate(lastModified);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSizeText() {
        return sizeText;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }

    public FileMeta(File child){
        this(child.getName(),child.getParent(),child.length(),
                child.lastModified(),child.isDirectory());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta meta = (FileMeta) o;
        return Objects.equals(name, meta.name) &&
                Objects.equals(path, meta.path) &&
                Objects.equals(isDirectory, meta.isDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory);
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
