fork { exec '''

node -e `console.log("Hi from nested nodejs!")`

''' }

# ruby -e "
# puts(\"Hi\")
# "

# node -e `console.log("Hi")`