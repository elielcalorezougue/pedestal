(ns api-pedestal.database
  (:require [com.stuartsierra.component :as component]))

(defrecord Database []
  component/Lifecycle

  (start [this]
    (prn "Start database")
    (assoc this :store (atom {})))

  (stop [this]
    (prn "Stop database")
    (assoc this :store nil)))


(defn new-database []
  (->Database))
