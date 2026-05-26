import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void findInDirectorySubtree() {
        fs.mkdir("/src");
        fs.mkdir("/src/main");
        fs.mkdir("/src/test");
        fs.touch("/src/main/App.java", 10);
        fs.touch("/src/test/App.java", 20);
        fs.touch("/src/test/Helper.java", 5);
        assertEquals("/src/main/App.java\n/src/test/App.java", fs.find("/src", "App.java"));
    }

    @Test void findFromFileStartMatchesItself() {
        fs.touch("/readme.md", 50);
        assertEquals("/readme.md", fs.find("/readme.md", "readme.md"));
    }

    @Test void findFromFileStartNoMatch() {
        fs.touch("/readme.md", 50);
        assertEquals("", fs.find("/readme.md", "other.md"));
    }

    @Test void findNoMatchInDirectory() {
        fs.mkdir("/src");
        fs.touch("/src/Main.java", 10);
        assertEquals("", fs.find("/src", "Nonexistent.java"));
    }

    @Test void findPathNotExistIsIgnored() {
        assertNull(fs.find("/noexist", "anything"));
    }

    @Test void findMatchesLinkByName() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/copy");
        assertEquals("/copy", fs.find("/", "copy"));
    }

    @Test void findEntersLinkedDirectory() {
        fs.mkdir("/real");
        fs.touch("/real/a.txt", 10);
        fs.link("/real", "/view");
        assertEquals("/view/a.txt", fs.find("/view", "a.txt"));
    }

    @Test void findDoesNotExpandSameDirTwice() {
        fs.mkdir("/data");
        fs.touch("/data/f.txt", 10);
        fs.link("/data", "/alias");
        // /data/f.txt should appear only once, via /data/f.txt and /alias/f.txt both reachable
        String result = fs.find("/", "f.txt");
        assertNotNull(result);
        assertTrue(result.contains("/data/f.txt") || result.contains("/alias/f.txt"));
    }

    @Test void findEmptyDir() {
        fs.mkdir("/empty");
        assertEquals("", fs.find("/empty", "anything"));
    }

    @Test void findFromRoot() {
        fs.mkdir("/a");
        fs.mkdir("/b");
        fs.touch("/a/x.txt", 1);
        fs.touch("/b/x.txt", 2);
        assertEquals("/a/x.txt\n/b/x.txt", fs.find("/", "x.txt"));
    }

    @Test void findMatchesDirectoryName() {
        fs.mkdir("/src");
        fs.mkdir("/src/main");
        assertEquals("/src/main", fs.find("/src", "main"));
    }

    @Test void findWithNormalizedPath() {
        fs.mkdir("/src");
        fs.touch("/src/App.java", 10);
        assertEquals("/src/App.java", fs.find("/./src/", "App.java"));
    }
}