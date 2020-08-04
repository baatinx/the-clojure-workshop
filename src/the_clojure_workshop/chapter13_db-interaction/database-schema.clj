(ns the-clojure-workshop.chapter13-db-interaction.database-schema
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as s]
            [hikari-cp.core :as hikari]))

;; (def create-app-user-ddl "CREATE TABLE app_user (
;;                           id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT USER_ID_PK PRIMARY KEY,
;;                           first_name VARCHAR(32),
;;                           surname VARCHAR(32),
;;                           height SMALLINT,
;;                           weight SMALLINT)")

(def create-app-user-ddl
  (jdbc/create-table-ddl :app_user
                         [[:id :int "GENERATED ALWAYS AS IDENTITY CONSTRAINT USER_ID_PK PRIMARY KEY"]
                          [:first_name "varchar(32)"]
                          [:surname "varchar(32)"]
                          [:height :smallint]
                          [:weight :smallint]]
                         {:entities s/lower-case}))

;; (def create-activity-ddl
;;   "CREATE TABLE activity (
;;    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
;;    activity_type VARCHAR(32),
;;    distance DECIMAL(5,2),
;;    duration INT,
;;    user_id INT REFERENCES app_user ON DELETE CASCADE)")

(def create-activity-ddl
  (jdbc/create-table-ddl :activity
                         [[:id :int "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "]
                          [:activity_type "varchar(32)"]
                          [:distance "decimal(5,2)"]
                          [:duration :int]
                          [:user_id :int "REFERENCES app_user ON DELETE CASCADE"]]
                         {:entities s/lower-case}))

(def db {:datasource (hikari/make-datasource {:jdbc-url "jdbc:derby:derby-local;create=true"})})
(defn create-schema
  []
  (jdbc/db-do-commands db [create-app-user-ddl create-activity-ddl]))

(create-schema)
;; => (0 0)


