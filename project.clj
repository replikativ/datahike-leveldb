(defproject io.replikativ/datahike-leveldb "0.2.1-SNAPSHOT"
  :description "Datahike with LevelDB as data storage."
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/replikativ/datahike-leveldb"

  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [alekcz/konserve-leveldb "0.1.0-SNAPSHOT"]
                 ;; should be removed with datahike >= 0.3.3
                 [io.replikativ/superv.async "0.2.11"]
                 [io.replikativ/hitchhiker-tree "0.1.9"]
                 ;;
                 [environ "1.2.0"]
                 [io.replikativ/datahike "0.3.2-SNAPSHOT"]]

  :aliases {"test-clj"     ["run" "-m" "datahike-postgres.test/core_test-clj"]
            "test-all"     ["do" ["clean"] ["test-clj"]]}

  :profiles {:dev {:source-paths ["test"]
                   :dependencies [[org.clojure/tools.nrepl     "0.2.13"]
                                  [org.clojure/tools.namespace "0.3.1"]]}})
