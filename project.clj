(defproject api_pedestal "0.1.0-SNAPSHOT"
  :description "API de exemplo usando Pedestal"
  :url "https://github.com/usuario/api-pedestal"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.pedestal/pedestal.service "0.5.10"]
                 [io.pedestal/pedestal.jetty "0.5.10"]
                 [org.slf4j/slf4j-api "2.0.0-alpha1"]
                 [org.slf4j/slf4j-log4j12 "2.0.0-alpha1"]
                 [log4j/log4j "1.2.17"]
                 [org.clojure/tools.analyzer "1.2.0"]
                 [com.stuartsierra/component "1.1.0"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.clojure/test.check "1.1.1"]]
  :main api-pedestal.core
  :repl-options {:init-ns api-pedestal.core
                 :timeout 60000}

  :repositories {"clojars" "https://repo.clojars.org/"})
