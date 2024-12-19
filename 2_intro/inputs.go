package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	fmt.Println("Reading input from file:")
	file, err := os.Open("input.txt")
	if err != nil {
		fmt.Println("Error opening file:", err)
		return
	}
	defer file.Close()

	line, _ := bufio.NewReader(file).ReadString('\n')
	fmt.Printf("Input from file: %s\n", line)
}
