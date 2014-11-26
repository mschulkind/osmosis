(ns osmosis.core
  (:require [figwheel.client :as fw]
            [clojure.browser.repl :as repl]
            [osmosis.controller :as controller]))

(enable-console-print!)

(repl/connect "http://localhost:9000/repl")

(fw/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback (fn [] (controller/main)))

(defonce not-used (controller/main))
