;; Takes an expression and a set of test/form pairs. Threads expr (via ->)
;; through each form for which the corresponding test
;; expression is true. Note that, unlike cond branching, cond-> threading does
;; not short circuit after the first true test expression.

(cond-> 1
  (= 1 1 ) (+ 1 ))
;; => 2

(cond-> 1
  (= 1 1) (+ 1)
  true inc 
  false (* 2)
  true (* 10))
;; => 30
