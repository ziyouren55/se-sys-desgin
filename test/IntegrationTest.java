import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private String run(String input) throws Exception {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            System.setOut(new PrintStream(buf));
            Main.main(new String[0]);
            return buf.toString().replace("\r\n", "\n");
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }

    @Test void specExample_LS1_listRootChildren() throws Exception {
        String out = run("MKDIR /usr\nTOUCH /readme.md 50\nLS /\n");
        assertEquals("readme.md\nusr\n", out);
    }

    @Test void specExample_LS2_lsOnFile() throws Exception {
        String out = run("TOUCH /readme.md 50\nLS /readme.md\n");
        assertEquals("readme.md\n", out);
    }

    @Test void specExample_INFO1_fileSize() throws Exception {
        String out = run("TOUCH /readme.md 50\nINFO /readme.md\n");
        assertEquals("50\n", out);
    }

    @Test void specExample_INFO2_dirRecursiveSize() throws Exception {
        String out = run("MKDIR /usr\nMKDIR /usr/local\nTOUCH /usr/local/test.txt 100\nTOUCH /readme.md 50\nINFO /\n");
        assertEquals("150\n", out);
    }

    @Test void specExample_section4_combined() throws Exception {
        String input = "MKDIR /usr\nMKDIR /usr/local\nTOUCH /usr/local/test.txt 100\nTOUCH /readme.md 50\nLS /\nINFO /\nINFO /usr\n";
        String out = run(input);
        assertEquals("readme.md\nusr\n150\n100\n", out);
    }

    // ---- 迭代二 题目示例 ----

    @Test void iter2_pathNormalization() throws Exception {
        String input = "MKDIR /usr\nMKDIR //usr///local\nTOUCH /usr/local/./a.txt 10\nTOUCH /usr/local/../b.txt 5\nLS /usr//local/\nINFO /usr/./local/../\n";
        String out = run(input);
        assertEquals("a.txt\n15\n", out);
    }

    @Test void iter2_findInSubtree() throws Exception {
        String input = "MKDIR /src\nMKDIR /src/main\nMKDIR /src/test\nTOUCH /src/main/App.java 10\nTOUCH /src/test/App.java 20\nTOUCH /src/test/Helper.java 5\nFIND /src App.java\n";
        String out = run(input);
        assertEquals("/src/main/App.java\n/src/test/App.java\n", out);
    }

    @Test void iter2_findFromFile() throws Exception {
        String input = "TOUCH /readme.md 50\nFIND /readme.md readme.md\nFIND /readme.md other.md\n";
        String out = run(input);
        assertEquals("/readme.md\n", out);
    }

    @Test void iter2_rmFileAndEmptyDir() throws Exception {
        String input = "MKDIR /tmp\nTOUCH /tmp/a.bin 7\nLS /tmp\nRM /tmp/a.bin\nLS /tmp\nRM /tmp\nLS /\n";
        String out = run(input);
        assertEquals("a.bin\n", out);
    }

    @Test void iter2_rmNonEmptyDir() throws Exception {
        String input = "MKDIR /tmp\nMKDIR /tmp/cache\nTOUCH /tmp/cache/a.bin 7\nRM /tmp/cache\nLS /tmp\n";
        String out = run(input);
        assertEquals("cache\n", out);
    }

    @Test void iter2_linkToFile() throws Exception {
        String input = "TOUCH /data.bin 12\nLINK /data.bin /copy\nLS /\nLS /copy\nINFO /copy\n";
        String out = run(input);
        assertEquals("copy\ndata.bin\ncopy\n12\n", out);
    }

    @Test void iter2_linkToDir() throws Exception {
        String input = "MKDIR /real\nTOUCH /real/a.txt 10\nLINK /real /view\nLS /view\nTOUCH /view/b.txt 5\nLS /real\nINFO /view\n";
        String out = run(input);
        assertEquals("a.txt\na.txt\nb.txt\n15\n", out);
    }

    @Test void iter2_infoDedupLink() throws Exception {
        String input = "MKDIR /data\nTOUCH /data/a 10\nTOUCH /data/b 20\nLINK /data /alias\nINFO /data\nINFO /alias\nINFO /\n";
        String out = run(input);
        assertEquals("30\n30\n30\n", out);
    }

    @Test void iter2_overwriteSemantics() throws Exception {
        String input = "TOUCH /x 5\nLINK /x /y\nINFO /\nTOUCH /y 30\nINFO /\nMKDIR /y\nLS /\nINFO /\n";
        String out = run(input);
        assertEquals("5\n35\nx\ny\n5\n", out);
    }

    @Test void iter2_section6_combined() throws Exception {
        String input = "MKDIR /usr\nMKDIR //usr///local\nTOUCH /usr/local/./a.txt 10\nTOUCH /usr/local/../b.txt 5\nLINK /usr/local /alias\nLS /\nINFO /\nFIND / a.txt\nRM /alias\nLS /\n";
        String out = run(input);
        assertEquals("alias\nusr\n15\n/usr/local/a.txt\nusr\n", out);
    }
}
