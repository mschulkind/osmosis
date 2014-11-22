(ns osmosis.core
  (:require [osmosis.server :as server]
            [cemerick.piggieback :as piggieback]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [api]]
            [cljs.repl.browser :as browser]))

(defn browser-repl []
  (piggieback/cljs-repl
    :repl-env (browser/repl-env :port 9000)))

(defn create-http-handler [routes-var]
  (reload/wrap-reload (api routes-var)))

(defn -main []
  (server/run create-http-handler))
