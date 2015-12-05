(ns freq-words.macros
  (:require [clojure.java.io :refer [resource]]
            [clojure.edn :as edn]))

(defmacro defedn
  "Read edn file from resources/edn."
  [symbol-name edn-name]
  (let [content (edn/read-string (slurp (resource (str "edn/" edn-name))))]
    `(def ~symbol-name ~content)))

(defmacro fore
  "Eager for"
  [seq-exprs body-expr]
  `(doall
     (for ~seq-exprs ~body-expr)))