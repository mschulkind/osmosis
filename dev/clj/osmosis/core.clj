(ns osmosis.core
  (:require [osmosis.server :as server]
            [cemerick.piggieback :as piggieback]
            [net.cgrand.reload]
            [ring.middleware.reload]
            [compojure.handler :refer [api]]
            [cljs.repl.browser :as browser]))

(defn browser-repl []
  (piggieback/cljs-repl
    :repl-env (browser/repl-env :port 9000)))

(defn create-http-handler [api]
  (ring.middleware.reload/wrap-reload api))

(defn -main []
  (net.cgrand.reload/auto-reload (the-ns 'osmosis.server))
  (server/run true create-http-handler))
