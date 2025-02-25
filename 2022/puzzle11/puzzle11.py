import sys
import re
from collections import deque


NUM_ROUNDS = 20


class Monkey:
    def __init__(self, id, items, operation, divisor, recipients):
        self.id = id
        self.items = items
        self.operation = operation
        self.divisor = divisor
        self.recipients = recipients
        self.num_inspections = 0

    def inspect_item(self):
        self.num_inspections += 1
        worry_level = self.items.popleft()
        operator, value = self.operation
        value = worry_level if value == "old" else int(value)
        if operator == "+":
            worry_level += value
        elif operator == "*":
            worry_level *= value
        return worry_level

    def add_item(self, item):
        self.items.append(item)

    def throw_item(self, item, monkey):
        monkey.add_item(item)

    def is_divisible(self, worry_level):
        return worry_level % self.divisor == 0

    def take_turn(self, monkeys):
        while len(self.items) > 0:
            worry_level = self.inspect_item()
            worry_level //= 3
            recipient_id = self.recipients[0] if self.is_divisible(worry_level) else self.recipients[1]
            self.throw_item(worry_level, monkeys[recipient_id])

def play_game(monkeys):
    for i in range(NUM_ROUNDS):
        for monkey in monkeys:
            monkey.take_turn(monkeys)

def part1(monkeys):
    play_game(monkeys)
    inspections = [monkey.num_inspections for monkey in monkeys]
    inspections.sort()
    monkey_business = inspections[-1] * inspections[-2]
    print(monkey_business)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        monkeys = []
        done = False
        while not done:
            id = int(re.search("\d", file.readline()).group())
            items = deque(int(worry_level) for worry_level in re.findall("(\d+),?", file.readline()))
            operation = re.search("old (\+|\*) (old|\d+)", file.readline()).groups()
            divisor = int(re.search("\d+", file.readline()).group())
            recipients = [int(re.search("\d", file.readline()).group()) for i in range(2)]
            monkeys.append(Monkey(id, items, operation, divisor, recipients))
            if file.readline() == "":
                done = True
    
    part1(monkeys)


if __name__ == "__main__":
    main()

