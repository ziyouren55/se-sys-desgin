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

    @Test void doubleSlashNormalizedCreatesDir() {
        // /usr//local 规范化为 /usr/local，父目录 /usr 不存在，忽略
        fs.mkdir("/usr//local");
        assertEquals("", fs.ls("/"));
    }

    @Test void trailingSlashNormalizedCreatesDir() {
        // /usr/ 规范化为 /usr，创建成功
        fs.mkdir("/usr/");
        assertEquals("usr", fs.ls("/"));
    }

    @Test void dotSegmentNormalizedCreatesDir() {
        // /usr/./bin 规范化为 /usr/bin，父 /usr 不存在，忽略
        fs.mkdir("/usr/./bin");
        assertEquals("", fs.ls("/"));
    }

    @Test void dotDotSegmentNormalizedCreatesDir() {
        // /usr/../bin 规范化为 /bin，根下直接创建
        fs.mkdir("/usr/../bin");
        assertEquals("bin", fs.ls("/"));
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

    @Test void mkdirRootIsIgnored() {
        fs.mkdir("/");
        assertEquals("", fs.ls("/"));
    }
}