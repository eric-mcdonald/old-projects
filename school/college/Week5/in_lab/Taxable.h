#ifndef TAXABLE_H_
#define TAXABLE_H_

namespace sict {

	class Taxable {
		float tax_rate;
	public:
		Taxable(float tax_rate_) : tax_rate(tax_rate_) {}
		float operator()(float price) {
			return price * (1.00f + tax_rate);
		}
	};

}  // namespace sict

#endif  // TAXABLE_H_
