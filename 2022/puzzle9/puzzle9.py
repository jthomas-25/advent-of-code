import sys
from enum import Enum


ROPE_LENGTH = 2


class Knot:
    class Direction(Enum):
        UP = "U"
        DOWN = "D"
        LEFT = "L"
        RIGHT = "R"
        NORTHWEST = "NW"
        NORTHEAST = "NE"
        SOUTHWEST = "SW"
        SOUTHEAST = "SE"

    VECTORS = {Direction.UP.value: (0, 1),
               Direction.DOWN.value: (0, -1),
               Direction.LEFT.value: (-1, 0),
               Direction.RIGHT.value: (1, 0),
               Direction.NORTHWEST.value: (-1, 1),
               Direction.NORTHEAST.value: (1, 1),
               Direction.SOUTHWEST.value: (-1, -1),
               Direction.SOUTHEAST.value: (1, -1)}

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def step(self, dir):
        dx, dy = Knot.VECTORS[dir]
        self.x += dx
        self.y += dy
    
    def follow(self, leader):
        moved = False
        direction = None
        
        distance = calc_manhattan_distance(leader, self)
        if distance == 2:
            # leader is directly up or down
            if leader.x == self.x:
                direction = Knot.Direction.UP.value if leader.y > self.y else Knot.Direction.DOWN.value
            # leader is directly left or right
            elif leader.y == self.y:
                direction = Knot.Direction.LEFT.value if leader.x < self.x else Knot.Direction.RIGHT.value
        elif distance > 2:
            # leader is northwest or northeast
            if leader.y > self.y:
                direction = Knot.Direction.NORTHWEST.value if leader.x < self.x else Knot.Direction.NORTHEAST.value
            # leader is southwest or southeast
            elif leader.y < self.y:
                direction = Knot.Direction.SOUTHWEST.value if leader.x < self.x else Knot.Direction.SOUTHEAST.value
        
        if direction:
            self.step(direction)
            moved = True
        
        return moved

def calc_manhattan_distance(head, tail):
    return abs(head.x - tail.x) + abs(head.y - tail.y)

def print_rope(rope):
    for i, knot in enumerate(rope):
        if i == 0:
            label = "HEAD:"
        elif i == ROPE_LENGTH-1:
            label = "TAIL:"
        else:
            label = " " * 5
        print(f"{label} {(knot.x, knot.y)}")

def run_sim(rope, motions):
    head = rope[0]
    tail = rope[-1]
    # visited at least once
    visited = [(0, 0)]
    
    for motion in motions:
        direction = motion[0]
        num_steps = int(motion[1])
        for _ in range(num_steps):
            head.step(direction)
            # other knots follow the leader
            for i in range(1, ROPE_LENGTH):
                follower = rope[i]
                leader = rope[i-1]
                moved = follower.follow(leader)
                if follower is tail and moved:
                    pos = (tail.x, tail.y)
                    if pos not in visited:
                        visited.append(pos)
    
    print(len(visited))

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        motions = [line.rstrip().split() for line in file]
    
    rope = [Knot(0, 0) for i in range(ROPE_LENGTH)]
    run_sim(rope, motions)


if __name__ == "__main__":
    main()
