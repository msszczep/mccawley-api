(ns mccawley-api.models.stats)


(defn get-tree-seq [node-type tree]
  "Get a sequence of all the nodes of a given type"
  (->> (tree-seq #(or (map? %) (vector? %))
                 identity
                 (read-string tree))
       (filter #(and (map? %) (node-type %)))
       (map node-type)))


(defn get-num-of-tokens [tree]
  (->> (get-tree-seq :word tree)
       (remove empty?)
       count))


(defn get-num-of-nodes [tree]
  (->> (get-tree-seq :pos tree)
       rest
       count))


(defn get-num-of-props [tree]
  (->> (get-tree-seq :pos tree)
       (filter #{"JJ" "JJR" "JJS" "VB" "VBD" "VBG" "VBN"
                 "VBP" "VBZ" "TO" "RB" "RBR" "RBS" "PP"})
       count))


(defn get-top-five [tree]
  (->> (rest (get-tree-seq :pos tree))
       frequencies
       (sort-by val)
       reverse
       (take 5)))

;; Idea to calculate depth (see below): Take the sequence of
;; the angled brackets in a parsed tree; the sequence reflects
;; the tree's gross structure.  Replace each left-bracket with 1,
;; replace each right-bracket with -1, make an additive sequence
;; of the resulting values, find the maximum result of that sequence
;; (the tree's depth) and subtract two to remove the ROOT and
;; topmost-S nodes which we don't count in the displayed tree.
;;
;; For example, a tree with the gross structure: [[[][[[]]]]]
;; gets transformed into the sequence  (1 1 1 -1 1 1 1 -1 -1 -1 -1 -1)
;; which becomes the additive sequence (1 2 3  2 3 4 5  4  3  2  1  0)
;; whose maximum value is 5, and whose depth for our purposes is
;; 5 - 2 = 3.

(defn get-max-depth [tree]
  (->> (loop [input-seq (map #(if (= % "[") 1 -1) (re-seq #"[\[\]]" tree))
              output-seq [0]]
         (if (empty? input-seq)
           output-seq
           (recur (rest input-seq)
                  (conj output-seq
                        (+ (last output-seq) (first input-seq))))))
       (apply max)
       dec
       dec))


(defn get-num-of-words [txt]
  (count (clojure.string/split txt #"\s+")))
