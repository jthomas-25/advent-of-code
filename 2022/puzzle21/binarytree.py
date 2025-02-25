class BinaryTree:
    class Node:
        def __init__(self, data):
            self.data = data
            self.left = None
            self.right = None

        def get_value(self):
            return self.data

        def is_leaf(self):
            return self.left is None and self.right is None
        
        def is_parent(self):
            return not self.is_leaf()

    def __init__(self):
        self.root = None

    def is_empty(self):
        return self.root is None

    def insert(self, value):
        self.root = self.Node(value)
        return self.root

    def insert_left(self, value, node):
        node.left = self.Node(value)
        return node.left
    
    def insert_right(self, value, node):
        node.right = self.Node(value)
        return node.right

    def search_at(self, target, node):
        if node is None:
            return None
        elif node.data.equals(target):
            return node
        else:
            left_subtree_node = self.search_at(target, node.left)
            if left_subtree_node:
                return left_subtree_node
            else:
                return self.search_at(target, node.right)

    def search(self, target):
        node = self.search_at(target, self.root)
        if node:
            return node.data

    def preorder_print(self, node):
        if node:
            print(node.data)
            self.preorder_print(node.left)
            self.preorder_print(node.right)

    def print(self):
        self.preorder_print(self.root)
    
class MonkeyTree(BinaryTree):
    def __init__(self):
        super().__init__()

    def postorder_traverse(self, node):
        if node.is_leaf():
            return node.data.yell()
        else:
            left_child_num = self.postorder_traverse(node.left)
            right_child_num = self.postorder_traverse(node.right)
            return node.data.yell(left_child_num, right_child_num)
    
    def traverse(self):
        return self.postorder_traverse(self.root)
    
    def solve_for_variable_left_child(self, parent_monkey, right_child_num):
        operation = parent_monkey.get_operation()
        if operation == "+":
            return parent_monkey.number - right_child_num
        elif operation == "-":
            return parent_monkey.number + right_child_num
        elif operation == "*":
            return parent_monkey.number // right_child_num
        elif operation == "/":
            return parent_monkey.number * right_child_num

    def solve_for_variable_right_child(self, parent_monkey, left_child_num):
        operation = parent_monkey.get_operation()
        if operation == "+":
            return parent_monkey.number - left_child_num
        elif operation == "-":
            return left_child_num - parent_monkey.number
        elif operation == "*":
            return parent_monkey.number // left_child_num
        elif operation == "/":
            return left_child_num // parent_monkey.number

    def recalculate_subtree(self, node, human):
        left, right = node.left, node.right
        if self.search_at(human, left):
            left.data.number = self.solve_for_variable_left_child(node.data, right.data.number)
            if left.is_parent():
                self.recalculate_subtree(left, human)
        else:
            right.data.number = self.solve_for_variable_right_child(node.data, left.data.number)
            if right.is_parent():
                self.recalculate_subtree(right, human)
    
    def recalculate_subtree_containing_human(self, human):
        if self.search_at(human, self.root.left):
            start = self.root.left
            start.data.number = self.root.right.data.number
        else:
            start = tree.root.right
            start.data.number = self.root.left.data.number
        self.recalculate_subtree(start, human)
