(ns osmosis.server
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [net.cgrand.enlive-html :as en]
            [ring.adapter.jetty :refer [run-jetty]]))

(declare dev?)

(defn sound-filenames []
  (for [f (read-string (slurp (io/resource "public/sound/manifest.edn")))]
    (let [base (last (re-find #"(.*)\.mp3" f))]
      {:base base :full (str "sound/" f)})))

(defn resource-path [path]
  (if dev?
    path
    (string/replace path "." ".min.")))

(en/deftemplate page (io/resource "public/html/index.html") []
  [:head]
  (en/do->
    (en/prepend
      (en/html [:link {:rel "stylesheet" 
                       :type "text/css"
                       :href (resource-path "css/bootstrap.css")}]))

    (en/append
      (en/html [:script {:type "text/javascript" 
                         :src (resource-path "js/react.js")}]))
    
    (en/append
      (en/html
        (for [f (sound-filenames)]
          [:audio {:src (:full f) 
                   :preload "auto" 
                   :id (str "sample-" (:base f))}])))
    
    (if dev?
      (en/append
        (en/html [:script {:type "text/javascript"
                           :src "js/goog/base.js"}]))
      identity))
  
  [:body]
  (if dev?
    (en/append
      (en/html 
        [:script {:type "text/javascript"} 
         "goog.require('osmosis.core')"]))
    identity))

(defroutes routes
  (resources "/css" {:root "public/css"})
  (resources "/js" {:root "public/js"})

  (resources "/js" {:root "reagent/"})

  (resources "/js" {:root "vendor/bootstrap/js"})
  (resources "/fonts" {:root "vendor/bootstrap/fonts"})
  (resources "/css" {:root "vendor/bootstrap/css"})

  (resources "/sound" {:root "public/sound"})

  (GET "/*" req (page)))

(defn run [d? create-http-handler]
  (def dev? d?)
  (let [port 4200]
    (print "Starting web server on port" port ".\n")
    (run-jetty (create-http-handler (api #'routes)) {:port port :join? false})))
