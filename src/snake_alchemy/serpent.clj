(ns snake-alchemy.serpent
  (:require [clojure.java.io :as io]))

(defrecord Application [operator operands])

(defmethod print-method Application [node ^java.io.Writer w]
  (let [op (.operator node)
        args (.operands node)]
    (.write w (apply str "(" (pr-str op) " " (pr-str args) ")"))))

(defrecord Literal [value])

(defmethod print-method Literal [node ^java.io.Writer w]
  (let [value (.value node)]
    (.write w (pr-str value))))

(defrecord Unless [predicate consequent])

(defmethod print-method Unless [node ^java.io.Writer w]
  (let [p (.predicate node)
        c (.consequent node)]
    (.write w (apply str "(unless " (pr-str p) (pr-str c) ")"))))

(defrecord Set [key value])

(defmethod print-method Set [node ^java.io.Writer w]
  (let [k (.key node)
        v (.value node)]
    (.write w (apply str "(set " (pr-str k) (pr-str v) ")"))))

(declare parse)

(defn matches-literal? [symbol ast]
  (and (instance? snake_alchemy.serpent.Literal ast)
       (= symbol (.value ast))))

(defn parse-symbol [code]
  (->Literal code))

(defn parse-number [code]
  (->Literal code))

(defn normalize-application [ast]
  (let [op (.operator ast)
        args (.operands ast)]
    (if (matches-literal? 'quote op)
      (first args)
      ast)))

(defn normalize [ast]
  (cond
    (instance? snake_alchemy.serpent.Application ast) (normalize-application ast)
    :else ast))

(defn parse-application [code]
  (let [[operator & operands] (map (comp normalize parse) code)]
    (cond
      (matches-literal? 'unless operator) (->Unless (first operands) (second operands))
      :else (->Application operator operands))))

(defn parse
  "assumes the Clojure reader has `read` a string containing valid Serpent LLL"
  [code]
  (cond
    (symbol? code) (parse-symbol code)
    (number? code) (parse-number code)
    (seq? code) (parse-application code)))

(comment
  (let [filename "small.se.lll"
        resource (io/resource filename)
        code (read-string (slurp resource))
        ast (parse code)]
    ast)
  )

