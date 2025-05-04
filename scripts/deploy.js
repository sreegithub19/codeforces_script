async function main() {
    // Get the contract factory
    const HelloWorld = await ethers.getContractFactory("HelloWorld");
    
    // Deploy the contract
    const helloWorld = await HelloWorld.deploy();
    
    // Wait for the deployment to finish (this is done during deploy)
    // The contract instance is ready for use after this line

    console.log("HelloWorld deployed to:", helloWorld.address);

    // Get the message from the contract
    const message = await helloWorld.message();
    console.log("Message from contract in deploy:", message);
}

// Execute the deployment script
main()
    .then(() => process.exit(0))
    .catch((error) => {
        console.error(error);
        process.exit(1);
    });
