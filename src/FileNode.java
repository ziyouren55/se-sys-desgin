public class FileNode implements Node {
    private final String name;
    private long fileSize;

    public FileNode(String name, long fileSize) {
        this.name = name;
        this.fileSize = fileSize;
    }

    public void setSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override public String name() { return name; }
    @Override public NodeType type() { return NodeType.FILE; }

    @Override
    public long size(SizeContext ctx) {
        if (!ctx.visit(this)) return 0;
        return fileSize;
    }
}