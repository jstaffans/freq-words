(ns freq-words.groups
  (:require-macros [freq-words.macros :refer [defedn fore]])
  (:require [reagent.core :as r :refer [atom]]))

(defedn words "words.edn")

(defonce group-selection (r/atom -1))

(defn checkbox-component
  [label id]
  (let [id (str "checkbox-" id)]
    ))

(defn group-control-component
  [group]
  [:div {:class "controls"}
   [:div
    [:a {:class "btn btn-success"} "Kör!"]
    [:a {:class    "btn btn-link pull-right"
         :on-click #(do
                     (-> js/document .-activeElement .blur)
                     (reset! group-selection -1))} "Stäng"]]])

(defn word-group-component
  [{:keys [group-index group]}]
  (let [[words more-words] [(take 3 group) (take 5 (drop 3 group))]
        class (str "word-group group-" group-index)
        selected? (= @group-selection group-index)]

    [:div {:class "col-md-4 word-col"}
     [:div {:class     (str class
                         (when (>= @group-selection 0)
                           (if selected?
                             " selected"
                             " not-selected")))
            :key       group-index
            :tab-index group-index
            :on-click  #(when (not selected?) (reset! group-selection group-index))}
      [:div {:class "preview"} (str (clojure.string/join ", " words)
                                 (if selected? "," " …"))]
      [:div {:class "sub-preview"}
       (if selected? (str (clojure.string/join ", " more-words) " …"))]
      (group-control-component group)]]))

(defn word-group-row-component
  [groups]
  [:div {:class "row" :key (-> groups first :group-index)}
   (fore [group groups]
     (word-group-component group))
   [:div {:style {:clear "left"}}]])

(defn group-select-component []
  [:div
   [:nav {:class "navbar navbar-default navbar-fixed-top"}
    [:div {:class "container container-header vertical-align"}
     [:div {:class "col-md-8 header-title"} "Välj grupp"]
     [:div {:class "col-md-4 header-control"}
      [:form
       [:div {:class "checkbox checkbox-success"}
        [:input {:type "checkbox" :id "checkbox-1"}]
        [:label {:for "checkbox-1"} "Slumpmässig ordföljd"]]]]]]
   [:div {:class "container container-groups"}
    (fore [group-row (->> (:groups words)
                          (map-indexed (fn [i group] (assoc {} :group-index (inc i) :group group)))
                          (partition-all 3))]
      (word-group-row-component group-row))]])

