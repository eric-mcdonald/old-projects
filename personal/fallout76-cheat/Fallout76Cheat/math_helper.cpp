#include "stdafx.h"

#include "math_helper.h"

namespace cheat {

	template<>
	float ClampDegrees(float angle, float max) {
		if (isnan(angle)) {
			angle = 0.0F;
		}
		while (angle > max) {
			angle -= max * 2.0F;
		}
		while (angle < -max) {
			angle += max * 2.0F;
		}
		if (angle > max) {
			angle = max;
		}
		if (angle < -max) {
			angle = -max;
		}
		return angle;
	}

}  // namespace cheat
