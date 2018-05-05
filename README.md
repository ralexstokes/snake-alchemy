# snake-alchemy

This tool converts valid [Serpent LLL](https://github.com/ethereum/serpent) into valid [Vyper LLL](https://github.com/ethereum/vyper).

Conversion is not total, in the sense that there could be valid Serpent expressions that this tool does not recognize as needing to convert for Vyper. If you find anything, please open an issue.

## Usage

Using [leiningen](https://leiningen.org/):

`lein run $FILENAME`

where `$FILENAME` is on the name of the file; this file must be in the classpath.

## License

See `LICENSE.md`.
