package br.usp.ime.arranger.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileCreator {

    private static final String PREFIX = "arranger";
    private static final String SUFFIX = ".tmp";

    private static final Map<Long, File> FILES = new HashMap<>();

    public File getFileOfSize(final long sizeInBytes) throws IOException {
        checkCache(sizeInBytes);
        return FILES.get(sizeInBytes);
    }

    private void checkCache(final long size) throws IOException {
        if (!FILES.containsKey(size) || !FILES.get(size).exists()) {
            addToCache(size);
        }
    }

    private void addToCache(final long size) throws IOException {
        synchronized (FILES) {
            if (!FILES.containsKey(size) || !FILES.get(size).exists()) {
                final File file = createFile(size);
                file.deleteOnExit();
                FILES.put(size, file);
            }
        }
    }

    // TODO: size - 1591 - ( ((size+317)/4170 teto - 1)*75 ) - 75 bytes por
    // pacote extra
    private File createFile(final long size) throws IOException {
        final File file = File.createTempFile(PREFIX + size + "_", SUFFIX);
        final BufferedWriter writer = Files.newBufferedWriter(file.toPath(),
                Charset.forName("UTF-8"));

        for (int i = 0; i < size; i++) {
            writer.append("a");
        }

        writer.close();
        return file;
    }

    public static File createTempFile() throws IOException {
        final File file = File.createTempFile(PREFIX, SUFFIX);
        file.deleteOnExit();
        return file;
    }

    public static void destroy() {
        if (!FILES.isEmpty()) {
            synchronized (FILES) {
                for (File file : FILES.values()) {
                    file.delete();
                }
                FILES.clear();
            }
        }
        deleteLeftOverFiles();
    }

    /**
     * Deletes all arranger*.tmp files.
     * 
     * @throws IOException
     */
    private static void deleteLeftOverFiles() {
        final String tmpDirProp = System.getProperty("java.io.tmpdir");
        final File tmpDir = new File(tmpDirProp);

        // One day, java will have lambda expressions...
        final File[] trash = tmpDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return (name.startsWith("arranger") && name.endsWith(".tmp"));
            }
        });

        for (File file2delete : trash) {
            file2delete.delete();
        }
    }
}