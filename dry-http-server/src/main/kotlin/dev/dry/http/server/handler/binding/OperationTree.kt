package dev.dry.http.server.handler.binding

class OperationTree {
    val roots = mutableListOf<OperationTreeNode>()

    data class OperationTreeNode(
        val name: String,
        val children: MutableList<OperationTreeNode> = mutableListOf(),
        val operations: MutableList<RequestHandlerOperationBinding> = mutableListOf(),
    )

    companion object {
        fun from(bindings: List<RequestHandlerOperationBinding>): OperationTree {
            val tree = OperationTree()
            for (binding in bindings) {
                var currentNode: OperationTreeNode? = null
                var nodes: MutableList<OperationTreeNode> = tree.roots
                for (part in binding.path.toString().split("/")) {
                    if (part.isNotEmpty()) {
                        val existingChild = nodes.find { it.name == part }
                        if (existingChild != null) {
                            currentNode = existingChild
                            nodes = existingChild.children
                        } else {
                            val newChild = OperationTreeNode(part)
                            nodes.add(newChild)
                            currentNode = newChild
                            nodes = newChild.children
                        }
                    }
                }

                currentNode?.operations?.add(binding)
            }
            return tree
        }

        fun printTree(tree: OperationTree) {
            for (node in tree.roots) {
                printTree(node)
            }
        }

        fun printTree(node: OperationTreeNode, depth: Int = 0) {
            println("${"  ".repeat(depth)}${node.name}")
            for (child in node.children) {
                printTree(child, depth + 1)
            }
        }
    }
}