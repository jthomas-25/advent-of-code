import sys


ADDX = "addx"
NOOP = "noop"
CRT_WIDTH, CRT_HEIGHT = 40, 6
LIT_PIXEL = "#"
DARK_PIXEL = "."


def check_signal_strengths(num_cycles, cycle, x, signal_strengths):
    for i in range(num_cycles):
        cycle += 1
        # check during 20th cycle and every 40 cycles after that
        if cycle == 20 + 40*len(signal_strengths):
            strength = cycle * x
            signal_strengths.append(strength)
    return cycle

def draw_pixels(num_cycles, cycle, sprite_x, scanlines):
    for i in range(num_cycles):
        row, draw_x = divmod(cycle, CRT_WIDTH)
        if sprite_x-1 <= draw_x <= sprite_x+1:
            # the sprite is visible at this position
            scanlines[row][draw_x] = LIT_PIXEL
        else:
            scanlines[row][draw_x] = DARK_PIXEL
        cycle += 1
    return cycle

def render_image(scanlines):
    for row in range(CRT_HEIGHT):
        print("".join(scanlines[row]))

def part1(insts):
    signal_strengths = []
    cycle = 0
    # X register
    x = 1
    
    for parts in insts:
        op = parts[0]
        if op == ADDX:
            cycle = check_signal_strengths(2, cycle, x, signal_strengths)
            value = int(parts[1])
            x += value
        elif op == NOOP:
            cycle = check_signal_strengths(1, cycle, x, signal_strengths)
    
    print(sum(signal_strengths))

def part2(insts):
    scanlines  = [[None] * CRT_WIDTH for row in range(CRT_HEIGHT)]
    cycle = 0
    # middle of the sprite
    sprite_x = 1
    
    for parts in insts:
        op = parts[0]
        if op == ADDX:
            cycle = draw_pixels(2, cycle, sprite_x, scanlines)
            value = int(parts[1])
            sprite_x += value
        elif op == NOOP:
            cycle = draw_pixels(1, cycle, sprite_x, scanlines)

    render_image(scanlines)

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        insts = [line.rstrip().split() for line in file]

    part1(insts)
    part2(insts)


if __name__ == "__main__":
    main()
