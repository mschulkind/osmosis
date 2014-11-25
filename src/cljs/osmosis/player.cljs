(ns osmosis.player
  (:require-macros [cljs.core.async.macros :refer [go-loop alt!]])
  (:require [osmosis.debug :refer [log]] 
            [overtone.music.pitch :as pitch]
            [plumbing.core :as pc :include-macros true]
            [cljs.core.async :as async]
            [clojure.string :as string]))

; Needed for mobile. Needs to be called at least once from a touch event before
; playing sounds.
(defonce samples-initted (atom false))
(defn init-samples []
  (when-not @samples-initted
    (doseq [a (array-seq (. js/document (getElementsByTagName "audio")))]
      (.play a)
      (.pause a)))
  (reset! samples-initted true))

(defn pitch-using-flats [pitch-keyword]
  (let [pitch-str (name pitch-keyword)]
    (if (= \# (second pitch-str))
    (let [octave (last pitch-str)]
      (keyword (str ({"G#" "AB" 
                      "A#" "BB" 
                      "C#" "DB" 
                      "D#" "EB" 
                      "F#" "GB"}
                     (subs (name pitch-str) 0 2))
                    octave)))
    pitch-keyword)))

(defn play-pitch [pitch]
  (let [sample-name (str "sample-" 
                         (string/upper-case (name (pitch-using-flats pitch))))
        sample (. js/document 
                  (getElementById sample-name))]
    ; Some browsers (chrome desktop) don't allow setting currentTime, so we
    ; fall back to calling load.
    (aset sample "currentTime" 0)
    (when-not (= 0 (.-currentTime sample)) (.load sample))
    (.play sample)))

(defn play-pitches [pitches]
  (doseq [p pitches]
    (play-pitch p)))

(defn scale-note 
  [root nth]
  (pitch/find-note-name (+ (pitch/nth-interval nth) (pitch/note root))))

(defn chord-degree
  [root degree]
  (map pitch/find-note-name (pitch/chord-degree degree root :ionian 3)))

(defn seq-walk-in-key
  [root from-degree to-degree]
  (map (fn [n] [1 (scale-note root n)])
       (if (> to-degree from-degree)
         (range from-degree (inc to-degree))
         (range from-degree (dec to-degree) -1))))

(defn seq-degree-in-key
  [root target-degree direction]
  (list
    ; Outline the key
    [2 (chord-degree root :i)]
    [1 (chord-degree root :iv)]
    [1 (chord-degree root :v)]
    [2 (chord-degree root :i)]

    ; Play the target note
    [4 (scale-note root target-degree)]

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
  [{root (rand-note :C2 :C3)}
   {degree (rand-int 8)}
   {direction :closest}]
  (seq-degree-in-key 
    root
    degree
    direction))

(defn flatten-max-1
  [seq]
  (filter #(and (coll? %)
                (number? (first %)))
          (tree-seq coll?
                    identity 
                    seq)))

(defonce options (atom {}))
(defonce control-ch (atom nil))

(defn ms-per-beat [tempo]
  (* 1000 (/ 60 tempo)))

(defn stop []
  (if @control-ch (async/put! @control-ch :stop))
  (reset! control-ch nil))

(defn start []
  (stop)
  (reset! control-ch (async/chan))
  (let [tempo 110]
    (go-loop
        [notes []]
        (if (empty? notes)
          ; We're empty, or just starting. Fill us up with a new set of notes.
          (let [new-notes (flatten-max-1 (random-seq-degree-in-key @options))]
            (recur 
              ; Extend the last note so we get a delay before looping.
              (concat (butlast new-notes) [(assoc (last new-notes) 0 4)])))
          (let
            [[duration pitch] (first notes)]
            (if (coll? pitch)
              (play-pitches pitch)
              (play-pitch pitch))
            (when 
              (= :play-next
                 (alt!
                   @control-ch :stop
                   (async/timeout 
                     (* duration (ms-per-beat tempo))) :play-next)) 
              (recur (rest notes))))))))

(defn set-options [os]
  (swap! options merge os))
