(ns the-clojure-workshop.chapter14-ring.fitness.api
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as  params]
            [compojure.core :refer [context defroutes DELETE GET PUT POST]]
            [compojure.route :as route]
            [muuntaja.middleware :as middleware]
            [clojure.edn :as edn]
            [jumblerg.middleware.cors :refer [wrap-cors]]
            [the-clojure-workshop.chapter13-db-interaction.fitness.schema :as schema]
            [the-clojure-workshop.chapter13-db-interaction.fitness.ingestion :as ingest]
            [the-clojure-workshop.chapter13-db-interaction.fitness.query :as query]))

;; ***
;; see also p.no 645 - ....... {:keys [id] :params}.....

(defroutes routes
  (context "/users" []
    (GET "/" []
      {:body (query/all-users schema/db)})
    (POST "/" req
      (let [ingest-result (ingest/user schema/db  (edn/read-string (slurp (:body req))))]
        {:status 201
         :headers {"Link" (str "/users/" (:1 ingest-result))}}))
    (GET "/:id" [id]
      (when-first [user (query/user schema/db id)]
        {:body user}))
    (GET "/:id/activities" [id]
      {:body (query/activities-by-user schema/db id)}))
  (context "/activities" []
    (GET "/" []
      {:body (query/all-activities schema/db)})
    (POST "/" req
      (let [ingest-result (ingest/activity schema/db (edn/read-string (slurp (:body req))))]
        {:status 201
         :headers {"Link" (str "/activities" (:1 ingest-result))}}))
    (GET "/:id" [id]
      (when-first [activity (query/activity schema/db id)]
        {:body activity})))
  (route/not-found "Not found"))

;; reporting context - pending

;; ***
;; It is important to remember that our body is represented as a stream, meaning
;; that it can only be read once. Any subsequent attempts to read from it will find it
;; is already exhausted. Particular care should be taken when debugging not to read
;; the body before it is actually utilized by the route. The same consideration should
;; be made when writing middleware that interacts with the body.

(defn run
  []
  (run-jetty (-> routes
                 middleware/wrap-format
                 params/wrap-params
                 (wrap-cors ".*")
                 (wrap-cors identity))
             {:port 8080
              :join? false}))

