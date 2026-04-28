import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LsTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void listRootMixed() {
        fs.mkdir("/usr");
        fs.touch("/readme.md", 50);
        // TreeMap 字典序：readme.md < usr
        assertEquals("readme.md\nusr", fs.ls("/"));
    }

    @Test void listEmptyDir() {
        fs.mkdir("/empty");
        assertEquals("", fs.ls("/empty"));
    }

    @Test void lsOnFileReturnsFileName() {
        fs.touch("/readme.md", 50);
        assertEquals("readme.md", fs.ls("/readme.md"));
    }

    @Test void alphabeticalOrder() {
        fs.touch("/z.txt", 1);
        fs.touch("/a.txt", 1);
        fs.touch("/m.txt", 1);
        assertEquals("a.txt\nm.txt\nz.txt", fs.ls("/"));
    }

    @Test void lsRootEmpty() {
        assertEquals("", fs.ls("/"));
    }
}
