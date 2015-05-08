(ns mccawley_back.handler
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [mccawley_back.routes.home :refer [home-routes]]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :refer [wrap-cors]]))

(defn init []
  (println "mccawley_back is starting"))

(defn destroy []
  (println "mccawley_back is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (wrap-cors :access-control-allow-origin #"http://localhost:10555/index.html"
                 :access-control-allow-methods [:get :put :post]
                 :access-control-allow-headers ["Content-Type"])
      (handler/site)
      (middleware/wrap-json-body)
      middleware/wrap-json-response))
