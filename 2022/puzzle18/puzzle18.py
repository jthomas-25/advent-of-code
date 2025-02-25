import sys
from collections import deque


NUM_FACES = 6


class Grid:
    def __init__(self):
        self.min_x, self.max_x = None, None
        self.min_y, self.max_y = None, None
        self.min_z, self.max_z = None, None
        self.lava_cubes = []

    def add_cube(self, cube_pos):
        self.lava_cubes.append(cube_pos)
        self.resize_if_needed(cube_pos)
    
    def resize_if_needed(self, cube_pos):
        x, y, z = cube_pos
        if self.min_x is None or self.min_x > x:
            self.min_x = x
        if self.max_x is None or self.max_x < x:
            self.max_x = x
        if self.min_y is None or self.min_y > y:
            self.min_y = y
        if self.max_y is None or self.max_y < y:
            self.max_y = y
        if self.min_z is None or self.min_z > z:
            self.min_z = z
        if self.max_z is None or self.max_z < z:
            self.max_z = z

    def in_bounds(self, x, y, z):
        return x in range(self.min_x, self.max_x + 1) and \
               y in range(self.min_y, self.max_y + 1) and \
               z in range(self.min_z, self.max_z + 1)

    def is_lava(self, pos):
        return pos in self.lava_cubes

    def surround_with_steam(self):
        self.min_x -= 1
        self.max_x += 1
        self.min_y -= 1
        self.max_y += 1
        self.min_z -= 1
        self.max_z += 1

def get_neighbors(x, y, z):
    # Von Neumann neighborhood of 1
    return [(x + 1, y, z),
            (x - 1, y, z),
            (x, y + 1, z),
            (x, y - 1, z),
            (x, y, z + 1),
            (x, y, z - 1)]

def part1(grid):
    surf_area = 0
    for cube in grid.lava_cubes:
        num_neighbors = sum(grid.is_lava(neighbor) for neighbor in get_neighbors(*cube))
        surf_area += (NUM_FACES - num_neighbors)
    print(surf_area)

def part2(grid):
    exterior_surf_area = 0
    # Starting in a corner of the grid, use breadth-first
    # search (BFS) to simulate the water/steam expanding
    # to reach the sides of the lava cubes.
    start = (grid.min_x, grid.min_y, grid.min_z)
    visited = [start]
    queue = deque()
    queue.append(start)
    while len(queue) > 0:
        current = queue.popleft()
        for neighbor in get_neighbors(*current):
            if grid.is_lava(neighbor):
                exterior_surf_area += 1
            elif grid.in_bounds(*neighbor) and neighbor not in visited:
                visited.append(neighbor)
                queue.append(neighbor)
    print(exterior_surf_area)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        grid = Grid()
        for line in file:
            pos = tuple(int(v) for v in line.split(","))
            grid.add_cube(pos)

    part1(grid)
    grid.surround_with_steam()
    part2(grid)


if __name__ == "__main__":
    main()
