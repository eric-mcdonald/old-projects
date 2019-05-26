#pragma once

namespace source_engine {

	struct RecvProp;
	struct RecvTable;
	typedef enum {
		DPT_Int = 0,
		DPT_Float,
		DPT_Vector,
		DPT_VectorXY, // Only encodes the XY of a vector, ignores Z
		DPT_String,
		DPT_Array,	// An array of the base types (can't be of datatables).
		DPT_DataTable,
#if 0 // We can't ship this since it changes the size of DTVariant to be 20 bytes instead of 16 and that breaks MODs!!!
		DPT_Quaternion,
#endif
#ifdef SUPPORTS_INT64
		DPT_Int64,
#endif
		DPT_NUMSendPropTypes
	} SendPropType;
	// This is passed into RecvProxy functions.
	struct CRecvProxyData {
		const RecvProp	*m_pRecvProp;		// The property it's receiving.
	};
	//-----------------------------------------------------------------------------
	// pStruct = the base structure of the datatable this variable is in (like C_BaseEntity)
	// pOut    = the variable that this this proxy represents (like C_BaseEntity::m_SomeValue).
	//
	// Convert the network-standard-type value in m_Value into your own format in pStruct/pOut.
	//-----------------------------------------------------------------------------
	typedef void(*RecvVarProxyFn)(const CRecvProxyData *pData, void *pStruct, void *pOut);
	// ------------------------------------------------------------------------ //
	// ArrayLengthRecvProxies are optionally used to get the length of the 
	// incoming array when it changes.
	// ------------------------------------------------------------------------ //
	typedef void(*ArrayLengthRecvProxyFn)(void *pStruct, int objectID, int currentArrayLength);
	// NOTE: DataTable receive proxies work differently than the other proxies.
	// pData points at the object + the recv table's offset.
	// pOut should be set to the location of the object to unpack the data table into.
	// If the parent object just contains the child object, the default proxy just does *pOut = pData.
	// If the parent object points at the child object, you need to dereference the pointer here.
	// NOTE: don't ever return null from a DataTable receive proxy function. Bad things will happen.
	typedef void(*DataTableRecvVarProxyFn)(const RecvProp *pProp, void **pOut, void *pData, int objectID);
	struct RecvProp {
		const char              *m_pVarName;
		SendPropType			m_RecvType;
		int						m_Flags;
		int						m_StringBufferSize;
		bool					m_bInsideArray;		// Set to true by the engine if this property sits inside an array.
													// Extra data that certain special property types bind to the property here.
		const void *m_pExtraData;
		// If this is an array (DPT_Array).
		RecvProp				*m_pArrayProp;
		ArrayLengthRecvProxyFn	m_ArrayLengthProxy;
		RecvVarProxyFn			m_ProxyFn;
		DataTableRecvVarProxyFn	m_DataTableProxyFn;	// For RDT_DataTable.
		RecvTable				*m_pDataTable;		// For RDT_DataTable.
		int						m_Offset;
		int						m_ElementStride;
		int						m_nElements;
		// If it's one of the numbered "000", "001", etc properties in an array, then
		// these can be used to get its array property name for debugging.
		const char				*m_pParentArrayPropName;
	};
	struct RecvTable {
		// Properties described in a table.
		RecvProp		*m_pProps;
		int				m_nProps;
		// The decoder. NOTE: this covers each RecvTable AND all its children (ie: its children
		// will have their own decoders that include props for all their children).
		void	*m_pDecoder;
		const char		*m_pNetTableName;	// The name matched between client and server.
		bool			m_bInitialized;
		bool			m_bInMainList;
	};

}  // namespace source_engine
