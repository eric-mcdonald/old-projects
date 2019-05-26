// Assembly Line Project
// AssemblyLine.cpp
// Elliott Coleshill, Chris Szalwinski
// 2019/02/17

#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include "Utilities.h"
#include "LineManager.h"
#include "AssemblyLine.h"

namespace sict {
	// index for manual validation
	//
	const int T{ 0 }; 

	// constructor stores file addresses
	//
	AssemblyLine::AssemblyLine(char* filename[], int nFiles) :
		fInventory(nFiles > 0 ? filename[0] : nullptr),
		fOrders(nFiles > 1 ? filename[1] : nullptr),
		fLine(nFiles > 2 ? filename[2] : nullptr) {
		if (nFiles < 1) throw std::string("*** Insufficient command line arguments ***\n");
	}

	// load inventory and validate it
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
		os << "\nFor Manual Validation: Item " << T + 1 << std::endl;
		os << " getName(): " << inventory[T].getName() << std::endl;
		os << " getSerialNumber(): " << inventory[T].getSerialNumber() << std::endl;
		os << " getQuantity(): " << inventory[T].getQuantity() << std::endl;
		--inventory[T];
		os << " getSerialNumber(): " << inventory[T].getSerialNumber() << std::endl;
		os << " getQuantity(): " << inventory[T].getQuantity() << std::endl;
	}

	// load customer orders and validats them
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

	// configure the assembly line and validate the configuration
	//
	void AssemblyLine::configureLine(std::ostream& os)
	{
		std::ifstream file;
		std::string record;

		// build the stations
		file.open(fInventory);
		if (!file) throw std::string("*** Unable to open inventory file ") + std::string(fInventory);
		while (!file.eof()) {
			std::getline(file, record);
			line.push_back(std::move(new Station(record)));
		}
		file.clear();
		file.close();
		os << "Items in Stock\n";
		os << "--------------\n";
		for (size_t i = 0; i < line.size(); i++)
			line[i]->display(os);

		// verify all the functionality of station T
		os << "\nFor Manual Validation: Station " << T + 1 << std::endl;
		line[T]->validate(os);
		--(*line[T]);
		line[T]->validate(os);
		os << std::endl;

		// load the Customer Orders
		file.open(fOrders);
		if (!file) throw std::string("**** Unable to open Customer Orders file ") + std::string(fOrders);
		while (!file.eof()) {
			std::getline(file, record);
			orders.push_back(std::move(CustomerOrder(record)));
		}
		file.close();

		os << "Customer Orders\n";
		os << "---------------\n";
		for (size_t i = 0; i < orders.size(); i++)
			orders[i].display(os);

		// configure the Assembly Line
		file.open(fLine);
		if (!file) throw std::string("*** Unable to open Assembly Line file ") + std::string(fLine);

		Utilities myUtil;
		record = "";
		std::string host = "";
		std::string next = "";
		size_t next_pos;
		nextStation.resize(line.size());
		std::fill(nextStation.begin(), nextStation.end(), line.size());
		while (!file.eof()) {
			next_pos = 0;
			std::getline(file, record);
			host = myUtil.extractToken(record, next_pos);
			if (next_pos != std::string::npos)
				next = myUtil.extractToken(record, next_pos);
			else
				next = "";

			//Loop through the line vector space and set the information
			auto it_x = std::find_if(line.begin(), line.end(), [&](Station* si) -> bool { return si->getName() == host; });
			auto it_y = std::find_if(line.begin(), line.end(), [&](Station* si) -> bool { return si->getName() == next; });
			if (it_x != line.end() && it_y != line.end()) {
				nextStation[it_x - line.begin()] = it_y - line.begin();
			}
		}
		file.close();

		os << std::endl;
		os << "Assembly Line Configuration\n";
		os << "---------------------------\n";
		for (size_t x = 0; x < line.size(); x++) {
			os << line[x]->getName() << " --> " 
			 << (nextStation[x] < line.size() ? line[nextStation[x]]->getName() : "END OF LINE ")
			 << std::endl;
		}

		// determine start index for longest path
		firstStation = 0;
		unsigned kmax = 0;
		for (size_t i = 0; i < line.size(); ++i) {
			size_t j = i;
			size_t k = 0;
			while (nextStation[j] < line.size()) {
				j = nextStation[j];
				k++;
			}
			if (k > kmax) firstStation = i;
		}

		// verify longest path
		os << "\nFor Manual Validation:" << std::endl;
		size_t i = firstStation;
		while (nextStation[i] != line.size()) {
			os << ' ';
			os << line[i]->getName() << " --> " << line[nextStation[i]]->getName() << std::endl;
			i = nextStation[i];
		}
		os << ' ';
		os << line[i]->getName() << " --> END OF LINE " << std::endl;
		os << std::endl;
	}

	// run the orders along the assembly line and displays the results
	//
	void AssemblyLine::run(std::ostream& os) {

		configureLine(os);
		os << "Start Processing Customer Orders\n";
		os << "--------------------------------\n";
		LineManager lm(line, nextStation, orders, firstStation, os);
		while (!lm.run(os));
		os << "\nResults of Processing Customer Orders\n";
		os << "-------------------------------------\n";
		lm.display(os);
	}
}