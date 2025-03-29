(ns api-pedestal.api-test
  (:require [clojure.test :refer :all]
            [api-pedestal.database :as database]
            [api-pedestal.rotas :as rotas]
            [com.stuartsierra.component :as component]
            [api-pedestal.servidor :as servidor]))


(def my-component-system
  (component/system-map
    :database (database/new-database)
    :rotas (rotas/new-rotas)
    :servidor (component/using (servidor/new-servidor) [:database :rotas])))

(def component-result (component/start my-component-system))
(def test-request (-> component-result :servidor :test-request))

(clojure.test/deftest criar-tarefa-test
  (clojure.test/testing "Criar Tarefa"
    (let [path "/tarefa?nome=Teste&status=Em%20Andamento"
          response (test-request :post path)
          body (:body response)]
      (clojure.test/is (= 200 (:status response)))
      (clojure.test/is (contains? body :mensagem))
      (clojure.test/is (contains? body :tarefa)))))

(clojure.test/deftest listar-tarefas-test
  (clojure.test/testing "Listar Tarefas"
    (let [path "/tarefa"
          response (test-request :get path)
          body (:body response)]
      (clojure.test/is (= 200 (:status response)))
      (clojure.test/is (coll? body)))))

(clojure.test/deftest atualizar-tarefa-test
  (clojure.test/testing "Atualizar Tarefa"
    (let [create-path "/tarefa?nome=Teste&status=Em%20Andamento"
          create-response (test-request :post create-path)
          tarefa-id (-> create-response :body :tarefa :id)
          update-path (str "/tarefa/" tarefa-id "?nome=Atualizado&status=Finalizado")
          update-response (test-request :patch update-path)
          body (:body update-response)]
      (clojure.test/is (= 200 (:status update-response)))
      (clojure.test/is (= "Tarefa atualizada com sucesso!" (:mensagem body))))))

(clojure.test/deftest remover-tarefa-test
  (clojure.test/testing "Remover Tarefa"
    (let [create-path "/tarefa?nome=Teste&status=Em%20Andamento"
          create-response (test-request :post create-path)
          tarefa-id (-> create-response :body :tarefa :id)
          delete-path (str "/tarefa/" tarefa-id)
          delete-response (test-request :delete delete-path)]
      (clojure.test/is (= 200 (:status delete-response)))
      (clojure.test/is (= "Removida com sucesso!" (:mensagem (:body delete-response)))))))

;; Função para rodar os testes
(defn run-tests []
  (clojure.test/crun-tests 'api-pedestal.api-test))