// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HelloWorld {
    string public message;

    // Event declaration
    event MessageUpdated(string newMessage);

    constructor() {
        message = "Hello, Solidity World!";
        emit MessageUpdated(message); // Emit the event
    }
}
