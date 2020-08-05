(ns the-clojure-workshop.chapter14-ring.fitness.application-layer-integration
  (:require [clj-http.client :as http]
            [the-clojure-workshop.chapter14-ring.fitness.api :as api]))

(def app (api/run))
;; (.stop app)

(-> (http/post "http://localhost:8080/users" 
               {:body (pr-str {:first_name "Sabira"
                               :surname "Bilal"
                               :height 91
                               :weight 35})})
    :headers
    (get "Link"))
;; => "/users/201"

(-> (http/post "http://localhost:8080/activities"
                 {:body (pr-str {:user_id 201
                                 :activity_type "run"
                                 :activity_date "2020-08-06"
                                 :distance 4970
                                 :duration 1200})})
      :headers
      (get "Link"))
;; => "/activities202"
