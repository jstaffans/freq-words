(ns freq-words.app
  (:require-macros [freq-words.macros :refer [defedn]])
  (:require [reagent.core :as r]
            [freq-words.groups :refer [group-select-component]]))


(defn init []
  (r/render-component [group-select-component]
    (.getElementById js/document "container")))
