(ns schemas.adapters.person
  (:require [schema.core :as s]
            [schemas.models.externals.person :as models.externals.person]
            [schemas.models.internals.person :as models.internals.person]))

(s/defn external-address->internal :- models.internals.person/Address
  "Converts an external address to an internal address"
  [address :- models.externals.person/Address]
  (-> address
      (select-keys [:street :city :state :zip])
      (assoc :id (random-uuid))))

(s/defn external->internal :- models.internals.person/Person
  "Converts an external person to an internal person"
  [person :- models.externals.person/Person]
  (-> person
      (select-keys [:name :age :email :nickname :address])
      (update :address external-address->internal)
      (assoc :id (random-uuid))))

(s/defn external-list->internal :- [models.internals.person/Person]
  "Converts an external list to an internal list"
  [{:keys [people]} :- models.externals.person/List]
  (map external->internal people))
