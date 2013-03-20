package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.usp.ime.arranger.utils.FileCreator;

public class FileCreatorTest {

    private transient FileCreator creator;

    @Before
    public void setUp() {
        creator = new FileCreator();
    }

    @After
    public void tearDown() throws IOException {
        FileCreator.destroy();
    }

    @Test
    public void shouldCreateFileWith1024bytes() throws IOException {
        createFileWithSize(1024);
    }

    @Test
    public void shouldCreateFileWith1024kbytes() throws IOException {
        createFileWithSize(1024 * 1024);
    }

    private void createFileWithSize(final long sizeInBytes) throws IOException {
        final File file = creator.getFileOfSize(sizeInBytes);
        assertEquals(sizeInBytes, file.length());
    }

    @Test
    public void shouldUseAlreadyCreatedFile() throws IOException,
            InterruptedException {
        final File file1 = creator.getFileOfSize(512);
        final File file2 = creator.getFileOfSize(512);

        assertEquals(file1.getAbsolutePath(), file2.getAbsolutePath());
    }

    @Test
    public void shouldNotRewriteFile() throws IOException, InterruptedException {
        final File file1 = creator.getFileOfSize(512);
        final long modTime1 = file1.lastModified();

        Thread.sleep(1000);

        final File file2 = creator.getFileOfSize(512);
        final long modTime2 = file2.lastModified();

        assertEquals(modTime1, modTime2);
    }

    @Test
    public void testContentRandomness() throws IOException {
        final File file = creator.getFileOfSize(42);
        final Path path = file.toPath();
        final byte[] bytes = Files.readAllBytes(path);
        final String actual = new String(bytes);

        final StringBuilder builder = new StringBuilder(actual.length());
        final char firstLetter = actual.charAt(0);
        for (int i = 0; i < actual.length(); i++) {
            builder.append(firstLetter);
        }
        final String sameLetter = builder.toString();

        assertNotEquals(sameLetter, actual);
    }
}