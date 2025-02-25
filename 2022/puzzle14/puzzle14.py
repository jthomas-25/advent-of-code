import sys


SAND_SOURCE_POS = (500, 0)
SAND_SOURCE = "+"
RESTING_SAND = "o"
ROCK = "#"
AIR = "."

FLOOR = False


class SandUnit:
    def __init__(self, pos):
        self.x, self.y = pos
        self.stopped = False
    
    def step(self, solid_tiles):
        if not blocked(self.x, self.y+1, solid_tiles):
            self.y += 1
        elif not blocked(self.x-1, self.y+1, solid_tiles):
            self.x -= 1
            self.y += 1
        elif not blocked(self.x+1, self.y+1, solid_tiles):
            self.x += 1
            self.y += 1
        else:
            self.stopped = True
    
    def out_of_bounds(self, min_x, max_x, min_y, max_y):
        return self.y > max_y or (self.x < min_x or self.x > max_x)

def blocked(x, y, solid_tiles):
    return solid_tiles.get((x, y)) is not None

def scan_rock_paths(filename):
    with open(filename, "r") as file:
        rock_tiles = set()
        source_x, source_y = SAND_SOURCE_POS
        min_x, max_x = source_x - 1, source_x + 1
        min_y, max_y = source_y, source_y + 1

        for line in file:
            path_points = line.rstrip().split(" -> ")
            for i in range(len(path_points)-1):
                x1, y1 = [int(v) for v in path_points[i].split(",")]
                x2, y2 = [int(v) for v in path_points[i+1].split(",")]

                if min_x > x1 or min_x > x2:
                    min_x = min(x1, x2)
                if max_x < x1 or max_x < x2:
                    max_x = max(x1, x2)
                if max_y < y1 or max_y < y2:
                    max_y = max(y1, y2)

                # horizontal line
                if y1 == y2:
                    for x in range(min(x1, x2), max(x1, x2)+1):
                        rock_tiles.add((x, y1))
                # vertical line
                else:
                    for y in range(min(y1, y2), max(y1, y2)+1):
                        rock_tiles.add((x1, y))
    
    return rock_tiles, min_x, max_x, min_y, max_y

def print_cave_ceiling(solid_tiles, min_x, max_x, min_y, max_y):
    for y in range(min_y, max_y+1):
        row = []
        for x in range(min_x, max_x+1):
            pos = (x, y)
            tile = solid_tiles.get(pos)
            if tile:
                row.append(tile)
            elif pos == SAND_SOURCE_POS:
                row.append(SAND_SOURCE)
            else:
                row.append(AIR)
        print("".join(row))

def simulate_sand(solid_tiles, min_x, max_x, min_y, max_y):
    total = 0
    sand_unit = SandUnit(SAND_SOURCE_POS)
    
    done = False
    while not done:
        # resize, including the floor, if the sand goes too far left or right
        if FLOOR:
            if sand_unit.x-1 < min_x:
                min_x -= 1
                solid_tiles[(min_x, max_y)] = ROCK
            elif sand_unit.x+1 > max_x:
                max_x += 1
                solid_tiles[(max_x, max_y)] = ROCK

        sand_unit.step(solid_tiles)
 
        if sand_unit.out_of_bounds(min_x, max_x, min_y, max_y):
            done = True
        elif sand_unit.stopped:
            pos = (sand_unit.x, sand_unit.y)
            solid_tiles[pos] = RESTING_SAND
            total += 1
            # if source is blocked
            if pos == SAND_SOURCE_POS:
                done = True
            # next sand unit
            else:
                sand_unit = SandUnit(SAND_SOURCE_POS)

    #print_cave_ceiling(solid_tiles, min_x, max_x, min_y, max_y)
    print(total)

def main():
    filename = sys.argv[1]
    rock_tiles, min_x, max_x, min_y, max_y = scan_rock_paths(filename)
    if FLOOR:
        max_y += 2
        for x in range(min_x, max_x+1):
            rock_tiles.add((x, max_y))
    
    solid_tiles = {pos: ROCK for pos in rock_tiles}
    simulate_sand(solid_tiles, min_x, max_x, min_y, max_y)


if __name__ == "__main__":
    main()
