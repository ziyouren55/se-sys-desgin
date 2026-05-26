public class LinkNode implements Node {
    private final String name;
    private final Node target;

    public LinkNode(String name, Node target) {
        this.name = name;
        this.target = target;
    }

    public Node target() { return target; }

    @Override public String name() { return name; }
    @Override public NodeType type() { return NodeType.LINK; }

    @Override
    public long size(SizeContext ctx) {
        return target.size(ctx);
    }
}