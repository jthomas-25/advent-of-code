import itertools


ACTIVE = '#'
INACTIVE = '.'


class Cube(object):
    def __init__(self, x, y, z, state=INACTIVE):
        self.x, self.y, self.z = x, y, z
        self.pos = (self.x, self.y, self.z)
        self.state = state
        self.next_state = None
        self.neighbors = []
        self.num_active_neighbors = None
        self.grid_layer = None
        self.grid = None 

    # Moore neighborhood of distance 1 in 3-D, not including the center
    # 26 neighbors
    def get_neighbor_coords(self):
        if not self.neighbors:
            delta_x_range = delta_y_range = delta_z_range = [-1, 0, 1]
            for vector in itertools.product(delta_x_range, delta_y_range, delta_z_range):
                dx, dy, dz = vector
                if dx == 0 and dy == 0 and dz == 0:
                    continue
                pos = (self.x + dx, self.y + dy, self.z + dz)
                self.neighbors.append(pos)
        return self.neighbors
    
    def get_num_active_neighbors(self):
        if self.num_active_neighbors is None:
            self.num_active_neighbors = 0
            for n_pos in self.get_neighbor_coords():
                if self.grid.has_cube_at(n_pos):
                    neighbor = self.grid.get_cube(n_pos)
                    if neighbor.is_active():
                        self.num_active_neighbors += 1
        return self.num_active_neighbors
    
    def is_active(self):
        return self.state == ACTIVE
    
    # compute the next state
    def step(self):
        self.next_state = self.state
        num_active_neighbors = self.get_num_active_neighbors()
        if self.is_active():
            if not (num_active_neighbors == 2 or num_active_neighbors == 3):
                self.next_state = INACTIVE
        else:
            if num_active_neighbors == 3:
                self.next_state = ACTIVE

    # apply the state computed in step()
    def advance(self):
        self.state = self.next_state
    
    def is_on_edge_of_layer(self):
        return self.is_on_top_edge_of_layer() or self.is_on_bottom_edge_of_layer() or self.is_on_left_edge_of_layer() or self.is_on_right_edge_of_layer()

    def is_on_top_edge_of_layer(self):
        return self.y == self.grid_layer.min_y if self.grid_layer else False

    def is_on_bottom_edge_of_layer(self):
        return self.y == self.grid_layer.max_y if self.grid_layer else False
    
    def is_on_left_edge_of_layer(self):
        return self.x == self.grid_layer.min_x if self.grid_layer else False
    
    def is_on_right_edge_of_layer(self):
        return self.x == self.grid_layer.max_x if self.grid_layer else False
    
    def is_on_edge_of_grid(self):
        return self.is_on_top_edge_of_grid() or self.is_on_bottom_edge_of_grid() or self.is_on_left_edge_of_grid() or self.is_on_right_edge_of_grid()
    
    def is_on_top_edge_of_grid(self):
        return self.y == self.grid.min_y if self.grid else False

    def is_on_bottom_edge_of_grid(self):
        return self.y == self.grid.max_y if self.grid else False
    
    def is_on_left_edge_of_grid(self):
        return self.x == self.grid.min_x if self.grid else False
    
    def is_on_right_edge_of_grid(self):
        return self.x == self.grid.max_x if self.grid else False

class Hypercube(Cube):
    def __init__(self, x, y, z, w, state=INACTIVE):
        super().__init__(x, y, z, state)
        self.w = w
        self.pos = (self.x, self.y, self.z, self.w)
    
    # Moore neighborhood of distance 1 in 4-D, not including the center
    # 80 neighbors
    def get_neighbor_coords(self):
        if not self.neighbors:
            delta_x_range = delta_y_range = delta_z_range = delta_w_range = [-1, 0, 1]
            for vector in itertools.product(delta_x_range, delta_y_range, delta_z_range, delta_w_range):
                dx, dy, dz, dw = vector
                if dx == 0 and dy == 0 and dz == 0 and dw == 0:
                    continue
                pos = (self.x + dx, self.y + dy, self.z + dz, self.w + dw)
                self.neighbors.append(pos)
        return self.neighbors
