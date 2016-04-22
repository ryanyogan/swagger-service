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
     :handler #(reset! links (vec (partition-all 6 %)))}))

;;
;; Helper Components
;;

(defn images [links]
  [:div.text-xs-center
   (for [row (partition-all 3 links)]
     ^{:key row}
     [:div.row
      (for [link row]
        ^{:key link}
        [:div.col-sm-4 [:img {:width 400 :src link}]])])])

;;
;; Page Components
;;

(defn home-page []
  (let
   [links (atom nil)
    page (atom 0)]
    (fetch-links! links 20)
    (fn []
      [:div
       (when @links
         [images (@links @page)])])))

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

