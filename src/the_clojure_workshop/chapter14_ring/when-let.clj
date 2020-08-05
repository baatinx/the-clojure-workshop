;; Roughly the same as (when (seq xs) (let [x (first xs)] body)) but xs is evaluated only once
(def s [1 2 3 4])

(when-first [n s]
  n)
;; => 1

(when [1 2 3 (println "HI!") 4]
  "Bye!")
;; => HI!
;;    "Bye!"

