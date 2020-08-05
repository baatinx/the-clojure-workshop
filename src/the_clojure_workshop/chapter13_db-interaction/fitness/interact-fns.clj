(ns the-clojure-workshop.chapter13-db-interaction.fitness.interact-fns
  (:require [the-clojure-workshop.chapter13-db-interaction.fitness.schema :as schema]
            [the-clojure-workshop.chapter13-db-interaction.fitness.ingestion :as ingest]
            [the-clojure-workshop.chapter13-db-interaction.fitness.query :as query]
            [clojure.java.jdbc :as jdbc]
            [semantic-csv.core :as sc]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(jdbc/execute! schema/db ["drop table activity"])
(jdbc/execute! schema/db ["drop table app_user"])

(schema/load)
;; => (0 0 )

(def users [{:first_name "Syed"
             :surname "Nasir"
             :height 150
             :weight 40}
            {:first_name "Afshaan"
             :surname "Hayaat"
             :height 135
             :weight 54}
            {:first_name "Mahroosha"
             :surname "Jalal"
             :height 176
             :weight 44}])

(doseq [user users]
   (ingest/user schema/db user))

(def accessors {:activity_type :type
                :distance :distance_metres
                :duration :duration_seconds
                :user_id :userid
                :activity_date (fn [{:keys [day month year]}] (str year "-" month "-" day))})

(defn apply-accessors
  [row accessors]
  (reduce-kv (fn [acc target-key accessor]
               (assoc acc target-key (accessor row)))
             {}
             accessors))

(def activities (->> 
                 (csv/read-csv (io/reader "resources/sample-activities.csv"))
                 sc/mappify
                 (map #(apply-accessors % accessors))))

(doseq [activity activities]
  (ingest/activity schema/db activity))



(query/all-activities schema/db)
(query/all-users schema/db)
(count (query/all-users schema/db))
(count (query/all-activities schema/db))
(query/user schema/db 1)
(query/activity schema/db 1)
(count (query/activities-by-user schema/db 1))
