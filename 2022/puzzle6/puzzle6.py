import sys
from collections import deque


def process_signal(filename, marker_size):
    with open(filename, "r") as file:
        queue = deque(maxlen=marker_size)
        char = file.read(1)
        while char != "\n":
            queue.append(char)
            if len(set(queue)) == marker_size:
                break
            char = file.read(1)
        if marker_size == 4:
            print(f"First start-of-packet marker after character {file.tell()}")
        elif marker_size == 14:
            print(f"First start-of-message marker after character {file.tell()}")

def main():
    filename = sys.argv[1]
    # part 1
    process_signal(filename, 4)
    # part 2
    process_signal(filename, 14)
    

if __name__ == "__main__":
    main()
