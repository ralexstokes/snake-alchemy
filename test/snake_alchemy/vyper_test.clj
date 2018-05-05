(ns snake-alchemy.vyper-test
  (:require [snake-alchemy.vyper :as vyper]
            [snake-alchemy.serpent :as serpent]
            [clojure.test :refer :all]))

(deftest unless-converts-to-if
  (testing "(unless p c) ~> (if (not p) c)"
    (let [src "(unless (iszero 5)
                (seq))"
          code (read-string src)
          ast (serpent/parse code)
          result (vyper/from-serpent ast)
          ;; i.e.
          ;; (if (not (iszero 5))
          ;;  (seq))
          target (serpent/->Application
                  (serpent/->Literal 'if)
                  (list (serpent/->Application (serpent/->Literal 'not)
                                               (list (serpent/->Application
                                                      (serpent/->Literal 'iszero)
                                                      (list (serpent/->Literal 5)))))
                        (serpent/->Application (serpent/->Literal 'seq) nil)))]
      (is (= target result)))))
