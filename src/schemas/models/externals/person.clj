(ns schemas.models.externals.person
  (:require [schema.core :as s]))

; Loose schema to receive an address
(s/defschema Address
  {:street   s/Str
   :city     s/Str
   :state    s/Str
   :zip      s/Str
   s/Keyword s/Any})

; Loose schema to receive a person
(s/defschema Person
  {:name                      s/Str
   :age                       s/Int
   :email                     s/Str
   (s/optional-key :nickname) s/Str
   :address                   Address
   s/Keyword                  s/Any})

; Loose schema to receive a list of people
(s/defschema List
  {:people   [Person]
   s/Keyword s/Any})
