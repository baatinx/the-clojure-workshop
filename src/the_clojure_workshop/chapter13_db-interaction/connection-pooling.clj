(ns the-clojure-workshop.chapter13-db-interaction.connection-pooling
  (:require [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hikari]))

;; option 1
(def db {:datasource (hikari/make-datasource {:jdbc-url "jdbc:derby:derby-local;create=true"})})
(jdbc/get-connection db)
;; => #object[com.zaxxer.hikari.pool.HikariProxyConnection 0x4022b055 "HikariProxyConnection@1076015189 
;;    wrapping org.apache.derby.impl.jdbc.EmbedConnection@1816106010 (XID = 13), (SESSIONID = 1), (DATABASE = derby-local), (DRDAID = null) "]

;; option 2

;; Alternatively, if we know the database already exists, then the create=true flag will not be required. 

(def db {:datasource (hikari/make-datasource {:database-name "derby-local"                                            
                                              :datasource-class-name "org.apache.derby.jdbc.EmbeddedDataSource"})})
;; => Syntax error (ClassNotFoundException) compiling at (form-init1926450246182458802.clj:1:22) org.apache.derby.jdbc.EmbeddedDataSource
;; => class clojure.lang.Compiler$CompilerException 
