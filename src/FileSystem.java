import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileSystem {
    private final DirNode root = new DirNode("");

    // 跟随链接链，返回最终非链接节点
    private Node followLinks(Node node) {
        while (node.type() == NodeType.LINK) {
            node = ((LinkNode) node).target();
        }
        return node;
    }

    // 解析路径，跟随全部链接（含末段），返回最终目标节点
    private Node resolveNode(String[] segments) {
        if (segments.length == 0) return root;
        DirNode parent = resolveParentDir(segments);
        if (parent == null) return null;
        Node entry = parent.getChild(segments[segments.length - 1]);
        if (entry == null) return null;
        return followLinks(entry);
    }

    // 解析路径，仅跟随中间段链接，末段返回目录项本身（可能是链接）
    private Node resolveEntry(String[] segments) {
        if (segments.length == 0) return root;
        DirNode parent = resolveParentDir(segments);
        if (parent == null) return null;
        return parent.getChild(segments[segments.length - 1]);
    }

    // 解析到父目录，跟随所有中间段链接
    private DirNode resolveParentDir(String[] segments) {
        DirNode cur = root;
        for (int i = 0; i < segments.length - 1; i++) {
            Node next = cur.getChild(segments[i]);
            if (next == null) return null;
            next = followLinks(next);
            if (next.type() != NodeType.DIR) return null;
            cur = (DirNode) next;
        }
        return cur;
    }

    // 列出目录子节点（按 TreeMap 字典序）
    private String listChildren(DirNode dir) {
        StringBuilder sb = new StringBuilder();
        for (Node child : dir.listChildren()) {
            if (sb.length() > 0) sb.append('\n');
            sb.append(child.name());
        }
        return sb.toString();
    }

    // ---- 命令实现 ----

    public void mkdir(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null || segments.length == 0) return;

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return;

        String name = segments[segments.length - 1];
        Node existing = parent.getChild(name);
        if (existing != null && existing.type() != NodeType.LINK && existing.type() == NodeType.DIR) {
            return; // 已是目录，保持不变
        }
        parent.putChild(new DirNode(name));
    }

    public void touch(String path, long size) {
        String[] segments = PathUtil.parse(path);
        if (segments == null || segments.length == 0) return;

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return;

        String name = segments[segments.length - 1];
        Node existing = parent.getChild(name);
        if (existing != null && existing.type() != NodeType.LINK && existing.type() == NodeType.FILE) {
            ((FileNode) existing).setSize(size);
        } else {
            parent.putChild(new FileNode(name, size));
        }
    }

    public String ls(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return null;

        if (segments.length == 0) {
            return listChildren(root);
        }

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return null;

        String name = segments[segments.length - 1];
        Node entry = parent.getChild(name);
        if (entry == null) return null;

        if (entry.type() == NodeType.FILE) {
            return entry.name();
        }
        if (entry.type() == NodeType.DIR) {
            return listChildren((DirNode) entry);
        }
        // LinkNode: 跟随链接
        Node target = followLinks(entry);
        if (target.type() == NodeType.FILE) {
            return entry.name(); // 链接指向文件 → 输出链接自身名称
        }
        return listChildren((DirNode) target); // 链接指向目录 → 列出目标目录子节点
    }

    public long info(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return -1;

        Node target = resolveNode(segments); // 跟随全部链接
        if (target == null) return -1;

        return target.size(new SizeContext());
    }

    public String find(String path, String targetName) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return null;

        Node entry = resolveEntry(segments); // 不跟随末段链接，以保留链接自身名称
        if (entry == null) return null;

        String startPath = PathUtil.normalize(path);
        Node actual = followLinks(entry);

        List<String> results = new ArrayList<>();
        Set<Node> expanded = new HashSet<>();

        findRecursive(entry, actual, startPath, targetName, expanded, results);

        if (results.isEmpty()) return "";
        Collections.sort(results);
        StringBuilder sb = new StringBuilder();
        for (String r : results) {
            if (sb.length() > 0) sb.append('\n');
            sb.append(r);
        }
        return sb.toString();
    }

    private void findRecursive(Node entry, Node actual, String currentPath, String targetName,
                               Set<Node> expanded, List<String> results) {
        if (entry.name().equals(targetName)) {
            results.add(currentPath);
        }

        if (actual.type() == NodeType.DIR) {
            if (!expanded.add(actual)) return;
            DirNode dir = (DirNode) actual;
            for (Node child : dir.listChildren()) {
                if (child.type() == NodeType.LINK) continue;
                String childPath = currentPath.equals("/") ? "/" + child.name() : currentPath + "/" + child.name();
                Node childActual = followLinks(child);
                findRecursive(child, childActual, childPath,
                              targetName, expanded, results);
            }
            for (Node child : dir.listChildren()) {
                if (child.type() != NodeType.LINK) continue;
                String childPath = currentPath.equals("/") ? "/" + child.name() : currentPath + "/" + child.name();
                Node childActual = followLinks(child);
                findRecursive(child, childActual, childPath,
                              targetName, expanded, results);
            }
        }
    }

    public void rm(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null || segments.length == 0) return; // 根目录不可删除

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return;

        String name = segments[segments.length - 1];
        Node entry = parent.getChild(name);
        if (entry == null) return;

        // 目录（非链接到目录）必须为空才能删除
        if (entry.type() != NodeType.LINK && entry.type() == NodeType.DIR) {
            if (((DirNode) entry).listChildren().iterator().hasNext()) return;
        }

        parent.removeChild(name);
    }

    public void link(String srcPath, String dstPath) {
        String[] srcSegments = PathUtil.parse(srcPath);
        if (srcSegments == null) return;

        String[] dstSegments = PathUtil.parse(dstPath);
        if (dstSegments == null || dstSegments.length == 0) return; // 不能链接到根目录

        Node srcNode = resolveNode(srcSegments); // 跟随源路径的全部链接
        if (srcNode == null) return;

        DirNode dstParent = resolveParentDir(dstSegments);
        if (dstParent == null) return;

        String dstName = dstSegments[dstSegments.length - 1];
        dstParent.putChild(new LinkNode(dstName, srcNode));
    }
}
