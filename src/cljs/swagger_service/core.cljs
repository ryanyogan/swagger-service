(ns swagger-service.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]])
  (:require-macros [secretary.core :refer [defroute]]))

;;
;; Edge Functions (Side Effects)
;;

(defn fetch-links! [links link-count]
  "Fetches links from api, returns an array the length of link-count"
  (GET "/api/cat-links"
    {:params  {:link-count link-count}
     :handler #(reset! links %)}))

;;
;; Page Components
;;

(defn home-page []
  (let
   [links (atom nil)]
    (fetch-links! links 20)
    (fn []
      [:div
       (for [link @links]
         [:img {:src link}])])))

;;
;; Component Mounting
;;

(defn mount-components []
  (reagent/render-component [home-page] (.getElementById js/document "app")))

;;
;; App Instantiators
;;

(defn init! []
  (mount-components))

