#include "pch.h"

BOOL APIENTRY DllMain(HMODULE /* hModule */, DWORD ul_reason_for_call, LPVOID /* lpReserved */)
{
	std::fstream test_file;
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
		test_file.open("D:/lanius/inject_test/success.txt", std::ios::out);
		if (test_file.is_open()) {
			test_file << "This file exists only if the DLL was injected successfully." << std::endl;
			test_file.close();
		}
		break;
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}
