import sys
import string


def part1(rucksacks, item_priorities):
    total = 0
    for sack in rucksacks:
        middle = len(sack) // 2
        compartment1 = sack[:middle]
        compartment2 = sack[middle:]
        common_item = set(compartment1).intersection(set(compartment2)).pop()
        total += item_priorities[common_item]
    print(total)

def part2(rucksacks, item_priorities):
    total = 0
    for i in range(0, len(rucksacks), 3):
        sack1, sack2, sack3 = rucksacks[i:i+3]
        common_item = set(sack1).intersection(set(sack2), set(sack3)).pop()
        total += item_priorities[common_item]
    print(total)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        rucksacks = [line.rstrip() for line in file]
    
    item_priorities = {char : i for i, char in enumerate(string.ascii_letters, start=1)}
    part1(rucksacks, item_priorities)
    part2(rucksacks, item_priorities)


if __name__ == "__main__":
    main()
