(ns mccawley_back.routes.home
  (:require [compojure.core :refer :all]
            [mccawley_back.models.parser :as parser]))


(defn return-output [t]
  {:body {:parsed-text (parser/parse-text t)}})


(defroutes home-routes
  (GET "/parse/:t"  {{t :t} :params} (return-output t))
  (POST "/parse/:t" {{t :t} :params} (return-output t)))
