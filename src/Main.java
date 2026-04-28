import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 3);
            String cmd = parts[0];

            switch (cmd) {
                case "MKDIR": {
                    if (parts.length < 2) break;
                    fs.mkdir(parts[1]);
                    break;
                }
                case "TOUCH": {
                    if (parts.length < 3) break;
                    long size;
                    try { size = Long.parseLong(parts[2]); } catch (NumberFormatException e) { break; }
                    fs.touch(parts[1], size);
                    break;
                }
                case "LS": {
                    if (parts.length < 2) break;
                    String result = fs.ls(parts[1]);
                    if (result != null && !result.isEmpty()) System.out.println(result);
                    break;
                }
                case "INFO": {
                    if (parts.length < 2) break;
                    long result = fs.info(parts[1]);
                    if (result >= 0) System.out.println(result);
                    break;
                }
            }
        }
    }
}
