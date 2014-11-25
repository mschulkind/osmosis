(ns osmosis.debug)

(defn log [x]
  (. js/console (log (prn-str x))))
