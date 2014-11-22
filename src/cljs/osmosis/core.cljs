(ns osmosis.core
  (:require [osmosis.view :as view]))

(defn main []
  (view/mount-components))

(main)
