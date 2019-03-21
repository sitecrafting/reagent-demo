(ns reagent-demo.core
    (:require
      [reagent.core :as r]))

;; -------------------------
;; Application State

(defonce state (r/atom {:spaceships [{:id "NCC-1701" :name "USS Enterprise"}
                                     {:id "YT-1300" :name "Millennium Falcon"}
                                     {:id "OV-204" :name "Atlantis"}]

                        :selection {}}))

(defn handler [current-state action value]
  "Transforms the current app-state into the desired new state according to
  action and value"
  (case action
    :update-ship (assoc current-state :selection value)))

(defn emit! [action value]
  (r/rswap! state handler action value))


;; -------------------------
;; Components

(defn spaceship [ship]
  [:li
   {:on-click #(emit! :update-ship ship)}
   (:name ship)])

(defn home-page []
  [:div
   [:h2 "Welcome to Reagent on Lando!"]
   [:h3 "Pick the best spaceship:"]
   [:ul
    (map
      (fn [ship]
        ^{:key (:id ship)} [spaceship ship])
      (:spaceships @state))]
   [:strong (str "You said it! The best spaceship is: "
                 (get-in @state [:selection :name])
                 " (ID: "
                 (get-in @state [:selection :id])
                 ")")]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
