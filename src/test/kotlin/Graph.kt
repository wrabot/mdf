fun <Node : Any> shortPath(
    start: Node,
    end: Node,
    cost: (origin: Node, destination: Node) -> Int = { _, _ -> 1 },
    estimatedEndCost: (Node) -> Int = { 0 }, // A*
    neighbors: (Node) -> List<Node>
) = shortPath(start, isEnd = { this == end }, cost, estimatedEndCost, neighbors)

fun <Node : Any> shortPath(
    start: Node,
    isEnd: Node.() -> Boolean,
    cost: (origin: Node, destination: Node) -> Int = { _, _ -> 1 },
    toEndMinimalCost: (Node) -> Int = { 0 }, // A*
    neighbors: Node.() -> List<Node>
): List<Node> {
    val extendedStart = ExtendedNode(start, 0, toEndMinimalCost(start), true)
    val extendedNodes = mutableMapOf(start to extendedStart)
    val todo = mutableListOf(extendedStart)
    while (true) {
        val currentExtendedNode = todo.removeFirstOrNull() ?: return emptyList()
        currentExtendedNode.todo = false
        if (currentExtendedNode.node.isEnd()) return generateSequence(currentExtendedNode) { it.predecessor }.map { it.node }.toList().reversed()
        neighbors(currentExtendedNode.node).forEach { nextNode ->
            val newFromStartCost = currentExtendedNode.fromStartCost + cost(currentExtendedNode.node, nextNode)
            val nextExtendedNode = extendedNodes[nextNode]
            when {
                nextExtendedNode == null -> {
                    val extendedNode = ExtendedNode(nextNode, newFromStartCost, newFromStartCost + toEndMinimalCost(nextNode), true)
                    extendedNode.predecessor = currentExtendedNode
                    extendedNodes[nextNode] = extendedNode
                    todo.add(-todo.binarySearch(extendedNode) - 1, extendedNode)
                }
                newFromStartCost < nextExtendedNode.fromStartCost -> {
                    var toIndex = todo.size
                    if (nextExtendedNode.todo) {
                        toIndex = todo.binarySearch(nextExtendedNode)
                        todo.removeAt(toIndex)
                    }
                    nextExtendedNode.predecessor = currentExtendedNode
                    nextExtendedNode.minimalCost -= nextExtendedNode.fromStartCost - newFromStartCost
                    nextExtendedNode.fromStartCost = newFromStartCost
                    nextExtendedNode.todo = true
                    todo.add(-todo.binarySearch(nextExtendedNode, toIndex = toIndex) - 1, nextExtendedNode)
                }
            }
        }
    }
}

private data class ExtendedNode<Node : Any>(val node: Node, var fromStartCost: Int, var minimalCost: Int, var todo: Boolean) : Comparable<ExtendedNode<Node>> {
    var predecessor: ExtendedNode<Node>? = null

    override fun compareTo(other: ExtendedNode<Node>): Int {
        val compare = minimalCost.compareTo(other.minimalCost)
        return if (compare != 0) compare else id.compareTo(other.id)
    }

    private val id = nextId++

    companion object {
        private var nextId = Int.MIN_VALUE
    }
}
