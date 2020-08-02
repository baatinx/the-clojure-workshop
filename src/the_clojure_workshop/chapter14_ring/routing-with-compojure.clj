(ns the_clojure_workshop.chapter14-ring.hello-world-app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]))

(defroutes routes
  (GET "/" request "Hello World")
  (GET "/route-1" request "Hello from route-1")
  (GET "/route-2" request "Hello from route-2")
  (route/not-found "Not the route you are looking for"))

(defn run
  []
  (run-jetty routes {:port 8080
                     :join? false}))

(def app (run))

(.stop app)