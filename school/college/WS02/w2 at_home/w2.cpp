// Workshop 2 - Copy and Move Semantics
// w2.cpp
// Chris Szalwinski
// 2019/01/10

#include <iostream>
#include <iomanip>
#include <utility>
#include "Text.h"
#include "Timekeeper.h"
using namespace sict;

int main (int argc, char* argv[]) {
	std::cout << "Command Line : ";
	for (int i = 0; i < argc; i++) {
		std::cout << argv[i] << ' ';
	}
	if (argc == 1) {
        std::cerr << argv[0] << ": missing file operand\n";
        return 1;
	}
	else if (argc != 2) {
        std::cerr << argv[0] << ": too many arguments\n";
        return 2;
    }

	Timekeeper t;
    {
        std::cout << std::fixed << std::setprecision(3);
		t.start();
        Text a;
		t.stop();
		t.recordEvent("0-arg Constructor");
        std::cout << "0-arg Constructor - a.size = " << a.size() << std::endl;

		t.start();
		Text b(argv[1]);
		t.stop();
		t.recordEvent("1-arg Constructor");
		std::cout << "1-arg Constructor - b.size = " << b.size() << std::endl;

		t.start();
		Text c = b;
		t.stop();
		t.recordEvent("Copy Constructor ");
        std::cout << "Copy Constructor  - c.size = " << c.size() << std::endl;

		t.start();
		a = b;
		t.stop();
		t.recordEvent("Copy Assignment  ");
        std::cout << "Copy Assignment   - a.size = " << a.size() << std::endl;

		t.start();
		Text d = std::move(a);
		t.stop();
		t.recordEvent("Move Constructor ");
        std::cout << "Move Constructor  - d.size = " << d.size() << std::endl;

        a = std::move(d);
		t.stop();
		t.recordEvent("Move Assignment  ");
        std::cout << "Move Assignment   - a.size = " << a.size() << std::endl;

        t.start();
    }
    t.stop();
	t.recordEvent("Destructor       ");

	t.report(std::cout);
}