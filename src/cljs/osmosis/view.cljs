(ns osmosis.view
  (:require
    [om-tools.core :refer-macros [defcomponent]]
    [om-tools.dom :include-macros true :as dom]
    [om.core :as om]
    [om-bootstrap.button :as b]
    [om-bootstrap.random :as r]))

(defonce app-state {:playing? false})

(defcomponent app [data owner]
  (render-state 
    [_ state]
    (let [attrs {:bs-size "large"
                 :on-click #(om/set-state! owner 
                                        :playing? (not (:playing? state)))}]
      (if (:playing? state)
        (b/button (assoc attrs :bs-style "danger") 
                  (r/glyphicon {:glyph "stop"} " Stop"))
        (b/button (assoc attrs :bs-style "success") 
                  (r/glyphicon {:glyph "play"} " Play"))))))

(defn mount-components []
  (om/root
    app
    app-state
    {:target (. js/document (getElementById "app"))}))
