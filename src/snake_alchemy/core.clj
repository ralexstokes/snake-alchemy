(ns snake-alchemy.core
  (:gen-class)
  (:require [snake-alchemy.transform :as transform]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]))

(defn- parse [source-str]
  (read-string source-str))

(def load (comp parse slurp io/resource))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> (first args)
      load
      transform/serpent->vyper
      pp/pprint))
