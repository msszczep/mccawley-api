(ns mccawley_back.models.parser
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


(defn parse-text [txt]
  (str (parse (tokenize txt))))
