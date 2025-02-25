class Tree:
    PRINT_SPACE_WIDTH = 2

    class Node:
        def __init__(self, data):
            self.data = data
            self.children = None
            self.parent = None
        
    def __init__(self):
        self.root = None

    def is_empty(self):
        return self.root is None
    
    def insert_at(self, value, node=None):
        if node is None:
            self.root = self.Node(value)
            return self.root
        elif node.data.equals(value):
            return node
        else:
            if node.children is None:
                node.children = []
            for child in node.children:
                if child.data.equals(value):
                    return child
            child = self.Node(value)
            node.children.append(child)
            child.parent = node
            return child
    
    def search_at(self, target, node=None):
        if self.root is None:
            return None
        elif node is None:
            node = self.root
        if node.data.equals(target):
            return node
        elif node.children is not None:
            for child in node.children:
                match = self.search_at(target, child)
                if match:
                    return match

    def search(self, target):
        node = self.search_at(target)
        if node:
            return node.data
    
    def contains(self, target):
        return self.search(target) is not None
    
    def preorder_print(self, node=None, depth=0):
        if node is None:
            node = self.root
        spaces = " " * (Tree.PRINT_SPACE_WIDTH * depth)
        print(f"{spaces}{node.data} ")
        if node.children is not None:
            for child in node.children:
                self.preorder_print(child, depth+1)

    def print(self):
        self.preorder_print()

class Filesystem(Tree):
    ROOT_DIR = "/"
    PARENT_DIR = ".."

    def __init__(self):
        super().__init__()
        self.insert_at(Directory(Filesystem.ROOT_DIR))
        # present working directory
        self.pwd = self.root
        self.capacity = 70000000

    def mkdir(self, dir_name):
        dir_name = self.to_absolute_pathname(dir_name)
        directory = Directory(dir_name)
        self.insert_at(directory, self.pwd)

    def create_file(self, file_name, size=0):
        file_name = self.to_absolute_pathname(file_name)
        file = File(file_name, size)
        self.insert_at(file, self.pwd)
    
    def cd(self, dir_name):
        if dir_name == Filesystem.ROOT_DIR:
            self.pwd = self.root
        elif dir_name == Filesystem.PARENT_DIR:
            if self.pwd is not self.root:
                self.pwd = self.pwd.parent
        elif self.pwd.children is not None:
            pathname = self.to_absolute_pathname(dir_name)
            for child in self.pwd.children:
                if child.data.is_directory and child.data.name == pathname:
                    self.pwd = child
                    break

    def ls(self):
        if self.pwd.children is not None:
            print("\n".join(str(child.data) for child in self.pwd.children))

    def to_absolute_pathname(self, file_name):
        if file_name.startswith("/"):
            return file_name
        else:
            return f"{self.pwd.data.name}{'' if self.pwd is self.root else '/'}{file_name}"

    def size(self, node=None):
        if node is None:
            node = self.root
        if not node.data.is_directory:
            return node.data.size
        elif node.children is None:
            return 0
        else:
            return sum(self.size(child) for child in node.children)

    def get_dirs(self, node=None, dirs=[]):
        if node is None:
            node = self.root
        if node.data.is_directory:
            dirs.append(node)
            if node.children is not None:
                for child in node.children:
                    self.get_dirs(child, dirs)
        return dirs

    def calc_free_space(self):
        return self.capacity - self.size()

class File:
    def __init__(self, name, size=0, is_directory=False):
        # absolute pathname
        self.name = name
        self.size = size
        self.is_directory = is_directory

    def __str__(self):
        if self.name == Filesystem.ROOT_DIR:
            name = self.name
        else:
            name = self.name[(self.name.rfind("/") + 1):]
        return f"dir {name}" if self.is_directory else f"{self.size} {name}"

    def equals(self, file):
        return self.name == file.name

class Directory(File):
    def __init__(self, pathname):
        super().__init__(pathname, is_directory=True)
