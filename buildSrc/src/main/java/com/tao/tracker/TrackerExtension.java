package com.tao.tracker;

import java.io.File;

public class TrackerExtension extends BaseExtension {

    public static final String NAME = "tracker";

    private File insertFile;
    private File whiteListFile;

    public File getInsertFile() {
        return insertFile;
    }

    public void setInsertFile(File insertFile) {
        this.insertFile = insertFile;
    }

    public File getWhiteListFile() {
        return whiteListFile;
    }

    public void setWhiteListFile(File whiteListFile) {
        this.whiteListFile = whiteListFile;
    }
}
