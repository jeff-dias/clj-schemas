(ns schemas.models.internals.person
  (:require [schema.core :as s]))

; Strict schema to handle an address
(s/defschema Address
  {:id     s/Uuid
   :street s/Str
   :city   s/Str
   :state  s/Str
   :zip    s/Str})

; Strict schema to handle a person
(s/defschema Person
  {:id                        s/Uuid
   :name                      s/Str
   :age                       s/Int
   :email                     s/Str
   (s/optional-key :nickname) s/Str
   :address                   Address})
