(ns freq-words.app
  (:require-macros [freq-words.macros :refer [defedn]])
  (:require [reagent.core :as reagent :refer [atom]]))

(defedn words "words.edn")

(defn group-select-component []
  [:div
   [:h1 "Välj grupp"]
   [:p (str "foo is " (:foo words))]])

(defn init []
  (reagent/render-component [group-select-component]
    (.getElementById js/document "container")))
