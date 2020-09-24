(ns datahike-leveldb.core
  (:require [datahike.store :refer [empty-store delete-store connect-store release-store scheme->index default-config config-spec]]
            [hitchhiker.tree.bootstrap.konserve :as kons]
            [konserve-leveldb.core :as k]
            [environ.core :refer [env]]
            [superv.async :refer [<?? S]]
            [clojure.spec.alpha :as s]))

(defmethod empty-store :level [{:keys [path]}]
  (kons/add-hitchhiker-tree-handlers
   (<?? S (k/new-leveldb-store path))))

(defmethod delete-store :level [{:keys [path]}]
  (let [store (<?? S (k/new-leveldb-store path))]
    (k/release store)
    (k/delete-store store)))

(defmethod connect-store :level [{:keys [path]}]
  (<?? S (k/new-leveldb-store path)))

(defmethod release-store :level [_ store]
  (k/release store))

(defmethod scheme->index :level [_]
  :datahike.index/hitchhiker-tree)

(defmethod default-config :level [config]
  (merge
   {:path (:datahike-store-path env)}
   config))

(s/def :datahike.store.level/backend #{:level})
(s/def :datahike.store.level/path string?)
(s/def ::level (s/keys :req-un [:datahike.store.level/backend
                                :datahike.store.level/path]))

(defmethod config-spec :level [_] ::level)
