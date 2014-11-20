(ns osmosis.core
  (:require [osmosis.server :as server]
            [cemerick.piggieback :as piggieback]
            [cljs.repl.browser :as browser]))

(defn browser-repl []
  (piggieback/cljs-repl
    :repl-env (browser/repl-env :port 9000)))

(defn -main []
  (server/run))
