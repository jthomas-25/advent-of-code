import sys
import copy


CRATE_WIDTH = 3


def read_stacks(file):
    stacks = []
    num_stacks = len(file.readline()) // (CRATE_WIDTH+1)
    file.seek(0)
    
    for stack_index in range(num_stacks):
        stack = []
        i = stack_index * (CRATE_WIDTH+1)
        j = i + CRATE_WIDTH
        
        # read crates until we hit the stack number (i.e. the bottom)
        crate = file.readline()[i:j]
        while crate != f" {stack_index+1} ":
            if not crate.isspace():
                stack.append(crate)
            crate = file.readline()[i:j]
        
        # keep crates in their original order
        stack.reverse()
        stacks.append(stack)
        # only go to the beginning of the file if we have more stacks to read
        if stack_index < num_stacks-1:
            file.seek(0)
    
    return stacks

def read_steps(file):
    steps = []
    for line in file:
        parts = line.split()
        num_crates_to_move = int(parts[1])
        source_stack = int(parts[3])
        dest_stack = int(parts[5])
        steps.append((num_crates_to_move, source_stack, dest_stack))
    return steps

def move_crates(stacks, num_crates_to_move, source_stack_num, dest_stack_num, multi=False):
    source_stack = stacks[source_stack_num-1]
    dest_stack = stacks[dest_stack_num-1]
    crates = [source_stack.pop() for i in range(num_crates_to_move)]
    if multi:
        # keep crates in their original order
        crates = reversed(crates)
    for crate in crates:
        dest_stack.append(crate)

def print_stacks(stacks):
    max_height = max(len(stack) for stack in stacks)
    empty_slot = " " * CRATE_WIDTH
    for i in range(max_height-1, -1, -1):
        for stack in stacks:
            if i > len(stack)-1:
                print(empty_slot, end=" ")
            else:
                print(stack[i], end=" ")
        print()
    labels = " ".join(f" {i+1} " for i in range(len(stacks)))
    print(labels)
    print()

def get_message(stacks):
    return "".join(stack[-1][1] for stack in stacks if len(stack) > 0)

def part1(stacks, steps):
    #print_stacks(stacks)
    for step in steps:
        move_crates(stacks, *step)
        #print_stacks(stacks)
    print(get_message(stacks))

def part2(stacks, steps):
    #print_stacks(stacks)
    for step in steps:
        move_crates(stacks, *step, multi=True)
        #print_stacks(stacks)
    print(get_message(stacks))

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        stacks = read_stacks(file)
        # read past blank line
        file.readline()
        steps = read_steps(file)
    
    original_config = copy.deepcopy(stacks)

    part1(stacks, steps)
    stacks = original_config
    part2(stacks, steps)


if __name__ == "__main__":
    main()
