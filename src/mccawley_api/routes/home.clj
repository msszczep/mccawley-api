(ns mccawley-api.routes.home
  (:require [compojure.core :refer :all]
            [mccawley-api.models.parser :as p]
            [mccawley-api.models.stats :as s]))


(defn return-one-sentence [t]
  (let [parsed-sentence (p/parse-sentence t)]
    {:body {:parsed-text parsed-sentence
            :num-words (s/get-num-of-words t)
            :num-nodes (s/get-num-of-nodes parsed-sentence)
            :num-tokens (s/get-num-of-tokens parsed-sentence)
            :num-props (s/get-num-of-props parsed-sentence)
            :max-depth (s/get-max-depth parsed-sentence)
            :s-expression (p/get-s-expression t)
            :top-five (s/get-top-five parsed-sentence)}}))


(defn return-multiple-sentences [t]
  (let [sentences (p/split-multiple-sentences t)]
    (for [sentence sentences]
      (let [parsed-sentence (p/parse-sentence sentence)]
        {:body {:parsed-text parsed-sentence
                :num-tokens (s/get-num-of-tokens parsed-sentence)
                :num-nodes (s/get-num-of-nodes parsed-sentence)
                :num-props (s/get-num-of-props parsed-sentence)
                :max-depth (s/get-max-depth parsed-sentence)
                :s-expression (p/get-s-expression sentence)
                :top-five (s/get-top-five parsed-sentence)}}))))


(defroutes home-routes
  (GET "/parse/:t"  {{t :t} :params} (return-one-sentence t))
  (POST "/parse/:t" {{t :t} :params} (return-one-sentence t))
  (GET "/parse-multi/:t"  {{t :t} :params} (return-multiple-sentences t))
  (POST "/parse-multi/:t" {{t :t} :params} (return-multiple-sentences t)))
