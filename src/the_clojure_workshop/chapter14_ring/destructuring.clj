(def m {:a 1
        :b 2 
        :c {:d 3 :e 4}})

(let [{:keys [a]} m]
  a)
;; => 1

(let [{:keys [d]} m]
  d)
;; => nil
;; how to get value of d via destructuring  - pending 
;; see also p.no 645 - ....... {:keys [id] :params}.....