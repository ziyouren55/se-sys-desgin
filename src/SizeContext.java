import java.util.HashSet;
import java.util.Set;

public class SizeContext {
    private final Set<Node> visited = new HashSet<>();

    public boolean visit(Node node) {
        return visited.add(node);
    }
}
