(ns osmosis.view
  (:require
    [om-tools.core :refer-macros [defcomponentk]]
    [om-tools.dom :include-macros true :as dom]
    [om.core :as om]
    [om-bootstrap.button :as b]
    [om-bootstrap.random :as r]))

(defonce app-state {:playing? false})

(defcomponentk app [data owner state]
  (render
    [_]
    (let [attrs {:bs-size "large"
                 :on-click #(swap! state assoc 
                                   :playing? (not (:playing? @state)))}]
      (if (:playing? @state)
        (b/button (assoc attrs :bs-style "danger") 
                  (r/glyphicon {:glyph "stop"} " Stop"))
        (b/button (assoc attrs :bs-style "success") 
                  (r/glyphicon {:glyph "play"} " Play"))))))

(defn mount-components []
  (om/root
    app
    app-state
    {:target (. js/document (getElementById "app"))}))
