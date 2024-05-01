# schemas
Project to contribute with Clojure beginners which aim to understand about the use of schemas.

#### TL;DR;
`Schema is a rich language for describing data shapes`

When we're onboarding on Clojure world and are coming from a strongly typed language. One of the things that is more painful is understand the data flow (where it comes from, where it goes). 
The schemas help in this moment because they represent something more readable.

## Schema types
Exist two schema types, the strict and the loose ones. 

Strict schemas are normally used when you're generating a reliable information. This schema type allows only keys contained in its definition.

Loose schemas are used when you're consuming a not reliable information. This schema type allows any key, even the ones not mapped in its definition. Although, it will map only the keys contained in its definition and discard the not mapped ones.

## Reading the code
In our code, we have examples of external (loose) and internal (strict) models.

### Loose person [\[ref\]](https://github.com/jeff-dias/clj-schemas/blob/bfef75b23a0b73681b94b9f5e70c7eaea335f8ef/src/schemas/models/externals/person.clj)
In this example we can see the allowed keys and, in the end, a key as `s/Keyword`. This key become the schema as loose.

Also, we can see here the instruction `s/optional` that allows a key be ommited when this schema is used.
```clojure
(s/defschema Person
  {:name                      s/Str
   :age                       s/Int
   :email                     s/Str
   (s/optional-key :nickname) s/Str
   :address                   Address
   s/Keyword                  s/Any})
```

### Strict person [\[ref\]](https://github.com/jeff-dias/clj-schemas/blob/bfef75b23a0b73681b94b9f5e70c7eaea335f8ef/src/schemas/models/internals/person.clj)
In this example we can just see the allowed keys. By default, the schemas are strict in Plumatic.

Here, we can see a new key if compared with the external model. This is totally normal if we consider the scenario of person creation, where we don't know the `ID` when we receive the request, but we can know it in our domain layer.
```clojure
(s/defschema Person
  {:id                        s/Uuid
   :name                      s/Str
   :age                       s/Int
   :email                     s/Str
   (s/optional-key :nickname) s/Str
   :address                   Address})
```

### Adapters [\[ref\]](https://github.com/jeff-dias/clj-schemas/blob/bfef75b23a0b73681b94b9f5e70c7eaea335f8ef/src/schemas/adapters/person.clj)
Adapters or mappers, are pretty common in architecture divided by layers, and are used when we need to adapt the external data into an internal one.

In our code we have adapters mainly because of we transform loose schemas into strict ones.

In the example below we can see even other Plumatic benefits, like `s/defn` which is a macro that allow us to put schema validation in our method params and response. Here we're receiving an external person model and adapting it into an internal one. Also, we're using the symbol `->` to create a `thread first` where each line response is used as the first parameter in the subsequence.
```clojure
(s/defn external->internal :- models.internals.person/Person
  "Converts an external person to an internal person"
  [person :- models.externals.person/Person]
  (-> person
      (select-keys [:name :age :email :nickname :address])
      (update :address external-address->internal)
      (assoc :id (random-uuid))))
```

### Unit tests [\[ref\]](https://github.com/jeff-dias/clj-schemas/blob/bfef75b23a0b73681b94b9f5e70c7eaea335f8ef/test/schemas/adapters/person_test.clj)
It's a good practice create unit tests for your adapters. They'll assure that your adapters are receiving and generating the expected data.

In our example we're using `schema-generators` to create mocks and use in our method assertions. Also, we're using `schema.test` that will validate if the methods parameters and the responses are valid according to the expected schemas.
```clojure
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
```

## Setup
- Configure Clojure. Learn more [here](https://clojure.org/guides/getting_started)
- Create your local Clojure app using the command `lein new app <my-app-name>`
- Import the Plumatic schema library. Learn more [here](https://github.com/plumatic/schema)
- For the unit tests, import the Plumatic schema generator library. Learn more [here](https://github.com/plumatic/schema-generators)

## Final comments
I hope these examples helped you to understand more about the use of schemas and how to modelate your domains in a way that will improve the onboarding of new developers who aren't used to Clojure and its paradigms.

## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
