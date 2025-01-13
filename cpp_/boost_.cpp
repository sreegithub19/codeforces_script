#include <boost/filesystem.hpp>
#include <iostream>

int main() {
    boost::filesystem::path p{"."};
    std::cout << "Current path for Boost file is: " << boost::filesystem::absolute(p) << std::endl;
    return 0;
}
