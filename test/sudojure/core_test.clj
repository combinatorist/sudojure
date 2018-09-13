(ns sudojure.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.matrix :refer :all]
            [sudojure.core :refer :all]))

(def example-sudoku-array
  (array [[0 0 0 8 4 7 0 5 0]
          [0 0 5 0 0 0 2 0 7]
          [0 4 0 0 6 0 0 3 0]
          [0 0 0 0 7 0 3 0 0]
          [3 0 0 0 0 0 0 0 2]
          [0 0 7 0 1 0 0 0 0]
          [0 5 0 0 9 0 0 1 0]
          [8 0 4 0 0 0 5 0 0]
          [0 1 0 6 8 5 0 0 0]]))

(def example-abstract-sudoku-array
  (array [[[-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1  1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1  1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1  1 -1 -1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1  1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1  1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1  1 -1]
           [-1 -1 -1 -1 -1 -1  1 -1 -1]
           [ 1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1  1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1  1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1  1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1 -1 -1  1 -1]
           [-1 -1  1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1  1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1  1 -1 -1]
           [-1 -1 -1 -1 -1  1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1  1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1  1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1  1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1  1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1  1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1  1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]]

          [[-1 -1 -1  1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [ 1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1  1 -1 -1 -1 -1]]

          [[-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1  1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]
           [-1 -1 -1 -1 -1 -1 -1 -1 -1]]]))

(def example-box-sudoku
  (array [[0, 0, 0, 0, 0, 5, 0, 4, 0],
          [8, 4, 7, 0, 0, 0, 0, 6, 0],
          [0, 5, 0, 2, 0, 7, 0, 3, 0],
          [0, 0, 0, 3, 0, 0, 0, 0, 7],
          [0, 7, 0, 0, 0, 0, 0, 1, 0],
          [3, 0, 0, 0, 0, 2, 0, 0, 0],
          [0, 5, 0, 8, 0, 4, 0, 1, 0],
          [0, 9, 0, 0, 0, 0, 6, 8, 5],
          [0, 1, 0, 5, 0, 0, 0, 0, 0]]))

(def example-partial-elimination
  (array [[-1,  0, -1,  0,  0,  0, -1,  0, -1],
          [-1,  0,  0, -1,  0, -1,  0,  0,  0],
          [-1,  0, -1, -1,  0, -1, -1,  0, -1],
          [-1,  0, -1, -1,  0, -1,  0,  0, -1],
          [ 0,  0, -1, -1,  0, -1, -1,  0,  0],
          [ 0,  0,  0,  0,  1,  0,  0,  0,  0],
          [ 0,  0,  0,  0,  0,  0,  0,  1,  0],
          [ 0,  0,  0, -1,  0, -1,  0,  0, -1],
          [ 0,  1,  0,  0,  0,  0,  0,  0,  0]]))

(deftest abstract-sudoku-array-test
  (testing "abstact-sudoku-array should convert from sudoku-array"
    (is (= example-abstract-sudoku-array
           (abstract-sudoku-array example-sudoku-array)))))

(deftest box-sudoku-test
  (testing "box-sudoku should convert from abstract-sudoku-array"
    (is (= example-box-sudoku
           (box-sudoku example-sudoku-array)))))

(deftest eliminate-test
  (testing "eliminate should find a true to mark all unknowns false")
    (let [result (eliminate example-abstract-sudoku-array)
          comparison (cmp (slice result 0) example-partial-elimination)]
      ; -1 (unkonwn) < 0 (false) < 1 (true), so result is at least as "conclusive" previous
      (is (and (= 0 (emin comparison))
               (= 1 (emax comparison))))))

(deftest deduce-test
  (testing "deduce should find last unknown value a true"
    (let [result (deduce (eliminate example-abstract-sudoku-array))
          ; we'll inspect the 2 in the 8th row and 5th column
          index-of-interest [(- 2 1) (- 8 1) (- 5 1)]]
      ; previously unknown
      (is (and (= -1 (apply select (cons example-abstract-sudoku-array index-of-interest)))
      ; now known there's a 1
               (= 1 (apply select (cons result index-of-interest))))))))

; example_one_line = '...847.5...5...2.7.4..6..3.....7.3..3.......2..7.1.....5..9..1.8.4...5...1.685...\n'
;
; example_sudoku2d = np.asarray([['.', '.', '.', '8', '4', '7', '.', '5', '.'],
;                                ['.', '.', '5', '.', '.', '.', '2', '.', '7'],
;                                ['.', '4', '.', '.', '6', '.', '.', '3', '.'],
;                                ['.', '.', '.', '.', '7', '.', '3', '.', '.'],
;                                ['3', '.', '.', '.', '.', '.', '.', '.', '2'],
;                                ['.', '.', '7', '.', '1', '.', '.', '.', '.'],
;                                ['.', '5', '.', '.', '9', '.', '.', '1', '.'],
;                                ['8', '.', '4', '.', '.', '.', '5', '.', '.'],
;                                ['.', '1', '.', '6', '8', '5', '.', '.', '.']])
;
; example_solution = np.asarray(
;     [[1, 3, 2, 8, 4, 7, 9, 5, 6],
;      [6, 8, 5, 1, 3, 9, 2, 4, 7],
;      [7, 4, 9, 5, 6, 2, 1, 3, 8],
;      [5, 2, 8, 4, 7, 6, 3, 9, 1],
;      [3, 6, 1, 9, 5, 8, 4, 7, 2],
;      [4, 9, 7, 2, 1, 3, 6, 8, 5],
;      [2, 5, 6, 7, 9, 4, 8, 1, 3],
;      [8, 7, 4, 3, 2, 1, 5, 6, 9],
;      [9, 1, 3, 6, 8, 5, 7, 2, 4]]
; )
;
; # http://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html
; hardest_sudoku = np.asarray(
;     [[8, 0, 0, 0, 0, 0, 0, 0, 0],
;      [0, 0, 3, 6, 0, 0, 0, 0, 0],
;      [0, 7, 0, 0, 9, 0, 2, 0, 0],
;      [0, 5, 0, 0, 0, 7, 0, 0, 0],
;      [0, 0, 0, 0, 4, 5, 7, 0, 0],
;      [0, 0, 0, 1, 0, 0, 0, 3, 0],
;      [0, 0, 1, 0, 0, 0, 0, 6, 8],
;      [0, 0, 8, 5, 0, 0, 0, 1, 0],
;      [0, 9, 0, 0, 0, 0, 4, 0, 0]]
; )
;
; class bq_tests(unittest.TestCase):
;     def test001_load(self):
;         content = sud.load('test001_load.txt')
;         self.assertEqual(
;             content,
;             example_one_line
;         )
;
;     def test002_from_one_line(self):
;         sudoku2d = sud.from_one_line(example_one_line)
;         self.assertEqual(
;             sudoku2d.shape,
;             (9,)*2
;         )
;         np.testing.assert_equal(
;             sudoku2d,
;             example_sudoku2d
;         )
;
;     def test003_array_to_int(self):
;         int_array = sud.array_to_int(example_sudoku2d)
;         np.testing.assert_equal(
;             int_array,
;             example_sudoku2d_int
;         )
;
;     def test004_abstract_array(self):
;         abstract_array = sud.abstract_array(example_sudoku2d_int)
;         np.testing.assert_equal(
;           abstract_array,
;           example_sudoku3d_bool
;         )
;
;         np.testing.assert_equal(
;           sud.to_int_array(abstract_array),
;           example_sudoku2d_int
;         )
;
;
;     def test005_rotate(self):
;         positional_3d_array = np.asarray(range(1, 9 ** 3 + 1)).reshape((9,) * 3)
;         result = sud.rotate(sud.rotate(sud.rotate(positional_3d_array)))
;         np.testing.assert_equal(
;             result,
;             positional_3d_array
;         )
;
;     def test006_group_ungroup(self):
;         # demonstrates logic used inside eliminate function
;         test_array = np.arange(27).reshape((3, 3, 3))
;         grouped = test_array.reshape((9, 3))
;         ungrouped = grouped.reshape((3, 3, 3))
;
;         np.testing.assert_equal(
;             ungrouped,
;             test_array
;         )
;
;     def test009_solve(self):
;         np.testing.assert_equal(
;             sud.to_int_array(sud.solve(example_sudoku3d_bool)),
;             example_solution
;         )
;
;     def test999_end_to_end(self):
;         np.testing.assert_equal(
;             sud.abstract_array(sud.array_to_int(
;                     sud.from_one_line(sud.load('test001_load.txt'))
;                 )),
;             example_sudoku3d_bool
;         )
;
;
; if __name__ == '__main__':
;         puzzles = {'example': example_sudoku2d_int, 'hardest': hardest_sudoku}
;         for name, puzzle in sorted(puzzles.items()):
;             sud.demo(puzzle, name)
;         unittest.main()
