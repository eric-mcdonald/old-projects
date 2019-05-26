// lanius-test-target.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"


int main()
{
	char target_str[256];
	const char *original_target_str = "This is the target string in memory.";
	strcpy_s(target_str, sizeof(target_str), original_target_str);
	std::cout << reinterpret_cast<void*>(target_str) << std::endl;
	while (!strcmp(target_str, original_target_str)) {
		std::cout << "Old: " << target_str << std::endl;
		Sleep(1000);
	}
	std::cout << "New: " << target_str << std::endl;
	getchar();
    return 0;
}

