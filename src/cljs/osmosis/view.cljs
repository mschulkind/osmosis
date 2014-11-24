(ns osmosis.view
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent.debug :refer-macros [dbg]]))

(defonce app-state (atom {:playing? false
                          :direction :closest}))

(defn play-button [state]
  (let [playing? (:playing? @state)
        [style icon text] (if playing?
                            ["danger" "stop" "Stop"]
                            ["success" "play" "Play"])]
  [:button 
   {:type "button" 
    :class (str "btn btn-lg btn-" style)
    :on-click #(swap! state assoc :playing? (not playing?))}
     [:span {:className (str "glyphicon glyphicon-" icon)}] (str " " text)
   ]))

(defn direction-selector [state]
  [:div
   [:h3 "Walk Direction"]
   (doall (for [[value label] [[:closest "Closest"]
                               [:up "Up"]
                               [:down "Down"]]]
            ^{:key value}
            [:div {:class "radio"} 
             [:label 
              [:input {:type "radio" 
                       :name "direction"
                       :checked (= value (:direction @state))
                       :on-change #(swap! state assoc :direction value)}] 
              label]]))])

(defn lowest-note-selector [state])

(defn player-options [state]
  [:form {:role "form"}
   [:div {:class "row"}
    [:div {:class "col-md-4"} [direction-selector state]]
    [:div {:class "col-md-4"} [lowest-note-selector state]]
    [:div {:class "col-md-4"}]]])

(defn app [state]
  [:div {:class "player"}
   [player-options state]
   [play-button state]])

(defn mount-components []
  (reagent/render-component
    [app app-state]
    (. js/document (getElementById "app"))))
