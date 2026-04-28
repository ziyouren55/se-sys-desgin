public class FileSystem {
    private final DirNode root = new DirNode("");

    public void mkdir(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return;

        if (segments.length == 0) return; // 根目录已存在

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return;

        String name = segments[segments.length - 1];
        Node existing = parent.getChild(name);
        if (existing instanceof DirNode) return; // 已是目录，保持不变
        parent.putChild(new DirNode(name));
    }

    public void touch(String path, long size) {
        String[] segments = PathUtil.parse(path);
        if (segments == null || segments.length == 0) return;

        DirNode parent = resolveParentDir(segments);
        if (parent == null) return;

        String name = segments[segments.length - 1];
        Node existing = parent.getChild(name);
        if (existing instanceof FileNode) {
            ((FileNode) existing).setSize(size);
        } else {
            parent.putChild(new FileNode(name, size));
        }
    }

    public String ls(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return null;

        Node target = resolveNode(segments);
        if (target == null) return null;

        if (target instanceof FileNode) {
            return target.name();
        }

        StringBuilder sb = new StringBuilder();
        for (Node child : ((DirNode) target).listChildren()) {
            if (sb.length() > 0) sb.append('\n');
            sb.append(child.name());
        }
        return sb.toString();
    }

    public long info(String path) {
        String[] segments = PathUtil.parse(path);
        if (segments == null) return -1;

        Node target = resolveNode(segments);
        if (target == null) return -1;

        return target.size(new SizeContext());
    }

    // 解析到目标节点
    private Node resolveNode(String[] segments) {
        if (segments.length == 0) return root;
        DirNode parent = resolveParentDir(segments);
        if (parent == null) return null;
        return parent.getChild(segments[segments.length - 1]);
    }

    // 解析到父目录（走到倒数第二段）
    private DirNode resolveParentDir(String[] segments) {
        DirNode cur = root;
        for (int i = 0; i < segments.length - 1; i++) {
            Node next = cur.getChild(segments[i]);
            if (!(next instanceof DirNode)) return null;
            cur = (DirNode) next;
        }
        return cur;
    }
}
