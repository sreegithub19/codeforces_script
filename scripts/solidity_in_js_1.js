const { ethers } = require("hardhat");
const solc = require("solc");

async function main() {
    // Define the Solidity contract as a string
    const sourceCode = `
    // SPDX-License-Identifier: MIT
    pragma solidity ^0.8.0;

    contract HelloWorld {
        string public message;

        // Event declaration
        event MessageUpdated(string newMessage);

        constructor() {
            message = "Hello, Solidity World in solidity_in_js_1.js!";
            emit MessageUpdated(message); // Emit the event
        }
    }`;

    // Compile the contract
    const input = {
        language: 'Solidity',
        sources: {
            'HelloWorld': {
                content: sourceCode,
            },
        },
        settings: {
            outputSelection: {
                '*': {
                    '*': ['*'],
                },
            },
        },
    };

    const output = JSON.parse(solc.compile(JSON.stringify(input)));

    // Extract the compiled contract's bytecode and ABI
    const contractData = output.contracts['HelloWorld']['HelloWorld'];
    const abi = contractData.abi;
    const bytecode = contractData.evm.bytecode.object;

    // Get a signer
    const [deployer] = await ethers.getSigners();

    // Get the contract factory using the ABI and bytecode
    const HelloWorldFactory = new ethers.ContractFactory(abi, bytecode, deployer);

    // Deploy the contract
    const helloWorld = await HelloWorldFactory.deploy();

    // Wait for the deployment to finish
    //await helloWorld.deployed();

    console.log("HelloWorld deployed to:", helloWorld.address);

    // Get the message from the contract
    const message = await helloWorld.message();
    console.log("Message from contract in deploy in solidity_in_js_1:", message);
}

// Execute the deployment script
main()
    .then(() => process.exit(0))
    .catch((error) => {
        console.error(error);
        process.exit(1);
    });