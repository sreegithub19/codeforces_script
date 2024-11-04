const hre = require("hardhat");

async function main() {
    // Deploy the HelloWorld contract
    const HelloWorld = await hre.ethers.getContractFactory("HelloWorld");
    const helloWorld = await HelloWorld.deploy(); // Wait for deployment

    await helloWorld.deployTransaction.wait(); // Wait for the transaction to be mined

    console.log("HelloWorld deployed to:", helloWorld.address);

    // Fetch and log the message
    const message = await helloWorld.message();
    console.log("Message from contract:", message);
}

main()
    .then(() => process.exit(0))
    .catch((error) => {
        console.error(error);
        process.exit(1);
    });
