import sys


def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        inventories = []
        inventory = []
        for line in file:
            if line == "\n":
                inventories.append(inventory)
                inventory = []
            else:
                calories = int(line)
                inventory.append(calories)
        inventories.append(inventory)

    # part 1
    total_cals = [sum(inv) for inv in inventories]
    total_cals.sort()
    # elf carrying the most calories
    print(total_cals[-1])
    
    # part 2
    # total of top three elves' calories
    print(sum(total_cals[-3:]))


if __name__ == "__main__":
    main()
