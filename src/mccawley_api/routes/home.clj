(ns mccawley-api.routes.home
  (:require [compojure.core :refer :all]
            [mccawley-api.models.parser :as parser]))


(defn return-one-sentence [t]
  {:body {:parsed-text (parser/parse-one-sentence t)}})


(defn return-multiple-sentences [t]
  {:body {:parsed-text (parser/parse-multiple-sentences t)}})


(defroutes home-routes
  (GET "/parse/:t"  {{t :t} :params} (return-one-sentence t))
  (POST "/parse/:t" {{t :t} :params} (return-one-sentence t))
  (GET "/parse-multi/:t"  {{t :t} :params} (return-multiple-sentences t))
  (POST "/parse-multi/:t" {{t :t} :params} (return-multiple-sentences t)))
