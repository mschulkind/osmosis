(ns osmosis.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [net.cgrand.enlive-html :as en]
            [ring.adapter.jetty :refer [run-jetty]]))

(en/deftemplate page (io/resource "public/index.html") []
  [:body] 
  (en/append 
    (en/html [:script {:type "text/javascript"} "goog.require('osmosis.dev')"])))

(defroutes routes
  (resources "/css" {:root "public/css"})
  (resources "/js" {:root "public/js"})
  (GET "/*" req (page)))

(def http-handler (api routes))

(defn run []
  (let [port 4200]
    (print "Starting web server on port" port ".\n")
    (run-jetty http-handler {:port port :join? false})))
