(ns datahike-leveldb.core-test
  (:require
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datahike.api :as d]
    [konserve-leveldb.core :refer :all]))


(deftest test-leveldb-store
  (let [uri "datahike:level:///tmp/api-leveldb"
        _ (d/delete-database uri)]

    (is (not (d/database-exists? uri)))

    (let [_ (d/create-database uri :schema-on-read true)
          conn (d/connect uri)]
      (d/transact conn [{ :db/id 1, :name  "Ivan", :age   15 }
                        { :db/id 2, :name  "Petr", :age   37 }
                        { :db/id 3, :name  "Ivan", :age   37 }
                        { :db/id 4, :age 15 }])

      (is (= (d/q '[:find ?e :where [?e :name]] @conn)
             #{[3] [2] [1]}))

      (d/release conn)

      (is (d/database-exists? uri))

      (d/delete-database uri))))