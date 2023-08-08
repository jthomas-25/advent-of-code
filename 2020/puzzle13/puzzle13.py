import sys
import string
from sympy import symbols, Eq, solveset
from sympy.solvers.diophantine.diophantine import diop_linear


def create_linear_system(listed_buses, offsets_from_t):
    #variables = symbols(" ".join(string.ascii_lowercase[:len(listed_buses)+1]), integer=True)
    variables = symbols(" ".join(string.ascii_lowercase[:len(listed_buses)]), integer=True)
    #variables = symbols('x_1:' + f"{len(listed_buses)+1}", integer=True)
    ids_to_variables = {}
    for id, var in zip(listed_buses, variables):
        ids_to_variables[id] = var
    print(ids_to_variables)
    
    system = []
    for i in range(len(listed_buses)-1):
        id1 = listed_buses[i]
        id2 = listed_buses[i+1]
        a, x = id1, ids_to_variables[id1]
        b, y = id2, ids_to_variables[id2]
        c = (offsets_from_t[id1] - offsets_from_t[id2])
        # ax - by - c = 0 or
        # ax = by + c or
        # ax + c = by
        equation = a*x - b*y - c
        system.append(equation)
    print(system)
    return system

def part2(listed_buses, offsets_from_t):
    system = create_linear_system(listed_buses, offsets_from_t)
    subscript = 0
    for i in range(len(system)-1, -1, -1):
        eq_to_solve = system[i]
        print(f"Solving {eq_to_solve}...")
        # get the general solution
        #y = symbols('y', integer=True)
        
        terms = eq_to_solve.as_ordered_terms()
        var_to_replace = terms[1].as_coeff_mul()[1][0]
        #y = symbols('y' + f"{}", integer=True)
        #y = symbols('y' + f"{int(str(var_to_replace)[1:])+1}", integer=True)
        
        # use this
        #n = symbols('n', integer=True)
        n = symbols('n' + f"{'' if i == 0 else i}", integer=True)
        #n = symbols('n' + f"{i}", integer=True)

        #n = symbols('n' + f"{'' if subscript == 0 else subscript}", integer=True)
        #y = symbols('y' + f"{'' if n == 0 else n}", integer=True)
        
        #solution = diop_linear(eq_to_solve, param=y)
        solution = diop_linear(eq_to_solve, param=n)
        print(f"\nbase solution: {solution}")
        #continue
        # find the smallest specific solution where x and y are both positive
        x_terms = solution[0].as_ordered_terms()
        print(f"x_terms: {x_terms}")
        #continue
        y_terms = solution[1].as_ordered_terms()
        
        x_0, y_0 = x_terms[1], y_terms[1]
        #print(f"before: {x_0} {y_0}")
        x_mul = x_terms[0].as_coeff_mul()[0]
        y_mul = y_terms[0].as_coeff_mul()[0]
        if x_0 > 0 and y_0 > 0:
            x_0 -= x_mul * (abs(x_0) // x_mul + 1)
            y_0 -= y_mul * (abs(y_0) // y_mul + 1)
        if x_0 < 0 and y_0 < 0:
            x_0 += x_mul * (abs(x_0) // x_mul + 1)
            y_0 += y_mul * (abs(y_0) // y_mul + 1)
        print(x_0, y_0)
        #return
        #continue
        pos_solution = (x_0 + x_terms[0], y_0 + y_terms[0])
        print(f"first positive solution: {pos_solution}")
        #continue
        
        #return
        
        # replace the variable of the second term with y
        # this "solves" for the first term so we can
        # substitute it in other equations aka "back substitution"
        #terms = eq_to_solve.as_ordered_terms()
        #var_to_replace = terms[1].as_coeff_mul()[1][0]
        #term_to_replace = terms[1]
        #print(eq_to_solve.subs(terms[1], pos_solution[1]))
        terms[1] = terms[1].subs(var_to_replace, pos_solution[1])
        print(terms)
        #print(eq_to_solve)
        #continue
        eq_to_solve = eq_to_solve.subs(var_to_replace, pos_solution[1])
        print(eq_to_solve)
        # solve for the first term
        #print(solve(eq_to_solve), terms[0])
        first_term_solution = list(solveset(eq_to_solve, terms[0]))[0]
        print(first_term_solution.as_ordered_terms())
        # solve for the variable attached to the first term
        #print(solveset(eq_to_solve, terms[0].as_coeff_mul()[1][0]))
        
        if i > 0:
            # perform back substitution
            system[i-1] = system[i-1].subs(terms[0], first_term_solution)
        subscript += 1
        
    t = first_term_solution.as_ordered_terms()[1]
    print(f"t = {t}")

def main(filename):
    
    with open(filename, "r") as file:
        estimate = int(file.readline().rstrip())
        bus_ids = file.readline().rstrip().split(",")    
    
    # assume all bus ids are prime numbers
    #bus_ids = [17, 'x', 13, 19]
    listed_buses = []
    offsets_from_t = {}
    for i, id in enumerate(bus_ids):
        if id != 'x':
            id = int(id)
            listed_buses.append(id)
            offsets_from_t[id] = i
    
    part2(listed_buses, offsets_from_t)

if __name__ == "__main__":
    main("test.txt")
