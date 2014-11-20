(ns osmosis.dev
  (:require [figwheel.client :as fw]
            [clojure.browser.repl :as repl]))

(enable-console-print!)

(repl/connect "http://localhost:9000/repl")

(fw/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws")