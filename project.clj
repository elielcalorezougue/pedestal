(defproject api_pedestal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.pedestal/pedestal.service "0.5.10"]
                 [io.pedestal/pedestal.jetty "0.5.10"]
                 [org.slf4j/slf4j-api "2.0.0-alpha1"]
                 [org.slf4j/slf4j-log4j12 "2.0.0-alpha1"]
                 [log4j/log4j "1.2.17"]
                 [org.clojure/tools.analyzer "1.2.0"]]

  :repl-options {:init-ns api-pedestal.core
                 repl-options {:timeout 60000}}  ; Aumenta o timeout para 60 segundos}
  :repositories {"clojars" "https://repo.clojars.org/"})




