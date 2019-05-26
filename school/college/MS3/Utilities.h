#ifndef UTILITIES_H_
#define UTILITIES_H_

#include <string>

namespace sict {

	class Utilities {
		size_t field_width;
		static char delimiter;
	public:
		Utilities();
		const std::string extractToken(const std::string &, size_t &/*next_pos*/);
		static const char getDelimiter();
		static void setDelimiter(const char);
		size_t getFieldWidth() const;
		void setFieldWidth(size_t);
	};

}  // namespace sict

#endif  // UTILITIES_H_
