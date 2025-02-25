import sys


class Integer:
    num_instances = 0

    def __init__(self, value):
        Integer.num_instances += 1
        self.id = Integer.num_instances
        self.index = self.id - 1
        self.value = value

def mix(id_numbers, map_ids_to_nums):
    list_size = len(id_numbers)
    bounds = range(0, list_size)
    zero = None
    # mix numbers in the order of the original list
    # note: dictionaries keep the order in which the key-value pairs were added
    for number in map_ids_to_nums.values():
        if number.value == 0:
            zero = number
            continue
        
        current_pos = number.index
        new_pos = current_pos + number.value
        
        if new_pos == 0:
            new_pos = list_size - 1
        elif new_pos == list_size - 1:
            new_pos = 0
        elif new_pos not in bounds:
            # Since the list is circular, the ends are connected.
            # This effectively means the number can be in [list_size - 1]
            # positions where it's between two other numbers.
            new_pos %= (list_size - 1)

        if new_pos < current_pos:
            # get the id numbers to the left of this number
            left_slice = id_numbers[new_pos:current_pos]
            # shift them to the right
            for id in reversed(left_slice):
                n = map_ids_to_nums[id]
                n.index += 1
                id_numbers[n.index] = n.id
        elif new_pos > current_pos:
            # get the id numbers to the right of this number
            right_slice = id_numbers[current_pos+1:new_pos+1]
            # shift them to the left
            for id in right_slice:
                n = map_ids_to_nums[id]
                n.index -= 1
                id_numbers[n.index] = n.id

        number.index = new_pos
        id_numbers[number.index] = number.id
    
    return zero

def find_grove_coordinates(zero, id_numbers, map_ids_to_nums):
    list_size = len(id_numbers)
    index_of_1000th_num = (zero.index + 1000) % list_size
    index_of_2000th_num = (zero.index + 2000) % list_size
    index_of_3000th_num = (zero.index + 3000) % list_size
    num_1000 = map_ids_to_nums[id_numbers[index_of_1000th_num]]
    num_2000 = map_ids_to_nums[id_numbers[index_of_2000th_num]]
    num_3000 = map_ids_to_nums[id_numbers[index_of_3000th_num]]
    return (num_1000.value, num_2000.value, num_3000.value)

def display_list(zero, id_numbers, map_ids_to_nums):
    # Because the list is circular, we'll start at 0
    # and go clockwise around the circle
    list_size = len(id_numbers)
    nums = [None] * list_size
    for i in range(list_size):
        index = (zero.index + i) % list_size
        id_of_ith_num = id_numbers[index]
        nums[i] = map_ids_to_nums[id_of_ith_num].value
    print(nums)

def part1(id_numbers, map_ids_to_nums):
    zero = mix(id_numbers, map_ids_to_nums)
    #display_list(zero, id_numbers, map_ids_to_nums)
    coords = find_grove_coordinates(zero, id_numbers, map_ids_to_nums)
    print(sum(coords))

def part2(id_numbers, map_ids_to_nums):
    decryption_key = 811589153 
    for num in map_ids_to_nums.values():
        num.value *= decryption_key
        # reset position
        num.index = num.id - 1
        id_numbers[num.index] = num.id
    
    for i in range(10):
        zero = mix(id_numbers, map_ids_to_nums)
        #print(f"After {i + 1} rounds of mixing:")
        #display_list(zero, id_numbers, map_ids_to_nums)

    coords = find_grove_coordinates(zero, id_numbers, map_ids_to_nums)
    print(sum(coords))

def main():
    filename = sys.argv[1]
    with open(filename, "r") as file:
        map_ids_to_nums = {i : Integer(int(line)) for i, line in enumerate(file, start=1)}
    
    id_numbers = list(map_ids_to_nums.keys())
    part1(id_numbers, map_ids_to_nums)
    part2(id_numbers, map_ids_to_nums)


if __name__ == "__main__":
    main()
