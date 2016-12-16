(ns kanban.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def app-state
  (r/atom {:columns [{:title   "Work"
                      :editing false
                      :cards   [{:title "Learn about Reagent"}
                                {:title   "Do something else"
                                 :editing false}]}
                     {:title   "Personal"
                      :editing false
                      :cards   [{:title   "Finish Reagent tutorials"
                                 :editing false}
                                {:title   "Finish Reagent tutorial number 2"
                                 :editing false}]}]}))

(defn set-title! [board col-idx card-idx title]
  (swap! board assoc-in [:columns col-idx :cards card-idx title] title))

(defn update-card-title! [card-cur title]
  (swap! card-cur assoc :title title))

(defn update-col-title! [col-cur title]
  (swap! col-cur assoc :title title))

(defn stop-editing! [card-cur]
  (swap! card-cur dissoc :editing))

(defn start-editing! [card-cur]
  (swap! card-cur assoc :editing true))

(defn add-card! [col-cur]
  (swap! col-cur update :cards conj {:title "Untitled"
                                     :editing true}))

(defn add-column! []
  (swap! app-state update :columns conj {:title "Untitled"
                                     :cards   []
                                     :editing true}))

(defn NewCard [col-cur]
  [:div.new-card {:on-click #(add-card! col-cur)}"+ add a card"])

(defn NewColumn []
  [:div.new-column {:on-click #(add-column!)} "+ add new column"])

(defn Card [card-cur]
  (let [{:keys [editing title]} @card-cur]
    (if editing
      [:div.card.editing
       [:input {:type         "text" :value title
                :autoFocus    true
                :on-change    #(update-card-title! card-cur (.. % -target -value))
                :on-blur      #(stop-editing! card-cur)
                :on-key-press #(if (= (.-charCode %) 13)
                                (stop-editing! card-cur))
                }]]
      [:div.card {:on-click #(start-editing! card-cur)} title])))

(defn Column [col-cur]
  (let [{:keys [title cards editing]} @col-cur]
    [:div.column
     (if editing
       [:input {:type "text" :value title
                :autoFocus true
                :on-change #(update-col-title! col-cur (.. % -target -value))
                :on-blur   #(stop-editing! col-cur)
                :on-key-press #(if (= (.-charCode %) 13)
                                 (stop-editing! col-cur))}]
       [:h2 title])
     (for [idx (range (count cards))]
       [Card (r/cursor col-cur [:cards idx])])
     [NewCard col-cur]]))

(defn Board [state]
  [:div.board
   (map-indexed (fn [idx col]
                  (let [col-cur (r/cursor state [:columns idx])]
                    [Column col-cur])) (:columns @state))
   [NewColumn]])

(r/render [Board app-state] (js/document.getElementById "app"))

