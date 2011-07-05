package de.uniluebeck.sourcegen.protobuf;

import java.util.List;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

public class ProtobufWorkspace {
    private static final String PACKAGE = "protobuf.package";
    private static final String DEFAULT_FILENAME = "protobuf.file";

    private String packageName;
    private String fileName;
    private PSourceFile defaultSourceFile = null;

    private final List<SourceFile> sourceFiles;

    public ProtobufWorkspace(Workspace w) {
        sourceFiles = w.getSourceFiles();
        packageName = w.getProperties().getProperty(PACKAGE);
        fileName = w.getProperties().getProperty(DEFAULT_FILENAME, "protobuf.prot");
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public PSourceFile getDefaultSourceFile() {

        if (defaultSourceFile == null) {
            defaultSourceFile = new PSourceFile(fileName);
            sourceFiles.add(defaultSourceFile);
        }

        return defaultSourceFile;
    }

}
