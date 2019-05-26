#pragma once

#include <cmath>

namespace cheat {

	template<class Value>
	inline Value LimitBetween(Value current, Value min, Value max) {
		if (current > max) {
			current = max;
		}
		if (current < min) {
			current = min;
		}
		return current;
	}
	template<class Value>
	Value ClampDegrees(Value angle, Value max) {
		return ((angle % max) + max) % max;
	}
	template<>
	float ClampDegrees(float /*angle*/, float /*max*/);

}  // namespace cheat
