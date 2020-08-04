(ns the-clojure-workshop.chapter13-db-interaction.crud
  (:require [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hikari]
            [clojure.string :as s]))

(def db {:datasource (hikari/make-datasource {:jdbc-url "jdbc:derby:derby-local;create=true"})})

;; ****************************************************************************************************
;; "C"rud

(jdbc/insert! db :app_user {:first_name "Mustafa"
                            :surname    "Basit"
                            :height     170
                            :weight     63}
              {:keywordize? false})

(jdbc/insert! db :app_user
              [:first_name :surname :height :weight]
              ["Seerat" "Mir" 120 50])

(jdbc/insert-multi! db :activity
                    [{:activity_type "run" :distance 8.67 :duration 2520 :user_id 1}
                     {:activity_type "run" :distance 8.67 :duration 2520 :user_id 2}
                     {:activity_type "cycle" :distance 17.68 :duration 5703 :user_id 2}])

(jdbc/insert-multi! db :activity
                    [:activity_type :distance :duration :user_id]
                    [["abc"  17.68 2703  1]
                     ["def"  17.68 2703  1]
                     ["ghi"  17.68 2703  1]])

;; doing multiple inserts in vector mode will execute the inserts in batched transactions.
;; This is more performant when doing a large number of inserts.

;; When working with map mode, we simply omit any unwanted key-value pairs. In vector mode, we must specify
;; the column names, and then insert nil values as required


;; ****************************************************************************************************
;; c"R"ud

(jdbc/query db ["select * from activity"])

(jdbc/query db ["select * from app_user"])
;; => ({:id 1, :first_name "Mustafa", :surname "Basit", :height 170, :weight 63}
;;     {:id 2, :first_name "Seerat", :surname "Mir", :height 120, :weight 50})

(jdbc/query db ["select * from app_user"] {:as-arrays? true})
;; => [[:id :first_name :surname :height :weight]
;;     [1 "Mustafa" "Basit" 170 63]
;;     [2 "Seerat" "Mir" 120 50]]

(jdbc/query db ["select * from app_user"] {:keywordize? false 
                                           :identifiers s/upper-case})
;; => ({"ID" 1, "FIRST_NAME" "Mustafa", "SURNAME" "Basit", "HEIGHT" 170, "WEIGHT" 63}
;;    {"ID" 2, "FIRST_NAME" "Seerat", "SURNAME" "Mir", "HEIGHT" 120, "WEIGHT" 50})

;; qualifiers
;; qualifier only works when keywordize? is true (the default) and allows us to specify a namespace 
;; for our keywords. Our keys are then returned in the form: :<qualifier>/<column name>
(jdbc/query db ["select * from app_user"] {:identifiers s/upper-case
                                           :qualifier "app_user"})
;; => (#:app_user{:ID 1, :FIRST_NAME "Mustafa", :SURNAME "Basit", :HEIGHT 170, :WEIGHT 63}
;;     #:app_user{:ID 2, :FIRST_NAME "Seerat", :SURNAME "Mir", :HEIGHT 120, :WEIGHT 50})

;; this indicates that we have a map where our keyword namespace is homogenous (every key has the same app_user namespace)
 
(-> 
 (jdbc/query db ["select * from app_user"] {:identifiers s/upper-case
                                           :qualifier "app_user"})
 first)
;; => #:app_user{:ID 1, :FIRST_NAME "Mustafa", :SURNAME "Basit", :HEIGHT 170, :WEIGHT 63}

(->
 (jdbc/query db ["select * from app_user"] {:identifiers s/upper-case
                                            :qualifier "app_user"})
 first
 keys)
;; => (:app_user/ID :app_user/FIRST_NAME :app_user/SURNAME :app_user/HEIGHT :app_user/WEIGHT)


;; Controlling Results with Custom Functions
;; It should be noted that the result-set-fn option you pass should not be lazy; otherwise,
;; the connection could be closed before the function completes. reduce (or a function
;; that calls reduce under the hood) is a good choice here


;; task - break duration into :hours :mins :sec
(defn add-user-friendly-duration
  [{:keys [duration] :as row}]
  (let [quot-rem (juxt quot rem)
        [hours remainder] (quot-rem duration (* 60 60))
        [minutes seconds] (quot-rem remainder 60)]
   (assoc row :friendly-duration
          (cond-> ""
            (pos? hours) (str hours "h ")
            (pos? minutes) (str minutes "m ")
            (pos? seconds) (str seconds "s")
            :always s/trim))))
 (jdbc/query db ["select * from activity"] {:row-fn add-user-friendly-duration})
;; => ({:id 1, :activity_type "run", :distance 8.67M, :duration 2520, :user_id 2, :friendly-duration "42m"}
;;     {:id 2, :activity_type "cycle", :distance 17.68M, :duration 3980, :user_id 2, :friendly-duration "1h 6m 20s"}
;;     {:id 3, :activity_type "run", :distance 8.67M, :duration 2520, :user_id 1, :friendly-duration "42m"})


;;task - sum distance traveled across all activities
(jdbc/query db ["select * from activity"]
            {:result-set-fn (fn [result-set]
                              (reduce (fn [total-distance {:keys [distance]}]
                                        (+ total-distance distance))
                                      0
                                      result-set))})
;; => 35.02M

;; row-fn + result-set-fn
(jdbc/query db ["select * from activity"]
            {:row-fn :distance
             :result-set-fn #(apply + %)})
;; => 35.02M

;; ****************************************************************************************************
;; cr"UD"
(jdbc/update! db :app_user {:weight 45} ["first_name = 'Seerat' and surname = 'Mir'"])
;; => (1)
 
(jdbc/update! db :activity {:duration 3980} ["id = 2"])

(jdbc/query db ["select * from app_user"])

(jdbc/delete! db :app_user ["first_name = 'Mustafa' and surname = 'Basit'"])
;; => [1]

;; check for ON DELETE CASCADE
(jdbc/query db ["select * from activity"])
;; => ()

(jdbc/delete! db :app_user [])
;; => [3]

(jdbc/db-do-commands db (jdbc/drop-table-ddl :activity))
(jdbc/db-do-commands db (jdbc/drop-table-ddl :app_user))
;; => (0)