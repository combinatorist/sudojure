(ns sudojure.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

; import numpy as np
; import warnings
;
; box_shape    = (3, 3)
; box_size     = box_shape[0] * box_shape[1]
; puzzle_shape = (box_size,) * 2
; puzzle_size  = box_size ** 2
;
; def load(file):
;     """
;     loads file
;     """
;     with open(file) as f:
;         contents = f.read()
;     return contents
;
;
; def from_one_line(one_line_sudoku):
;     """
;     creates a 2d array view from sudoku in "one line" format
;     """
;     length = len(one_line_sudoku)
;     if length > puzzle_size:
;         warning_body = ["Too many values: Needed {}, but received {}",
;                         "Ignored values: {} ..."]
;         warnings.warn('\n'.join(warning_body).format(puzzle_size, length,
;             one_line_sudoku[puzzle_size:puzzle_size + 7]
;             .encode('string_escape')))
;     sudoku1d = np.array([char for char in one_line_sudoku[:puzzle_size]])
;     sudoku2d = sudoku1d.copy().reshape(puzzle_shape)
;     return sudoku2d
;
;
; def array_to_int(array, find='.', replace=0):
;     """
;     converts array to int dtype
;     default replaces '.' with 0 (helpful for simple sudoku format: .ss)
;     """
;     array[array == find] = replace
;     int_array = np.asarray(array, dtype=int)
;     return int_array
;
;
; def abstract_array(array, value_list = range(1, box_size + 1)):
;     """
;     creates boolean array with additional dimension representing values
;     """
;     array_list = []
;     for i in value_list:
;         array_list.append(array == i)
;     boolean_array = np.stack(array_list)
;     three_valued_array = np.asarray(boolean_array, dtype=object)
;     three_valued_array[three_valued_array == False] = None
;     return three_valued_array
;
; def to_int_array(array):
;     """
;     reverses `abstract_array` function above
;     """
;     array = array.copy()
;     for index in range(len(array)):
;         value_array = array[index]
;         value_array[value_array != True] = 0
;         value_array[value_array == True] = index + 1
;
;     return sum(array)
;
; def box_sudoku(sudoku2d):
;     """
;     creates a view that indicates the boxes
;     """
;     # First converts shape from 9 rows by 9 columns to (3, 3, 3, 3) [unit_rows]
;     unit_row_sudoku = sudoku2d.reshape(box_shape * 2)
;     # Then, swaps middle 2 axes and converts to new (9, 9) [9 boxes by 9 cells]
;     return np.swapaxes(unit_row_sudoku, -3, -2).reshape(puzzle_shape)
;
; def rotate(array):
;     """
;     rotates axes once by pushing first axis to the last position
;     """
;     return np.moveaxis(array, 0, -1)
;
; def eliminate(array):
;     """
;     marks unknowns false where they would conflict with known values
;     """
;     array = array.copy()
;     for axis in range(3):
;         groups = array.reshape((puzzle_size, box_size))
;         for group in groups:
;             occupied = max(group)
;             if occupied == True:
;                 group[group != True] = False
;
;         array = groups.reshape((box_size,) * 3)
;         array = rotate(array)
;
;     layers = []
;     for layer in array:
;         groups = box_sudoku(layer)
;         for group in groups:
;             occupied = max(group)
;             if occupied == True:
;                 group[group != True] = False
;
;         layers.append(box_sudoku(groups))
;     array = np.stack(layers)
;     return array
;
; def deduce(array):
;     """
;     marks unknowns true where alternatives have been eliminated
;     """
;     array = array.copy()
;     for axis in range(3):
;         groups = array.reshape((puzzle_size, box_size))
;         for group in groups:
;             exhausted = len([x for x in group if x == False])
;             if exhausted == box_size - 1:
;                 group[group != False] = True
;
;         array = groups.reshape((box_size,) * 3)
;         array = rotate(array)
;
;     layers = []
;     for layer in array:
;         groups = box_sudoku(layer)
;         for group in groups:
;             exhausted = len([x for x in group if x == False])
;             if exhausted == box_size - 1:
;                 group[group != False] = True
;
;         layers.append(box_sudoku(groups))
;     array = np.stack(layers)
;     return array
;
; def resolve(array):
;     """
;     attempts one round of progress on puzzle
;     """
;     return deduce(eliminate(array))
;
; def solve(array):
;     """
;     solves entire puzzle as far as possible
;     """
;     resolved = resolve(array.copy())
;     while np.any(np.not_equal(resolved, array)):
;         array = resolved.copy()
;         resolved = resolve(array.copy())
;
;     return resolved
;
; def progress(int_array):
;     """
;     calculates progress so far on sudoku
;     """
;     return float(np.count_nonzero(int_array)) / int_array.size
;
; def demo(int_array, name = 'your puzzle'):
;     """
;     prints sudoku before / after solution
;     """
;     status = progress(int_array)
;     print 'Attempting "{}" sudoku puzzle ({}%)'.format(name, status * 100)
;     print int_array
;     solution = to_int_array(solve(abstract_array(int_array)))
;     print 'Solution so far... ({}%)'.format(progress(solution) * 100)
;     print solution
;
; if __name__ == '__main__':
;   main()
