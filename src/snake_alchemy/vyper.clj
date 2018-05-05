(ns snake-alchemy.vyper
  (:require [snake-alchemy.serpent :as serpent]))

(defn mk-not-if [p c]
  (serpent/->Application
   (serpent/->Literal 'if)
   (list (serpent/->Application
          (serpent/->Literal 'not)
          (list p))
         c)))

(defn parse-serpent-unless [ast]
  (let [p (.predicate ast)
        c (.consequent ast)]
    (mk-not-if p c)))

(declare from-serpent)

(defn parse-serpent-application [ast]
  (let [operator (.operator ast)
        operands (.operands ast)]
    (serpent/->Application operator (map from-serpent operands))))

(defn from-serpent
  "takes a value produced by `snake-alchemy.serpent/parse`; implies that serpent AST types are valid vyper AST types unless they are converted here"
  [ast]
  (cond
    (instance? snake_alchemy.serpent.Literal ast) ast
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
