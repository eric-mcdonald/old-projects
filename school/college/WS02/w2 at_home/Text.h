#include <string>

namespace sict {

	class Text {
		std::string *strings;
		size_t strings_sz;
	public:
		Text();
		Text(const char *);
		Text(const Text &);
		Text(Text &&);
		~Text();
		Text &operator=(const Text &);
		Text &operator=(Text &&);
		size_t size() const;
	};

}  // namespace sict
