#ifndef SICT_DATA_TABLE_H
#define SICT_DATA_TABLE_H

#include <iostream>
#include <iomanip>
#include <vector>
#include <cmath>
#include <algorithm>
#include <numeric>

namespace sict {

	static constexpr std::streamsize kFieldWidth = 8, kNumPrecision = 4;

	bool SortCoordsByY(std::pair<float, float> coord1, std::pair<float, float> coord2) {
		return coord1.second < coord2.second;
	}
	float CalcSlope(const std::vector<float> &x_vals, const std::vector<float> &y_vals) {
		float vals_sz = static_cast<float>(x_vals.size());
		float x_mean = accumulate(x_vals.begin(), x_vals.end(), 0.0F) / vals_sz, y_mean = accumulate(y_vals.begin(), y_vals.end(), 0.0F) / vals_sz;
		float numerator = 0.0F, denominator = 0.0F;
		for (size_t i = 0; i < vals_sz; ++i) {
			numerator += (x_vals[i] - x_mean) * (y_vals[i] - y_mean);
			denominator += (x_vals[i] - x_mean) * (x_vals[i] - x_mean);
		}
		return numerator / denominator;
	}

	template<class CoordType>
	class DataTable {
		std::vector<std::pair<CoordType, CoordType>> coords;
	public:
		DataTable(std::ifstream &input) {
			while (input.good()) {
				CoordType x, y;
				input >> x;
				input >> y;
				coords.push_back(std::pair<CoordType, CoordType>(x, y));
			}
		}
		void displayData(std::ostream &out) const {
			out << "Data Values" << std::endl;
			out << "------------" << std::endl;
			out << "       x       y" << std::endl;
			for (auto &it : coords) {
				out << std::setfill(' ') << std::right << std::setw(kFieldWidth) << std::fixed << std::setprecision(kNumPrecision) << it.first << std::setw(0) << " " << std::setw(7) << it.second << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			}
		}
		void displayStatistics(std::ostream &out) const {
			out << "Statistics" << std::endl;
			out << "----------" << std::endl;
			CoordType x_mean = 0.0F, y_mean = 0.0F;
			for (auto &it : coords) {
				x_mean += it.first;
				y_mean += it.second;
			}
			x_mean /= static_cast<float>(coords.size());
			y_mean /= static_cast<float>(coords.size());
			CoordType y_sigma = 0.0F;
			for (auto &it : coords) {
				y_sigma += (it.second - y_mean) * (it.second - y_mean);
			}
			y_sigma /= static_cast<float>(coords.size() - 1);
			y_sigma = sqrt(y_sigma);
			std::vector<std::pair<CoordType, CoordType>> sorted_coords(coords);
			std::sort(sorted_coords.begin(), sorted_coords.end(), SortCoordsByY);
			std::vector<CoordType> x_vals, y_vals;
			for (auto &it : sorted_coords) {
				x_vals.push_back(it.first);
				y_vals.push_back(it.second);
			}
			CoordType slope = CalcSlope(x_vals, y_vals);
			out << std::fixed << std::setprecision(kNumPrecision) << "  y mean    = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << y_mean << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			out << std::fixed << std::setprecision(kNumPrecision) << "  y sigma   = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << y_sigma << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			out << std::fixed << std::setprecision(kNumPrecision) << "  y median  = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << sorted_coords[sorted_coords.size() / 2].second << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			out << std::fixed << std::setprecision(kNumPrecision) << "  slope     = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << slope << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			out << std::fixed << std::setprecision(kNumPrecision) << "  intercept = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << y_mean - slope * x_mean << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
		}
	};

}  // namespace sict

#endif  // SICT_DATA_TABLE_H
