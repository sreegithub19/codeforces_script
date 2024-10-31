print("Hello, Lua World!")

-- Using io.popen with a multiline string
local command = [[
python3 -c '
print("Hello from Python!")
'
]]

local handle = io.popen(command)
local result = handle:read("*a")
handle:close()

print("Output from Python:")
print(result)

