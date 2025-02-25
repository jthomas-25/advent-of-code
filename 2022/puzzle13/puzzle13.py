# Mergesort algorithm adapted from Java version at:
# ianfinlayson.net/class/cpsc340/notes/10-searching-sorting/03-mergesort

import packetdata
from enum import Enum


class Result(Enum):
    IN_ORDER = 1
    NOT_IN_ORDER = -1
    CONTINUE = 0

def compare_values(left, right):
    if isinstance(left, int) and isinstance(right, int):
        if left < right:
            return Result.IN_ORDER
        if left > right:
            return Result.NOT_IN_ORDER
        # same integer, check next part of input
        return Result.CONTINUE
    elif isinstance(left, list) and isinstance(right, list):
        # from the Python documentation: https://docs.python.org/3/library/functions.html#zip
        # "By default, zip() stops when the shortest iterable is exhausted."
        for l, r in zip(left, right):
            result = compare_values(l, r)
            if result is not Result.CONTINUE:
                return result
        # no decision, go to tiebreaker
        if len(left) < len(right):
            # left ran out of items first
            return Result.IN_ORDER
        elif len(left) > len(right):
            # right ran out of items first
            return Result.NOT_IN_ORDER
        else:
            # check next part of input
            return Result.CONTINUE
    elif isinstance(left, int):
        return compare_values([left], right)
    else:
        return compare_values(left, [right])

def merge(array, start, end):
    middle = (start + end) // 2
    temp_index = 0
    # create a temporary list
    temp = [None] * (end - start + 1)
    # merge in sorted data from the two halves
    left = start
    right = middle + 1
    # while both halves have data
    while left <= middle and right <= end:
        result = compare_values(array[left], array[right])
        # if packets are in order
        if result is Result.IN_ORDER:
            # take from left
            temp[temp_index] = array[left]
            temp_index += 1
            left += 1
        # packets are not in order or are equal
        else:
            # take from right
            temp[temp_index] = array[right]
            temp_index += 1
            right += 1
    # add the remaining elements from the left half
    while left <= middle:
        temp[temp_index] = array[left]
        temp_index += 1
        left += 1
    # add the remaining elements from the right half
    while right <= end:
        temp[temp_index] = array[right]
        temp_index += 1
        right += 1
    # move from temp array to the original array
    for i in range(start, end+1, 1):
        array[i] = temp[i - start]

def merge_sort(array, start, end):
    if start < end:
        middle = (start + end) // 2
        # sort left half
        merge_sort(array, start, middle)
        # sort right half
        merge_sort(array, middle+1, end)
        # merge the two halves
        merge(array, start, end)

def sort(array):
    merge_sort(array, 0, len(array)-1)

def part1(packets):
    pairs = ((packets[i], packets[i+1]) for i in range(0, len(packets)-1, 2))
    indices = [i for i, pair in enumerate(pairs, start=1) if compare_values(*pair) is Result.IN_ORDER]
    print(sum(indices))

def part2(packets):
    # divider packets
    div_packet1, div_packet2 = [[2]], [[6]]
    packets.append(div_packet1)
    packets.append(div_packet2)
    sort(packets)
    decoder_key = (packets.index(div_packet1)+1) * (packets.index(div_packet2)+1)
    print(decoder_key)

def main():
    packets = packetdata.input_packets
    part1(packets)
    part2(packets)


if __name__ == "__main__":
    main()
