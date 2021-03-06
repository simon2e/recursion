(ns recursion)

(defn product [coll]
  (if (empty? coll)
    1
    (* (first coll) (product(rest coll)))))

(defn singleton? [coll]
  (and (not (empty? coll))
       (empty? (next coll))))

(defn my-last [coll]
  (if (empty? (next coll))
    (first coll)
    (recur (next coll))))

(defn max-element [a-seq]
  (loop [coll (next a-seq), acc (first a-seq)]
    (if (nil? (first coll))
      acc
      (recur (next coll) (max acc (first coll))))))

(defn seq-max [seq-1 seq-2]
    (if (> (count seq-1) (count seq-2))
         seq-1
         seq-2))

(defn seq-max-r [s1 s2]
  (loop [r1 s1, r2 s2]
    (cond
     (empty? r2) s1
     (empty? r1) s2
     :else (recur (next r1) (next r2)))))

(defn longest-sequence [a-seq]
  (loop [acc-seq nil, colls a-seq]
    (if (empty? colls)
      acc-seq
      (recur (seq-max acc-seq (first colls)) (next colls)))))

(defn my-filter [pred? coll]
   (cond (empty? coll)
         '()
         (pred? (first coll))
         (cons (first coll) (lazy-seq (my-filter pred? (next coll))))
         :else (lazy-seq (my-filter pred? (next coll)))))

(defn sequence-contains? [elem a-seq]
  (loop [coll a-seq]
    (cond (empty? coll) false
          (= elem (first coll)) true
          :else (recur (next coll)))))

(defn my-take-while [pred? coll]
  (cond (empty? coll) '()
        (not (pred? (first coll))) '()
        :else (cons (first coll) (lazy-seq (my-take-while pred? (next coll))))))

(defn my-drop-while [pred? coll]
  (cond (empty? coll) '()
        (not (pred? (first coll))) coll
        :else (recur pred? (next coll))))

(defn seq= [seq-a seq-b]
  (let [empty-a? (empty? seq-a)
        empty-b? (empty? seq-b)
        ]
    (cond
        (and empty-a? empty-b?) true
        (or empty-a? empty-b?)  false
        (not (= (first seq-a) (first seq-b))) false
        :else (recur (next seq-a) (next seq-b))
        )))

(defn my-map  [f seq-1 seq-2]
  (let [f1 (first seq-1)
        f2 (first seq-2)]
    (if
      (or (empty? seq-1) (empty? seq-2))
      '()
      (cons (f f1 f2) (lazy-seq (my-map f (next seq-1) (next seq-2)))))))


(defn power  "calculate power of n to k"(
  [n k]
  (power n k 1))
  ([n k acc]
   (if (< k 1)
     acc
     (recur n (dec k) (* acc n)))))

(defn fib " calculate fibonnaci number n"
  ([n] (fib n 0 1))
  ([n f-nth a]
   (if (< n 1)
     f-nth
     (recur (dec n) (+ f-nth a) f-nth))))

(defn my-repeat [how-many-times what-to-repeat]
  (if (< how-many-times 1)
    '()
    (cons what-to-repeat (lazy-seq (my-repeat (dec how-many-times) what-to-repeat)))))

(defn my-range [up-to]
  (let [val (dec up-to)]
    (if (< up-to 1)
      '()
      (cons val (lazy-seq (my-range val))))))

(defn tails [coll] "create sequence of all subsets from tail back"
  (if (empty? coll)
    '(())
    (cons (seq coll) (tails (next coll)))))

(defn inits
  "create sequence of all subsets from start forward"
  ([coll]
   (map #(take % coll) (range (inc (count coll))))))

(defn rotations
  ([coll]
   (if (empty? coll)
     '(())
     (rotations coll (count coll))))
  ([coll n]
   (if (< n 1)
     '()
     (cons coll (rotations (concat (next coll) (list (first coll))) (dec n))))))

(defn my-frequencies-helper [coll item]
  (assoc coll item (inc (get coll item 0))))

(defn my-frequencies
  "count frequencies of different elements"
  ([a-seq] (my-frequencies a-seq {}))
  ([a-seq freqs]
   (if (empty? a-seq)
     freqs
     (recur (next a-seq) (my-frequencies-helper freqs (first a-seq))))))

(defn un-frequencies [a-map]
  (mapcat (fn [[item count]] (repeat count item)) a-map))

(defn my-take [n coll]
  (if (or (empty? coll) (< n 1))
    '()
    (lazy-seq (cons (first coll) (my-take (dec n)(next coll))))))

(defn my-drop [n coll]
  (cond
   (empty? coll) '()
   (< n 1) coll
   :else (recur (dec n) (next coll))))

(defn halve [a-seq]
  (let [some (int (/ (count a-seq) 2))]
    (vector (take some a-seq) (drop some a-seq))))

(defn seq-merge [a-seq b-seq]
  (let [a (first a-seq)
        b (first b-seq)]
    (cond
     (empty? a-seq)
     b-seq
     (empty? b-seq)
     a-seq
     :else
     (if (< a b)
       (lazy-seq (cons a (seq-merge (next a-seq) b-seq)))
       (lazy-seq (cons b (seq-merge a-seq (next b-seq))))))))

(defn merge-sort [a-seq]
  (let [how-many (count a-seq)
        some (int (/ how-many 2))]
    (if (< how-many 2)
      a-seq
      (let
        [left (take some a-seq)
         right (drop some a-seq)]
        (seq-merge (merge-sort left) (merge-sort right))))))

(defn split-into-monotonics
  ([a-seq]
   (if (empty? a-seq)
     '()
     (split-into-monotonics a-seq (first a-seq) '() :none)))
  ([a-seq last acc direction]
     (if (empty? a-seq)
       (list (reverse acc))
       (let [current (first a-seq)
             this-delta (cond
                         (== last current) :none
                         (< current last) :down
                         :else :up)
             next-direction (if (= direction :none) this-delta direction)]
         (cond
          (or (= direction :none)         ; collect same values, or first value
              (= this-delta :none))
          (split-into-monotonics (next a-seq)
                                 current
                                 (cons current acc)
                                 next-direction)
          (= this-delta direction)                ; accumulate if in same direction
          (split-into-monotonics (next a-seq)
                                 current
                                 (cons current acc)
                                 next-direction)
          :else                                ; changing direction
          (cons (reverse acc) (split-into-monotonics (next a-seq)
                                            current
                                            (list current)
                                            :none)))))))

(defn drop-nth [seq idx]
  (concat (take idx seq) (drop (inc idx) seq)))

(defn conseach [item colls]
  (map #(cons item %) colls))

(defn permutations [coll]
  (cond (empty? coll)
        '(())
        (empty? (next coll))
        (list coll)
        :else
        (let [scoll (seq coll)]
          (mapcat
           #(conseach (nth scoll %) (permutations (drop-nth scoll %)))
           (range (count scoll))))))

(defn sub1s [a-set]
  (set (map #(disj a-set %) a-set)))

(defn powerset [a-set]
  (let [sset (if (set? a-set) a-set (set a-set))]
    (if (empty? sset)
      #{#{}}
      (apply conj #{} sset
             (mapcat powerset (sub1s sset))))))
