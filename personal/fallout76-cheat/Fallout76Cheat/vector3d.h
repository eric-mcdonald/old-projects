#pragma once

#include "vector2d.h"

namespace cheat {

	template<class Value>
	class Vector3d : public Vector2d<Value> {
		Value z;
	public:
		Vector3d() : Vector2d(), z(0) {}
		Vector3d(Value x_, Value y_, Value z_) : Vector2d<Value>(x_, y_), z(z_) {}
		virtual Value CalcLength() const override {
			return sqrt(get_x() * get_x() + get_y() * get_y() + z * z);
		}
		virtual void Normalize() override {
			Vector2d<Value>::Normalize();
			Value length = CalcLength();
			if (length != 0) {
				z /= length;
			}
		}
		bool operator==(const Vector3d &rhs) const {
			return *reinterpret_cast<Vector2d<Value>*>(this) == reinterpret_cast<Vector2d<Value>&>(rhs) && z == rhs.z;
		}
		bool operator!=(const Vector3d &rhs) const {
			return !(*this == rhs);
		}
		Vector3d &operator+=(const Vector3d &rhs) {
			*reinterpret_cast<Vector2d<Value>*>(this) += reinterpret_cast<Vector2d<Value>&>(rhs);
			z += rhs.z;
			return *this;
		}
		Vector3d &operator-=(const Vector3d &rhs) {
			*reinterpret_cast<Vector2d<Value>*>(this) -= reinterpret_cast<Vector2d<Value>&>(rhs);
			z -= rhs.z;
			return *this;
		}
		Vector3d &operator*=(const Vector3d &rhs) {
			*reinterpret_cast<Vector2d<Value>*>(this) *= reinterpret_cast<Vector2d<Value>&>(rhs);
			z *= rhs.z;
			return *this;
		}
		Vector3d &operator/=(const Vector3d &rhs) {
			*reinterpret_cast<Vector2d<Value>*>(this) /= reinterpret_cast<Vector2d<Value>&>(rhs);
			z /= rhs.z;
			return *this;
		}
		Vector3d operator-() const {
			return Vector3d(-get_x(), -get_y(), -z);
		}
		Vector3d operator-(const Vector3d &rhs) const {
			return Vector3d(get_x() - rhs.get_x(), get_y() - rhs.get_y(), z - rhs.z);
		}
		Vector3d operator+(const Vector3d &rhs) const {
			return Vector3d(get_x() + rhs.get_x(), get_y() + rhs.get_y(), z + rhs.z);
		}
		Vector3d operator*(const Vector3d &rhs) const {
			return Vector3d(get_x() * rhs.get_x(), get_y() * rhs.get_y(), z * rhs.z);
		}
		Vector3d operator/(const Vector3d &rhs) const {
			return Vector3d(get_x() / rhs.get_x(), get_y() / rhs.get_y(), z / rhs.z);
		}
		Value get_z() const {
			return z;
		}
		void set_z(const Value &z_) {
			z = z_;
		}
	};

}  // namespace cheat
