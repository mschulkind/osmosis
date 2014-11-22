(defproject osmosis "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [compojure "1.2.1"]
                 [enlive "1.1.5"]
                 [overtone "0.10-SNAPSHOT"]
                 [prismatic/plumbing "0.3.5"]
                 [ring "1.3.1"]
                 [om "0.8.0-alpha2"]
                 [prismatic/om-tools "0.3.6"]
                 [racehub/om-bootstrap "0.3.1"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.4-SNAPSHOT"]
            [com.cemerick/austin "0.1.4"]
            [lein-haml-sass "0.2.7-SNAPSHOT"]
            [lein-environ "1.0.0"]]

  :main osmosis.core

  :profiles 
  {:dev {:dependencies [[figwheel "0.1.4-SNAPSHOT"]]
         :source-paths ["dev/clj"]}}

  :haml {:src "resources/haml"
         :output-directory "resources/public/"}

  :sass {:src "resources/sass"
         :output-directory "resources/public/css"}

  :cljsbuild 
  {:builds [{:id "dev" 
             :source-paths ["src/cljs" 
                            "dev/cljs" 
                            "../overtone/generated/src/cljs"]
             :compiler {:output-to "resources/public/js/application.js"
                        :output-dir "resources/public/js/"
                        :optimizations :none
                        :source-map true}}]}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :css-dirs ["resources/public/css"]})
