(ns osmosis.scratch
  (:require [osmosis.player :as player]
            [overtone.music.pitch :as pitch]))

(player/play-pitch :C2)

(player/start)

(pitch/chord-degree :i :c2 :ionian)

(player/pitch-using-flats :C#3)
(player/pitch-using-flats :C3)

(player/flatten-max-1 (player/random-seq-degree-in-key {}))
(player/random-seq-degree-in-key {})
