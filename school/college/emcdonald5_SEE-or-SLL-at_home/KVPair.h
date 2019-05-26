#ifndef KVPAIR_H_
#define KVPAIR_H_

#include <iostream>

namespace sict {

	template<class Key, class Value>
	class KVPair {
		Key key;
		Value value;
	public:
		KVPair() : key(), value() {}
		KVPair(const Key &key_, const Value &value_) : key(key_), value(value_) {}
		template<typename ConvertFn>
		void display(std::ostream &out, ConvertFn convert) const {
			out << std::setfill(' ') << std::setw(11) << std::left << key << std::setw(0) << ":" << std::setw(11) << std::right << value << std::setw(0) << " " << std::setw(9) << convert(value) << std::setw(0) << std::endl;
		}
	};

}  // namespace sict

#endif  // KVPAIR_H_
