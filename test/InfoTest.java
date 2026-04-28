import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InfoTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void fileSize() {
        fs.touch("/readme.md", 50);
        assertEquals(50L, fs.info("/readme.md"));
    }

    @Test void emptyDirSizeIsZero() {
        fs.mkdir("/usr");
        assertEquals(0L, fs.info("/usr"));
    }

    @Test void dirSizeRecursive() {
        fs.mkdir("/usr");
        fs.mkdir("/usr/local");
        fs.touch("/usr/local/test.txt", 100);
        fs.touch("/readme.md", 50);
        assertEquals(150L, fs.info("/"));
    }

    @Test void nestedDirSize() {
        fs.mkdir("/usr");
        fs.mkdir("/usr/local");
        fs.touch("/usr/local/test.txt", 100);
        assertEquals(100L, fs.info("/usr"));
    }

    @Test void infoAfterTouchOverwrite() {
        fs.touch("/f.txt", 10);
        fs.touch("/f.txt", 99);
        assertEquals(99L, fs.info("/f.txt"));
    }

    @Test void rootSizeMultipleFiles() {
        fs.touch("/a.txt", 10);
        fs.touch("/b.txt", 20);
        fs.touch("/c.txt", 30);
        assertEquals(60L, fs.info("/"));
    }
}
