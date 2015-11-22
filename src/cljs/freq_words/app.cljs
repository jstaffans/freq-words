(ns freq-words.app
  (:require-macros [freq-words.macros :refer [defedn]])
  (:require [reagent.core :as reagent :refer [atom]]))

(defedn words "words.edn")

(defn word-group-component
  [words group-counter]
  [:div {:class (str "col-md-4 word-group group-" (swap! group-counter inc)) :key (first words)}
   (str (clojure.string/join ", " words) " …")])

(defn word-group-row-component
  [groups group-counter]
  [:div {:class "row" :key (-> groups flatten first)}
   (for [group groups]
     (word-group-component (take 3 group) group-counter))
   [:div {:style {:clear "left"}}]])

(defn group-select-component []
  [:div
   [:h1 {:class "group-select"} "Välj grupp"]
   (let [group-counter (atom 0)]
     (for [groups (partition-all 3 (:groups words))]
       (word-group-row-component groups group-counter)))])

(defn init []
  (reagent/render-component [group-select-component]
    (.getElementById js/document "container")))
