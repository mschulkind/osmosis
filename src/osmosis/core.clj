(ns osmosis.core
  (:require [overtone.core :as c])
  (:use overtone.inst.sampled-piano
        overtone.music.pitch))

(defn play-note [n] (sampled-piano (note n) :decay 5 :sustain 0))
(defn play-notes [notes] (doseq [n notes] (play-note n)))

(defn play-scale-note 
  [root nth]
  (play-note (+ (nth-interval nth) (note root))))

(defn play-chord-degree
  [root degree]
  (play-notes (chord-degree degree root :ionian)))

(defn seq-walk-in-key
  [root from-degree to-degree]
  (map (fn [n] [1 #(play-scale-note root n)])
       (if (> to-degree from-degree)
         (range from-degree (inc to-degree))
         (range from-degree (dec to-degree) -1))))

(defn seq-degree-in-key
  [root target-degree direction]
  (list
    ; Outline the key
    [2 #(play-chord-degree root :i)]
    [1 #(play-chord-degree root :iv)]
    [1 #(play-chord-degree root :v)]
    [2 #(play-chord-degree root :i)]

    ; Play the target note
    [4 #(play-scale-note root target-degree)]

    ; Walk down to the root
    (if (= direction :up)
      (seq-walk-in-key root target-degree 7)
      (seq-walk-in-key root target-degree 0))))

(defn play-seq
  ([seq] (play-seq seq (c/metronome 110) 0))
  ([seq metro current-beat]
   (let [s (first seq)
         duration (first s)
         soundf (last s)]
     (when s
       (if (seq? s)
         (play-seq s metro current-beat)
         (c/at (metro current-beat) (soundf)))
       (recur 
         (rest seq) 
         metro 
         (+ current-beat
            (if (seq? s)
              (apply + (map first s))
              duration)))))))
