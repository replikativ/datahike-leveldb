(ns datahike-leveldb.core-test
  (:require
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datahike.api :as d]
    [datahike-leveldb.core]))


(deftest test-leveldb-store
  (let [cfg {:store {:backend :level
                     :path "/tmp/test-leveldb"}
             :schema-flexibility :read
             :keep-history? false}]

    (testing "Existent database detected as such"
      (d/delete-database cfg)
      (d/create-database cfg)
      (is (d/database-exists? cfg)))

    (testing "Database connection and searchability"
      (let [conn (d/connect cfg)]
        (d/transact conn [{:db/id 1, :name "Ivan", :age 15}
                          {:db/id 2, :name "Petr", :age 37}
                          {:db/id 3, :name "Ivan", :age 37}
                          {:db/id 4, :age 15}])

        (is (= (d/q '[:find ?e :where [?e :name]] @conn)
               #{[3] [2] [1]}))
        (d/release conn)))

    (testing "Nonexistent database detected as such"
      (d/delete-database cfg)
      (is (not (d/database-exists? cfg))))))
