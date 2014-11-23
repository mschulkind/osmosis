(ns osmosis.view
  (:require
    [reagent.core :as reagent :refer [atom]]))

(defonce app-state (atom {:playing? false}))

(defn play-button [state]
  (let [playing? (:playing? @state)
        [style icon text] (if playing?
                            ["danger" "stop" "Stop"]
                            ["success" "play" "Play"])]
  [:button 
   {:type "button" 
    :className (str "btn btn-lg btn-" style)
    :on-click #(swap! state assoc :playing? (not playing?))}
     [:span {:className (str "glyphicon glyphicon-" icon)}] (str " " text)
   ]))

(defn app [state]
  [:div {:class "player"}
   [play-button state]])

(defn mount-components []
  (reagent/render-component
    [app app-state]
    (. js/document (getElementById "app"))))
