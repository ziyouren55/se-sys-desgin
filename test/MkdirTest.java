import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MkdirTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void createDirUnderRoot() {
        fs.mkdir("/usr");
        assertEquals("usr", fs.ls("/"));
    }

    @Test void createNestedDir() {
        fs.mkdir("/usr");
        fs.mkdir("/usr/local");
        assertEquals("local", fs.ls("/usr"));
    }

    @Test void parentNotExistIsIgnored() {
        fs.mkdir("/a/b");
        assertEquals("", fs.ls("/"));
    }

    @Test void doubleSlashIsIgnored() {
        fs.mkdir("/usr//local");
        assertEquals("", fs.ls("/"));
    }

    @Test void trailingSlashIsIgnored() {
        fs.mkdir("/usr/");
        assertEquals("", fs.ls("/"));
    }

    @Test void dotSegmentIsIgnored() {
        fs.mkdir("/usr/./bin");
        assertEquals("", fs.ls("/"));
    }

    @Test void dotDotSegmentIsIgnored() {
        fs.mkdir("/usr/../bin");
        assertEquals("", fs.ls("/"));
    }

    @Test void existingDirKeptAsDir() {
        fs.mkdir("/usr");
        fs.mkdir("/usr");
        assertEquals("usr", fs.ls("/"));
    }

    @Test void existingFileReplacedByDir() {
        fs.touch("/x", 10);
        fs.mkdir("/x");
        assertEquals(0L, fs.info("/x"));
    }
}
