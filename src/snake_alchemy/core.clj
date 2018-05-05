(ns snake-alchemy.core
  (:gen-class)
  (:require [snake-alchemy.vyper :as vyper]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]))

(def load (comp read-string slurp io/resource))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> (first args)
      load
      vyper/from-serpent
      pp/pprint))
