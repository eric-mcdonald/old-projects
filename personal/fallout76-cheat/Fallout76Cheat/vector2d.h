#pragma once

namespace cheat {

	template<class Value>
	class Vector2d {
		Value x, y;
	public:
		Vector2d() : x(0), y(0) {}
		Vector2d(Value x_, Value y_) : x(x_), y(y_) {}
		virtual Value CalcLength() const {
			return static_cast<Value>(sqrt(x * x + y * y));
		}
		virtual void Normalize() {
			Value length = CalcLength();
			if (length != 0) {
				x /= length;
				y /= length;
			}
		}
		bool operator==(const Vector2d &rhs) const {
			return x == rhs.x && y == rhs.y;
		}
		bool operator!=(const Vector2d &rhs) const {
			return !(*this == rhs);
		}
		Vector2d &operator+=(const Vector2d &rhs) {
			x += rhs.x;
			y += rhs.y;
			return *this;
		}
		Vector2d &operator-=(const Vector2d &rhs) {
			x -= rhs.x;
			y -= rhs.y;
			return *this;
		}
		Vector2d &operator*=(const Vector2d &rhs) {
			x *= rhs.x;
			y *= rhs.y;
			return *this;
		}
		Vector2d &operator/=(const Vector2d &rhs) {
			x /= rhs.x;
			y /= rhs.y;
			return *this;
		}
		Vector2d operator-() const {
			return Vector2d(-x, -y);
		}
		Vector2d operator-(const Vector2d &rhs) const {
			return Vector2d(x - rhs.x, y - rhs.y);
		}
		Vector2d operator+(const Vector2d &rhs) const {
			return Vector2d(x + rhs.x, y + rhs.y);
		}
		Vector2d operator*(const Vector2d &rhs) const {
			return Vector2d(x * rhs.x, y * rhs.y);
		}
		Vector2d operator/(const Vector2d &rhs) const {
			return Vector2d(x / rhs.x, y / rhs.y);
		}
		Value get_x() const {
			return x;
		}
		Value get_y() const {
			return y;
		}
		void set_x(const Value &x_) {
			x = x_;
		}
		void set_y(const Value &y_) {
			y = y_;
		}
	};

}  // namespace cheat
