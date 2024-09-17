/////////////////////////////////////// 1. Look for changes here  /////////////////////////////////////////

const readline = require('readline');
const path = require('path');

console.log(`================================ ${path.parse(path.basename(__filename))["name"]} ================================`)

let fs = require('fs');
const inputFilePath = `input/input_${path.parse(path.basename(__filename))["name"]}_.txt`;
const outputFilePath = `output/output_${path.parse(path.basename(__filename))["name"]}_.txt`;


//////////////////////////////////////// 2. Problem-dependent and language-dependent code  /////////////////////////////////////////



const { spawn, exec } = require('child_process');

// Define the Python command and arguments
const pythonCommand = 'python3'; // Use 'python' if needed
const pythonArgs = ['-c', `
with open('${inputFilePath}', 'r') as infile:
    input_content = infile.read().strip()

lines = input_content.splitlines()

import sys

def find(u):
	if un[u] != u:
		un[u] = find(un[u])
	return un[u]

def union(u, v):
	un[find(u)] = find(v)

def dfs(pre, u, stack): #union vertexes in a cycle to a single one
	for v in map0[u]:
		if v == pre:
			continue
		if v in visited:
			while stack and visited[stack[-1]] > visited[v]:
				w = stack.pop()
				union(v, w)
		else:
			stack.append(v)
			visited[v] = visited[u] + 1
			dfs(u, v, stack)
			if stack[-1] == v:
				stack.pop()

#c0 is the max number of legs if previous vertexes are merged to a caterpillar of length 1
#d0 is (the max total vertexes if previous vertexes are merged to half a caterpillar) - c0
def dfs2(pre, u):
	visited.add(u)
	legs[-1] += max(0, len(map1[u]) - 2)
	m1, m2 = 0, 0
	for v in map1[u]:
		if v == pre:
			continue
		d1 = dfs2(u, v)
		if d1 > m1:
			m2 = m1
			m1 = d1
		elif d1 > m2:
			m2 = d1
	lens[-1] = max(lens[-1], m1 + m2 + 1)
	return m1 + 1

#a cycle anywhere must be merged to a single vertex
#paths not connected with main path can be merged to a caterpillar of length 1
sys.setrecursionlimit(2000) #cf default might be 1000
#input = sys.stdin.buffer.readline
n, m = map(int, lines[0].split())
un = list(range(n + 1))
map0 = [[] for _ in range(n + 1)]
for z in range(m):
	u, v = map(int, lines[z+1].split())
	map0[u].append(v)
	map0[v].append(u)
visited = {}
for i in range(1, n + 1):
	if i not in visited:
		visited[i] = 0
		dfs(-1, i, [i])
for i in range(1, n + 1): #root of every group
	un[i] = find(i)
map1 = [set() for _ in range(n + 1)] #edges in the simplified graph without cycle
for u in range(1, n + 1):
	for v in map0[u]:
		map1[un[u]].add(un[v])
for i in range(1, n + 1):
	if i in map1[i]:
		map1[i].remove(i)
lens = [] #max length of main path of every caterpillar
legs = [] #legs of every caterpillar
visited = set()
for i in range(1, n + 1):
	if un[i] == i and i not in visited: #vertex left in map1
		lens.append(0)
		legs.append(0)
		dfs2(-1, i)
print(str(n - sum(lens) - sum(legs) + len(lens) - 1)) #join caterpillars head to tail

`];


  ///////////////////////////////////////// 3. Fixed code /////////////////////////////////////////////

// Spawn the Python process
const pythonProcess = spawn(pythonCommand, pythonArgs);

// Initialize a variable to store the output
let output = '';

// Handle standard output
pythonProcess.stdout.on('data', (data) => {
  output += data.toString(); // Append data to the output variable
});

// Handle standard error
pythonProcess.stderr.on('data', (data) => {
  console.error(`Python Error: ${data.toString().trim()}`);
});

// Handle errors related to spawning the process
pythonProcess.on('error', (error) => {
  console.error(`Error executing Python command: ${error}`);
  process.exit(1);
});

// Handle process exit
pythonProcess.on('close', (code) => {
  if (code !== 0) {
    console.error(`Python process exited with code ${code}`);
    process.exit(code);
  }

  // Trim any extra whitespace or newlines from the output
  const trimmedOutput = output.trim();
  console.log(`Output of python code is :${trimmedOutput}`);



let result1 = trimmedOutput;
// Read output.txt and compare directly in one line
fs.readFile(outputFilePath, 'utf8', (err, data) => {
    if (err) {
        console.error('Error reading output file:', err);
        return;
    }
    const isMatch = result1.trim() === data.trim();
    if(isMatch)console.log(true);

    else{
        console.error("Wrong!!");
        process.exit(1);
    }
    });


});


//////////////////////////////////////////////////////////////////////////////////