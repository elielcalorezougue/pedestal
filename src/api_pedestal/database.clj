(ns api-pedestal.database
  (:require [com.stuartsierra.component :as component]))

(defrecord Database [store]
  component/Lifecycle

  (start [this]
    (println "Start Database")
    (if store
      this
      (assoc this :store (atom {}))))

  (stop [this]
    (println "Stop Database")
    (assoc this :store nil)))

(defn new-database
  "Cria uma instância do componente Database. Pode receber um estado inicial opcional para testes."
  ([] (->Database nil))
  ([initial-data] (->Database (atom initial-data))))

