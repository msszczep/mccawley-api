(ns mccawley-api.models.parser
  (:use (edu.stanford.nlp.parser.lexparser)
        (edu.stanford.nlp.ling.Word)
        (edu.stanford.nlp.process)
        (java.io.StringReader)
        (java.util.ArrayList)))
; Adapted from https://github.com/gilesc/stanford-corenlp/blob/master/src/corenlp.clj

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


(defmulti parse class)


(defmethod parse java.lang.String [s]
  (parse (tokenize s)))


(defmethod parse :default [coll]
  [coll]
  (.apply (load-parser) (java.util.ArrayList. (map word coll))))


(defn transform-clj-to-jsobj [s]
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


(defn parse-text [txt]
  (transform-clj-to-jsobj (str (parse (tokenize txt)))))
