import sys
from grid import Grid3D, Grid4D


NUM_CYCLES = 6
DISPLAY_GRID = False
RUN_3D = True
RUN_4D = False


def get_dimensions_of_initial_region(cube_rows):
    size = len(cube_rows)
    # 'zero-centered' region if odd number of rows
    if size % 2 == 1:
        min_x = (size // 2) * -1
        max_x = min_x * -1
    else:
        min_x = (size // 2 - 1) * -1
        max_x = (min_x * -1) + 1
    min_y = min_x
    max_y = max_x
    return min_x, max_x, min_y, max_y

def setup_3d_grid(cube_rows):
    min_x, max_x, min_y, max_y = get_dimensions_of_initial_region(cube_rows)
    grid = Grid3D(min_x - 1, max_x + 1, min_y - 1, max_y + 1)
    # all active cubes in the initial region will be surrounded by inactive cubes
    for z in range(-1, 2):
        grid.add_layer(z)
    # set state for the initial region and track the active cubes in it
    for i, y in enumerate(range(min_y, max_y + 1)):
        row = cube_rows[i]
        for j, x in enumerate(range(min_x, max_x + 1)):
            pos = (x, y, 0)
            cube = grid.get_cube(pos)
            cube.state = row[j]
            if cube.is_active():
                grid.active_cubes[cube.pos] = cube
                grid.num_active_cubes += 1
                start_layer = grid.get_layer(0)
                start_layer.active_cubes[cube.pos] = cube
                start_layer.num_active_cubes += 1
    return grid

def setup_4d_grid(cube_rows):
    min_x, max_x, min_y, max_y = get_dimensions_of_initial_region(cube_rows)
    grid = Grid4D(min_x - 1, max_x + 1, min_y - 1, max_y + 1)
    # all active cubes in the initial region will be surrounded by inactive cubes
    for w in range(-1, 2):
        for z in range(-1, 2):
            grid.add_layer(z, w)
    # set state for the initial region and track the active cubes in it
    for i, y in enumerate(range(min_y, max_y + 1)):
        row = cube_rows[i]
        for j, x in enumerate(range(min_x, max_x + 1)):
            pos = (x, y, 0, 0)
            cube = grid.get_cube(pos)
            cube.state = row[j]
            if cube.is_active():
                grid.active_cubes[cube.pos] = cube
                grid.num_active_cubes += 1
                start_layer = grid.get_layer((0, 0))
                start_layer.active_cubes[cube.pos] = cube
                start_layer.num_active_cubes += 1
    return grid

def part1(cube_rows):
    grid = setup_3d_grid(cube_rows)
    print("Running 3-D sim...")
    run_sim(grid)

def part2(cube_rows):
    grid = setup_4d_grid(cube_rows)
    print("Running 4-D sim...")
    run_sim(grid)

def run_sim(grid):
    print("Before any cycles:\n")
    display_grid(grid)
    print(f"Number of active cubes: {grid.num_active_cubes}")
    for i in range(1, NUM_CYCLES + 1):
        execute_cycle(grid)
        print(f"\nAfter {i} cycle{'s' if i > 1 else ''}:\n")
        display_grid(grid)
        print(f"Number of active cubes: {grid.num_active_cubes}")

def execute_cycle(grid):
    cubes_to_toggle = []
    for cube in grid.cubes.values():
        cube.step()
        if cube.next_state != cube.state:
            cubes_to_toggle.append(cube)
    for cube in cubes_to_toggle:
        cube.advance()
    toggled_cubes = cubes_to_toggle
    grid.update_list_of_active_cubes(toggled_cubes)
    grid.expand_if_needed()

def display_grid(grid):
    if DISPLAY_GRID:
        grid.display(only_show_active_layers=True, follow_active_cubes=True, verbose=False)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        cube_rows = [line.rstrip() for line in file.readlines()]
    if RUN_3D:
        part1(cube_rows)
    elif RUN_4D:
        part2(cube_rows)

if __name__ == "__main__":
    main()
