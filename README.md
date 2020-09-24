# datahike-leveldb

<p align="center">
<a href="https://clojurians.slack.com/archives/CB7GJAN0L"><img src="https://img.shields.io/badge/clojurians%20slack-join%20channel-blueviolet"/></a>
<a href="https://clojars.org/io.replikativ/datahike-leveldb"> <img src="https://img.shields.io/clojars/v/io.replikativ/datahike-leveldb.svg" /></a>
<a href="https://circleci.com/gh/replikativ/datahike-leveldb"><img src="https://circleci.com/gh/replikativ/datahike-leveldb.svg?style=shield"/></a>
<a href="https://github.com/replikativ/datahike-leveldb/tree/development"><img src="https://img.shields.io/github/last-commit/replikativ/datahike-leveldb/development"/></a>
<a href="https://versions.deps.co/replikativ/datahike-leveldb" title="Dependencies Status"><img src="https://versions.deps.co/replikativ/datahike-leveldb/status.svg" /></a>
</p>


[Datahike](https://github.com/replikativ/datahike) with [LevelDB](https://github.com/google/leveldb) as data storage.


## Usage

Add to your leiningen dependencies:

[![Clojars Project](http://clojars.org/io.replikativ/datahike-leveldb/latest-version.svg)](http://clojars.org/io.replikativ/datahike-leveldb)

After including the datahike API and the datahike-leveldb namespace, you can use the LevelDB backend now using the keyword `:level`

```clojure
(ns project.core
  (:require [datahike.api :as d]
            [datahike-leveldb.core]))

;; Create a config map with postgres as storage medium
(def config {:backend :level
             :path    "/tmp/example-db"})

;; Create a database at this place, by default configuration we have a strict
;; schema and temporal index
(d/create-database config)

(def conn (d/connect config))

;; The first transaction will be the schema we are using:
(d/transact conn [{:db/ident :name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one }
                  {:db/ident :age
                   :db/valueType :db.type/long
                   :db/cardinality :db.cardinality/one }])

;; Let's add some data and wait for the transaction
(d/transact conn [{:name  "Alice", :age   20 }
                  {:name  "Bob", :age   30 }
                  {:name  "Charlie", :age   40 }
                  {:age 15 }])

;; Search the data
(d/q '[:find ?e ?n ?a
       :where
       [?e :name ?n]
       [?e :age ?a]]
  @conn)
;; => #{[3 "Alice" 20] [4 "Bob" 30] [5 "Charlie" 40]}

;; Clean up the database if it is not needed any more
(d/delete-database config)
```


## License

Copyright © 2019  lambdaforge UG (haftungsbeschränkt)

This program and the accompanying materials are made available under the terms of the Eclipse Public License 1.0.
