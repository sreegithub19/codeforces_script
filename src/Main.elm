module Main exposing (..)

import Browser
import Html exposing (Html, text)

main : Program () ()
main =
    Browser.sandbox { init = (), update = \_ model -> model, view = \_ -> text "Hello, Elm World!" }
