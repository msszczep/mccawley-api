(ns mccawley-api.models.parser
  "
  Adapted from
  https://github.com/gilesc/stanford-corenlp/blob/master/src/corenlp.clj
  "
  (:use (edu.stanford.nlp.parser.lexparser)
        (edu.stanford.nlp.ling.Word)
        (edu.stanford.nlp.process)
        (java.io.StringReader)
        (java.util.ArrayList)))


(defmulti word type)


(defmethod word String [s]
  (edu.stanford.nlp.ling.Word. s))


(defmethod word edu.stanford.nlp.ling.Word [w] w)


(def ^{:private true} load-parser
  (memoize
    (fn [] (edu.stanford.nlp.parser.lexparser.LexicalizedParser/loadModel))))


(defn tokenize [t]
  (.tokenize (edu.stanford.nlp.process.PTBTokenizer/newPTBTokenizer
              (java.io.StringReader. t))))


(defn split-sentences [text]
  (->> text
       java.io.StringReader.
       edu.stanford.nlp.process.DocumentPreprocessor.
       .iterator
       iterator-seq
       (map #(vec (map (comp word str) %)))))


(defmulti parse class)


(defmethod parse java.lang.String [s]
  (parse s))


(defmethod parse :default [coll]
  [coll]
  (->> coll
       (map word)
       java.util.ArrayList.
       (.apply (load-parser))))


(defn transform-clj-obj [s]
  (clojure.string/replace
    (apply str
      (for [item (-> s (clojure.string/split #"\(") rest)]
        (let [num-right-parens (count (re-seq #"\)" item))]
          (if (zero? num-right-parens)
            (str "{:pos \"" (clojure.string/trim item)
                 "\", :word \"\", :children [")
            (let [w (clojure.string/split item #" ")]
              (str "{:pos \"" (first w) "\", :word \""
                   (clojure.string/replace (last w) #"\)" "")
                   "\", :children ["
                   (apply str (repeat num-right-parens "]},"))))))))
    #",$" ""))


(defn parse-one-sentence [txt]
  (-> txt
      tokenize
      parse
      str
      transform-clj-obj))


(defn parse-multiple-sentences [t]
  (map (comp transform-clj-obj str parse) (split-sentences t)))
