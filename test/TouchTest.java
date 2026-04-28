import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TouchTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void createFileUnderRoot() {
        fs.touch("/readme.md", 50);
        assertEquals(50L, fs.info("/readme.md"));
    }

    @Test void createFileInSubDir() {
        fs.mkdir("/usr");
        fs.touch("/usr/hello.txt", 30);
        assertEquals(30L, fs.info("/usr/hello.txt"));
    }

    @Test void parentNotExistIsIgnored() {
        fs.touch("/a/b.txt", 10);
        assertEquals("", fs.ls("/"));
    }

    @Test void overwriteFileSizeUpdated() {
        fs.touch("/f.txt", 10);
        fs.touch("/f.txt", 99);
        assertEquals(99L, fs.info("/f.txt"));
    }

    @Test void existingDirReplacedByFile() {
        fs.mkdir("/x");
        fs.touch("/x", 42);
        assertEquals(42L, fs.info("/x"));
    }

    @Test void doubleSlashIsIgnored() {
        fs.touch("/a//b.txt", 10);
        assertEquals("", fs.ls("/"));
    }

    @Test void trailingSlashIsIgnored() {
        fs.touch("/a/", 10);
        assertEquals("", fs.ls("/"));
    }

    @Test void dotSegmentIsIgnored() {
        fs.touch("/./f.txt", 10);
        assertEquals("", fs.ls("/"));
    }
}
