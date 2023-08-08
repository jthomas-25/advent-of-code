from cube import Cube, Hypercube


# base class for all grids
class Grid(object):
    def __init__(self):
        self.cubes = {}
        self.active_cubes = {}
        self.num_active_cubes = 0
    
    def get_cube(self, pos):
        return self.cubes.get(pos)

    def has_cube_at(self, pos):
        return self.get_cube(pos) is not None
    
    def place_cube(self, cube):
        self.cubes[cube.pos] = cube
        cube.grid = self

    def has_at_least_one_active_cube(self):
        return self.num_active_cubes >= 1

# base class for resizable n-dimensional grids, where n >= 3
class LayeredGrid(Grid):
    def __init__(self, min_x=0, max_x=0, min_y=0, max_y=0):
        super().__init__()
        self.layers = {}
        self.min_x, self.max_x = min_x, max_x
        self.min_y, self.max_y = min_y, max_y

    def add_layer(self, pos):
        pass
    
    def get_layer(self, pos):
        return self.layers.get(pos)
    
    def has_layer_at(self, pos):
        return self.get_layer(pos) is not None
    
    def place_cube_at(self, pos):
        pass
    
    def place_cube(self, cube):
        super().place_cube(cube)
        self.resize_if_needed(cube.grid_layer)
    
    def resize_if_needed(self, layer):
        if self.min_x > layer.min_x:
            self.min_x = layer.min_x
        if self.max_x < layer.max_x:
            self.max_x = layer.max_x
        if self.min_y > layer.min_y:
            self.min_y = layer.min_y
        if self.max_y < layer.max_y:
            self.max_y = layer.max_y
    
    def update_list_of_active_cubes(self, toggled_cubes):
        for cube in toggled_cubes:
            if cube.is_active():
                # inform neighbors that this cube is now active
                for n_pos in cube.get_neighbor_coords():
                    if self.has_cube_at(n_pos):
                        neighbor = self.get_cube(n_pos)
                        neighbor.num_active_neighbors = neighbor.get_num_active_neighbors() + 1
                self.active_cubes[cube.pos] = cube
                self.num_active_cubes += 1
                cube.grid_layer.active_cubes[cube.pos] = cube
                cube.grid_layer.num_active_cubes += 1
            else:
                # inform neighbors that this cube is now inactive
                for n_pos in cube.get_neighbor_coords():
                    if self.has_cube_at(n_pos):
                        neighbor = self.get_cube(n_pos)
                        neighbor.num_active_neighbors = neighbor.get_num_active_neighbors() - 1
                del self.active_cubes[cube.pos]
                self.num_active_cubes -= 1
                del cube.grid_layer.active_cubes[cube.pos]
                cube.grid_layer.num_active_cubes -= 1

    def expand_if_needed(self):
        expand_positive_x, expand_negative_x = False, False
        expand_negative_y, expand_positive_y = False, False
        layers = list(self.layers.values())
        for layer in layers:
            if layer.has_at_least_one_active_cube():
                # expand layers along the x and y axes for all layers
                for cube in layer.active_cubes.values():
                    if not expand_positive_x and cube.is_on_right_edge_of_layer():
                        expand_positive_x = True
                        for l in self.layers.values():
                            l.expand_positive_x()
                    elif not expand_negative_x and cube.is_on_left_edge_of_layer():
                        expand_negative_x = True
                        for l in self.layers.values():
                            l.expand_negative_x()
                    if not expand_negative_y and cube.is_on_top_edge_of_layer():
                        expand_negative_y = True
                        for l in self.layers.values():
                            l.expand_negative_y()
                    elif not expand_positive_y and cube.is_on_bottom_edge_of_layer():
                        expand_positive_y = True
                        for l in self.layers.values():
                            l.expand_positive_y()
        self.add_new_layers_if_needed()
    
    def add_new_layers_if_needed(self):
        pass
    
    # top-down frame of view that all layers will share
    def get_viewport(self, follow_active_cubes=False):
        if follow_active_cubes:
            # find the smallest rectangular region that contains all active cubes
            min_x, min_y = 0, 0    # top-left
            max_x, max_y = 0, 0    # bottom-right
            for cube in self.active_cubes.values():
                if min_x > cube.x:
                    min_x = cube.x
                if max_x < cube.x:
                    max_x = cube.x
                if min_y > cube.y:
                    min_y = cube.y
                if max_y < cube.y:
                    max_y = cube.y
        else:
            # show each layer in its entirety
            min_x, min_y = self.min_x, self.min_y
            max_x, max_y = self.max_x, self.max_y
        viewport = [min_x, min_y, max_x, max_y]
        return viewport
    
    def display(self, only_show_active_layers=False, follow_active_cubes=False, verbose=False):
        pass

# resizable three-dimensional grid
class Grid3D(LayeredGrid):
    def __init__(self, min_x=0, max_x=0, min_y=0, max_y=0):
        super().__init__(min_x, max_x, min_y, max_y)
        self.min_z, self.max_z = 0, 0

    def add_layer(self, z):
        if not self.has_layer_at(z):
            layer = GridLayer3D(z, self)
            self.layers[z] = layer
            for y in range(self.min_y, self.max_y + 1):
                for x in range(self.min_x, self.max_x + 1):
                    pos = (x, y, z)
                    self.place_cube_at(pos)

    def place_cube_at(self, pos):
        x, y, z = pos
        self.get_layer(z).place_cube_at(x, y)

    def resize_if_needed(self, layer):
        super().resize_if_needed(layer)
        if self.min_z > layer.z:
            self.min_z = layer.z
        if self.max_z < layer.z:
            self.max_z = layer.z

    # expand along the z axis
    def add_new_layers_if_needed(self):
        top_layer = self.get_layer(self.max_z)
        bottom_layer = self.get_layer(self.min_z)
        if top_layer.has_at_least_one_active_cube():
            self.expand_positive_z()
        if bottom_layer.has_at_least_one_active_cube():
            self.expand_negative_z()

    def expand_positive_z(self):
        top_layer = self.get_layer(self.max_z)
        self.add_layer(top_layer.z + 1)
    
    def expand_negative_z(self):
        bottom_layer = self.get_layer(self.min_z)
        self.add_layer(bottom_layer.z - 1)
    
    def display(self, only_show_active_layers=False, follow_active_cubes=False, verbose=False):
        viewport = self.get_viewport(follow_active_cubes)
        for z in range(self.min_z, self.max_z + 1):
            layer = self.get_layer(z)
            if only_show_active_layers:
                if layer.has_at_least_one_active_cube():
                    layer.display(viewport, follow_active_cubes, verbose)
            else:
                layer.display(viewport, follow_active_cubes, verbose)

# resizable four-dimensional grid
class Grid4D(LayeredGrid):
    def __init__(self, min_x=0, max_x=0, min_y=0, max_y=0):
        super().__init__(min_x, max_x, min_y, max_y)
        self.min_z, self.max_z = 0, 0
        self.min_w, self.max_w = 0, 0

    def add_layer(self, z, w):
        if not self.has_layer_at((z, w)):
            layer = GridLayer4D(z, w, self)
            self.layers[(z, w)] = layer
            for y in range(self.min_y, self.max_y + 1):
                for x in range(self.min_x, self.max_x + 1):
                    pos = (x, y, z, w)
                    self.place_cube_at(pos)

    def place_cube_at(self, pos):
        x, y, z, w = pos
        self.get_layer((z, w)).place_cube_at(x, y)
    
    def resize_if_needed(self, layer):
        super().resize_if_needed(layer)
        if self.min_z > layer.z:
            self.min_z = layer.z
        if self.max_z < layer.z:
            self.max_z = layer.z
        if self.min_w > layer.w:
            self.min_w = layer.w
        if self.max_w < layer.w:
            self.max_w = layer.w
    
    # expand along the z and w axes
    def add_new_layers_if_needed(self):
        layers = list(self.layers.values())
        for layer in layers:
            if layer.has_at_least_one_active_cube():
                if layer.z == self.max_z:
                    self.expand_positive_z()
                elif layer.z == self.min_z:
                    self.expand_negative_z()
                if layer.w == self.max_w:
                    self.expand_positive_w()
                elif layer.w == self.min_w:
                    self.expand_negative_w()
     
    def expand_positive_z(self):
        z = self.max_z + 1
        for w in range(self.min_w, self.max_w + 1):
            self.add_layer(z, w)
        
    def expand_negative_z(self):
        z = self.min_z - 1
        for w in range(self.min_w, self.max_w + 1):
            self.add_layer(z, w)

    def expand_positive_w(self):
        w = self.max_w + 1
        for z in range(self.min_z, self.max_z + 1):
            self.add_layer(z, w)

    def expand_negative_w(self):
        w = self.min_w - 1
        for z in range(self.min_z, self.max_z + 1):
            self.add_layer(z, w)

    def display(self, only_show_active_layers=False, follow_active_cubes=False, verbose=False):
        viewport = self.get_viewport(follow_active_cubes)
        for w in range(self.min_w, self.max_w + 1):
            for z in range(self.min_z, self.max_z + 1):
                layer = self.get_layer((z, w))
                if only_show_active_layers:
                    if layer.has_at_least_one_active_cube():
                        layer.display(viewport, follow_active_cubes, verbose)
                else:
                    layer.display(viewport, follow_active_cubes, verbose)

# resizable two-dimensional grid
class Grid2D(Grid):
    def __init__(self):
        super().__init__()
        self.min_x, self.max_x = 0, 0
        self.min_y, self.max_y = 0, 0
    
    def place_cube_at(self, x, y):
        if not self.has_cube_at((x, y)):
            cube = Cube(x, y, 0)
            self.place_cube(cube)

    def place_cube(self, cube):
        pos = (cube.x, cube.y)
        self.cubes[pos] = cube
        cube.grid = self
        self.resize_if_needed(cube)
    
    def expand_positive_x(self):
        x = 0 if self.max_x == 0 else self.max_x + 1
        for y in range(self.min_y, self.max_y + 1):
            self.place_cube_at(x, y)

    def expand_negative_x(self):
        x = 0 if self.min_x == 0 else self.min_x - 1
        for y in range(self.min_y, self.max_y + 1):
            self.place_cube_at(x, y)

    def expand_negative_y(self):
        y = 0 if self.min_y == 0 else self.min_y - 1
        for x in range(self.min_x, self.max_x + 1):
            self.place_cube_at(x, y)

    def expand_positive_y(self):
        y = 0 if self.max_y == 0 else self.max_y + 1
        for x in range(self.min_x, self.max_x + 1):
            self.place_cube_at(x, y)
    
    def resize_if_needed(self, cube):
        if self.min_x > cube.x:
            self.min_x = cube.x
        if self.max_x < cube.x:
            self.max_x = cube.x
        if self.min_y > cube.y:
            self.min_y = cube.y
        if self.max_y < cube.y:
            self.max_y = cube.y
    
    def display(self, viewport=None, follow_active_cubes=False, verbose=False):
        if not viewport:
            viewport = self.get_viewport(follow_active_cubes)
        if verbose:
            self.show_with_xy_labels(viewport)
        else:
            self.show_without_xy_labels(viewport)
    
    def show_without_xy_labels(self, viewport):
        min_x, min_y, max_x, max_y = viewport
        for y in range(min_y, max_y + 1):
            for x in range(min_x, max_x + 1):
                pos = (x, y)
                cube = self.get_cube(pos)
                print(cube.state, end="")
            print()
        print()

    def show_with_xy_labels(self, viewport):
        min_x, min_y, max_x, max_y = viewport
        for y in range(min_y, max_y + 1):
            for x in range(min_x, max_x + 1):
                pos = (x, y)
                cube = self.get_cube(pos)
                num_spaces = 1 if x == min_x else 2
                print(" " * num_spaces + cube.state, end="")
            # y coord label
            num_spaces = 2 if 0 <= y <= 9 else 1
            print(" " * num_spaces + str(y))
        # x coord label
        for x in range(min_x, max_x + 1):
            if x == min_x:
                num_spaces = 0
            elif 0 <= x <= 9:
                num_spaces = 2
            else:
                num_spaces = 1
            print(" " * num_spaces + str(x), end="")
        print("\n")

# grid that is perpendicular (orthogonal) to the z-axis at the given z-coordinate
class GridLayer3D(Grid2D):
    def __init__(self, z, grid):
        super().__init__()
        self.z = z
        self.grid = grid

    def place_cube_at(self, x, y):
        if not self.has_cube_at((x, y)):
            cube = Cube(x, y, self.z)
            self.place_cube(cube)

    def place_cube(self, cube):
        super().place_cube(cube)
        cube.grid_layer = self
        self.grid.place_cube(cube)

    def display(self, viewport, follow_active_cubes, verbose):
        print(f"z = {self.z}")
        super().display(viewport, follow_active_cubes, verbose)

class GridLayer4D(Grid2D):
    def __init__(self, z, w, grid):
        super().__init__()
        self.z, self.w = z, w
        self.grid = grid
    
    def place_cube_at(self, x, y):
        if not self.has_cube_at((x, y)):
            cube = Hypercube(x, y, self.z, self.w)
            self.place_cube(cube)
    
    def place_cube(self, cube):
        super().place_cube(cube)
        cube.grid_layer = self
        self.grid.place_cube(cube)
    
    def display(self, viewport, follow_active_cubes, verbose):
        print(f"z = {self.z}, w = {self.w}")
        super().display(viewport, follow_active_cubes, verbose)
