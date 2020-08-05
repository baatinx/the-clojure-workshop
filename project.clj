(defproject the-clojure-workshop "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 
                 ;; chapter 13 - depn.
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.apache.derby/derby "10.15.2.0"]
                 [hikari-cp "2.13.0"] 
                 [semantic-csv "0.2.1-alpha1"]
                 [org.clojure/data.csv "1.0.0"]
                 [clojure.java-time "0.3.2"]
                 [medley "1.3.0"]
                 
                 ;; chapter 14 -depn.
                 [ring/ring-core "1.8.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [compojure "1.6.1"]
                 [metosin/muuntaja "0.6.7"]
                 [metosin/jsonista "0.2.6"]
                 [clj-http "3.10.1"]
                 [org.clojure/data.json "1.0.0"]]
  :main ^:skip-aot the-clojure-workshop.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
