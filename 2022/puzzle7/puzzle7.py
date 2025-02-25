import sys
from tree import Filesystem


def setup_filesystem(input_filename):
    with open(input_filename, "r") as file:
        filesystem = Filesystem()
        for line in file:
            parts = line.rstrip().split()
            start = parts[0]
            if start == "$":
                cmd_name = parts[1]
                if cmd_name == "cd":
                    dir_name = parts[2]
                    filesystem.cd(dir_name)
                elif cmd_name == "ls":
                    pass
            elif start == "dir":
                dir_name = parts[1]
                filesystem.mkdir(dir_name)
            else:
                file_size = int(start)
                file_name = parts[1]
                filesystem.create_file(file_name, size=file_size)
    
    return filesystem

def part1(dir_sizes):
    print(sum(size for size in dir_sizes if size <= 100000))

def part2(filesystem, dir_sizes):
    update_size = 30000000
    required_space = update_size - filesystem.calc_free_space()
    print(min(size for size in dir_sizes if size >= required_space))

def main():
    filename = sys.argv[1]
    filesystem = setup_filesystem(filename)
    
    dir_sizes = [filesystem.size(dir) for dir in filesystem.get_dirs()]
    part1(dir_sizes)
    part2(filesystem, dir_sizes)


if __name__ == "__main__":
    main()
