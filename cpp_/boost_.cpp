#include <boost/filesystem.hpp>
#include <boost/regex.hpp>
#include <iostream>
#include <string>

int main() {
    boost::filesystem::path p{"."};
    std::cout << "Current path for Boost file is: " << boost::filesystem::absolute(p) << std::endl;

    // Define a regular expression
    boost::regex expr{"\\d{3}-\\d{2}-\\d{4}"}; // Matches a pattern like "123-45-6789" (e.g., Social Security Number format)

    // Input string
    std::string input = "My SSN is 123-45-6789.";

    // Search for the pattern in the input string
    boost::smatch matches;
    if (boost::regex_search(input, matches, expr)) {
        std::cout << "Found a match: " << matches[0] << std::endl;
    } else {
        std::cout << "No match found." << std::endl;
    }

    return 0;
}
