// Assembly Line Project
// AssemblyLine.cpp
// Elliott Coleshill, Chris Szalwinski
// 2019/02/17

#include <iostream>
#include <fstream>
#include <string>
#include "Utilities.h"
#include "AssemblyLine.h"

namespace sict {
	const int T{ 0 }; // index for manual validation

	// constructor stores file addresses
	//
	AssemblyLine::AssemblyLine(char* filename[], int nFiles) :
		fInventory(nFiles > 0 ? filename[0] : nullptr),
		fOrders(nFiles > 1 ? filename[1] : nullptr) {
		if (nFiles < 1) throw std::string("*** Insufficient command line arguments ***\n");
	}

	// loads inventory and validates it
	//
	void AssemblyLine::loadInventory(std::ostream& os)
	{
		std::ifstream file(fInventory);
		if (!file) throw std::string("*** Unable to open Inventory file ") + std::string(fInventory);
		std::string record;
		while (!file.eof()) {
			std::getline(file, record);
			inventory.push_back(std::move(ItemSet(record)));
		}
		file.close();
		os << "Items in Stock\n";
		os << "--------------\n";
		for (size_t i = 0; i < inventory.size(); i++)
			inventory[i].display(os, true);

		// Verify that all the functionality of inventory item T is working
		os << "\nFor Manual Validation: Item " << T+1 << std::endl;
		os << " getName(): " << inventory[T].getName() << std::endl;
		os << " getSerialNumber(): " << inventory[T].getSerialNumber() << std::endl;
		os << " getQuantity(): " << inventory[T].getQuantity() << std::endl;
		--inventory[T];
		os << " getSerialNumber(): " << inventory[T].getSerialNumber() << std::endl;
		os << " getQuantity(): " << inventory[T].getQuantity() << std::endl;
	}

	// loads customer orders and validates them
	//
	void AssemblyLine::loadOrders(std::ostream& os)
	{
		if (inventory.empty()) loadInventory(os);
		std::ifstream file(fOrders);
		if (!file) throw std::string("*** Unable to open Customer Orders file ") + std::string(fOrders);
		std::string record;
		while (!file.eof()) {
			std::getline(file, record);
			orders.push_back(std::move(CustomerOrder(record)));
		}
		file.close();
		os << "\nCustomer Orders\n";
		os << "---------------\n";
		for (size_t i = 0; i < orders.size(); i++)
			orders[i].display(os);

		os << "\nFor Manual Validation" << std::endl;
		orders[orders.size() - 1].display(os);
		CustomerOrder tmp(std::move(orders[orders.size() - 1]));
		orders.pop_back();
		tmp.display(os);
		os << std::endl;

		std::string strRecord = "Chloe|Flight PC|CPU|GPU|Power Supply";
		CustomerOrder tmp2(strRecord);
		tmp2.display(os);
		tmp2 = std::move(orders[orders.size() - 1]);
		orders.pop_back();
		tmp2.display(os);

		os << "\nFor Manual Validation Filling" << std::endl;
		tmp2.display(os);
		os << " isFilled(): " << (tmp2.isFilled() ? "true" : "false") << std::endl;
		tmp2.fillItem(inventory[T], os);
		os << " isFilled(): " << (tmp2.isFilled() ? "true" : "false") << std::endl;

		for (size_t i = 0; i < inventory.size(); i++)
			tmp2.fillItem(inventory[i], os);
		os << " isFilled(): " << (tmp2.isFilled() ? "true" : "false") << std::endl;
	}
}