module Main exposing (..)

import Browser
import Html exposing (Html, text)

-- The main function to run the application
main : Program () () ()
main =
    Browser.sandbox
        { init = ()
        , update = \_ model -> model
        , view = \_ -> text "Hello, Elm World!"
        }
