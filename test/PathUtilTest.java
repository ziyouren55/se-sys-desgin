import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PathUtilTest {

    @Test void rootIsValid() {
        assertArrayEquals(new String[0], PathUtil.parse("/"));
    }

    @Test void singleSegment() {
        assertArrayEquals(new String[]{"usr"}, PathUtil.parse("/usr"));
    }

    @Test void multipleSegments() {
        assertArrayEquals(new String[]{"usr", "local", "bin"}, PathUtil.parse("/usr/local/bin"));
    }

    @Test void doubleSlashIsIllegal() {
        assertNull(PathUtil.parse("/usr//local"));
    }

    @Test void trailingSlashIsIllegal() {
        assertNull(PathUtil.parse("/usr/local/"));
    }

    @Test void dotSegmentIsIllegal() {
        assertNull(PathUtil.parse("/usr/./bin"));
    }

    @Test void dotDotSegmentIsIllegal() {
        assertNull(PathUtil.parse("/usr/../bin"));
    }

    @Test void emptyStringIsIllegal() {
        assertNull(PathUtil.parse(""));
    }

    @Test void relativePathIsIllegal() {
        assertNull(PathUtil.parse("usr/local"));
    }
}
