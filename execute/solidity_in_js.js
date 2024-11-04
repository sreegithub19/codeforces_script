const solc = require('solc');

// Define your Solidity contract as a string with SPDX license identifier
const source = `
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HelloWorld {
    string public greeting;

    constructor() {
        greeting = "Hello, World!";
    }

    function setGreeting(string memory _greeting) public {
        greeting = _greeting;
    }

    function getGreeting() public view returns (string memory) {
        return greeting;
    }
}
`;

// Prepare the input for the compiler
const input = {
    language: 'Solidity',
    sources: {
        'HelloWorld.sol': {
            content: source,
        },
    },
    settings: {
        outputSelection: {
            '*': {
                '*': ['*'], // Select all outputs (bytecode, ABI, etc.)
            },
        },
    },
};

// Compile the contract
const output = JSON.parse(solc.compile(JSON.stringify(input)));

// Check for compilation errors
if (output.errors) {
    output.errors.forEach(err => {
        console.error('Error:', err.formattedMessage);
    });
    process.exit(1);
}

// Ensure contracts are present in the output
if (output.contracts && output.contracts['HelloWorld.sol']) {
    const contract = output.contracts['HelloWorld.sol'].HelloWorld;

    // Output only the ABI
    console.log('ABI:', JSON.stringify(contract.abi, null, 2));
    
    // Directly output the initial greeting
    console.log('Greeting: Hello, World!');
} else {
    console.error('No contracts found in the output.');
}