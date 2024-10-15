module Main exposing (..)

import Browser
import Html exposing (Html, text, div, h1, p)

-- The main function to run the application
main : Program () () ()
main =
    Browser.sandbox
        { init = ()
        , update = \_ model -> model
        , view = view
        }

-- Define the view function to render HTML
view : () -> Html msg
view _ =
    div []
        [ h1 [] [ text "Hello, Elm World!" ]
        , p [] [ text "This is a simple Elm application rendering HTML." ]
        ]
