(ns sudojure.core
  (:require [clojure.core.matrix :refer :all]
            [clojure.core.matrix.selection :refer :all]))


(defn puzzle-value-list
  "returns the cannonical values for an array"
  [array-in]
  (range 1 (+ (dimension-count array-in 0) 1)))

(def default-box-shape [3 3])
(def default-box-size (ecount (new-array default-box-shape)))

(def default-puzzle-shape [default-box-size default-box-size])
(def default-puzzle-size (ecount (new-array default-puzzle-shape)))
(def default-puzzle-value-list (range default-puzzle-size))

(defn abstract-sudoku-array
  "creates boolean array with additional dimension representing values"
  [sudoku-array]
  (let [values (puzzle-value-list sudoku-array)
        ; implementing 3 valued logic as [-1, 0, 1] for now
        comparisons (map (fn [x] (eq sudoku-array x)) values)]
       (emap (fn [x] (if (= x 0) -1 x)) (array comparisons))))

(defn box-sudoku
  "creates a view that indicates the boxes"
  [abstract-sudoku]
  ; First converts shape from 9 rows by 9 columns to (3, 3, 3, 3) [unit-rows]
  (let [unit-row-sudoku
          (reshape abstract-sudoku (concat default-box-shape default-box-shape))
  ; Then, swaps middle 2 axes and converts to new (9, 9) [9 boxes by 9 cells]
        transposed (transpose unit-row-sudoku [0 2 1 3])]
       (reshape transposed default-puzzle-shape)))

(defn abstract-box-sudoku
  "creates a view that indicates the boxes in an abstract sudoku"
  [abstract-sudoku]
  ; First converts shape from 9 vals x 9 rows x 9 columns to (9, 3, 3, 3, 3) [unit-rows]
  (let [unit-row-sudoku
          (reshape abstract-sudoku (cons default-puzzle-size (concat default-box-shape default-box-shape)))
  ; Then, swaps middle 2 axes and converts to new (9, 9, 9) [nums x boxes x cells]
        transposed (transpose unit-row-sudoku [0 1 3 2 4])]
       (reshape transposed [default-box-size default-box-size default-box-size])))

(defn rotate-puzzle
  "rotates axes once by pushing first axis to the last position"
  [sudoku-array]
  (transpose sudoku-array [1 2 0]))

(defn set-local-eliminate!
  "marks unknowns true where alternatives have been eliminated in a group (mutable sub-array)"
  [group]
  (let [where-true (select group (where-slice (fn [x] (= x 1))))
        count-true (ecount where-true)
        occupied? (> count-true 0)]
       ; taking max with 0, turns -1 (unknown) to 0 (false), ignoring 1 (true)
       (if occupied? (emap! (fn [x] (max x 0)) group))))

(defn eliminate
  "marks unknowns false where they would conflict with known values"
  [sudoku-array]
  (let [sud-array (mutable sudoku-array)]
    (for [axis (range 3)]
      ((for [group (reshape sud-array [default-puzzle-size default-box-size])]
        (set-local-eliminate! group))
      (rotate (reshape sud-array [default-box-size default-box-size default-box-size]))))

    (for [group (abstract-box-sudoku sud-array)] (set-local-eliminate! group))
    ;; somtimes relying on mutability of shape, sometimes not
    (immutable (abstract-box-sudoku sud-array))))

(defn set-local-deduce!
  "marks unknowns true where alternatives have been eliminated in a group (mutable sub-array)"
  [group]
  (let [where-false (select group (where-slice (fn [x] (= x 0))))
        count-false (ecount where-false)
        exhausted? (= count-false (- default-box-size 1))]
       ; abs turns -1 (unknown) to 1 (true), ignoring 0 (false)
       (if exhausted? (emap! (abs) group))))

(defn deduce
  [sudoku-array]
  (let [sud-array (mutable sudoku-array)]
    (for [axis (range 3)]
      ((for [group (reshape sud-array [default-puzzle-size default-box-size])]
        (set-local-deduce! group))
      (rotate (reshape sud-array [default-box-size default-box-size default-box-size]))))

    (for [group (abstract-box-sudoku sud-array)] (set-local-deduce! group))
    ;; somtimes relying on mutability of shape, sometimes not
    (immutable (abstract-box-sudoku sud-array))))

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
