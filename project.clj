(defproject osmosis "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [compojure "1.2.1"]
                 [enlive "1.1.5"]
                 [prismatic/plumbing "0.3.5"]
                 [ring "1.3.1"]
                 [reagent "0.4.3"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.5-SNAPSHOT"]
            [com.cemerick/austin "0.1.4"]
            [lein-haml-sass "0.2.7-SNAPSHOT"]]

  :main osmosis.core

  :uberjar-name "osmosis.jar"

  :profiles 
  {:dev {:dependencies [[figwheel "0.1.5-SNAPSHOT"]]
         :source-paths ["dev/clj"]
         :cljsbuild {:builds {:app {:source-paths ["dev/cljs"]}}}} 

   :uberjar {:hooks [leiningen.cljsbuild
                     ]
             :source-paths ["prod/clj"]
             :prep-tasks [["haml-sass" "once"] "javac" "compile"]
             :omit-source true
             :aot :all
             :cljsbuild {:builds {:app {:source-paths ["prod/cljs"]
                                        :compiler {
                                                   :optimizations :advanced
                                                   :pretty-print false}}}}}}

  :haml {:src "src/haml"
         :output-directory "resources/public/html"}


  :sass {:src "src/sass"
         :output-directory "resources/public/css"}

  :cljsbuild 
  {:builds {:app {:source-paths ["src/cljs" 
                                 "../overtone/generated/src/cljs"]
                  :compiler 
                  {:output-to "resources/public/js/application.js"
                   :output-dir "resources/public/js/"
                   :optimizations :none
                   :source-map "resources/public/js/application.js.map"}}}}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :css-dirs ["resources/public/css"]})
