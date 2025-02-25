import sys
from enum import Enum


class Hand(Enum):
    ROCK = 0
    PAPER = 1
    SCISSORS = 2

class Outcome(Enum):
    WIN = 1
    DRAW = 0
    LOSE = -1

HAND_SCORES = {Hand.ROCK: 1, Hand.PAPER: 2, Hand.SCISSORS: 3}
OUTCOME_SCORES = {Outcome.WIN: 6, Outcome.DRAW: 3, Outcome.LOSE: 0}


def decrypt_hand(hand):
    if hand == "A" or hand == "X":
        return Hand.ROCK
    elif hand == "B" or hand == "Y":
        return Hand.PAPER
    elif hand == "C" or hand == "Z":
        return Hand.SCISSORS

def calc_round_score(opponent_hand, my_hand):
    outcome = compare_hands(opponent_hand, my_hand)
    return HAND_SCORES[my_hand] + OUTCOME_SCORES[outcome]

def compare_hands(opponent_hand, my_hand):
    if opponent_hand is my_hand:
        return Outcome.DRAW
    elif (opponent_hand is Hand.ROCK and my_hand is Hand.PAPER) or \
         (opponent_hand is Hand.PAPER and my_hand is Hand.SCISSORS) or \
         (opponent_hand is Hand.SCISSORS and my_hand is Hand.ROCK):
         return Outcome.WIN
    else:
        return Outcome.LOSE

def calc_required_hand(opponent_hand, desired_outcome):
    # must lose
    if desired_outcome == "X":
        if opponent_hand is Hand.ROCK:
            return Hand.SCISSORS
        if opponent_hand is Hand.PAPER:
            return Hand.ROCK
        if opponent_hand is Hand.SCISSORS:
            return Hand.PAPER
    # must tie
    elif desired_outcome == "Y":
        return opponent_hand
    # must win
    elif desired_outcome == "Z":
        if opponent_hand is Hand.ROCK:
            return Hand.PAPER
        if opponent_hand is Hand.PAPER:
            return Hand.SCISSORS
        if opponent_hand is Hand.SCISSORS:
            return Hand.ROCK

def part1(opponent_hands, my_hands):
    my_score = 0
    for opponent_hand, my_hand in zip(opponent_hands, my_hands):
        opponent_hand = decrypt_hand(opponent_hand)
        my_hand = decrypt_hand(my_hand)
        my_score += calc_round_score(opponent_hand, my_hand)
    print(my_score)

def part2(opponent_hands, desired_outcomes):
    my_score = 0
    for opponent_hand, outcome in zip(opponent_hands, desired_outcomes):
        opponent_hand = decrypt_hand(opponent_hand)
        my_hand = calc_required_hand(opponent_hand, outcome)
        my_score += calc_round_score(opponent_hand, my_hand)
    print(my_score)
    
def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        opponent_hands = []
        column_b = []
        for line in file:
            columns = line.rstrip().split()
            opponent_hands.append(columns[0])
            column_b.append(columns[1])
    
    part1(opponent_hands, column_b)
    part2(opponent_hands, column_b)


if __name__ == "__main__":
    main()
