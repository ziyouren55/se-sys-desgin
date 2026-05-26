import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PathUtilTest {

    @Test void rootIsValid() {
        assertArrayEquals(new String[0], PathUtil.parse("/"));
        assertEquals("/", PathUtil.normalize("/"));
    }

    @Test void singleSegment() {
        assertArrayEquals(new String[]{"usr"}, PathUtil.parse("/usr"));
        assertEquals("/usr", PathUtil.normalize("/usr"));
    }

    @Test void multipleSegments() {
        assertArrayEquals(new String[]{"usr", "local", "bin"}, PathUtil.parse("/usr/local/bin"));
        assertEquals("/usr/local/bin", PathUtil.normalize("/usr/local/bin"));
    }

    @Test void doubleSlashNormalized() {
        assertArrayEquals(new String[]{"usr", "local"}, PathUtil.parse("/usr//local"));
        assertEquals("/usr/local", PathUtil.normalize("/usr//local"));
    }

    @Test void trailingSlashNormalized() {
        assertArrayEquals(new String[]{"usr", "local"}, PathUtil.parse("/usr/local/"));
        assertEquals("/usr/local", PathUtil.normalize("/usr/local/"));
    }

    @Test void dotSegmentNormalized() {
        assertArrayEquals(new String[]{"usr", "bin"}, PathUtil.parse("/usr/./bin"));
        assertEquals("/usr/bin", PathUtil.normalize("/usr/./bin"));
    }

    @Test void dotDotSegmentNormalized() {
        assertArrayEquals(new String[]{"bin"}, PathUtil.parse("/usr/../bin"));
        assertEquals("/bin", PathUtil.normalize("/usr/../bin"));
    }

    @Test void dotDotAtRootStaysAtRoot() {
        assertEquals("/", PathUtil.normalize("/.."));
        assertArrayEquals(new String[0], PathUtil.parse("/.."));
    }

    @Test void allRedundantSlashes() {
        assertEquals("/", PathUtil.normalize("///"));
    }

    @Test void emptyStringIsIllegal() {
        assertNull(PathUtil.parse(""));
        assertNull(PathUtil.normalize(""));
    }

    @Test void relativePathIsIllegal() {
        assertNull(PathUtil.parse("usr/local"));
        assertNull(PathUtil.normalize("usr/local"));
    }

    @Test void complexNormalization() {
        assertEquals("/a/b", PathUtil.normalize("/a//b/./c/../"));
    }
}