import sys
from collections import deque


START = "S"
END = "E"
MIN_ELEVATION = "a"
MAX_ELEVATION = "z"


def find_start_and_end(grid):
    start = None
    end = None
    for y in range(len(grid)):
        for x in range(len(grid[0])):
            square = grid[y][x]
            if square == START:
                start = (x, y)
            elif square == END:
                end = (x, y)
    return start, end

def get_neighbors(x, y):
    # Von Neumann neighborhood of 1
    return [(x + 1, y),
            (x - 1, y),
            (x, y + 1),
            (x, y - 1)]

def in_bounds(x, y, grid):
    return x in range(0, len(grid[0])) and y in range(0, len(grid))

def get_elevation(x, y, grid):
    square = grid[y][x]
    if square == START:
        return ord(MIN_ELEVATION)
    elif square == END:
        return ord(MAX_ELEVATION)
    else:
        return ord(square)

def get_cost(neighbor, current, grid):
    # the cost of an edge is (distance from a to b) + 1
    # we add 1 as a constant since we always take one step
    diff_e = get_elevation(*neighbor, grid) - get_elevation(*current, grid)
    return abs(diff_e) + 1 if diff_e <= 1 else sys.maxsize

def shortest_path(start, end, grid):
    # Dijkstra's algorithm
    tentative = {(x, y) : sys.maxsize for y in range(len(grid)) for x in range(len(grid[0]))}
    tentative[start] = 0

    prev = {start: None}

    queue = deque()
    queue.append(start)

    while len(queue) > 0:
        current = queue.popleft()
        for neighbor in get_neighbors(*current):
            if in_bounds(*neighbor, grid):
                distance = tentative[current] + get_cost(neighbor, current, grid)
                if tentative[neighbor] > distance:
                    tentative[neighbor] = distance
                    prev[neighbor] = current
                    queue.append(neighbor)

    path = []
    current = end
    if prev.get(current) is not None:
        while current is not None:
            path.append(current)
            current = prev.get(current)
    
    return path

def show_path(path, grid):
    if path:
        for y in range(len(grid)):
            row = [None] * len(grid[0])
            for x in range(len(row)):
                if (x, y) in path:
                    row[x] = grid[y][x].capitalize()
                else:
                    row[x] = grid[y][x]
            print("".join(row))

def part1(start, end, grid):
    path = shortest_path(start, end, grid)
    #show_path(path, grid)
    length = len(path) 
    print(length-1)
    return length

def part2(first_path_length, end, grid):
    choices = [(x, y) for y in range(len(grid)) for x in range(len(grid[0])) if grid[y][x] == MIN_ELEVATION]
    lengths = [first_path_length]
    lengths.extend(len(path) for c in choices if (path := shortest_path(c, end, grid)))
    print(min(lengths)-1)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        heightmap = [line.rstrip() for line in file]
    
    start, end = find_start_and_end(heightmap)
    first_path_length = part1(start, end, heightmap)
    part2(first_path_length, end, heightmap)


if __name__ == "__main__":
    main()
