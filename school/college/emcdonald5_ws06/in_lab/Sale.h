#ifndef SALE_H_
#define SALE_H_

#include <iostream>
#include <vector>

#include "iProduct.h"

namespace sict {

	class Sale {
		std::vector<iProduct*> products;
	public:
		Sale(const char *);
		~Sale();
		void display(std::ostream &) const;
	};

}  // namespace sict

#endif  // SALE_H_
