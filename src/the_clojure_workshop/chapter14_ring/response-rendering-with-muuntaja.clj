(ns the-clojure-workshop.chapter14-ring.response-rendering-with-muuntaja
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [muuntaja.middleware :as middleware]))

(defroutes routes
  (GET "/" request "Hello World")
  (GET "/string" request "a simple string response")
  (GET "/data-structure" request {:body {:a 1
                                         :b #{ 2 3 4}
                                         :c "nested data structure"}})
  (route/not-found "Not Found"))

(defn run
  []
  (run-jetty (middleware/wrap-format routes) {:port 8080
                                              :join? false}))
(def app (run))

(.stop app)