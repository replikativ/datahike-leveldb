(ns datahike-leveldb.core-test
  (:require
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datahike.api :as d]
    [datahike.store :as ds]
    [datahike-leveldb.core]))


(deftest test-leveldb-store
  (let [cfg {:store {:backend :level
                     :path "/tmp/test-leveldb"}
             :schema-flexibility :read
             :keep-history? false}]

    (d/delete-database cfg)

    (d/create-database cfg)

    (d/delete-database cfg)

    #_(let [_ 
          conn (d/connect cfg)]
      (d/transact conn [{ :db/id 1, :name  "Ivan", :age   15 }
                        { :db/id 2, :name  "Petr", :age   37 }
                        { :db/id 3, :name  "Ivan", :age   37 }
                        { :db/id 4, :age 15 }])

      (is (= (d/q '[:find ?e :where [?e :name]] @conn)
             #{[3] [2] [1]}))


      ;; release connection before existence check
      (ds/release-store cfg (:store @conn))

      (d/delete-database cfg))))
