import java.util.Collection;
import java.util.TreeMap;

public class DirNode implements Node {
    private final String name;
    private final TreeMap<String, Node> children = new TreeMap<>();

    public DirNode(String name) {
        this.name = name;
    }

    public Node getChild(String childName) {
        return children.get(childName);
    }

    public void putChild(Node node) {
        children.put(node.name(), node);
    }

    public void removeChild(String childName) {
        children.remove(childName);
    }

    public Collection<Node> listChildren() {
        return children.values();
    }

    @Override public String name() { return name; }
    @Override public NodeType type() { return NodeType.DIR; }

    @Override
    public long size(SizeContext ctx) {
        if (!ctx.visit(this)) return 0;
        long total = 0;
        for (Node child : children.values()) {
            total += child.size(ctx);
        }
        return total;
    }
}
