#ifndef SICT_ELEMENT_H
#define SICT_ELEMENT_H
// Workshop 8 - Smart Pointers
// Element.h
// Chris Szalwinski from Cornel Barna
// 2019/03/17

#include <iomanip>
#include <string>
#include <fstream>

extern const int FWC;
extern const int FWD;
extern const int FWP;

namespace sict {

    struct Description {
        unsigned code;
        std::string desc;
        bool load(std::ifstream& f) {
            f >> code >> desc;
            return f.good();
        }

		void display(std::ostream& os) const {
            os << std::setw(FWC) << code
			   << std::setw(FWD) << desc
			   << std::endl;
        }
    };

    struct Price {
        unsigned code;
        double price;
        bool load(std::ifstream& f) {
            f >> code >> price;
            return f.good();
        }

		void display(std::ostream& os) const {
            os << std::setw(FWC) << code << std::setw(FWP)
            << price << std::endl;
        }
    };

    struct Product {
        std::string desc;
        double price;
		int m_id;
		static size_t idGenerator;
		// this variable is used to print trace messages from
		//     constructors/destructor
		static bool Trace;

		Product() {
			m_id = ++Product::idGenerator;
			if (Product::Trace)
				std::cout << "    DC [" << m_id << "]" << std::endl;
		}

        Product(const std::string& str, double p) {
			this->desc = str;
			this->price = p;
			m_id = ++Product::idGenerator;
			if (Product::Trace)
				std::cout << "     C [" << m_id << "]" << std::endl;
		}

		Product(const Product& other) {
			this->desc = other.desc;
			this->price = other.price;
			m_id = ++Product::idGenerator;
			if (Product::Trace)
				std::cout << "    CC [" << m_id
			          << "] from [" << other.m_id << "]"
			          << std::endl;
		}

		~Product() {
			if (Product::Trace)
				std::cout << "    ~D [" << m_id << "]" << std::endl;
		}

        void display(std::ostream& os) const {
            os << std::setw(FWD) << desc
			   << std::setw(FWP) << price
			   << std::endl;
        }
		void AssertState() {
			if (price < 0.0) {
				throw std::string("*** Negative prices are invalid ***");  // Eric McDonald: Constructs the string explicitly to be caught by the program's inner catch block.
			}
		}
    };
}
#endif