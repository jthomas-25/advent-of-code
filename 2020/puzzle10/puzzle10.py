import networkx as nx

def print_paths(paths):
    print(f"Paths: {len(paths)}")
    for p in paths:
        print(p)

# store a list of all simple paths from start to end in paths
# the runtime is LONG and the space complexity is HUGE for large graphs
def all_simple_paths(graph, visited, start, end, path, paths):
    # we don't need to check if we've visited the start because there are no cycles
    visited[start] = True
    # treat path like a stack
    path.append(start)
    if start == end:
        # found path
        paths.append(path.copy())
        # backtrack
        visited[start] = False
        path.pop()
        return
    for n in graph.neighbors(start):
        all_simple_paths(graph, visited, n, end, path, paths)
        
    visited[start] = False
    path.pop()

# only counts all the simple paths from start to end -- it doesn't store them
# uses dynamic programming to avoid recomputing paths
def count_simple_paths(graph, visited, start, end, path, num_paths_from_current_node_to_end):
    # we don't need to check if we've visited the start because there are no cycles
    total = 0
    visited[start] = True
    # treat path like a stack
    path.append(start)
    if start == end:
        # found path, backtrack
        visited[start] = False
        path.pop()
        num_paths_from_current_node_to_end[start] = 1
        return 1
    for n in graph.neighbors(start):
        if num_paths_from_current_node_to_end[n] == 0:
            total += count_simple_paths(graph, visited, n, end, path, num_paths_from_current_node_to_end)
        else:
            total += num_paths_from_current_node_to_end[n]
            
    visited[start] = False
    path.pop()
    num_paths_from_current_node_to_end[start] = total
    return total

def count_paths(g, start, end):
    num_paths_from_current_node_to_end = {}
    visited = {}
    for node in g:
        num_paths_from_current_node_to_end[node] = 0
        visited[node] = False
    path = []
    count_simple_paths(g, visited, start, end, path, num_paths_from_current_node_to_end)
    #print(num_paths_from_current_node_to_end)
    return num_paths_from_current_node_to_end[start]
    
    # unfortunately, NetworkX doesn't have a function that counts the number of simple paths...
    # paths = list(nx.all_simple_paths(g, start, end))
    # return len(paths)

def part1(adapters):
    num_one_jolt_diffs = 0
    num_three_jolt_diffs = 0
    for i in range(len(adapters)-1):
        diff = adapters[i+1] - adapters[i]
        if diff == 1:
            num_one_jolt_diffs += 1
        elif diff == 3:
            num_three_jolt_diffs += 1
    print(f"Number of 1-jolt diffs * Number of 3-jolt diffs = {num_one_jolt_diffs * num_three_jolt_diffs}")

def part2(adapters):
    # create a directed acyclic graph (DAG)
    g = nx.DiGraph()
    g.add_nodes_from(adapters)
    for i in range(len(adapters)):
        for j in range(len(adapters)):
            diff = adapters[j] - adapters[i]
            if (diff >= 1 and diff <= 3) and not g.has_edge(adapters[i], adapters[j]):
                g.add_edge(adapters[i], adapters[j], weight=diff)
    #nx.draw_networkx(g)
    # try to reduce the number of edges to check
    # find all nodes that have multiple outgoing edges
    a = [node for node in g if len(list(g.neighbors(node))) > 1]
    # find all nodes that have multiple incoming edges
    b = [node for node in g if len(list(g.predecessors(node))) > 1]
    # start = a[0]
    # end = b[-1]
    start = adapters[0]
    end = adapters[-1]
    num_arrangements = count_paths(g, start, end)
    print(f"Number of distinct arrangements: {num_arrangements}")

def main(filename):
    adapters = [0]
    with open(filename, "r") as file:
        adapters.extend([int(line) for line in file.readlines()])
    adapters.append(max(adapters) + 3)
    adapters.sort()
    part1(adapters)
    part2(adapters)

if __name__ == "__main__":
    main("input.txt")