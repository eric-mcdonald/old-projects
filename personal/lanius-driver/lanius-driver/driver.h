#pragma once

#pragma warning(disable : 4214)

#include <ntdef.h>
#include <ntifs.h>
#include <ntddk.h>
#include <wdf.h>
#include <windef.h>

const wchar_t *device_name = L"\\Device\\LaniusDevice", *device_sym_link = L"\\DosDevices\\LaniusDevice";
PDEVICE_OBJECT device_obj = NULL;

DRIVER_INITIALIZE DriverEntry;
DRIVER_UNLOAD Unload;
/*void PloadImageNotifyRoutine(
	PUNICODE_STRING,
	HANDLE,
	PIMAGE_INFO
);*/
typedef NTSTATUS(__fastcall *MmCopyVirtualMemoryFn)(IN PEPROCESS FromProcess, IN CONST VOID *FromAddress, IN PEPROCESS ToProcess, OUT PVOID ToAddress, IN SIZE_T BufferSize, IN KPROCESSOR_MODE PreviousMode, OUT PSIZE_T NumberOfBytesCopied);
MmCopyVirtualMemoryFn cpy_virtual_mem = NULL;
typedef PPEB(__fastcall *PsGetProcessPebFn)(IN PEPROCESS Process);
typedef struct _RTL_BALANCED_NODE32
{
	union
	{
		ULONG Children[2];
		struct
		{
			ULONG Left;
			ULONG Right;
		};
	};
	union
	{
		ULONG Red : 1;
		ULONG Balance : 2;
		ULONG ParentValue;
	};
} RTL_BALANCED_NODE32, *PRTL_BALANCED_NODE32;
typedef enum _LDR_DLL_LOAD_REASON {
	LoadReasonStaticDependency = 0,
	LoadReasonStaticForwarderDependency = 1,
	LoadReasonDynamicForwarderDependency = 2,
	LoadReasonDelayloadDependency = 3,
	LoadReasonDynamicLoad = 4,
	LoadReasonAsImageLoad = 5,
	LoadReasonAsDataLoad = 6,
	LoadReasonUnknown = -1,
} LDR_DLL_LOAD_REASON;
typedef struct _LDR_DATA_TABLE_ENTRY32
{
	LIST_ENTRY32 InLoadOrderLinks;
	LIST_ENTRY32 InMemoryOrderLinks;
	union
	{
		LIST_ENTRY32 InInitializationOrderLinks;
		LIST_ENTRY32 InProgressLinks;
	};
	ULONG DllBase;
	ULONG EntryPoint;
	ULONG SizeOfImage;
	UNICODE_STRING32 FullDllName;
	UNICODE_STRING32 BaseDllName;
	union
	{
		UCHAR FlagGroup[4];
		ULONG Flags;
		struct
		{
			ULONG PackagedBinary : 1;
			ULONG MarkedForRemoval : 1;
			ULONG ImageDll : 1;
			ULONG LoadNotificationsSent : 1;
			ULONG TelemetryEntryProcessed : 1;
			ULONG ProcessStaticImport : 1;
			ULONG InLegacyLists : 1;
			ULONG InIndexes : 1;
			ULONG ShimDll : 1;
			ULONG InExceptionTable : 1;
			ULONG ReservedFlags1 : 2;
			ULONG LoadInProgress : 1;
			ULONG LoadConfigProcessed : 1;
			ULONG EntryProcessed : 1;
			ULONG ProtectDelayLoad : 1;
			ULONG ReservedFlags3 : 2;
			ULONG DontCallForThreads : 1;
			ULONG ProcessAttachCalled : 1;
			ULONG ProcessAttachFailed : 1;
			ULONG CorDeferredValidate : 1;
			ULONG CorImage : 1;
			ULONG DontRelocate : 1;
			ULONG CorILOnly : 1;
			ULONG ReservedFlags5 : 3;
			ULONG Redirected : 1;
			ULONG ReservedFlags6 : 2;
			ULONG CompatDatabaseProcessed : 1;
		};
	};
	USHORT ObsoleteLoadCount;
	USHORT TlsIndex;
	LIST_ENTRY32 HashLinks;
	ULONG TimeDateStamp;
	ULONG EntryPointActivationContext;
	ULONG Lock;
	ULONG DdagNode;
	LIST_ENTRY32 NodeModuleLink;
	ULONG LoadContext;
	ULONG ParentDllBase;
	ULONG SwitchBackContext;
	RTL_BALANCED_NODE32 BaseAddressIndexNode;
	RTL_BALANCED_NODE32 MappingInfoIndexNode;
	ULONG OriginalBase;
	LARGE_INTEGER LoadTime;
	ULONG BaseNameHashValue;
	LDR_DLL_LOAD_REASON LoadReason;
	ULONG ImplicitPathOptions;
	ULONG ReferenceCount;
	ULONG DependentLoadFlags;
	UCHAR SigningLevel; // since REDSTONE2
} LDR_DATA_TABLE_ENTRY32, *PLDR_DATA_TABLE_ENTRY32;
typedef struct _PEB_LDR_DATA32
{
	ULONG Length;
	BOOLEAN Initialized;
	ULONG SsHandle;
	LIST_ENTRY32 InLoadOrderModuleList;
	LIST_ENTRY32 InMemoryOrderModuleList;
	LIST_ENTRY32 InInitializationOrderModuleList;
	ULONG EntryInProgress;
	BOOLEAN ShutdownInProgress;
	ULONG ShutdownThreadId;
} PEB_LDR_DATA32, *PPEB_LDR_DATA32;
typedef struct _PEB32
{
	UCHAR InheritedAddressSpace;
	UCHAR ReadImageFileExecOptions;
	UCHAR BeingDebugged;
	UCHAR BitField;
	ULONG Mutant;
	ULONG ImageBaseAddress;
	PPEB_LDR_DATA32 Ldr;
	ULONG ProcessParameters;
	ULONG SubSystemData;
	ULONG ProcessHeap;
	ULONG FastPebLock;
	ULONG AtlThunkSListPtr;
	ULONG IFEOKey;
	ULONG CrossProcessFlags;
	ULONG UserSharedInfoPtr;
	ULONG SystemReserved;
	ULONG AtlThunkSListPtr32;
	ULONG ApiSetMap;
} PEB32, *PPEB32;
typedef struct _KGDTENTRY
{
	WORD LimitLow;
	WORD BaseLow;
	ULONG HighWord;
} KGDTENTRY, *PKGDTENTRY;
typedef struct _KIDTENTRY
{
	WORD Offset;
	WORD Selector;
	WORD Access;
	WORD ExtendedOffset;
} KIDTENTRY, *PKIDTENTRY;
typedef struct _KEXECUTE_OPTIONS
{
	ULONG ExecuteDisable : 1;
	ULONG ExecuteEnable : 1;
	ULONG DisableThunkEmulation : 1;
	ULONG Permanent : 1;
	ULONG ExecuteDispatchEnable : 1;
	ULONG ImageDispatchEnable : 1;
	ULONG Spare : 2;
} KEXECUTE_OPTIONS, *PKEXECUTE_OPTIONS;
typedef struct _KPROCESS
{
	DISPATCHER_HEADER Header;
	LIST_ENTRY ProfileListHead;
	ULONG DirectoryTableBase;
	ULONG Unused0;
	KGDTENTRY LdtDescriptor;
	KIDTENTRY Int21Descriptor;
	WORD IopmOffset;
	UCHAR Iopl;
	UCHAR Unused;
	ULONG ActiveProcessors;
	ULONG KernelTime;
	ULONG UserTime;
	LIST_ENTRY ReadyListHead;
	SINGLE_LIST_ENTRY SwapListEntry;
	PVOID VdmTrapcHandler;
	LIST_ENTRY ThreadListHead;
	ULONG ProcessLock;
	ULONG Affinity;
	union
	{
		ULONG AutoAlignment : 1;
		ULONG DisableBoost : 1;
		ULONG DisableQuantum : 1;
		ULONG ReservedFlags : 29;
		LONG ProcessFlags;
	};
	CHAR BasePriority;
	CHAR QuantumReset;
	UCHAR State;
	UCHAR ThreadSeed;
	UCHAR PowerState;
	UCHAR IdealNode;
	UCHAR Visited;
	union
	{
		KEXECUTE_OPTIONS Flags;
		UCHAR ExecuteOptions;
	};
	ULONG StackCount;
	LIST_ENTRY ProcessListEntry;
	UINT64 CycleTime;
} KPROCESS, *PKPROCESS;
typedef struct _DESCRIPTOR
{
	WORD Pad;
	WORD Limit;
	ULONG Base;
} DESCRIPTOR, *PDESCRIPTOR;
typedef struct _KSPECIAL_REGISTERS
{
	ULONG Cr0;
	ULONG Cr2;
	ULONG Cr3;
	ULONG Cr4;
	ULONG KernelDr0;
	ULONG KernelDr1;
	ULONG KernelDr2;
	ULONG KernelDr3;
	ULONG KernelDr6;
	ULONG KernelDr7;
	DESCRIPTOR Gdtr;
	DESCRIPTOR Idtr;
	WORD Tr;
	WORD Ldtr;
	ULONG Reserved[6];
} KSPECIAL_REGISTERS, *PKSPECIAL_REGISTERS;
typedef struct _KPROCESSOR_STATE
{
	CONTEXT ContextFrame;
	KSPECIAL_REGISTERS SpecialRegisters;
} KPROCESSOR_STATE, *PKPROCESSOR_STATE;
typedef struct _CACHED_KSTACK_LIST
{
	SLIST_HEADER SListHead;
	LONG MinimumFree;
	ULONG Misses;
	ULONG MissesLast;
} CACHED_KSTACK_LIST, *PCACHED_KSTACK_LIST;
typedef struct _KNODE
{
	SLIST_HEADER PagedPoolSListHead;
	SLIST_HEADER NonPagedPoolSListHead[3];
	SLIST_HEADER PfnDereferenceSListHead;
	ULONG ProcessorMask;
	UCHAR Color;
	UCHAR Seed;
	UCHAR NodeNumber;
	ULONG Flags;
	ULONG MmShiftedColor;
	ULONG FreeCount[2];
	PSINGLE_LIST_ENTRY PfnDeferredList;
	CACHED_KSTACK_LIST CachedKernelStacks;
} KNODE, *PKNODE;
typedef struct _PP_LOOKASIDE_LIST
{
	PGENERAL_LOOKASIDE P;
	PGENERAL_LOOKASIDE L;
} PP_LOOKASIDE_LIST, *PPP_LOOKASIDE_LIST;
typedef struct _KDPC_DATA
{
	LIST_ENTRY DpcListHead;
	ULONG DpcLock;
	LONG DpcQueueDepth;
	ULONG DpcCount;
} KDPC_DATA, *PKDPC_DATA;
typedef struct _FX_SAVE_AREA
{
	BYTE U[520];
	ULONG NpxSavedCpu;
	ULONG Cr0NpxState;
} FX_SAVE_AREA, *PFX_SAVE_AREA;
typedef struct
{
	LONG * IdleHandler;
	ULONG Context;
	ULONG Latency;
	ULONG Power;
	ULONG TimeCheck;
	ULONG StateFlags;
	UCHAR PromotePercent;
	UCHAR DemotePercent;
	UCHAR PromotePercentBase;
	UCHAR DemotePercentBase;
	UCHAR StateType;
} PPM_IDLE_STATE, *PPPM_IDLE_STATE;
typedef struct
{
	ULONG Type;
	ULONG Count;
	ULONG Flags;
	ULONG TargetState;
	ULONG ActualState;
	ULONG OldState;
	ULONG TargetProcessors;
	PPM_IDLE_STATE State[1];
} PPM_IDLE_STATES, *PPPM_IDLE_STATES;
typedef struct
{
	UINT64 StartTime;
	UINT64 EndTime;
	ULONG Reserved[4];
} PROCESSOR_IDLE_TIMES, *PPROCESSOR_IDLE_TIMES;
typedef struct
{
	ULONG IdleTransitions;
	ULONG FailedTransitions;
	ULONG InvalidBucketIndex;
	UINT64 TotalTime;
	ULONG IdleTimeBuckets[6];
} PPM_IDLE_STATE_ACCOUNTING, *PPPM_IDLE_STATE_ACCOUNTING;
typedef struct
{
	ULONG StateCount;
	ULONG TotalTransitions;
	ULONG ResetCount;
	UINT64 StartTime;
	PPM_IDLE_STATE_ACCOUNTING State[1];
} PPM_IDLE_ACCOUNTING, *PPPM_IDLE_ACCOUNTING;
typedef struct
{
	ULONG Frequency;
	ULONG Power;
	UCHAR PercentFrequency;
	UCHAR IncreaseLevel;
	UCHAR DecreaseLevel;
	UCHAR Type;
	UINT64 Control;
	UINT64 Status;
	ULONG TotalHitCount;
	ULONG DesiredCount;
} PPM_PERF_STATE, *PPPM_PERF_STATE;
typedef struct
{
	ULONG Count;
	ULONG MaxFrequency;
	ULONG MaxPerfState;
	ULONG MinPerfState;
	ULONG LowestPState;
	ULONG IncreaseTime;
	ULONG DecreaseTime;
	UCHAR BusyAdjThreshold;
	UCHAR Reserved;
	UCHAR ThrottleStatesOnly;
	UCHAR PolicyType;
	ULONG TimerInterval;
	ULONG Flags;
	ULONG TargetProcessors;
	LONG * PStateHandler;
	ULONG PStateContext;
	LONG * TStateHandler;
	ULONG TStateContext;
	ULONG *FeedbackHandler;
	PPM_PERF_STATE State[1];
} PPM_PERF_STATES, *PPPM_PERF_STATES;
typedef struct _PROCESSOR_POWER_STATE
{
	PVOID IdleFunction;
	PPPM_IDLE_STATES IdleStates;
	UINT64 LastTimeCheck;
	UINT64 LastIdleTime;
	PROCESSOR_IDLE_TIMES IdleTimes;
	PPPM_IDLE_ACCOUNTING IdleAccounting;
	PPPM_PERF_STATES PerfStates;
	ULONG LastKernelUserTime;
	ULONG LastIdleThreadKTime;
	UINT64 LastGlobalTimeHv;
	UINT64 LastProcessorTimeHv;
	UCHAR ThermalConstraint;
	UCHAR LastBusyPercentage;
	BYTE Flags[6];
	KTIMER PerfTimer;
	KDPC PerfDpc;
	ULONG LastSysTime;
	void *PStateMaster;
	ULONG PStateSet;
	ULONG CurrentPState;
	ULONG Reserved0;
	ULONG DesiredPState;
	ULONG Reserved1;
	ULONG PStateIdleStartTime;
	ULONG PStateIdleTime;
	ULONG LastPStateIdleTime;
	ULONG PStateStartTime;
	ULONG WmiDispatchPtr;
	LONG WmiInterfaceEnabled;
} PROCESSOR_POWER_STATE, *PPROCESSOR_POWER_STATE;
typedef struct _KPRCB
{
	WORD MinorVersion;
	WORD MajorVersion;
	PKTHREAD CurrentThread;
	PKTHREAD NextThread;
	PKTHREAD IdleThread;
	UCHAR Number;
	UCHAR NestingLevel;
	WORD BuildType;
	ULONG SetMember;
	CHAR CpuType;
	CHAR CpuID;
	union
	{
		WORD CpuStep;
		struct
		{
			UCHAR CpuStepping;
			UCHAR CpuModel;
		};
	};
	KPROCESSOR_STATE ProcessorState;
	ULONG KernelReserved[16];
	ULONG HalReserved[16];
	ULONG CFlushSize;
	UCHAR PrcbPad0[88];
	KSPIN_LOCK_QUEUE LockQueue[33];
	PKTHREAD NpxThread;
	ULONG InterruptCount;
	ULONG KernelTime;
	ULONG UserTime;
	ULONG DpcTime;
	ULONG DpcTimeCount;
	ULONG InterruptTime;
	ULONG AdjustDpcThreshold;
	ULONG PageColor;
	UCHAR SkipTick;
	UCHAR DebuggerSavedIRQL;
	UCHAR NodeColor;
	UCHAR PollSlot;
	ULONG NodeShiftedColor;
	PKNODE ParentNode;
	ULONG MultiThreadProcessorSet;
	void *MultiThreadSetMaster;
	ULONG SecondaryColorMask;
	ULONG DpcTimeLimit;
	ULONG CcFastReadNoWait;
	ULONG CcFastReadWait;
	ULONG CcFastReadNotPossible;
	ULONG CcCopyReadNoWait;
	ULONG CcCopyReadWait;
	ULONG CcCopyReadNoWaitMiss;
	LONG MmSpinLockOrdering;
	LONG IoReadOperationCount;
	LONG IoWriteOperationCount;
	LONG IoOtherOperationCount;
	LARGE_INTEGER IoReadTransferCount;
	LARGE_INTEGER IoWriteTransferCount;
	LARGE_INTEGER IoOtherTransferCount;
	ULONG CcFastMdlReadNoWait;
	ULONG CcFastMdlReadWait;
	ULONG CcFastMdlReadNotPossible;
	ULONG CcMapDataNoWait;
	ULONG CcMapDataWait;
	ULONG CcPinMappedDataCount;
	ULONG CcPinReadNoWait;
	ULONG CcPinReadWait;
	ULONG CcMdlReadNoWait;
	ULONG CcMdlReadWait;
	ULONG CcLazyWriteHotSpots;
	ULONG CcLazyWriteIos;
	ULONG CcLazyWritePages;
	ULONG CcDataFlushes;
	ULONG CcDataPages;
	ULONG CcLostDelayedWrites;
	ULONG CcFastReadResourceMiss;
	ULONG CcCopyReadWaitMiss;
	ULONG CcFastMdlReadResourceMiss;
	ULONG CcMapDataNoWaitMiss;
	ULONG CcMapDataWaitMiss;
	ULONG CcPinReadNoWaitMiss;
	ULONG CcPinReadWaitMiss;
	ULONG CcMdlReadNoWaitMiss;
	ULONG CcMdlReadWaitMiss;
	ULONG CcReadAheadIos;
	ULONG KeAlignmentFixupCount;
	ULONG KeExceptionDispatchCount;
	ULONG KeSystemCalls;
	ULONG PrcbPad1[3];
	PP_LOOKASIDE_LIST PPLookasideList[16];
	GENERAL_LOOKASIDE_POOL PPNPagedLookasideList[32];
	GENERAL_LOOKASIDE_POOL PPPagedLookasideList[32];
	ULONG PacketBarrier;
	LONG ReverseStall;
	PVOID IpiFrame;
	UCHAR PrcbPad2[52];
	VOID * CurrentPacket[3];
	ULONG TargetSet;
	PVOID WorkerRoutine;
	ULONG IpiFrozen;
	UCHAR PrcbPad3[40];
	ULONG RequestSummary;
	void *SignalDone;
	UCHAR PrcbPad4[56];
	KDPC_DATA DpcData[2];
	PVOID DpcStack;
	LONG MaximumDpcQueueDepth;
	ULONG DpcRequestRate;
	ULONG MinimumDpcRate;
	UCHAR DpcInterruptRequested;
	UCHAR DpcThreadRequested;
	UCHAR DpcRoutineActive;
	UCHAR DpcThreadActive;
	ULONG PrcbLock;
	ULONG DpcLastCount;
	ULONG TimerHand;
	ULONG TimerRequest;
	PVOID PrcbPad41;
	KEVENT DpcEvent;
	UCHAR ThreadDpcEnable;
	UCHAR QuantumEnd;
	UCHAR PrcbPad50;
	UCHAR IdleSchedule;
	LONG DpcSetEventRequest;
	LONG Sleeping;
	ULONG PeriodicCount;
	ULONG PeriodicBias;
	UCHAR PrcbPad5[6];
	LONG TickOffset;
	KDPC CallDpc;
	LONG ClockKeepAlive;
	UCHAR ClockCheckSlot;
	UCHAR ClockPollCycle;
	UCHAR PrcbPad6[2];
	LONG DpcWatchdogPeriod;
	LONG DpcWatchdogCount;
	LONG ThreadWatchdogPeriod;
	LONG ThreadWatchdogCount;
	ULONG PrcbPad70[2];
	LIST_ENTRY WaitListHead;
	ULONG WaitLock;
	ULONG ReadySummary;
	ULONG QueueIndex;
	SINGLE_LIST_ENTRY DeferredReadyListHead;
	UINT64 StartCycles;
	UINT64 CycleTime;
	UINT64 PrcbPad71[3];
	LIST_ENTRY DispatcherReadyListHead[32];
	PVOID ChainedInterruptList;
	LONG LookasideIrpFloat;
	LONG MmPageFaultCount;
	LONG MmCopyOnWriteCount;
	LONG MmTransitionCount;
	LONG MmCacheTransitionCount;
	LONG MmDemandZeroCount;
	LONG MmPageReadCount;
	LONG MmPageReadIoCount;
	LONG MmCacheReadCount;
	LONG MmCacheIoCount;
	LONG MmDirtyPagesWriteCount;
	LONG MmDirtyWriteIoCount;
	LONG MmMappedPagesWriteCount;
	LONG MmMappedWriteIoCount;
	ULONG CachedCommit;
	ULONG CachedResidentAvailable;
	PVOID HyperPte;
	UCHAR CpuVendor;
	UCHAR PrcbPad9[3];
	UCHAR VendorString[13];
	UCHAR InitialApicId;
	UCHAR CoresPerPhysicalProcessor;
	UCHAR LogicalProcessorsPerPhysicalProcessor;
	ULONG MHz;
	ULONG FeatureBits;
	LARGE_INTEGER UpdateSignature;
	UINT64 IsrTime;
	UINT64 SpareField1;
	FX_SAVE_AREA NpxSaveArea;
	PROCESSOR_POWER_STATE PowerState;
	KDPC DpcWatchdogDpc;
	KTIMER DpcWatchdogTimer;
	PVOID WheaInfo;
	PVOID EtwSupport;
	SLIST_HEADER InterruptObjectPool;
	LARGE_INTEGER HypercallPagePhysical;
	PVOID HypercallPageVirtual;
	PVOID RateControl;
	CACHE_DESCRIPTOR Cache[5];
	ULONG CacheCount;
	ULONG CacheProcessorMask[5];
	UCHAR LogicalProcessorsPerCore;
	UCHAR PrcbPad8[3];
	ULONG PackageProcessorSet;
	ULONG CoreProcessorSet;
} KPRCB, *PKPRCB;
typedef struct _KTHREAD
{
	DISPATCHER_HEADER Header;
	UINT64 CycleTime;
	ULONG HighCycleTime;
	UINT64 QuantumTarget;
	PVOID InitialStack;
	PVOID StackLimit;
	PVOID KernelStack;
	ULONG ThreadLock;
	union
	{
		KAPC_STATE ApcState;
		UCHAR ApcStateFill[23];
	};
	CHAR Priority;
	WORD NextProcessor;
	WORD DeferredProcessor;
	ULONG ApcQueueLock;
	ULONG ContextSwitches;
	UCHAR State;
	UCHAR NpxState;
	UCHAR WaitIrql;
	CHAR WaitMode;
	LONG WaitStatus;
	union
	{
		PKWAIT_BLOCK WaitBlockList;
		PKGATE GateObject;
	};
	union
	{
		ULONG KernelStackResident : 1;
		ULONG ReadyTransition : 1;
		ULONG ProcessReadyQueue : 1;
		ULONG WaitNext : 1;
		ULONG SystemAffinityActive : 1;
		ULONG Alertable : 1;
		ULONG GdiFlushActive : 1;
		ULONG Reserved : 25;
		LONG MiscFlags;
	};
	UCHAR WaitReason;
	UCHAR SwapBusy;
	UCHAR Alerted[2];
	union
	{
		LIST_ENTRY WaitListEntry;
		SINGLE_LIST_ENTRY SwapListEntry;
	};
	PKQUEUE Queue;
	ULONG WaitTime;
	union
	{
		struct
		{
			SHORT KernelApcDisable;
			SHORT SpecialApcDisable;
		};
		ULONG CombinedApcDisable;
	};
	PVOID Teb;
	union
	{
		KTIMER Timer;
		UCHAR TimerFill[40];
	};
	union
	{
		ULONG AutoAlignment : 1;
		ULONG DisableBoost : 1;
		ULONG EtwStackTraceApc1Inserted : 1;
		ULONG EtwStackTraceApc2Inserted : 1;
		ULONG CycleChargePending : 1;
		ULONG CalloutActive : 1;
		ULONG ApcQueueable : 1;
		ULONG EnableStackSwap : 1;
		ULONG GuiThread : 1;
		ULONG ReservedFlags : 23;
		LONG ThreadFlags;
	};
	union
	{
		KWAIT_BLOCK WaitBlock[4];
		struct
		{
			UCHAR WaitBlockFill0[23];
			UCHAR IdealProcessor;
		};
		struct
		{
			UCHAR WaitBlockFill1[47];
			CHAR PreviousMode;
		};
		struct
		{
			UCHAR WaitBlockFill2[71];
			UCHAR ResourceIndex;
		};
		UCHAR WaitBlockFill3[95];
	};
	UCHAR LargeStack;
	LIST_ENTRY QueueListEntry;
	PKTRAP_FRAME TrapFrame;
	PVOID FirstArgument;
	union
	{
		PVOID CallbackStack;
		ULONG CallbackDepth;
	};
	PVOID ServiceTable;
	UCHAR ApcStateIndex;
	CHAR BasePriority;
	CHAR PriorityDecrement;
	UCHAR Preempted;
	UCHAR AdjustReason;
	CHAR AdjustIncrement;
	UCHAR Spare01;
	CHAR Saturation;
	ULONG SystemCallNumber;
	ULONG Spare02;
	ULONG UserAffinity;
	PKPROCESS Process;
	ULONG Affinity;
	PKAPC_STATE ApcStatePointer[2];
	union
	{
		KAPC_STATE SavedApcState;
		UCHAR SavedApcStateFill[23];
	};
	CHAR FreezeCount;
	CHAR SuspendCount;
	UCHAR UserIdealProcessor;
	UCHAR Spare03;
	UCHAR Iopl;
	PVOID Win32Thread;
	PVOID StackBase;
	union
	{
		KAPC SuspendApc;
		struct
		{
			UCHAR SuspendApcFill0[1];
			CHAR Spare04;
		};
		struct
		{
			UCHAR SuspendApcFill1[3];
			UCHAR QuantumReset;
		};
		struct
		{
			UCHAR SuspendApcFill2[4];
			ULONG KernelTime;
		};
		struct
		{
			UCHAR SuspendApcFill3[36];
			void *WaitPrcb;
		};
		struct
		{
			UCHAR SuspendApcFill4[40];
			PVOID LegoData;
		};
		UCHAR SuspendApcFill5[47];
	};
	UCHAR PowerState;
	ULONG UserTime;
	union
	{
		KSEMAPHORE SuspendSemaphore;
		UCHAR SuspendSemaphorefill[20];
	};
	ULONG SListFaultCount;
	LIST_ENTRY ThreadListEntry;
	LIST_ENTRY MutantListHead;
	PVOID SListFaultAddress;
	PVOID MdlForLockedTeb;
} KTHREAD, *PKTHREAD;
typedef struct _TEB
{
	NT_TIB NtTib;
	PVOID EnvironmentPointer;
	CLIENT_ID ClientId;
	PVOID ActiveRpcHandle;
	PVOID ThreadLocalStoragePointer;
	PPEB ProcessEnvironmentBlock;
	ULONG LastErrorValue;
	ULONG CountOfOwnedCriticalSections;
	PVOID CsrClientThread;
	PVOID Win32ThreadInfo;
	ULONG User32Reserved[26];
	ULONG UserReserved[5];
	PVOID WOW32Reserved;
	ULONG CurrentLocale;
	ULONG FpSoftwareStatusRegister;
	VOID * SystemReserved1[54];
	LONG ExceptionCode;
	// More stuff here...
} TEB, *PTEB;
//typedef VOID(_fastcall *KeAcquireInStackQueuedSpinLockRaiseToSynchFn)(PKSPIN_LOCK, PKLOCK_QUEUE_HANDLE);
//KeAcquireInStackQueuedSpinLockRaiseToSynchFn aquire_lock_handle = NULL;
//typedef VOID(_fastcall *KeReleaseInStackQueuedSpinLockFn)(PKLOCK_QUEUE_HANDLE);
//KeReleaseInStackQueuedSpinLockFn release_lock = NULL;
typedef PPEB32(_fastcall *PsGetProcessWow64ProcessFn)(PEPROCESS a1);
PsGetProcessPebFn get_proc_peb = NULL;
PsGetProcessWow64ProcessFn get_proc_wow64_proc = NULL;
typedef NTKERNELAPI PVOID (*PsGetProcessSectionBaseAddressFn)(__in PEPROCESS Process);
PsGetProcessSectionBaseAddressFn get_proc_section_base_addr = NULL;
//typedef NTSTATUS(_fastcall *PsSuspendThreadFn)(PETHREAD, PULONG);
//PsSuspendThreadFn suspend_thread = NULL;
//typedef NTSTATUS(_fastcall *PsResumeThreadFn)(PETHREAD, PULONG);
//PsResumeThreadFn resume_thread = NULL;
typedef NTSTATUS(_fastcall *PsGetContextThreadFn)(PETHREAD, PCONTEXT, KPROCESSOR_MODE);
PsGetContextThreadFn get_ctx_thread = NULL;
typedef NTSTATUS(_fastcall *PsSetContextThreadFn)(PETHREAD, PCONTEXT, KPROCESSOR_MODE);
PsSetContextThreadFn set_ctx_thread = NULL;
typedef enum _KAPC_ENVIRONMENT
{
	OriginalApcEnvironment,
	AttachedApcEnvironment,
	CurrentApcEnvironment,
	InsertApcEnvironment
} KAPC_ENVIRONMENT, *PKAPC_ENVIRONMENT;
typedef NTKERNELAPI VOID(NTAPI *KeInitializeApcFn)(PKAPC Apc,PKTHREAD Thread,KAPC_ENVIRONMENT Environment, void *KernelRoutine, void *RundownRoutine, void *NormalRoutine, KPROCESSOR_MODE ProcessorMode, PVOID NormalContext);
KeInitializeApcFn init_apc = NULL;
typedef LPSTR(NTAPI *PsGetProcessImageFileNameFn)(PEPROCESS Process);
PsGetProcessImageFileNameFn get_proc_image_file_name = NULL;
typedef NTKERNELAPI BOOLEAN(NTAPI *KeInsertQueueApcFn)(__inout PRKAPC Apc, __in_opt PVOID SystemArgument1, __in_opt PVOID SystemArgument2, __in KPRIORITY Increment);
KeInsertQueueApcFn insert_queue_apc = NULL;

#define SIOCTL_TYPE 40000
#define IOCTL_READ_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2048, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_WRITE_PROC_MEM CTL_CODE(SIOCTL_TYPE, 2049, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_GET_MODULES CTL_CODE(SIOCTL_TYPE, 2050, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_HIJACK_THREAD CTL_CODE(SIOCTL_TYPE, 2051, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_ALLOC_VIRTUAL_MEM CTL_CODE(SIOCTL_TYPE, 2052, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_FREE_VIRTUAL_MEM CTL_CODE(SIOCTL_TYPE, 2053, METHOD_BUFFERED, FILE_ANY_ACCESS)
#define IOCTL_SUSPEND_THREAD CTL_CODE(SIOCTL_TYPE, 2054, METHOD_BUFFERED, FILE_ANY_ACCESS)  // TODO(Eric McDonald): Implement this IOCTL code.
#define IOCTL_RESUME_THREAD CTL_CODE(SIOCTL_TYPE, 2055, METHOD_BUFFERED, FILE_ANY_ACCESS)  // TODO(Eric McDonald): Implement this IOCTL code.
#define IOCTL_INJECT_LIB CTL_CODE(SIOCTL_TYPE, 2056, METHOD_BUFFERED, FILE_ANY_ACCESS)

struct MemoryIoRequest {
	DWORD proc_id;
	void *base_addr;
	void *buffer;
	size_t buffer_sz;
};
struct Image {
	wchar_t full_img_name[MAX_PATH];
	HANDLE proc_id;
	ULONGLONG base_addr;
	SIZE_T size;
};
struct GetModulesRequest {
	DWORD proc_id;
	struct Image loaded_images_buf[256];
	size_t loaded_images_ret;
};
struct HijackThreadRequest {
	DWORD proc_id;
	DWORD thread_id;
	void *new_rip;
};
struct AllocVirtualMemoryRequest {
	DWORD proc_id;
	void *base_addr;
	ULONG_PTR zero_bits;
	SIZE_T region_sz;
	DWORD alloc_type;
	DWORD protect;
};
struct FreeVirtualMemoryRequest {
	DWORD proc_id;
	void *base_addr;
	SIZE_T region_sz;
	DWORD free_type;
};
struct SuspendThreadRequest {
	DWORD thread_id;
	LONG prev_suspend_count;
	void *zw_suspend_thread_ptr;
};
struct ResumeThreadRequest {
	DWORD thread_id;
	LONG suspend_count;
	void *zw_resume_thread_ptr;
};
typedef HMODULE(WINAPI *LoadLibraryFn)(_In_ LPCWSTR lpFileName);
struct InjectLibRequest {
	// Do not use TCHAR as it is defined as char in x64 drivers.
	wchar_t proc_filename[MAX_PATH + 1];
	wchar_t lib_path[MAX_PATH + 1];
	LoadLibraryFn load_lib_func;
};

//struct Image loaded_images[8192] = { 0 };
//size_t loaded_images_sz = 0;
//static const ULONG kMaxSemCount = 32;
//KSEMAPHORE thread_semaphore;
//KAPC thread_apc;

struct InjectLibRequest *inject_queue;
size_t inject_queue_count;
