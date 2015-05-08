(ns mccawley_back.handler
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [mccawley_back.routes.home :refer [home-routes]]
            [ring.middleware.json :as middleware]))

(defn init []
  (println "mccawley_back is starting"))

(defn destroy []
  (println "mccawley_back is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (middleware/wrap-json-body)
      middleware/wrap-json-response))
