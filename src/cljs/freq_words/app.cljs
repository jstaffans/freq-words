(ns freq-words.app
  (:require-macros [freq-words.macros :refer [defedn]])
  (:require [reagent.core :as r :refer [atom]]))

(defedn words "words.edn")

(def group-selection (r/atom nil))

(defn word-group-component
  [words group-counter]
  (let [group-index (swap! group-counter inc)
        class (str "col-md-4 word-group group-" group-index)]
    [:div {:class    (str class
                       (when-let [selection @group-selection]
                         (if (= selection group-index)
                           " selected"
                           " not-selected")))
           :key      (first words)
           :on-click #(do
                       (.log js/console group-index)
                       (reset! group-selection group-index))}
     (str (clojure.string/join ", " words) " …")]))

(defn word-group-row-component
  [groups group-counter]
  [:div {:class "row" :key (-> groups flatten first)}
   (doall
     (for [group groups]
       (word-group-component (take 3 group) group-counter)))
   [:div {:style {:clear "left"}}]])

(defn group-select-component []
  [:div
   [:h1 {:class "group-select"} "Välj grupp"]
   (let [group-counter (r/atom 0)]
     (doall
       (for [groups (partition-all 3 (:groups words))]
         (word-group-row-component groups group-counter))))])

(defn init []
  (r/render-component [group-select-component]
    (.getElementById js/document "container")))
