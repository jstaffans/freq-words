(ns freq-words.groups
  (:require-macros [freq-words.macros :refer [defedn fore]])
  (:require [reagent.core :as r :refer [atom]]))

(defedn words "words.edn")

(defonce group-selection (r/atom -1))

(defn group-control-component
  [group]
  [:div {:class "controls"}
   [:span "Slumpmässig ordning"]
   [:span "Tidtagning"]
   [:div
    [:span "Kör!"]
    [:a {:class "pull-right" :on-click #(reset! group-selection -1)} "Stäng"]]])

(defn word-group-component
  [{:keys [group-index group]}]
  (let [words (take 3 group)
        class (str "col-md-4 word-group group-" group-index)
        selected? (= @group-selection group-index)]
    [:div {:class    (str class
                       (when (>= @group-selection 0)
                         (if selected?
                           " selected"
                           " not-selected")))
           :key      group-index
           :on-click #(when (not selected?) (reset! group-selection group-index))}
     [:div {:class "preview"} (str (clojure.string/join ", " words) " …")]
     (group-control-component group)]))

(defn word-group-row-component
  [groups]
  [:div {:class "row" :key (-> groups first :group-index)}
   (fore [group groups]
     (word-group-component group))
   [:div {:style {:clear "left"}}]])

(defn group-select-component []
  [:div
   [:h1 {:class "group-select"} "Välj grupp"]
   (fore [group-row (->> (:groups words)
                         (map-indexed (fn [i group] (assoc {} :group-index i :group group)))
                         (partition-all 3))]
     (word-group-row-component group-row))])

