(ns the-clojure-workshop.chapter14-ring.serving-static-files
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]))

(defroutes routes
  (route/files "/files/" {:root "./resources/"})
  ;;                       ****  ./---------/--------- relative to root folder 
  (route/not-found "Not Found"))

(defn run
  []
  (run-jetty routes {:port 8080
                     :join? false}))

(def app (run))
(.stop app)


