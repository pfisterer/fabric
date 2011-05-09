package de.uniluebeck.sourcegen.protobuf;

import java.util.List;
import java.util.Properties;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

public class ProtobufWorkspace {
    private static final String PACKAGE = "protobuf.package";
    private static final String DEFAULT_FILENAME = "protobuf.file";

    private String packageName;
    private String fileName;
    private PSourceFile defaultSourceFile = null;

    private final List<SourceFile> sourceFiles;

    public ProtobufWorkspace(Workspace w, Properties properties) {
        sourceFiles = w.getSourceFiles();
        packageName = properties.getProperty(PACKAGE);
        fileName = properties.getProperty(DEFAULT_FILENAME, "protobuf.prot");
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
