(defproject snake-alchemy "0.1.0-SNAPSHOT"
  :description "turning snakes into more snakes"
  :url "https://github.com/ralexstokes/snake-alchemy"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot snake-alchemy.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
