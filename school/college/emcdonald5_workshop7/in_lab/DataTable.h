#ifndef SICT_DATA_TABLE_H
#define SICT_DATA_TABLE_H

#include <iostream>
#include <iomanip>
#include <vector>
#include <cmath>

namespace sict {
	
	static constexpr std::streamsize kFieldWidth = 8, kNumPrecision = 4;

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
			CoordType y_mean = 0;
			for (auto &it : coords) {
				y_mean += it.second;
			}
			y_mean /= static_cast<float>(coords.size());
			std::vector<CoordType> sigma_nums;
			for (auto &it : coords) {
				sigma_nums.push_back((it.second - y_mean) * (it.second - y_mean));
			}
			CoordType y_sigma = 0;
			for (auto &it : sigma_nums) {
				y_sigma += it;
			}
			y_sigma /= static_cast<float>(sigma_nums.size() - 1);
			y_sigma = sqrt(y_sigma);
			out << std::fixed << std::setprecision(kNumPrecision) << "  y mean    = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << y_mean << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
			out << std::fixed << std::setprecision(kNumPrecision) << "  y sigma   = " << std::setfill(' ') << std::right << std::setw(kFieldWidth) << y_sigma << std::setw(0) << std::setprecision(0) << std::defaultfloat << std::endl;
		}
	};

}  // namespace sict

#endif  // SICT_DATA_TABLE_H
