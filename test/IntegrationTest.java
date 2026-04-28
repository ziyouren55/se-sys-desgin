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
            return buf.toString();
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
}
