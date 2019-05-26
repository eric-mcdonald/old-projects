#ifndef IPRODUCT_H_
#define IPRODUCT_H_

#include <iostream>

namespace sict {

	class iProduct {
	public:
		virtual double price() const = 0;
		virtual void display(std::ostream &) const = 0;
	};

	std::ostream &operator<<(std::ostream &, const iProduct &);
	iProduct *readRecord(std::ifstream& file);

}  // namespace sict

#endif  // IPRODUCT_H_
