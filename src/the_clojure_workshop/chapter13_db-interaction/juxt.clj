(def j (juxt identity inc dec))

(j 10)
;; => [10 11 9]

((juxt :a :b) {:a 1 :b 2})
;; => [1 2]

(name :a)
;; => a

(quot 26 5)
;; => 5

(rem 26 5)
;; => 1


(let [{:keys [name] :as row} {:name "Seerat" :surname "Mir"}]
  (println "full row/map - " row)
  (println "name only -" name))
;; => full row/map -  {:name Seerat, :surname Mir}
;; => name only - Seerat


((juxt + - min max) 3 2 4)
((juxt take drop) 3 [1 2 3 4 5 6 7])
