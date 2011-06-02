package fabric.wsdlschemaparser.wsdl;

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

import de.uniluebeck.itm.tr.util.Logging;

public class WSDLLoadTest {
    private static Collection<File> wsdlFiles;

    static {
        //TODO Replace with Logging.setDebugLoggingDefaults();
        PatternLayout patternLayout = new PatternLayout("%-13d{HH:mm:ss,SSS} | %-20.20C{3} | %-5p | %m%n");
        final Appender appender = new ConsoleAppender(patternLayout);
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.DEBUG);
    }

    static class WsdlFilesFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".wsdl");
        }
    }

    @BeforeClass
    public static void loadWsdlFiles() {
        Logging.setLoggingDefaults();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        wsdlFiles = new LinkedList<File>();

        // String dirs[] = new String[] { "src/test/resources/wsdls" };
        String dirs[] = new String[] { "src/test/resources/wsdls", "src/test/resources/wsdls/xmethods" };

        for (String dir : dirs) {
            File directory = new File(dir);
            assertTrue("Directory " + directory + " not found.", directory.isDirectory());
            wsdlFiles.addAll(Arrays.asList(directory.listFiles(new WsdlFilesFilter())));
        }

    }

    @Test
    public void testXmethodsWsdlFileLoading() throws Exception {

        for (File wsdlFile : wsdlFiles) {
            new FWSDL(wsdlFile);
        }

    }
}
