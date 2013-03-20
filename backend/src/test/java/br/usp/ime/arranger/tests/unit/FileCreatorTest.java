package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import br.usp.ime.arranger.utils.FileCreator;

public class FileCreatorTest {

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
        final FileCreator creator = new FileCreator();
        final File file = creator.getFileOfSize(sizeInBytes);

        assertEquals(sizeInBytes, file.length());
    }

    @Test
    public void shouldUseAlreadyCreatedFile() throws IOException,
            InterruptedException {
        final FileCreator creator = new FileCreator();

        final File file1 = creator.getFileOfSize(512);
        final File file2 = creator.getFileOfSize(512);

        assertEquals(file1.getAbsolutePath(), file2.getAbsolutePath());
    }

    @Test
    public void shouldNotRewriteFile() throws IOException, InterruptedException {
        final FileCreator creator = new FileCreator();

        final File file1 = creator.getFileOfSize(512);
        final long modTime1 = file1.lastModified();

        Thread.sleep(1000);

        final File file2 = creator.getFileOfSize(512);
        final long modTime2 = file2.lastModified();

        assertEquals(modTime1, modTime2);
    }
}