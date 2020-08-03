(ns the-clojure-workshop.chapter14-worwking-with-request-body
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET PUT DELETE]]
            [compojure.route :as route]
            [muuntaja.middleware :as middleware]
            [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.edn :as edn]))

(def db (atom {}))

(defroutes routes
  (GET "/data-structure" request
    (when-let [data-structure (@db :data)]
      {:body data-structure}))
  (PUT "/data-structure" request
    (swap! db assoc :data (:body-params request))
    {:status 201})
  (DELETE "/data-structure" request
    (swap! db dissoc :data))
  (route/not-found "Not Found"))

(defn run
  []
  (run-jetty (middleware/wrap-format routes) {:port 8080
                                              :join? false}))
(def app (run))
;; (.stop app)

(-> (http/put "http://localhost:8080/data-structure"
              {:content-type :application/json
               :body (json/write-str {:a 1
                                      :b #{2 3 4}})})
    :status)
;; => 201
 
(def app (run))
(.stop app)

(-> (http/get "http://localhost:8080/data-structure"
              {:accept :application/edn})
    :body
    edn/read-string)
;; => {:b [4 3 2], :a 1}

;; ***
;; Notice that we attempted to persist a set as part of our payload; however, it
;; has been returned as a vector. This is an important point to note: JSON to EDN
;; conversion leads to a loss of data. This is due to EDN having more built-in type
;; support than JSON (for example, sets and keywords).


(-> (http/put "http://localhost:8080/data-structure"
              {:content-type :application/edn
               :body (pr-str {:a 1
                              :b #{2 3 4}})})
    :status)
;;=> 201

(-> (http/get "http://localhost:8080//data-structure"
              {:accept :application/edn})
    :body)
;;=> "{:b [4 3 2], :a 1}"

(-> (http/delete "http://localhost:8080/data-structure")
    :status)
;;=> 200
;; The preceding 200 status indicates that the deletion was successful

(-> (http/get "http://localhost:8080"
              {:accept :application/edn}))
;; => Execution error (ExceptionInfo) at slingshot.support/stack-trace (support.clj:201).
;; => clj-http: status 404

;; Great – we have learned that the wrap-format middleware will assist us in formatting
;; JSON and EDN request bodies as well as response bodies as we noted earlier. We
;; know that the request body will be consumed by the wrap-format middleware and the
;; EDN-formatted result placed in the body-params of the incoming request.