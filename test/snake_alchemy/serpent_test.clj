(ns snake-alchemy.serpent-test
  (:require [snake-alchemy.serpent :as serpent]
            [clojure.test :refer :all]))

(deftest removes-quote
  (testing "(iszero 'foo) ~> (iszero foo)"
    (let [src "(iszero 'foo)"
          code (read-string src)
          ast (serpent/parse code)
          target (serpent/->Application
                  (serpent/->Literal 'iszero)
                  (list (serpent/->Literal 'foo)))]
      (is (= target ast)))))

(deftest can-match-literal
  (testing "can find a `foo` literal"
    (let [literal (serpent/->Literal 'foo)
          matches (serpent/matches-literal? 'foo literal)]
      (is matches))))
