#ifndef KVLIST_H_
#define KVLIST_H_

namespace sict {

	template<class ElemType>
	class KVList {
		ElemType *elements;
		size_t elements_max, elements_count;
	public:
		KVList() : elements(nullptr), elements_max(0), elements_count(0) {}
		KVList(size_t elements_max_) : KVList() {
			elements_max = elements_max_;
			if (elements_max_ > 0) {
				elements = new ElemType[elements_max_];
			}
		}
		KVList(KVList &&src) {
			elements = src.elements;
			elements_max = src.elements_max;
			elements_count = src.elements_count;
			src.elements = nullptr;
			src.elements_max = src.elements_count = 0;
		}
		KVList(const KVList &) = delete;
		~KVList() {
			if (elements != nullptr) {
				delete[] elements;
				elements = nullptr;
				elements_max = elements_count = 0;
			}
		}
		KVList &operator=(const KVList &) = delete;
		KVList &operator=(KVList &&) = delete;
		const ElemType &operator[](size_t index) const {
			return elements[index];
		}
		template<typename ConvertFn>
		void display(std::ostream& out, ConvertFn convert) const {
			for (size_t i = 0; i < elements_count; ++i) {
				elements[i].display(out, convert);
			}
		}
		void push_back(const ElemType &element) {
			if (elements_count < elements_max) {
				elements[elements_count++] = element;
			}
		}
	};

}  // namespace sict

#endif  // KVLIST_H_
