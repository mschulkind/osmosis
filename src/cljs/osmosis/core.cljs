(ns osmosis.core
  (:require [osmosis.view :as view]
            [osmosis.player :as player]))

(defn update-player-options [view-state]
  (player/set-options 
    (select-keys view-state [:direction])))

(defn player-watcher [_ _ old-state new-state]
  (update-player-options new-state)
  (when (not= (:playing? old-state) (:playing? new-state))
    (if (:playing? new-state)
      (player/start)
      (player/stop))))

(defn main []
  (let [view-state (view/mount-components)]
    (remove-watch view-state :player)
    (add-watch view-state :player player-watcher)
    (println "added")
    (println view-state)
    (update-player-options @view-state)
    
    (when (:playing? @view-state)
      (player/start))))
