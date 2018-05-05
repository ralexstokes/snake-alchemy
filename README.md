# snake-alchemy

This tool converts valid [Serpent LLL](https://github.com/ethereum/serpent) into valid [Vyper LLL](https://github.com/ethereum/vyper).

Conversion is not total, in the sense that there could be valid Serpent expressions that this tool does not recognize as needing to convert for Vyper. If you find anything, please open an issue.

## Usage

Using [leiningen](https://leiningen.org/):

`lein run $FILENAME`

where `$FILENAME` is the name of the file to convert; this file must be on the classpath.

For example,

`lein run purity_checker.se.lll`

will convert the Serpent LLL in `purity_checker.se.lll` to Vyper LLL and pretty print this code to STDOUT.

## License

See `LICENSE`.
