#pragma warning(disable : 4100)
#pragma warning(disable : 4055)
#pragma warning(disable : 4311)
#pragma warning(disable : 4201)
#pragma warning(disable : 4152)

#include "driver.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct _PEB_LDR_DATA {
	BYTE       Reserved1[8];
	PVOID      Reserved2[3];
	LIST_ENTRY InMemoryOrderModuleList;
} PEB_LDR_DATA, *PPEB_LDR_DATA;
typedef struct _PEB {
	BYTE                          Reserved1[2];
	BYTE                          BeingDebugged;
	BYTE                          Reserved2[1];
	PVOID                         Reserved3[2];
	PPEB_LDR_DATA                 Ldr;
	void  *ProcessParameters;
	BYTE                          Reserved4[104];
	PVOID                         Reserved5[52];
	void *PostProcessInitRoutine;
	BYTE                          Reserved6[128];
	PVOID                         Reserved7[1];
	ULONG                         SessionId;
} PEB, *PPEB;
typedef struct _LDR_DATA_TABLE_ENTRY
{
	LIST_ENTRY InLoadOrderLinks;
	LIST_ENTRY InMemoryOrderLinks;
	LIST_ENTRY InInitializationOrderLinks;
	PVOID DllBase;
	PVOID EntryPoint;
	ULONG SizeOfImage;
	UNICODE_STRING FullDllName;
	UNICODE_STRING BaseDllName;
	ULONG Flags;
	WORD LoadCount;
	WORD TlsIndex;
	union
	{
		LIST_ENTRY HashLinks;
		struct
		{
			PVOID SectionPointer;
			ULONG CheckSum;
		};
	};
	union
	{
		ULONG TimeDateStamp;
		PVOID LoadedImports;
	};
	void *EntryPointActivationContext;
	PVOID PatchInformation;
	LIST_ENTRY ForwarderLinks;
	LIST_ENTRY ServiceTagLinks;
	LIST_ENTRY StaticLinks;
} LDR_DATA_TABLE_ENTRY, *PLDR_DATA_TABLE_ENTRY;

NTSTATUS ControlDeviceCallback(PDEVICE_OBJECT device_obj_, PIRP irp) {
	NTSTATUS status = STATUS_SUCCESS;
	PIO_STACK_LOCATION stack_loc = IoGetCurrentIrpStackLocation(irp);
	struct MemoryIoRequest *io_req = NULL;
	PEPROCESS process = { 0 };
	struct GetModulesRequest *get_mods_req = NULL;
	struct HijackThreadRequest *hijack_thread_req = NULL;
	struct AllocVirtualMemoryRequest *alloc_virtual_mem_req = NULL;
	struct FreeVirtualMemoryRequest *free_virtual_mem_req = NULL;
	struct SuspendThreadRequest *suspend_thread_req = NULL;
	struct ResumeThreadRequest *resume_thread_req = NULL;
	struct InjectLibRequest *inject_lib_req = NULL;
	if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_READ_PROC_MEM || stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_WRITE_PROC_MEM) {
		io_req = irp->AssociatedIrp.SystemBuffer;
		status = PsLookupProcessByProcessId((HANDLE)io_req->proc_id, &process);
		if (status != STATUS_SUCCESS) {
			irp->IoStatus.Status = status;
			IoCompleteRequest(irp, IO_NO_INCREMENT);
			return status;
		}
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_GET_MODULES) {
		get_mods_req = irp->AssociatedIrp.SystemBuffer;
		status = PsLookupProcessByProcessId((HANDLE)get_mods_req->proc_id, &process);
		if (status != STATUS_SUCCESS) {
			irp->IoStatus.Status = status;
			IoCompleteRequest(irp, IO_NO_INCREMENT);
			return status;
		}
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_HIJACK_THREAD) {
		hijack_thread_req = irp->AssociatedIrp.SystemBuffer;
		status = PsLookupProcessByProcessId((HANDLE)hijack_thread_req->proc_id, &process);
		if (status != STATUS_SUCCESS) {
			irp->IoStatus.Status = status;
			IoCompleteRequest(irp, IO_NO_INCREMENT);
			return status;
		}
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_ALLOC_VIRTUAL_MEM) {
		alloc_virtual_mem_req = irp->AssociatedIrp.SystemBuffer;
		status = PsLookupProcessByProcessId((HANDLE)alloc_virtual_mem_req->proc_id, &process);
		if (status != STATUS_SUCCESS) {
			irp->IoStatus.Status = status;
			IoCompleteRequest(irp, IO_NO_INCREMENT);
			return status;
		}
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_FREE_VIRTUAL_MEM) {
		free_virtual_mem_req = irp->AssociatedIrp.SystemBuffer;
		status = PsLookupProcessByProcessId((HANDLE)free_virtual_mem_req->proc_id, &process);
		if (status != STATUS_SUCCESS) {
			irp->IoStatus.Status = status;
			IoCompleteRequest(irp, IO_NO_INCREMENT);
			return status;
		}
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_SUSPEND_THREAD) {
		suspend_thread_req = irp->AssociatedIrp.SystemBuffer;
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_RESUME_THREAD) {
		resume_thread_req = irp->AssociatedIrp.SystemBuffer;
	}
	else if (stack_loc->Parameters.DeviceIoControl.IoControlCode == IOCTL_INJECT_LIB) {
		inject_lib_req = irp->AssociatedIrp.SystemBuffer;
	}
	SIZE_T io_info = 0;
	KAPC_STATE apc_state = { 0 };
	struct InjectLibRequest *new_inject_queue = NULL;
	switch (stack_loc->Parameters.DeviceIoControl.IoControlCode) {
	case IOCTL_READ_PROC_MEM:
		status = cpy_virtual_mem(process, io_req->base_addr, PsGetCurrentProcess(), io_req->buffer, io_req->buffer_sz, KernelMode, &io_info);
		irp->IoStatus.Status = STATUS_SUCCESS;
		break;
	case IOCTL_WRITE_PROC_MEM:
		status = cpy_virtual_mem(PsGetCurrentProcess(), io_req->buffer, process, io_req->base_addr, io_req->buffer_sz, KernelMode, &io_info);
		irp->IoStatus.Status = STATUS_SUCCESS;
		break;
	case IOCTL_GET_MODULES:
		if (status == STATUS_SUCCESS) {
			/*PPEB32 peb32 = get_proc_wow64_proc(process);
			if (peb32) {
				PPEB_LDR_DATA32 ldr_data = peb32->Ldr;
				if (ldr_data) {
					for (ULONG list_entry = ldr_data->InMemoryOrderModuleList.Flink; (LIST_ENTRY32 *)list_entry != &ldr_data->InMemoryOrderModuleList; list_entry = *((ULONG *)list_entry)) {
						if (!list_entry) {
							continue;
						}
						PLDR_DATA_TABLE_ENTRY32 ldr_data_table_entry = (PLDR_DATA_TABLE_ENTRY32)list_entry;
						wchar_t img_name_buf[MAX_PATH] = { '\0' };
						mbstowcs(img_name_buf, (const char *)ldr_data_table_entry->FullDllName.Buffer, ldr_data_table_entry->FullDllName.Length);
						if (!wcscmp(img_name_buf, loaded_images[i].full_img_name)) {
							loaded_images[i].base_addr = (ULONG)ldr_data_table_entry->DllBase;
							break;
						}
					}
				}
			}
			else {*/
			PPEB peb = get_proc_peb(process);
			if (peb) {
				KeStackAttachProcess(process, &apc_state);
				PPEB_LDR_DATA ldr_data = peb->Ldr;
				if (ldr_data) {
					for (LIST_ENTRY *list_entry = ldr_data->InMemoryOrderModuleList.Flink; list_entry != &ldr_data->InMemoryOrderModuleList; list_entry = list_entry ? list_entry->Flink : NULL) {
						if (!list_entry) {
							continue;
						}
						PLDR_DATA_TABLE_ENTRY ldr_data_table_entry = CONTAINING_RECORD(list_entry, LDR_DATA_TABLE_ENTRY, InMemoryOrderLinks);
						if (ldr_data_table_entry->FullDllName.Length == 0 || !ldr_data_table_entry->FullDllName.Buffer) {
							continue;
						}
						memset(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name, 0, sizeof(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name));
						memcpy(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name, ldr_data_table_entry->FullDllName.Buffer, ldr_data_table_entry->FullDllName.Length <= sizeof(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name) - sizeof(wchar_t) ? ldr_data_table_entry->FullDllName.Length : sizeof(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name) - sizeof(wchar_t));
						//wcscpy_s(get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].full_img_name, ldr_data_table_entry->FullDllName.Length / sizeof(wchar_t), ldr_data_table_entry->FullDllName.Buffer);
						/*wcscpy_s(loaded_images[i].full_img_name, ldr_data_table_entry->FullDllName.Length / sizeof(wchar_t), ldr_data_table_entry->FullDllName.Buffer);
						UNICODE_STRING full_img_name_unicode;
						RtlInitUnicodeString(&full_img_name_unicode, loaded_images[i].full_img_name);
						if (RtlEqualUnicodeString(&ldr_data_table_entry->FullDllName, &full_img_name_unicode, TRUE)) {
							loaded_images[i].base_addr = (ULONG)ldr_data_table_entry->DllBase;
							break;
						}*/
						PVOID section_base_addr = get_proc_section_base_addr(process);
						if (section_base_addr) {
							get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].base_addr = (ULONGLONG)ldr_data_table_entry->DllBase;
							get_mods_req->loaded_images_buf[get_mods_req->loaded_images_ret].size = (ULONG)ldr_data_table_entry->SizeOfImage;
							++get_mods_req->loaded_images_ret;
						}
					}
				}
				KeUnstackDetachProcess(&apc_state);
			}
			//}
		}
		irp->IoStatus.Status = STATUS_SUCCESS;
		io_info = sizeof(struct GetModulesRequest);
		break;
	case IOCTL_HIJACK_THREAD:
		if (status == STATUS_SUCCESS) {
			KeStackAttachProcess(process, &apc_state);
			PETHREAD thread;
			status = PsLookupThreadByThreadId((HANDLE)hijack_thread_req->thread_id, &thread);
			if (status == STATUS_SUCCESS) {
				CONTEXT *thread_ctx = NULL;
				size_t thread_ctx_sz = sizeof(CONTEXT);
				status = ZwAllocateVirtualMemory(ZwCurrentProcess(), &thread_ctx, 0, &thread_ctx_sz, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
				if (status == STATUS_SUCCESS) {
					thread_ctx->ContextFlags = CONTEXT_ALL;
					status = get_ctx_thread(thread, thread_ctx, UserMode);
					if (status == STATUS_SUCCESS && thread_ctx->ContextFlags & CONTEXT_CONTROL) {
						thread_ctx->Rip = (ULONG64)hijack_thread_req->new_rip;
						status = set_ctx_thread(thread, thread_ctx, UserMode);
					}
					thread_ctx_sz = 0;
					status = ZwFreeVirtualMemory(ZwCurrentProcess(), &thread_ctx, &thread_ctx_sz, MEM_RELEASE);
				}
			}
			KeUnstackDetachProcess(&apc_state);
		}
		irp->IoStatus.Status = STATUS_SUCCESS;
		io_info = sizeof(struct HijackThreadRequest);
		break;
	case IOCTL_ALLOC_VIRTUAL_MEM:
		KeStackAttachProcess(process, &apc_state);
		void **alloc_base_addr = &alloc_virtual_mem_req->base_addr;
		size_t *alloc_region_sz = &alloc_virtual_mem_req->region_sz;
		status = ZwAllocateVirtualMemory(ZwCurrentProcess(), alloc_base_addr, alloc_virtual_mem_req->zero_bits, alloc_region_sz, alloc_virtual_mem_req->alloc_type, alloc_virtual_mem_req->protect);
		alloc_virtual_mem_req->region_sz = *alloc_region_sz;
		alloc_virtual_mem_req->base_addr = *alloc_base_addr;
		KeUnstackDetachProcess(&apc_state);
		io_info = sizeof(struct AllocVirtualMemoryRequest);
		break;
	case IOCTL_FREE_VIRTUAL_MEM:
		KeStackAttachProcess(process, &apc_state);
		void **free_base_addr = &free_virtual_mem_req->base_addr;
		size_t *free_region_sz = &free_virtual_mem_req->region_sz;
		status = ZwFreeVirtualMemory(ZwCurrentProcess(), free_base_addr, free_region_sz, free_virtual_mem_req->free_type);
		free_virtual_mem_req->region_sz = *free_region_sz;
		free_virtual_mem_req->base_addr = *free_base_addr;
		KeUnstackDetachProcess(&apc_state);
		io_info = sizeof(struct FreeVirtualMemoryRequest);
		break;
	case IOCTL_SUSPEND_THREAD:
		break;
	case IOCTL_RESUME_THREAD:
		break;
	case IOCTL_INJECT_LIB:
		new_inject_queue = ExAllocatePool(NonPagedPool, (inject_queue_count + 1) * sizeof(struct InjectLibRequest));
		if (inject_queue) {
			memcpy(new_inject_queue, inject_queue, inject_queue_count * sizeof(struct InjectLibRequest));
			ExFreePool(inject_queue);
		}
		inject_queue = new_inject_queue;
		inject_queue[inject_queue_count++] = *inject_lib_req;
		io_info = inject_queue_count;
		break;
	default:
		DbgPrint("Invalid I/O control code received: %u", stack_loc->Parameters.DeviceIoControl.IoControlCode);
	}
	if (status != STATUS_SUCCESS) {
		irp->IoStatus.Status = status;
		IoCompleteRequest(irp, IO_NO_INCREMENT);
		return status;
	}
	irp->IoStatus.Information = io_info;
	IoCompleteRequest(irp, IO_NO_INCREMENT);
	return status;
}
void LoadLibApc(void *context, void *sysarg1, void *sysarg2) {
	struct InjectLibRequest *inject_lib_req = context;
	inject_lib_req->load_lib_func(inject_lib_req->lib_path);
}
// The distance between LoadLibApc and LoadLibApc_end is equal to the length of the LoadLibApc function.
void LoadLibApc_end() {}
VOID CleanupLoadLib(__in PRKAPC Apc, __inout void *NormalRoutine, __inout PVOID *NormalContext, __inout PVOID *SystemArgument1, __inout PVOID *SystemArgument2) {
	ExFreePool(Apc);
}
void CreateThreadNotifyRoutineInject(HANDLE ProcessId, HANDLE ThreadId, BOOLEAN Create) {
	if (Create) {
		PEPROCESS process;
		if (PsLookupProcessByProcessId(ProcessId, &process) != STATUS_SUCCESS) {
			return;
		}
		KAPC_STATE apc_state = { 0 };
		KeStackAttachProcess(process, &apc_state);
		CHAR *proc_image_filename = get_proc_image_file_name(process);
		if (!proc_image_filename) {
			KeUnstackDetachProcess(&apc_state);
			return;
		}
		UNICODE_STRING proc_image_filename_unicode;
		ANSI_STRING proc_image_filename_ansi;
		RtlInitAnsiString(&proc_image_filename_ansi, proc_image_filename);
		RtlAnsiStringToUnicodeString(&proc_image_filename_unicode, &proc_image_filename_ansi, TRUE);
		size_t old_queue_count = inject_queue_count;
		for (size_t i = 0; i < inject_queue_count; ++i) {
			UNICODE_STRING proc_filename_unicode;
			RtlInitUnicodeString(&proc_filename_unicode, inject_queue[i].proc_filename);
			PETHREAD thread;
			NTSTATUS status = PsLookupThreadByThreadId(ThreadId, &thread);
			if (status == STATUS_SUCCESS/* && RtlEqualUnicodeString(&proc_image_filename_unicode, &proc_filename_unicode, TRUE)*/) {
				//PKAPC apc = ExAllocatePool(NonPagedPool, sizeof(KAPC));
				PVOID inject_ctx = NULL, user_apc_ptr = NULL;
				SIZE_T inject_ctx_sz = sizeof(struct InjectLibRequest), user_apc_sz = (ULONG_PTR)LoadLibApc_end - (ULONG_PTR)LoadLibApc;
				ZwAllocateVirtualMemory(ZwCurrentProcess(), &inject_ctx, 0, &inject_ctx_sz, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
				ZwAllocateVirtualMemory(ZwCurrentProcess(), &user_apc_ptr, 0, &user_apc_sz, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
				SIZE_T bytes_copied = 0;
				cpy_virtual_mem(NtCurrentProcess(), &inject_queue[i], process, inject_ctx, inject_ctx_sz, KernelMode, &bytes_copied);
				cpy_virtual_mem(NtCurrentProcess(), LoadLibApc, process, user_apc_ptr, user_apc_sz, KernelMode, &bytes_copied);
				//init_apc(apc, thread, 0, CleanupLoadLib, NULL, user_apc_ptr, UserMode, inject_ctx);
				//insert_queue_apc(apc, 0, NULL, 0);
				--inject_queue_count;
				break;
			}
		}
		RtlFreeUnicodeString(&proc_image_filename_unicode);
		if (old_queue_count != inject_queue_count) {
			if (inject_queue_count != 0U) {
				struct InjectLibRequest *new_inject_queue = ExAllocatePool(NonPagedPool, inject_queue_count * sizeof(struct InjectLibRequest));
				if (inject_queue) {
					memcpy(new_inject_queue, inject_queue, inject_queue_count * sizeof(struct InjectLibRequest));
					ExFreePool(inject_queue);
				}
				inject_queue = new_inject_queue;
			}
			else {
				ExFreePool(inject_queue);
				inject_queue = NULL;
			}
		}
		KeUnstackDetachProcess(&apc_state);
	}
}
NTSTATUS CreateCallback(PDEVICE_OBJECT device_obj_, PIRP irp) {
	return STATUS_SUCCESS;
}
NTSTATUS CloseCallback(PDEVICE_OBJECT device_obj_, PIRP irp) {
	return STATUS_SUCCESS;
}
NTSTATUS DriverEntry(_In_ PDRIVER_OBJECT  DriverObject, _In_ PUNICODE_STRING RegistryPath) {
	NTSTATUS status = STATUS_SUCCESS;
	UNICODE_STRING device_sym_link_unicode, device_name_unicode;
	RtlInitUnicodeString(&device_sym_link_unicode, device_sym_link);
	RtlInitUnicodeString(&device_name_unicode, device_name);
	inject_queue = NULL;
	inject_queue_count = 0;
	UNICODE_STRING mm_cpy_virtual_mem_unicode;
	RtlInitUnicodeString(&mm_cpy_virtual_mem_unicode, L"MmCopyVirtualMemory");
	cpy_virtual_mem = (MmCopyVirtualMemoryFn)MmGetSystemRoutineAddress(&mm_cpy_virtual_mem_unicode);
	status = cpy_virtual_mem == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING get_proc_peb_unicode;
	RtlInitUnicodeString(&get_proc_peb_unicode, L"PsGetProcessPeb");
	get_proc_peb = (PsGetProcessPebFn)MmGetSystemRoutineAddress(&get_proc_peb_unicode);
	status = get_proc_peb == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING get_proc_section_base_addr_unicode;
	RtlInitUnicodeString(&get_proc_section_base_addr_unicode, L"PsGetProcessSectionBaseAddress");
	get_proc_section_base_addr = (PsGetProcessSectionBaseAddressFn)MmGetSystemRoutineAddress(&get_proc_section_base_addr_unicode);
	status = get_proc_section_base_addr == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING get_proc_wow64_proc_unicode;
	RtlInitUnicodeString(&get_proc_wow64_proc_unicode, L"PsGetProcessWow64Process");
	get_proc_wow64_proc = (PsGetProcessWow64ProcessFn)MmGetSystemRoutineAddress(&get_proc_wow64_proc_unicode);
	status = get_proc_wow64_proc == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING get_proc_image_file_name_unicode;
	RtlInitUnicodeString(&get_proc_image_file_name_unicode, L"PsGetProcessImageFileName");
	get_proc_image_file_name = (PsGetProcessImageFileNameFn)MmGetSystemRoutineAddress(&get_proc_image_file_name_unicode);
	status = get_proc_image_file_name == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING insert_queue_apc_unicode;
	RtlInitUnicodeString(&insert_queue_apc_unicode, L"KeInsertQueueApc");
	insert_queue_apc = (KeInsertQueueApcFn)MmGetSystemRoutineAddress(&insert_queue_apc_unicode);
	status = insert_queue_apc == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	/*UNICODE_STRING aquire_lock_handle_unicode;
	RtlInitUnicodeString(&aquire_lock_handle_unicode, L"KeAcquireInStackQueuedSpinLockRaiseToSynch");
	aquire_lock_handle = (KeAcquireInStackQueuedSpinLockRaiseToSynchFn)MmGetSystemRoutineAddress(&aquire_lock_handle_unicode);
	status = aquire_lock_handle == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING release_lock_unicode;
	RtlInitUnicodeString(&release_lock_unicode, L"KeReleaseInStackQueuedSpinLock");
	release_lock = (KeReleaseInStackQueuedSpinLockFn)MmGetSystemRoutineAddress(&release_lock_unicode);
	status = release_lock == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}*/
	/*UNICODE_STRING suspend_thread_unicode;
	RtlInitUnicodeString(&suspend_thread_unicode, L"PsSuspendThread");
	suspend_thread = (PsSuspendThreadFn)MmGetSystemRoutineAddress(&suspend_thread_unicode);
	status = suspend_thread == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		//return status;
	}
	UNICODE_STRING resume_thread_unicode;
	RtlInitUnicodeString(&resume_thread_unicode, L"PsResumeThread");
	resume_thread = (PsResumeThreadFn)MmGetSystemRoutineAddress(&resume_thread_unicode);
	status = resume_thread == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		//return status;
	}*/
	UNICODE_STRING get_ctx_thread_unicode;
	RtlInitUnicodeString(&get_ctx_thread_unicode, L"PsGetContextThread");
	get_ctx_thread = (PsGetContextThreadFn)MmGetSystemRoutineAddress(&get_ctx_thread_unicode);
	status = get_ctx_thread == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	UNICODE_STRING set_ctx_thread_unicode;
	RtlInitUnicodeString(&set_ctx_thread_unicode, L"PsSetContextThread");
	set_ctx_thread = (PsSetContextThreadFn)MmGetSystemRoutineAddress(&set_ctx_thread_unicode);
	status = set_ctx_thread == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	/*status = PsSetLoadImageNotifyRoutine(PloadImageNotifyRoutine);
	if (status != STATUS_SUCCESS) {
		return status;
	}*/
	UNICODE_STRING init_apc_unicode;
	RtlInitUnicodeString(&init_apc_unicode, L"KeInitializeApc");
	init_apc = (KeInitializeApcFn)MmGetSystemRoutineAddress(&init_apc_unicode);
	status = init_apc == NULL ? STATUS_INVALID_ADDRESS : STATUS_SUCCESS;
	if (status != STATUS_SUCCESS) {
		return status;
	}
	status = PsSetCreateThreadNotifyRoutine(CreateThreadNotifyRoutineInject);
	if (status != STATUS_SUCCESS) {
		return status;
	}
	status = IoCreateDevice(DriverObject, 0, &device_name_unicode, FILE_DEVICE_UNKNOWN, FILE_DEVICE_UNKNOWN, FALSE, &device_obj);
	if (status != STATUS_SUCCESS) {
		return status;
	}
	status = IoCreateSymbolicLink(&device_sym_link_unicode, &device_name_unicode);
	if (status != STATUS_SUCCESS) {
		return status;
	}
	DriverObject->DriverUnload = Unload;
	DriverObject->MajorFunction[IRP_MJ_CREATE] = CreateCallback;
	DriverObject->MajorFunction[IRP_MJ_CLOSE] = CloseCallback;
	DriverObject->MajorFunction[IRP_MJ_DEVICE_CONTROL] = ControlDeviceCallback;
	return status;
}
/*void PloadImageNotifyRoutine(
	PUNICODE_STRING FullImageName,
	HANDLE ProcessId,
	PIMAGE_INFO ImageInfo
) {
	if (FullImageName == NULL || FullImageName->Buffer == NULL || FullImageName->Length == 0 || ImageInfo == NULL) {
		return;
	}
	struct Image image = { 0 };
	image.proc_id = ProcessId;
	memcpy(image.full_img_name, FullImageName->Buffer, FullImageName->Length <= sizeof(image.full_img_name) - sizeof(wchar_t) ? FullImageName->Length : sizeof(image.full_img_name) - sizeof(wchar_t));
	image.base_addr = (ULONG)ImageInfo->ImageBase;
	image.size = ImageInfo->ImageSize;
	if (loaded_images_sz < sizeof(loaded_images) / sizeof(struct Image)) {
		loaded_images[loaded_images_sz++] = image;
	}
}*/
VOID Unload(_In_ struct _DRIVER_OBJECT *DriverObject) {
	UNICODE_STRING device_sym_link_unicode;
	RtlInitUnicodeString(&device_sym_link_unicode, device_sym_link);
	IoDeleteSymbolicLink(&device_sym_link_unicode);
	IoDeleteDevice(DriverObject->DeviceObject);
	PsRemoveCreateThreadNotifyRoutine(CreateThreadNotifyRoutineInject);
	if (inject_queue) {
		ExFreePool(inject_queue);
		inject_queue = NULL;
		inject_queue_count = 0;
	}
	//PsRemoveLoadImageNotifyRoutine(PloadImageNotifyRoutine);
	//loaded_images_sz = 0;
}