(ns snake-alchemy.vyper
  (:require [snake-alchemy.serpent :as serpent]
            [clojure.string :as string]
            [clojure.string :as str]))

(declare from-serpent)

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
    (mk-not-if (from-serpent p) (from-serpent c))))

(defn parse-serpent-get [ast]
  (from-serpent
   (.value ast)))
(symbol ( str '__aloc_start_ (gensym)))

(defn next-symbol [s]
  (->> (gensym)
       (str s)
       symbol))

(defn parse-serpent-alloc
  ([ast] (parse-serpent-alloc ast (next-symbol '__alloc_start)))
  ([ast start-symbol]
   (let [expr (from-serpent
               (.expr ast))]
     (serpent/->Application
      (serpent/->Literal 'with)
      (list (serpent/->Literal start-symbol)
            (serpent/->Application (serpent/->Literal 'msize) '())
            (serpent/->Application
             (serpent/->Literal 'seq)
             (list
              (serpent/->Application
               (serpent/->Literal 'mstore)
               (list (serpent/->Application
                      (serpent/->Literal 'add)
                      (list expr (serpent/->Literal start-symbol)))
                     (serpent/->Literal 0)))
              (serpent/->Literal start-symbol))))))))

(defn parse-serpent-application [ast]
  (let [operator (.operator ast)
        operands (.operands ast)]
    (serpent/->Application (from-serpent operator) (map from-serpent operands))))

(defn from-serpent
  "takes a value produced by `snake-alchemy.serpent/parse`; implies that serpent AST types are valid vyper AST types unless they are converted here"
  [ast]
  (cond
    (instance? snake_alchemy.serpent.Literal ast) ast
    (instance? snake_alchemy.serpent.Unless ast) (parse-serpent-unless ast)
    (instance? snake_alchemy.serpent.Get ast) (parse-serpent-get ast)
    (instance? snake_alchemy.serpent.Alloc ast) (parse-serpent-alloc ast)
    (instance? snake_alchemy.serpent.Application ast) (parse-serpent-application ast)
    :else ast))

(defn pp-literal [ast]
  (let [value (.value ast)]
    (if (number? value)
      value
      (str "\"" value "\""))))

(defn pp-ast [ast]
  (cond
    (instance? snake_alchemy.serpent.Literal ast) (pp-literal ast)
    (instance? snake_alchemy.serpent.Application ast) (let [op (pp-ast (.operator ast))
                                                            args (map pp-ast (.operands ast))
                                                            coll (apply vector op args)]
                                                        (->> coll
                                                             (interpose ",\n")
                                                             (into [])))
    :else (throw (ex-info "invalid ast type for pretty-printing" {::node ast}))))

(defn pretty-print->python
  "renders the `ast` as embedded Python to be used directly in the Vyper compiler"
  [ast]
  (let [output (pp-ast ast)]
    (-> output
        print-str
        (str/replace #"(\d)N" "$1"))))

(comment
  (def code (read-string (slurp (clojure.java.io/resource "test.se.lll"))))
  (def code (read-string "(return 0 (lll (set 'addr (calldataload 4))) 0)"))
  code
  (def serpent (serpent/parse code))
  serpent
  (def vyper (from-serpent serpent))
  vyper

  (def output (pretty-print->python vyper))
  (->> output
       println)
  )
