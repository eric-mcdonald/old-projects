#include <string>

namespace sict {

	class Text {
		std::string *strings;
		size_t strings_sz;
	public:
		Text();
		Text(const char *);
		Text(const Text &);
		~Text();
		Text &operator=(const Text &);
		size_t size() const;
	};

}  // namespace sict
