(ns snake-alchemy.serpent
  (:require [clojure.java.io :as io]))

(deftype Application [operator operands])

(defmethod print-method Application [node ^java.io.Writer w]
  (let [op (.operator node)
        args (.operands node)]
    (.write w (apply str "(" op " " (pr-str args) ")"))))

(deftype Literal [value])

(defmethod print-method Literal [node ^java.io.Writer w]
  (let [value (.value node)]
    (.write w (str value))))

(deftype Unless [predicate consequent])

(defmethod print-method Unless [node ^java.io.Writer w]
  (let [p (.predicate node)
        c (.consequent node)]
    (.write w (apply str "(unless " (pr-str p) (pr-str c) ")"))))

(declare parse)

(defn parse-symbol [code]
  (->Literal code))

(defn parse-number [code]
  (->Literal code))

(defn normalize-application [ast]
  (let [op (.operator ast)
        args (.operands ast)]
    (if (= op 'quote)
      (first args)
      ast)))

(defn normalize [ast]
  (cond
    (instance? snake_alchemy.serpent.Application ast) (normalize-application ast)
    :else ast))

(defn parse-application [code]
  (let [operator (first code)
        operands (map parse (rest code))
        normalized (map normalize operands)]
    (cond
      (= operator 'unless) (->Unless (first normalized) (rest normalized))
      :else (->Application operator normalized))))

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

