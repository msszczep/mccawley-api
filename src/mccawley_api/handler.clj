(ns mccawley-api.handler
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [mccawley-api.routes.home :refer [home-routes]]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :refer [wrap-cors]]))

(defn init []
  (println "mccawley-api is starting"))

(defn destroy []
  (println "mccawley-api is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-cors :access-control-allow-origin #".+"
                 :access-control-allow-methods [:get])
      (middleware/wrap-json-body)
      middleware/wrap-json-response))
