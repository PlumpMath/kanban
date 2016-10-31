(ns kanban.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def app-state
  (r/atom {:columns [{:title "Todos"
                      :editing true
                      :cards [{:title "Learn about Reagent"}
                              {:title "Do something else"
                               :editing true}
                              ]}]}))

;; (defn Card [card]
;;   (if (:editing card)
;;     [:div.card.editing [:input {:type "text" :value (:title card)}]]
;;     [:div.card (:title card)]))

(defn Card [card]
  (if (:editing card)
    [:div.card.editing [:input {:type "text" :value (:title card)}]]
    [:div.card (:title card)] )
  )

(defn NewCard []
  [:div.new-card "+ add a card"])

(defn NewColumn []
  [:div.new-column "+ add new column"])

(defn Column [{:keys [title cards editing]}]
  [:div.column
   (if editing
     [:input {:type "text" :value title}]
     [:h2 title])
   (for [c cards]
     [Card c])
   [NewCard]])

(defn Board [state]
  [:div.board
   (for [c (:columns @state)]
     [Column c])
   [NewColumn]])

(defn greeting []
  [:h1 (:text @app-state)])

(r/render [Board app-state] (js/document.getElementById "app"))
