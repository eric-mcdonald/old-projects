// lanius-test-patcher.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#define SIOCTL_TYPE 40000
#define IOCTL_READ_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2048, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_WRITE_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2049, METHOD_BUFFERED, FILE_ANY_ACCESS)

struct MemoryIoRequest {
	unsigned int proc_id;
	void *base_addr;
	void *buffer;
	size_t buffer_sz;
};

int main()
{
	HANDLE device_file = CreateFile(L"\\\\.\\LaniusDevice", GENERIC_READ | GENERIC_WRITE, FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (device_file == INVALID_HANDLE_VALUE) {
		return GetLastError();
	}
	static const unsigned int kTargetId = 3532;
	static void *kTargetAddr = reinterpret_cast<void*>(0x643EF6F4A0);
	static const size_t kBufferSz = 256;
	static const char *kToWrite = "New test string.";
	MemoryIoRequest *read_req = new MemoryIoRequest();
	read_req->proc_id = kTargetId;
	read_req->base_addr = kTargetAddr;
	read_req->buffer = new char[kBufferSz];
	read_req->buffer_sz = kBufferSz;
	if (!DeviceIoControl(device_file, IOCTL_READ_PROC_MEM, read_req, sizeof(MemoryIoRequest), NULL, 0, NULL, NULL)) {
		CloseHandle(device_file);
		if (read_req->buffer) {
			delete[] read_req->buffer;
		}
		delete read_req;
		return GetLastError();
	}
	std::cout << "Read: " << reinterpret_cast<const char*>(read_req->buffer) << std::endl;
	MemoryIoRequest *write_req = new MemoryIoRequest();
	write_req->proc_id = kTargetId;
	write_req->base_addr = kTargetAddr;
	write_req->buffer = new char[kBufferSz];
	memcpy(write_req->buffer, kToWrite, strlen(kToWrite) + 1);
	write_req->buffer_sz = kBufferSz;
	if (!DeviceIoControl(device_file, IOCTL_WRITE_PROC_MEM, write_req, sizeof(MemoryIoRequest), NULL, 0, NULL, NULL)) {
		CloseHandle(device_file);
		if (read_req->buffer) {
			delete[] read_req->buffer;
		}
		if (write_req->buffer) {
			delete[] write_req->buffer;
		}
		delete read_req;
		delete write_req;
		return GetLastError();
	}
	std::cout << "Write: " << reinterpret_cast<const char*>(write_req->buffer) << std::endl;
	CloseHandle(device_file);
	getchar();
	if (read_req->buffer) {
		delete[] read_req->buffer;
	}
	if (write_req->buffer) {
		delete[] write_req->buffer;
	}
	delete read_req;
	delete write_req;
    return 0;
}

