(ns the-clojure-workshop.chapter13-db-interaction.fitness.query
  (:require [clojure.java.jdbc :as jdbc]
            [java-time :as t]
            [medley.core :as medley]))

(defn all-users
  [db]
  (jdbc/query db ["select * from app_user"]))

(defn user
  [db user-id]
  (jdbc/query db [(str "select * from app_user where id = " user-id)]))

(defn all-activities
  [db]
  (jdbc/query db ["select * from activity"]))

(defn activity
  [db activity-id]
  (jdbc/query db [(str "select * from activity where id = " activity-id)]))

(defn activities-by-user
  [db user-id]
  (jdbc/query db [(str "select * from activity where user_id = " user-id)]))

;; pending - most-active-user, monthly-activity-by-user
