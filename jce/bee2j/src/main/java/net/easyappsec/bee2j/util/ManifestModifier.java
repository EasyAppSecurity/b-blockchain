package net.easyappsec.bee2j.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rusakovich
 */
public final class ManifestModifier {

    private static final Logger logger = Logger.getLogger(ManifestModifier.class.getName());

    private ManifestModifier() {
    }

    public static void main(String[] arguments) {

        String arquivesStringPath = arguments[0];
        logger.info("Atempting to update archives located at: " + arquivesStringPath);

        String manifestAddition = getManifestContent(arguments);
        logger.info("Manifest content to be added:\n " + manifestAddition);

        logger.info("Creating temporary manifest file");
        File manifestFile = null;
        try {
            manifestFile = createsTemporaryManifestFile(manifestAddition);
            logger.info("Temporary manifest file has been created at: " + manifestFile.getAbsolutePath());
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Error when creating temporary manifest file. Message: " + ioe.getMessage());
        }

        logger.info("Reading files from: " + arquivesStringPath);
        File arquivesPath = new File(arquivesStringPath);
        if (arquivesPath.exists() && arquivesPath.isDirectory()) {

            File[] files = arquivesPath.listFiles();

            for (File f : files) {
                try {
                    logger.info("Adding attributes from manifest [".concat(manifestFile.getAbsolutePath()).concat("] into [").concat(f.getAbsolutePath()).concat("]"));
                    logger.info(addManifestAttributes(manifestFile, f));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error when unsigning archive [" + f.getAbsolutePath() + "]. Message: " + e.getMessage());
                }
            }

        }

    }

    /**
     * Breaks down an array of strings (starting from position [1]) in several
     * lines.
     *
     * @param args
     * @return String in manifest format
     */
    private static String getManifestContent(String[] args) {
        StringBuffer result = new StringBuffer();

        for (int i = 1; i < args.length; i++) {
            result.append(args[i]);
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Creates a temporary manifest file
     *
     * @return File pointing to the temporary manifest file created
     * @throws IOException
     */
    private static File createsTemporaryManifestFile(String content) throws IOException {
        File file = File.createTempFile("UnsignArchiveTempManifest", ".mf");

        FileWriter fw = new FileWriter(file.getAbsoluteFile());

        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();

        return file;
    }

    private static String addManifestAttributes(File manifestFile, File jarFile) throws IOException {
        StringBuffer result = new StringBuffer();

        String commandLine = System.getenv("JAVA_HOME")
                .concat(File.separator)
                .concat("bin")
                .concat(File.separator)
                .concat("jar");

        ProcessBuilder processBuilder = new ProcessBuilder(commandLine, "ufm", jarFile.getAbsolutePath(), manifestFile.getAbsolutePath());

        logger.info("Executing command line: " + commandLine + " ufm " + jarFile.getAbsolutePath() + " " + manifestFile.getAbsolutePath());

        Process process = processBuilder.start();

        BufferedReader stdInput = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String s = null;
        while ((s = stdInput.readLine()) != null) {
            result.append(s);
        }

        while ((s = stdError.readLine()) != null) {
            result.append(s);
        }

        return result.toString();
    }

}
