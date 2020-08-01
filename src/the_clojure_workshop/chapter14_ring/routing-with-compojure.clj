(ns the_clojure_workshop.chapter14-ring.hello-world-app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]))

(def route 
  (GET "/" request "Hello World"))

(defn run
  []
  (run-jetty route {:port 8080
                    :join? false}))

(def app (run))
(.stop app)