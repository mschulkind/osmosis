(ns osmosis.core
  (:require [osmosis.server :as server])
  (:gen-class))

(defn create-http-handler [api]
  api)

(defn -main []
  (server/run false create-http-handler))
