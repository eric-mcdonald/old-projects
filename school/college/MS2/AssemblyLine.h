#ifndef SICT_ASSEMBLY_LINE_H
#define SICT_ASSEMBLY_LINE_H
// Assembly Line Project
// AssemblyLine.h
// Elliott Coleshill, Chris Szalwinski
// 2019/02/17

#include <iostream>
#include <vector>
#include "ItemSet.h"
#include "CustomerOrder.h"

namespace sict {
	class AssemblyLine {
		std::vector<ItemSet> inventory;
		std::vector<CustomerOrder>orders;
		const char* fInventory{ nullptr };
		const char* fOrders{ nullptr };
	public:
		AssemblyLine(char* filename[], int nfiles);
		void loadInventory(std::ostream& os);
		void loadOrders(std::ostream& os);
	};
}
#endif