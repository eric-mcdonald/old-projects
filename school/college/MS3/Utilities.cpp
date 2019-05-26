#include "Utilities.h"

namespace sict {

	char Utilities::delimiter = '\0';

	Utilities::Utilities() : field_width(0) {}
	const std::string Utilities::extractToken(const std::string &str, size_t &next_pos) {
		const size_t prev_pos = next_pos;
		next_pos = str.find_first_of(delimiter, next_pos + 1);
		if (next_pos == std::string::npos) {
			next_pos = str.length() - 1;
			return prev_pos + 1 < str.length() ? str.substr(prev_pos + 1) : str;
		}
		size_t pos = str.substr(prev_pos + 1).find_first_of(delimiter) + (prev_pos + 1);
		if (pos == std::string::npos) {
			return str;
		}
		else if (pos == next_pos + 1) {
			throw str;
		}
		std::string token = str.substr(prev_pos)[0] == delimiter ? str.substr(prev_pos + 1, next_pos - (prev_pos + 1)) : str.substr(prev_pos, next_pos - prev_pos);
		if (field_width < token.length()) {
			field_width = token.length();
		}
		next_pos = pos;
		return token;
	}
	const char Utilities::getDelimiter() {
		return delimiter;
	}
	void Utilities::setDelimiter(const char delimiter_) {
		delimiter = delimiter_;
	}
	size_t Utilities::getFieldWidth() const {
		return field_width;
	}
	void Utilities::setFieldWidth(size_t field_width_) {
		field_width = field_width_;
	}

}  // namespace sict
