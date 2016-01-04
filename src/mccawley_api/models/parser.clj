(ns mccawley-api.models.parser
  (:require [corenlp :as stanford-corenlp]))


(def negative-set #{"no" "not" "never" "n't" "nor"})


(def sentiment-map
  (->> (slurp "/Users/msszczep1/Desktop/mitchells/workspace/mccawley-api/src/mccawley_api/models/AFINN-en-165.txt")
       clojure.string/split-lines
       (map #(clojure.string/split % #"\t"))
       (into {})))


(defn transform-clj-obj [s]
  (clojure.string/replace
    (apply str
      (for [item (-> s (clojure.string/split #"\(") rest)]
        (let [num-right-parens (count (re-seq #"\)" item))]
          (if (zero? num-right-parens)
            (str "{:pos \"" (clojure.string/trim item)
                 "\", :word \"\", :children [")
            (let [word-pair (clojure.string/split item #" ")
                  word (clojure.string/replace (last word-pair) #"\)" "")]
              (str "{:pos \"" (first word-pair) "\", :word \"" word
                   "\", :sentiment \""
                   (get sentiment-map (clojure.string/lower-case word) 0)
                   "\", :entity \""
                   (first (stanford-corenlp/named-entities word))
                   "\", :semneg \""
                   (if (contains? negative-set
                                  (clojure.string/lower-case word)) 1 0)
                   "\", :children ["
                   (apply str (repeat num-right-parens "]},"))))))))
    #",$" ""))


(defn parse-one-sentence [txt]
  (-> txt
      stanford-corenlp/tokenize
      stanford-corenlp/parse
      str
      transform-clj-obj))


(defn parse-multiple-sentences [t]
  (map (comp transform-clj-obj str stanford-corenlp/parse)
       (stanford-corenlp/split-sentences t)))
