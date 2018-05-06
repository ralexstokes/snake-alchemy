(ns snake-alchemy.core
  (:gen-class)
  (:require [snake-alchemy.vyper :as vyper]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [snake-alchemy.serpent :as serpent]))

(def load-from-name (comp read-string slurp io/resource))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> (first args)
      load-from-name
      serpent/parse
      vyper/from-serpent
      vyper/pretty-print->python
      println))
