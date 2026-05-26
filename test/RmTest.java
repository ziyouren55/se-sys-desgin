import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RmTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void removeFile() {
        fs.touch("/a.bin", 7);
        fs.rm("/a.bin");
        assertEquals("", fs.ls("/"));
    }

    @Test void removeEmptyDir() {
        fs.mkdir("/tmp");
        fs.rm("/tmp");
        assertEquals("", fs.ls("/"));
    }

    @Test void removeNonEmptyDirIsIgnored() {
        fs.mkdir("/tmp");
        fs.mkdir("/tmp/cache");
        fs.touch("/tmp/cache/a.bin", 7);
        fs.rm("/tmp/cache");
        assertEquals("cache", fs.ls("/tmp"));
    }

    @Test void removeLinkDoesNotAffectTarget() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/copy");
        fs.rm("/copy");
        assertEquals("data.bin", fs.ls("/"));
        assertEquals(12L, fs.info("/data.bin"));
    }

    @Test void removeNonExistentIsIgnored() {
        fs.rm("/noexist");
        assertEquals("", fs.ls("/"));
    }

    @Test void removeRootIsIgnored() {
        fs.mkdir("/usr");
        fs.rm("/");
        assertEquals("usr", fs.ls("/"));
    }

    @Test void removeFromLinkedDir() {
        fs.mkdir("/real");
        fs.touch("/real/a.txt", 10);
        fs.link("/real", "/view");
        // RM through link
        fs.rm("/view/a.txt");
        assertEquals("", fs.ls("/real"));
        assertEquals("", fs.ls("/view"));
    }

    @Test void removeLinkToDir() {
        fs.mkdir("/real");
        fs.touch("/real/a.txt", 10);
        fs.link("/real", "/view");
        fs.rm("/view");
        assertEquals("a.txt", fs.ls("/real"));
    }

    @Test void cannotRemoveRootEvenWhenEmpty() {
        fs.rm("/");
        assertEquals("", fs.ls("/"));
    }
}