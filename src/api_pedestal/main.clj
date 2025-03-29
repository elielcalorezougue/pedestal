(ns api-pedestal.main
  (:require [api-pedestal.servidor :as servidor]
            [com.stuartsierra.component :as component]
            [api-pedestal.database :as database]
            [api-pedestal.rotas :as rotas]
            [clojure.pprint :refer [pprint]]))

(defn my-component-system []
  (component/system-map
    :database (database/new-database)
    :rotas (rotas/new-rotas)
    :servidor (component/using (servidor/new-servidor) [:database :rotas])))

(def component-result (component/start (my-component-system)))
(def test-request (-> component-result :servidor :test-request))


;(prn (test-request :get "/hello?name=Eliel"))
;(prn (test-request :post "/tarefa?nome=Correr&status=pendente"))
;(prn (test-request :post "/tarefa?nome=Correr&status=concluido"))
;(prn "Listando todas as tarefas"
;       (test-request :get "/lista-tarefas"))

(test-request :get "/hello?name=Eliel")
(test-request :post "/tarefa?nome=Ler&status=pendente")
(test-request :post "/tarefa?Estudar=Ler&status=pendente")
(test-request :post "/tarefa?nome=Correr&status=pendente")
(read-string (:body (test-request :get "/tarefa")))
(test-request :post "/tarefa?nome=Ler&status=realizado")
(test-request :post "/tarefa?Estudar=Ler&status=realizado")
(test-request :post "/tarefa?nome=Correr&status=realizado")
