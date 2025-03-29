(ns api-pedestal.rotas
  (:require [io.pedestal.http.route :as route]
            [com.stuartsierra.component :as component]))

;; ========= HANDLERS  =========

(defn listar-tarefas [request]
  {:status 200
   :body   @(get request :store)})

(defn criar-tarefa-mapa [uuid nome status]
  {:id uuid :nome nome :status status})

(defn criar-tarefa [request]
  (prn "request: " request)
  (prn "Body params:" (:body-params request))
  (let [uuid (java.util.UUID/randomUUID)
        nome (get-in request [:json-params  :nome])
        status (get-in request [:json-params  :status])
        tarefa (criar-tarefa-mapa uuid nome status)
        store (get request :store)]
    (swap! store assoc uuid tarefa)
    {:status 200
     :body {:mensagem "Tarefa registrada com sucesso!"
            :tarefa   tarefa}}))

(defn funcao-hello [request]
  {:status 200
   :body (str "Hello World " (get-in request [:json-params  :name]))})

(defn remover-tarefa [request]
  (let [store (get request :store)
        tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (java.util.UUID/fromString tarefa-id)]
    (swap! store dissoc tarefa-id-uuid)
    {:status 200 :body {:mensagem "Removida com sucesso!"}}))

(defn atualizar-tarefa [request]
  (let [tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (try
                         (java.util.UUID/fromString tarefa-id)
                         (catch IllegalArgumentException e
                           (throw (ex-info "ID inválido" {:id tarefa-id}))))
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa tarefa-id-uuid nome status)
        store (get request :store)]
    (swap! store assoc tarefa-id-uuid tarefa)
    {:status 200
     :body {:mensagem "Tarefa atualizada com sucesso!"
            :tarefa   tarefa}}))
                                               ;; Responde com a mensagem de sucesso e os dados da tarefa
;; ========= COMPONENT =========

(defrecord Rotas []
  component/Lifecycle

  (start [this]
    (prn "Start rotas")
    (let [routes (route/expand-routes
                   #{["/hello" :get funcao-hello :route-name :hello-world]
                     ["/tarefa" :post criar-tarefa :route-name :criar-tarefa]
                     ["/tarefa" :get listar-tarefas :route-name :listar-tarefas]
                     ["/tarefa/:id" :delete remover-tarefa :route-name :remover-tarefa]
                     ["/tarefa/:id" :patch atualizar-tarefa :route-name :atualizar-tarefa]})]
      (assoc this :endpoints routes)))

  (stop [this]
    (prn "Stop rotas")
    (assoc this :endpoints nil)))

(defn new-rotas []
  (->Rotas))
