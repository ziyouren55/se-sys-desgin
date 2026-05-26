import java.util.ArrayList;
import java.util.List;

public class PathUtil {

    /**
     * 规范化绝对路径。返回 null 表示不是绝对路径。
     * 处理：冗余 /、.、..、尾斜杠。
     */
    public static String normalize(String path) {
        if (path == null || path.isEmpty()) return null;
        if (!path.startsWith("/")) return null;

        String[] parts = path.split("/");
        List<String> stack = new ArrayList<>();
        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) continue;
            if (part.equals("..")) {
                if (!stack.isEmpty()) stack.remove(stack.size() - 1);
            } else {
                stack.add(part);
            }
        }

        if (stack.isEmpty()) return "/";
        StringBuilder sb = new StringBuilder();
        for (String s : stack) {
            sb.append('/').append(s);
        }
        return sb.toString();
    }

    /**
     * 将绝对路径解析为路径段数组。
     * 返回 null 表示非法路径，返回空数组表示根路径 "/"。
     */
    public static String[] parse(String path) {
        String normalized = normalize(path);
        if (normalized == null) return null;
        if (normalized.equals("/")) return new String[0];
        return normalized.substring(1).split("/");
    }
}