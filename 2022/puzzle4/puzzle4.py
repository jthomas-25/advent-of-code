import sys


def is_subrange(range, target_range):
    return range.start in target_range and range.stop-1 in target_range

def ranges_overlap(range1, range2):
    if is_subrange(range1, range2) or is_subrange(range2, range1):
        return True
    else:
        return range1.start in range2 or range1.stop-1 in range2

def part1(pairs):
    total = 0
    for pair in pairs:
        range1, range2 = pair
        if is_subrange(range1, range2) or is_subrange(range2, range1):
            total += 1
    print(total)

def part2(pairs):
    total = 0
    for pair in pairs:
        range1, range2 = pair
        if ranges_overlap(range1, range2):
            total += 1
    print(total)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        pairs = []
        for line in file:
            pair = []
            ranges = line.rstrip().split(",")
            for r in ranges:
                start, stop = [int(bound) for bound in r.split("-")]
                pair.append(range(start, stop+1))
            pairs.append(pair)
    
    part1(pairs)
    part2(pairs)


if __name__ == "__main__":
    main()
