(ns reagent-demo.core
    (:require
      [reagent.core :as r]))

;; -------------------------
;; Application State

(defonce state (r/atom {:spaceships [{:id "NCC-1701" :name "USS Enterprise"}
                                     {:id "YT-1300" :name "Millennium Falcon"}
                                     {:id "OV-204" :name "Atlantis"}]

                        :new-ship {:id "ABC" :name "My Very Own Spaceship"}

                        :selection {}}))

(defn handler [current-state action value]
  "Transforms the current app-state into the desired new state according to
  action and value"
  (case action
    :update-ship (assoc current-state :selection value)

    :update-new-ship-name (assoc-in current-state [:new-ship :name] value)

    :update-new-ship-id (assoc-in current-state [:new-ship :id] value)

    ; this one's a little more advanced.
    ; We want to update two pieces of state: The new-ship and our list of
    ; spaceships. The list of spaceships should get the new value added to it,
    ; while new-ship should get reset to a blank value.
    :add-ship (-> current-state
                  ; reset new-ship
                  (assoc :new-ship {})
                  (update :spaceships (fn [ships] (vec (conj ships value)))))))

(defn emit! [action value]
  "Get the new, transformed app state according to action and value,
  and swap out the old for the new"
  (r/rswap! state handler action value))


;; -------------------------
;; Components

(defn spaceship [ship]
  [:li
   {:on-click #(emit! :update-ship ship)}
   (:name ship)])

(defn best-spaceship [ship]
  "Check if a spaceship has been selected. If one has, display its name and id"
	(if (some? (:id ship))
    (let [{:keys [name id]} ship]
      [:strong (str "You said it! The best spaceship is: " name " (ID: " id ")")])
    [:p "Who knows what the best spaceship is??"]))

(defn ship-adder [new-ship]
  [:div {:class "new-ship"}
   [:h4 "Add a ship:"]
   [:label {:for "new-ship-id"} "ID:"]
   [:br]
   [:input {:on-change #(emit! :update-new-ship-id (.-target.value %))
            :value (:id new-ship)
            :id "new-ship-id"}]
   [:br]
   [:label {:for "new-ship-name"} "Name:"]
   [:br]
   [:input {:on-change #(emit! :update-new-ship-name (.-target.value %))
            :value (:name new-ship)
            :name "new-ship-name"}]
   [:br]
   [:button {:on-click #(emit! :add-ship new-ship)} "Add!"]])

(defn home-page []
  [:div
   [:h2 "Welcome to Reagent on Lando!"]
   [:h3 "Pick the best spaceship:"]
   [:ul
    ; Map over all spaceships in our state, rendering a subcomponent for each
    (map
      (fn [ship]
        ; this funky ^{:key ...} thing is a React quirk, since React requires
        ; every item in a collection of subcomponents to have a `key` prop.
        ; Here, we use the Clojure "metadata" syntax to tell Reagent to use
        ; a spaceship's id as its key.
        ^{:key (:id ship)}
        ; this is actual function call we're mapping to - it just calls the
        ; component as a hiccup (HTML) element
        [spaceship ship])
      ; The collection of things we're mapping over:
      (:spaceships @state))]
   [best-spaceship (:selection @state)]
   [ship-adder (:new-ship @state)]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
