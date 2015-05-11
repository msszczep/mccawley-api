(defproject mccawley-api "0.1.0-SNAPSHOT"
  :description "Backend for McCawley"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [ring/ring-json "0.3.1"]
                 [ring-server "0.4.0"]
                 [ring-cors "0.1.7"]
                 [javax.servlet/servlet-api "2.5"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.1"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.1" :classifier "models"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler mccawley-api.handler/app
         :init mccawley-api.handler/init
         :destroy mccawley-api.handler/destroy}
  :min-lein-version "2.5.1"
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
