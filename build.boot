(set-env!
 :source-paths    #{"src/clj" "src/cljs" "src/scss"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "0.0-2814-4" :scope "test"]
                 [adzerk/boot-cljs-repl "0.1.9"      :scope "test"]
                 [adzerk/boot-reload    "0.2.4"      :scope "test"]
                 [pandeiro/boot-http    "0.6.1"      :scope "test"]
                 [reagent "0.5.0"]])

(require
  '[clojure.java.io       :as io]
  '[boot.util             :as util]
  '[boot.core             :as core]
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload    :refer [reload]]
  '[pandeiro.boot-http    :refer [serve]])

(deftask sass
  []
  (let [tmp (core/tmp-dir!)]
    (core/with-pre-wrap fs 
      (let [in-files (core/input-files fs)
            in-main (first (core/by-re [#"^(?!_).*\.scss"] in-files))
            out-dir (io/file tmp "stylesheets")
            out (io/file out-dir "main.css")]
        (.mkdirs out-dir)
        (util/dosh "sassc"
          "--style" "compressed"
          (.getPath (core/tmp-file in-main))
          (.getPath out))
        (-> fs
          (core/add-resource tmp)
          (core/commit!))))))

(deftask build []
  (comp (speak)
        (sass)
        (cljs)))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced
                       ;; pseudo-names true is currently required
                       ;; https://github.com/martinklepsch/pseudo-names-error
                       ;; hopefully fixed soon
                       :pseudo-names true})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none
                       :unified-mode true
                       :source-map true}
                 reload {:on-jsload 'freq-words.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

