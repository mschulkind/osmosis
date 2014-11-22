(ns osmosis.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [net.cgrand.enlive-html :as en]
            [net.cgrand.reload]
            [ring.adapter.jetty :refer [run-jetty]]))

(en/deftemplate page (io/resource "public/index.html") []
  [:body] 
  (en/append 
    (en/html [:script {:type "text/javascript"} "goog.require('osmosis.dev')"]))
  
  [:head]
  (en/prepend
    (en/html [:script {:type "text/javascript" :src "js/react.js"}])))

(defroutes routes
  (resources "/css" {:root "public/css"})
  (resources "/js" {:root "public/js"})

  (resources "/js" {:root "react/"})

  (resources "/js" {:root "vendor/bootstrap/js"})
  (resources "/fonts" {:root "vendor/bootstrap/fonts"})
  (resources "/css" {:root "vendor/bootstrap/css"})

  (GET "/*" req (page)))

(defn run [create-http-handler]
  (let [port 4200]
    (net.cgrand.reload/auto-reload *ns*)
    (print "Starting web server on port" port ".\n")
    (run-jetty (create-http-handler #'routes) {:port port :join? false})))
