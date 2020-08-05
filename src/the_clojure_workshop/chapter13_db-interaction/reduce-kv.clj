;; increment map values 
(def abc-map {:a 1
              :b 2
              :c 3})
(seq abc-map)
;; => ([:a 1] [:b 2] [:c 3])

;; via reduce 
(reduce (fn [new-map [k v]]
          (assoc new-map k (inc v)))
        {}
        abc-map)
;; => {:a 2, :b 3, :c 4}


;; via reduce-kv
(reduce-kv (fn [new-map k v]
             (assoc new-map k (inc v)))
           {}
           abc-map)
;; => {:a 2, :b 3, :c 4}

(reduce-kv (fn [res idx itm] (assoc res idx itm)) {} ["one" "two" "three"])
