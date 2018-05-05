(ns snake-alchemy.vyper
  (:require [snake-alchemy.serpent :as serpent]))

(deftype Application [operator operands])

(defmethod print-method Application [node ^java.io.Writer w]
  (let [op (.operator node)
        args (.operands node)]
    (.write w (apply str "(" op " " args ")"))))

(deftype Literal [value])

(defmethod print-method Literal [node ^java.io.Writer w]
  (let [value (.value node)]
    (.write w (str value))))

(declare from-serpent)

(defn parse-serpent-literal [ast]
  (->Literal (.value ast)))

(defn parse-serpent-application [ast]
  (let [operator (.operator ast)
        operands (.operands ast)]
    (->Application operator
                   (mapcat from-serpent-without-quotes operands))))

(defn from-serpent
  "takes a value produced by `snake-alchemy.serpent/parse`; implies that serpent AST types are valid vyper AST types unless they are converted here"
  [ast]
  (cond
    (instance? snake_alchemy.serpent.Unless ast) (parse-serpent-unless ast)
    (instance? snake_alchemy.serpent.Application ast) (parse-serpent-application ast)
    :else ast))

(comment
  (def code (read-string (slurp (clojure.java.io/resource "small.se.lll"))))
  code
  (def serpent (serpent/parse code))
  serpent
  (from-serpent serpent)

  )
