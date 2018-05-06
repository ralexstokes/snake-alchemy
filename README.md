# snake-alchemy

This tool converts valid [Serpent LLL](https://github.com/ethereum/serpent) into valid [Vyper LLL](https://github.com/ethereum/vyper).

Conversion is not total, in the sense that there could be valid Serpent expressions that this tool does not recognize as needing to convert for Vyper. If you find anything missing, please open an issue.

## Overview

The general idea is to supply some Serpent LLL, perhaps generated by the Serpent compiler, to this tool. Given that Serpent LLL is Lisp, we can reuse the Clojure reader to handle parsing. Once the stringly-typed code has been `read`, we can apply a series of transformations to get a valid Vyper LLL AST.

The list of transformations performed by this library include:

- Suppress 'quoted forms'. `(set 'addr (calldataload)) ~> (set addr (calldataload))`

- Convert the 'unless' form into an if form. `(unless p c) ~> (if (not p) c)`

- Convert the 'get' form into a direct reference (to an enclosing with expression). `(get 'foo) ~> foo`

- Convert the 'alloc' form into its implementation from the Serpent compiler. `(alloc expr) ~> (with start (msize) (seq (mstore (+ start expr) 0) start))`

Once we have the valid Vyper LLL AST we can pretty-print it to a file for use in other libraries. There is an option to pretty-print the AST as embedded Python as for now there is not a straightforward way for Vyper to read its LLL from e.g. a source file.

## Usage

Using [leiningen](https://leiningen.org/):

`lein run $FILENAME`

where `$FILENAME` is the name of the file to convert; this file must be on the classpath.

For example,

`lein run purity_checker.se.lll`

will convert the Serpent LLL in `purity_checker.se.lll` to Vyper LLL and pretty print this code to STDOUT.

## License

See `LICENSE`.
