(ns the_clojure_workshop.chapter14_ring.ring-web-app-intro
  (:require [ring.adapter.jetty :refer [run-jetty]]))

(defn handler
  [request]
  {:status 200
   :body "hello world"})

(def app (run-jetty handler {:port 8080
                             :join? false}))

(.stop app)