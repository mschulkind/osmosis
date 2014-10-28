(ns user
  (:use overtone.core))

(defn start []
  (boot-server)
  (require 'user2)
  (in-ns 'user2))
