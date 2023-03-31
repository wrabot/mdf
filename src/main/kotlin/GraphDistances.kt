fun <Node : Any> distances(
    start: Node,
    cost: (origin: Node, destination: Node) -> Int = { _, _ -> 1 },
    neighbors: Node.() -> List<Node>
): Map<Node, Int> {
    val distances = mutableMapOf(start to 0)
    val todo = mutableListOf(start)
    while (true) {
        val currentNode = todo.removeFirstOrNull() ?: return distances
        val currentDistance = distances[currentNode]!!
        neighbors(currentNode).forEach { nextNode ->
            val nextDistance = currentDistance + cost(currentNode, nextNode)
            if (nextDistance < distances.getOrDefault(nextNode, Int.MAX_VALUE)) {
                distances[nextNode] = nextDistance
                if (nextNode !in todo) todo.add(nextNode)
            }
        }
    }
}
