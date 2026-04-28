public class PathUtil {

    /**
     * 将绝对路径解析为路径段数组。
     * 返回 null 表示非法路径（调用方应静默忽略）。
     * 返回空数组表示根路径 "/"。
     */
    public static String[] parse(String path) {
        if (path == null || path.isEmpty()) return null;
        if (!path.startsWith("/")) return null;

        // 根路径
        if (path.equals("/")) return new String[0];

        // 尾斜杠（非根）
        if (path.endsWith("/")) return null;

        // 连续斜杠
        if (path.contains("//")) return null;

        // 切分路径段（跳过第一个空串）
        String[] segments = path.substring(1).split("/");

        for (String seg : segments) {
            if (seg.equals(".") || seg.equals("..")) return null;
        }

        return segments;
    }
}
