(ns api-pedestal.servidor
  (:require
    [io.pedestal.http.route :as route]
    [io.pedestal.http :as http]
    [io.pedestal.test :as test]
    [api-pedestal.database :as database]))

(defn assoc-store [context]
  (update context :request assoc :store database/store))

(def db-interceptor
  {:name :db-interceptor
   :enter assoc-store})

(defn listar-tarefas [request]
                    {:status 200 :body @(:store request)})

(defn criar-tarefa-mapa [uuid nome status]
  {:id uuid :nome nome :status status})

(defn criar-tarefa [request]
  (let [uuid (java.util.UUID/randomUUID)
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa uuid nome status)
        store (:store request)]
    (swap! store assoc uuid tarefa)
    {:status 200 :body {:mensagem "Tarefa registrada com sucesso!"
                        :tarefa tarefa}}))

(defn funcao-hello [request]
  {:status 200 :body (str "Hello World " (get-in [:query-params :name] request))})


(defn remover-tarefa [request]
  (let [store (:store request)
        tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (java.util.UUID/fromString tarefa-id)]
    (swap! store dissoc tarefa-id-uuid)
    {:status 200 :body {:mensagem "Removida com sucesso!"}}))

(defn atualizar-tarefa [request]
  (let [tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (java.util.UUID/fromString tarefa-id)
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa tarefa-id-uuid nome status)
        store (:store request)]
    (swap! store assoc tarefa-id-uuid tarefa)
    {:status 200 :body {:mensagem "Tarefa atualizada com sucesso!"
                        :tarefa   tarefa}}))

(def routes (route/expand-routes
              #{["/hello" :get funcao-hello :route-name :hello-world]
                ["/tarefa" :post [db-interceptor criar-tarefa] :route-name :criar-tarefa]
                ["/tarefa" :get [db-interceptor listar-tarefas] :route-name :listar-tarefas]
                ["/tarefa/:id" :delete [db-interceptor remover-tarefa :route-name :remover-tarefa]]
                ["/tarefa/:id" :patch [db-interceptor atualizar-tarefa] :route-name :atualizar-tarefa]}))


(def service-map {::http/routes routes
                  ::http/port 9999
                  ::http/type :jetty
                  :http/join? false})

(defonce server (atom nil))

(defn start-server []
  (reset! server (http/start (http/create-server service-map))))

(defn test-request [verb url]
  (test/response-for (::http/service-fn @server) verb url))

(defn stop-server []
  (http/stop @server))

(defn restart-server []
  (stop-server)
  (start-server))

(try
   (start-server)
     (catch Exception e (prn "Erro ao executar start" (.getMessage e))))

(try
  (restart-server)
  (catch Exception e (prn "Erro ao executar restart" (.getMessage e))))


(prn "Server started/restarted")

;(prn (test-request :get "/hello?name=Eliel"))
;(prn (test-request :post "/tarefa?nome=Correr&status=pendente"))
;(prn (test-request :post "/tarefa?nome=Correr&status=concluido"))
;(prn "Listando todas as tarefas"
;       (test-request :get "/lista-tarefas"))

(test-request :get "/hello?name=Eliel")
(test-request :post "/tarefa?nome=Ler&status=pendente")
(test-request :post "/tarefa?Estudar=Ler&status=pendente")
(test-request :post "/tarefa?nome=Correr&status=pendente")
(clojure.edn/read-string (:body (test-request :get "/tarefa")))
(test-request :post "/tarefa?nome=Ler&status=realizado")
(test-request :post "/tarefa?Estudar=Ler&status=realizado")
(test-request :post "/tarefa?nome=Correr&status=realizado")




;(prn "Started server http")


