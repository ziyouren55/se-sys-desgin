import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    private FileSystem fs;

    @BeforeEach void setUp() { fs = new FileSystem(); }

    @Test void linkToFile() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/copy");
        assertEquals("copy\ndata.bin", fs.ls("/"));
    }

    @Test void lsOnLinkToFileShowsLinkName() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/copy");
        assertEquals("copy", fs.ls("/copy"));
    }

    @Test void infoOnLinkReturnsTargetSize() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/copy");
        assertEquals(12L, fs.info("/copy"));
    }

    @Test void linkToDirectory() {
        fs.mkdir("/real");
        fs.touch("/real/a.txt", 10);
        fs.link("/real", "/view");
        assertEquals("a.txt", fs.ls("/view"));
    }

    @Test void createFileThroughLink() {
        fs.mkdir("/real");
        fs.touch("/real/a.txt", 10);
        fs.link("/real", "/view");
        fs.touch("/view/b.txt", 5);
        assertEquals("a.txt\nb.txt", fs.ls("/real"));
    }

    @Test void infoDedupWithLinkSharing() {
        fs.mkdir("/data");
        fs.touch("/data/a", 10);
        fs.touch("/data/b", 20);
        fs.link("/data", "/alias");
        assertEquals(30L, fs.info("/data"));
        assertEquals(30L, fs.info("/alias"));
        assertEquals(30L, fs.info("/"));
    }

    @Test void linkSourceMustExist() {
        fs.link("/noexist", "/copy");
        assertEquals("", fs.ls("/"));
    }

    @Test void linkDstParentMustExist() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/noexist/copy");
        assertEquals("data.bin", fs.ls("/"));
    }

    @Test void linkCannotTargetRoot() {
        fs.touch("/data.bin", 12);
        fs.link("/data.bin", "/");
        assertEquals("data.bin", fs.ls("/"));
    }

    @Test void linkOverwritesExistingEntry() {
        fs.touch("/data.bin", 12);
        fs.touch("/copy", 5);
        fs.link("/data.bin", "/copy");
        assertEquals(12L, fs.info("/copy"));
    }

    @Test void linkToFileThenTouchOverwritesLink() {
        fs.touch("/x", 5);
        fs.link("/x", "/y");
        assertEquals(5L, fs.info("/"));
        fs.touch("/y", 30);
        // /y replaced by new file, /x unchanged
        assertEquals(35L, fs.info("/"));
        assertEquals(5L, fs.info("/x"));
    }

    @Test void linkToFileThenMkdirOverwritesLink() {
        fs.touch("/x", 5);
        fs.link("/x", "/y");
        fs.mkdir("/y");
        // /y replaced by empty dir, /x unchanged
        assertEquals("x\ny", fs.ls("/"));
        assertEquals(5L, fs.info("/"));
    }

    @Test void linkNormalizesPath() {
        fs.touch("/data.bin", 12);
        fs.link("/./data.bin/", "/copy");
        assertEquals(12L, fs.info("/copy"));
    }
}