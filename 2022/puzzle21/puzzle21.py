import sys
import re
from collections import deque
from binarytree import MonkeyTree


class Monkey:
    def __init__(self, name):
        self.name = name
        self.number = None
    
    def __str__(self):
        return f"{self.name}: {self.number}"
    
    def equals(self, monkey):
        return self.name == monkey.name

class NumberMonkey(Monkey):
    def __init__(self, name, number):
        super().__init__(name)
        self.number = number

    def yell(self):
        return self.number

class MathMonkey(Monkey):
    def add(a, b):
        return a + b
    
    def subtract(a, b):
        return a - b
    
    def multiply(a, b):
        return a * b
    
    def divide(a, b):
        return a // b

    jobs = {"+": add,
            "-": subtract,
            "*": multiply,
            "/": divide}

    def __init__(self, name, job):
        super().__init__(name)
        self.job = job

    def get_operation(self):
        return self.job[1]

    def yell(self, left_monkey_num, right_monkey_num):
        operation = self.job[1]
        do_math = MathMonkey.jobs.get(operation)
        self.number = do_math(left_monkey_num, right_monkey_num)
        return self.number

def build_tree(names_to_monkeys):
    tree = MonkeyTree()
    root = tree.insert(names_to_monkeys["root"])

    queue = deque()
    queue.append(root)
    while len(queue) > 0:
        parent = queue.popleft()
        math_monkey = parent.get_value()
        name1, operation, name2 = math_monkey.job
        left_monkey = names_to_monkeys[name1]
        right_monkey = names_to_monkeys[name2]
        left = tree.insert_left(left_monkey, parent)
        right = tree.insert_right(right_monkey, parent)
        if left_monkey.number is None:
            queue.append(left)
        if right_monkey.number is None:
            queue.append(right)
    
    return tree

def part1(tree):
    root_number = tree.traverse()
    print(root_number)
    
def part2(tree, me):
    tree.recalculate_subtree_containing_human(me)
    print(me.number)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        names_to_monkeys = {}
        for line in file:
            name, job_string = line.rstrip().split(": ")
            match = re.search("\d+", job_string)
            if match:
                number = int(match.group(0))
                monkey = NumberMonkey(name, number)
                names_to_monkeys[name] = monkey
            else:
                job = job_string.split()
                monkey = MathMonkey(name, job)
                names_to_monkeys[name] = monkey
    
    tree = build_tree(names_to_monkeys)
    part1(tree)
    me = names_to_monkeys["humn"]
    part2(tree, me)
    

if __name__ == "__main__":
    main()
