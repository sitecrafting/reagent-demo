# Reagent Demo

This is a simplistic demo app for Reagent, a ClojureScript library written on top of React.js.

## Setup

For hot module reloading in the browser, `lando start` is enough to get started.

For REPL-driven development, though, the process is a little more in-depth. This is because the REPL/Lando integration is a work-in-progress, and there are still some major kinks to work out before `lando lein figwheel` is a pleasant experience.

### REPL Setup

The first thing you will need to do is make sure you have Java installed:

```
java --version
```

If the above command fails, you will need to [install the Java Runtime Environment](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html#A1097144) for your platform. Either the JDK or JRE will work - if in doubt, install the JRE.

Next, you will need to install Leiningen, the Clojure build tool. Follow the installation instructions [here](https://leiningen.org/) and double-check the installation by running `lein version`.

Finally, navigate to the root of this repo and start up the project:

```
cd /path/to/reagent-demo
lein figwheel
```

The first time you run `lein figwheel` it will take a while because it needs to download the project dependencies. When it's done with that, it should open a browser window, connect to the REPL server, and start listening for code changes on a websocket.

You should be able to edit the project's .cljs files (`src/reagent_demo/core.cljs`, for example) and see the changes loaded into the browser every time you save. You should also be able to manipulate application state from the REPL (note that the leading semicolons denote comments):

```
; navigate into the correct namespace
app:cljs.user=> (ns reagent-demo.core)
; save core.cljs, then try reordering the state...
app:reagent-demo.core=> (swap! state update :spaceships (fn [ships] (sort-by :name ships)))
; now, try adding a new spaceship...
app:reagent-demo.core=> (swap! state update :spaceships conj {:id 123 :name "My Very Own Spaceship"})
```

### Next Steps

Check out the `reverse-ships-button` and `new-ship` branches for more advanced use-cases!

## Building for production

```
lein clean
lein package
```
