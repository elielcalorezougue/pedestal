(ns api-pedestal.servidor
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :as test]
            [io.pedestal.interceptor :as i]
            [com.stuartsierra.component :as component]))

(defrecord Servidor [database rotas server test-request]
  component/Lifecycle

  (start [this]
    (println "Starting servidor...")

    (let [assoc-store (fn [context]
                        (update context :request assoc :store (:store database)))

          db-interceptor {:name  :db-interceptor
                          :enter assoc-store}

          service-map (-> {::http/routes (:endpoints rotas)
                           ::http/port   9999
                           ::http/type   :jetty
                           :http/join?   false}
                          http/default-interceptors
                          (update ::http/interceptors conj
                                  http/json-body)
                          (update ::http/interceptors conj (i/interceptor db-interceptor)))

          server-instance (http/create-server service-map)]

      (http/start server-instance)

      (assoc this
        :server server-instance
        :test-request (fn [verb url]
                        (test/response-for (::http/service-fn server-instance) verb url)))))

  (stop [this]
    (println "Stopping servidor...")
    (when server
      (http/stop server))
    (assoc this :server nil :test-request nil)))

(defn new-servidor []
  (map->Servidor {}))












