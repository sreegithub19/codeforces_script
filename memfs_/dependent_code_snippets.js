const { exec } = require('child_process');

// Python code snippets as strings
const addSnippet = `
def add(a, b):
    return a + b
result = add(5, 3)
print(result)
`;

const multiplySnippet = `
def multiply(a, b, constant):
    return (a + b) * constant
result = multiply(a, b, constant)
print(result)
`;

// Function to execute a Python snippet and return the result
function executePythonSnippet(pythonCode, callback) {
    exec(`python -c "${pythonCode}"`, (err, stdout, stderr) => {
        if (err) {
            console.error(`Error executing Python code: ${stderr}`);
            return;
        }
        callback(stdout.trim()); // Use callback to pass the result back
    });
}

// First, execute the addSnippet to get the result
executePythonSnippet(addSnippet, (addResult) => {
    console.log('Add result:', addResult);

    // Now use the result of addResult in the multiplySnippet
    const multiplySnippetWithAddResult = `
def multiply(a, b, constant):
    return (a + b) * constant
result = multiply(${addResult}, 3, 2)  # Using the add result here
print(result)
`;

    // Execute the multiply snippet with the result of the add function
    executePythonSnippet(multiplySnippetWithAddResult, (multiplyResult) => {
        console.log('Multiply result:', multiplyResult);
    });
});
