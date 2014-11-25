(ns osmosis.player
  (:require [overtone.music.pitch :as pitch]
            [plumbing.core :as pc :include-macros true]))

;(defn play-note [n] (sampled-piano (note n) :decay 5 :sustain 0))
(defn play-note [n] true)
(defn play-notes [notes] (doseq [n notes] (play-note n)))

(defn play-scale-note 
  [root nth]
  (play-note (+ (pitch/nth-interval nth) (pitch/note root))))

(defn play-chord-degree
  [root degree]
  (play-notes (pitch/chord-degree degree root :ionian)))

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
    (seq-walk-in-key 
      root target-degree
      (case direction
        :closest (if (> target-degree 3) 7 0)
        :up 7
        :down 0
        0))))

(defn rand-note
  [from to]
  (pitch/find-note-name (rand-nth 
                          (range (pitch/note from) 
                                 (pitch/note to)))))

(pc/defnk random-seq-degree-in-key
  [{root (rand-note :A2 :B4)}
   {degree (rand-int 8)}
   {direction :closest}]
  (seq-degree-in-key 
    root
    degree
    direction))

(defn flatten-max-1
  [seq]
  (filter #(and (coll? %)
                (not (coll? (first %))))
          (tree-seq coll?
                    identity 
                    seq)))

;(defn play-seq
  ;([seq] (play-seq (flatten-max-1 seq) (c/metronome 110) 0))
  ;([seq metro current-beat]
   ;(let [s (first seq)
         ;duration (first s)
         ;soundf (last s)
         ;next-beat (+ current-beat duration)]
     ;(when s
       ;(c/at (metro current-beat) (soundf))
       ;(c/apply-by (metro next-beat) 
                   ;#'play-seq 
                   ;[(rest seq) metro next-beat])))))

(defn loop-seqf
  [seqf]
  (concat [(seqf)
           [4 #()]] 
          (lazy-seq (loop-seqf seqf))))

(defonce options (atom {}))

(defn start []
  (println "start"))

(defn stop []
  (println "stop"))

(defn set-options [os]
  (swap! options merge os))
