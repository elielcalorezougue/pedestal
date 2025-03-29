(ns api-pedestal.core
  (:require [com.stuartsierra.component :as component]
            [api-pedestal.database :as database]
            [api-pedestal.rotas :as rotas]
            [api-pedestal.servidor :as servidor]))

(def my-component-system
  (component/system-map
    :database (database/new-database)
    :rotas (rotas/new-rotas)
    :servidor (component/using (servidor/new-servidor) [:database :rotas])))

(defn -main []
  (println "Iniciando o sistema...")
  (component/start my-component-system)
  (println "Sistema iniciado!"))



