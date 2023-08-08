# Guide to hexagonal grid coordinates:
# https://www.redblobgames.com/grids/hexagons/#coordinates

import re
import sys


DIRECTIONS = ["ne", "e", "se", "sw", "w", "nw"]
WHITE = 'w'
BLACK = 'b'
CENTER_TILE_POS = (0, 0)


# hexagonal tile with a pointy top
class Tile(object):
    def __init__(self, pos, color=WHITE):
        self.pos = pos
        self.color = color
        self.next_color = None
        self.neighbors = {}
        self.grid = None

    def __str__(self):
        return f"{self.pos}: {self.color}"

    # 6 neighbors
    def get_neighbor_coords(self):
        if not self.neighbors:
            for dir in DIRECTIONS:
                q, r = move(dir, self.pos[0], self.pos[1])
                self.neighbors[dir] = (q, r)
        return self.neighbors.values()

    # decide the next color
    def step(self):
        self.next_color = self.color
        current_neighbors = []
        for n_pos in self.get_neighbor_coords():
            if self.grid.has_tile_at(n_pos):
                neighbor = self.grid.get_tile(n_pos)
                current_neighbors.append(neighbor)
        num_adjacent_black_tiles = len([n for n in current_neighbors if n.color == BLACK])
        if self.color == BLACK:
            if num_adjacent_black_tiles == 0 or num_adjacent_black_tiles > 2:
                self.next_color = WHITE
        else:
            if num_adjacent_black_tiles == 2:
                self.next_color = BLACK

    # apply the color decided in step()
    def advance(self):
        self.color = self.next_color

    def flip(self):
        self.color = BLACK if self.color == WHITE else WHITE
    
class HexGrid(object):
    def __init__(self):
        self.tiles = {}
        center_tile = Tile(CENTER_TILE_POS)
        self.place_tile(center_tile)
        self.rings = [[center_tile]]
        self.num_black_tiles = 0

    def add_ring(self):
        ring = []
        outermost_ring = self.get_ring(-1)
        for tile in outermost_ring:
            for n_pos in tile.get_neighbor_coords():
                if not self.has_tile_at(n_pos):
                    neighbor = Tile(n_pos)
                    self.place_tile(neighbor)
                    ring.append(neighbor)
        self.rings.append(ring)

    def get_ring(self, n):
        return self.rings[n]
    
    def print_ring(self, n):
        ring = self.get_ring(n)
        print(f"Ring num: {n}, Size: {len(ring)}")
        for tile in ring:
            print(tile)

    def place_tile(self, tile):
        self.tiles[tile.pos] = tile
        tile.grid = self

    def get_tile(self, pos):
        return self.tiles.get(pos)

    def has_tile_at(self, pos):
        return self.get_tile(pos) is not None

    def print_info(self):
        for i in range(len(self.rings)):
            self.print_ring(i)

def move(dir, q, r):
    # axial coordinates
    if dir == "ne":
        return q + 1, r - 1
    if dir == "e":
        return q + 1, r + 0
    if dir == "se":
        return q + 0, r + 1
    if dir == "sw":
        return q - 1, r + 1
    if dir == "w":
        return q - 1, r + 0
    if dir == "nw":
        return q + 0, r - 1

def calc_floor_dimensions(flipped_tiles):
    min_q, max_q = 0, 0
    min_r, max_r = 0, 0
    for pos in flipped_tiles.keys():
        q, r = pos
        if min_q > q:
            min_q = q
        if max_q < q:
            max_q = q
        if min_r > r:
            min_r = r
        if max_r < r:
            max_r = r
    return min_q, max_q, min_r, max_r

def setup_floor(flipped_tiles):
    min_q, max_q, min_r, max_r = calc_floor_dimensions(flipped_tiles)
    # print(f"q_range: {min_q} to {max_q}, r_range: {min_r} to {max_r}")
    # get the smallest grid that encloses the patttern
    # fill in the missing floor tiles so that it's symmetrical
    # make enough space; outermost black tiles should be surrounded by white tiles
    num_rings = max(abs(min_q), abs(max_q), abs(min_r), abs(max_r)) + 1
    floor = HexGrid()
    for i in range(num_rings):
        floor.add_ring()
    # set floor state
    for pos in flipped_tiles.keys():
        if floor.has_tile_at(pos):
            tile = floor.get_tile(pos)
            tile.color = flipped_tiles[pos].color
            if tile.color == BLACK:
                floor.num_black_tiles += 1
    return floor

def part1(instructions, flipped_tiles):
    for string_of_directions in instructions:
        # since there are no delimiters, let's use regular expressions to parse the string
        # find all non-overlapping matches
        pattern = "(" + "|".join(DIRECTIONS) + ")"
        dirs = re.findall(pattern, string_of_directions)
        cur_pos = CENTER_TILE_POS
        for dir in dirs:
            q, r = move(dir, cur_pos[0], cur_pos[1])
            cur_pos = (q, r)
        if cur_pos not in flipped_tiles.keys():
            cur_tile = Tile(cur_pos)
            flipped_tiles[cur_pos] = cur_tile
        else:
            cur_tile = flipped_tiles[cur_pos]
        cur_tile.flip()
    num_black_tiles = len([tile for tile in flipped_tiles.values() if tile.color == BLACK])
    print(f"Number of black tiles: {num_black_tiles}")

def part2(floor):
    print("Running sim...")
    days_to_run_sim = 100
    for day in range(1, days_to_run_sim + 1):
        tiles_to_flip = []
        for tile in floor.tiles.values():
            tile.step()
            if tile.next_color != tile.color:
                tiles_to_flip.append(tile)
        for tile in tiles_to_flip:
            tile.advance()
            if tile.color == BLACK:
                floor.num_black_tiles += 1
            else:
                floor.num_black_tiles -= 1
        outermost_ring = floor.get_ring(-1)
        if any(tile.color == BLACK for tile in outermost_ring):
            floor.add_ring()
        print_results(day, floor)

def print_results(day, floor):
    if day <= 10 or day % 10 == 0:
        print(f"Day {day}:")
        print(f"Number of black tiles: {floor.num_black_tiles}")
        if day == 10:
            print()

def main():
    filename = sys.argv[1]
    instructions = None
    with open(filename, "r") as file:
        instructions = [line.rstrip() for line in file.readlines()]
    # tiles that have been flipped at least once
    flipped_tiles = {}
    part1(instructions, flipped_tiles)
    floor = setup_floor(flipped_tiles)
    # sanity check
    # floor.print_info()
    part2(floor)

if __name__ == "__main__":
    main()
