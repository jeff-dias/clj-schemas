(ns schemas.adapters.person-test
  (:require [clojure.test :refer [is testing]]
            [schema-generators.complete :as complete]
            [schema-generators.generators :as generators]
            [schema.core :as s]
            [schema.test]
            [schemas.adapters.person :as adapters.person]
            [schemas.models.externals.person :as models.externals.person]
            [schemas.models.internals.person :as models.internals.person]))

(schema.test/deftest external-address->internal-test
  (testing "Given a external address"
    (testing "Should adapt it to an internal address"
      (testing "Using samples generator"
        (let [external-address {:street (generators/generate s/Str)
                                :city   (generators/generate s/Str)
                                :state  (generators/generate s/Str)
                                :zip    (generators/generate s/Str)}]
          (is (->> external-address
                   adapters.person/external-address->internal
                   (s/validate models.internals.person/Address)))))

      (testing "Using complete generator"
        (let [external-address (complete/complete {} models.externals.person/Address)]
          (is (->> external-address
                   adapters.person/external-address->internal
                   (s/validate models.internals.person/Address)))))

      (testing "Using both generators"
        (let [external-address (complete/complete {:street (generators/generate s/Str)}
                                                  models.externals.person/Address)]
          (is (->> external-address
                   adapters.person/external-address->internal
                   (s/validate models.internals.person/Address))))))))

(schema.test/deftest external->internal-test
  (testing "Given a external person"
    (testing "Should adapt it to an internal person"
      (testing "Using samples generator"
        (let [external-person {:name    (generators/generate s/Str)
                               :age     (generators/generate s/Int)
                               :email   (generators/generate s/Str)
                               :address {:street (generators/generate s/Str)
                                         :city   (generators/generate s/Str)
                                         :state  (generators/generate s/Str)
                                         :zip    (generators/generate s/Str)}}]
          (is (->> external-person
                   adapters.person/external->internal
                   (s/validate models.internals.person/Person)))))

      (testing "Using complete generator"
        (let [external-person (complete/complete {} models.externals.person/Person)]
          (is (->> external-person
                   adapters.person/external->internal
                   (s/validate models.internals.person/Person)))))

      (testing "Using both generators"
        (let [external-person (complete/complete {:name (generators/generate s/Str)}
                                                 models.externals.person/Person)]
          (is (->> external-person
                   adapters.person/external->internal
                   (s/validate models.internals.person/Person))))))))

(schema.test/deftest external-list->internal-test
  (testing "Given a external people list"
    (testing "Should adapt it to an internal people list"
      (testing "Using samples generator"
        (let [external-people {:people (generators/sample 3 models.externals.person/Person)}]
          (is (->> external-people
                   adapters.person/external-list->internal
                   (s/validate [models.internals.person/Person])))))

      (testing "Using complete generator"
        (let [external-people {:people [(complete/complete {} models.externals.person/Person)
                                        (complete/complete {} models.externals.person/Person)
                                        (complete/complete {} models.externals.person/Person)]}]
          (is (->> external-people
                   adapters.person/external-list->internal
                   (s/validate [models.internals.person/Person])))))

      (testing "Using both generators"
        (let [external-people {:people (conj (generators/sample 3 models.externals.person/Person)
                                             (complete/complete {} models.externals.person/Person))}]
          (is (->> external-people
                   adapters.person/external-list->internal
                   (s/validate [models.internals.person/Person]))))))))
