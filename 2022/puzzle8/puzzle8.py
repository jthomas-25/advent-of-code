import sys


UP = "U"
DOWN = "D"
LEFT = "L"
RIGHT = "R"
DIRECTIONS = [UP, DOWN, LEFT, RIGHT]


def is_on_edge(row, col, grid_height, grid_width):
    return row == 0 or \
           row == grid_height-1 or \
           col == 0 or \
           col == grid_width-1

def is_visible(tree, grid, grid_height, grid_width):
    row, col = tree
    tree_height = grid[row][col]
    if is_on_edge(row, col, grid_height, grid_width):
        return True
    # from the top
    if all(grid[r][col] < tree_height for r in range(0, row)):
        return True
    # from the bottom
    elif all(grid[r][col] < tree_height for r in range(row+1, grid_height)):
        return True
    # from the left
    elif all(grid[row][c] < tree_height for c in range(0, col)):
        return True
    # from the right
    elif all(grid[row][c] < tree_height for c in range(col+1, grid_width)):
        return True
    else:
        return False

def calc_scenic_score(tree, grid_info):
    product = 1;
    for direction in DIRECTIONS:
        product *= calc_viewing_distance(tree, direction, *grid_info)
    return product

def calc_viewing_distance(tree, direction, grid, grid_height, grid_width):
    row, col = tree
    tree_height = grid[row][col]
    if is_on_edge(row, col, grid_height, grid_width):
        return 0

    total = 0
    # to the top
    if direction == UP:
        for r in range(row-1, -1, -1):
            total += 1
            if grid[r][col] >= tree_height:
                break
    # to the bottom
    elif direction == DOWN:
        for r in range(row+1, grid_height):
            total += 1
            if grid[r][col] >= tree_height:
                break
    # to the left
    elif direction == LEFT:
        for c in range(col-1, -1, -1):
            total += 1
            if grid[row][c] >= tree_height:
                break
    # to the right
    elif direction == RIGHT:
        for c in range(col+1, grid_width):
            total += 1
            if grid[row][c] >= tree_height:
                break
    
    return total

def part1(grid_info):
    print(sum(is_visible((row, col), *grid_info) for row in range(grid_info[1]) for col in range(grid_info[2])))

def part2(grid_info):
    scenic_scores = [calc_scenic_score((row, col), grid_info) for row in range(grid_info[1]) for col in range(grid_info[2])]
    print(max(scenic_scores))

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        grid = [[int(tree) for tree in line.rstrip()] for line in file]
    
    height = len(grid)
    width = len(grid[0])
    grid_info = (grid, height, width)
    part1(grid_info)
    part2(grid_info)


if __name__ == "__main__":
    main()
