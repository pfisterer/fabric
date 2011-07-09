package fabric.wsdlschemaparser.schema;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.tr.util.Logging;

public class SchemaLoadTest {
    private static Collection<File> files;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SchemaLoadTest.class);

    static {
        // TODO Replace with Logging.setDebugLoggingDefaults();
        PatternLayout patternLayout = new PatternLayout("%-13d{HH:mm:ss,SSS} | %-20.20C{3} | %-5p | %m%n");
        final Appender appender = new ConsoleAppender(patternLayout);
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    static class XsdFilesFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".xsd");
        }
    }

    @BeforeClass
    public static void loadXsdFiles() {
        Logging.setLoggingDefaults();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        files = new LinkedList<File>();

        // String dirs[] = new String[] { "src/test/resources/wsdls" };
        String dirs[] = new String[] { "src/test/resources/schemas" };

        for (String dir : dirs) {
            File directory = new File(dir);
            assertTrue("Directory " + directory + " not found.", directory.isDirectory());
            files.addAll(Arrays.asList(directory.listFiles(new XsdFilesFilter())));
        }

    }

    @Test
    public void testXsdFileLoading() throws Exception {

        for (File file : files) {
            log.info("--------------------------------------------------------------");
            log.info("Loading file: {}", file);
            log.info("--------------------------------------------------------------");
            new FSchema(file);
        }

    }
}
