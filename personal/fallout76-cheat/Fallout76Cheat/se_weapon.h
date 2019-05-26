#pragma once

#include <cstdint>

#include "se_entity_wrapper.h"

namespace source_engine {

	enum WeaponIds {
		WEAPON_NONE = 0,
		WEAPON_DEAGLE,
		WEAPON_ELITE,
		WEAPON_FIVESEVEN,
		WEAPON_GLOCK,
		WEAPON_AK47 = 7,
		WEAPON_AUG,
		WEAPON_AWP,
		WEAPON_FAMAS,
		WEAPON_G3SG1,
		WEAPON_GALILAR = 13,
		WEAPON_M249,
		WEAPON_M4A1 = 16,
		WEAPON_MAC10,
		WEAPON_P90 = 19,
		WEAPON_MP5SD = 23,
		WEAPON_UMP45,
		WEAPON_XM1014,
		WEAPON_BIZON,
		WEAPON_MAG7,
		WEAPON_NEGEV,
		WEAPON_SAWEDOFF,
		WEAPON_TEC9,
		WEAPON_TASER,
		WEAPON_HKP2000,
		WEAPON_MP7,
		WEAPON_MP9,
		WEAPON_NOVA,
		WEAPON_P250,
		WEAPON_SHIELD,
		WEAPON_SCAR20,
		WEAPON_SG556,
		WEAPON_SSG08,
		WEAPON_KNIFEGG,
		WEAPON_KNIFE,
		WEAPON_FLASHBANG,
		WEAPON_HEGRENADE,
		WEAPON_SMOKEGRENADE,
		WEAPON_MOLOTOV,
		WEAPON_DECOY,
		WEAPON_INCGRENADE,
		WEAPON_C4,
		WEAPON_HEALTHSHOT = 57,
		WEAPON_KNIFE_T = 59,
		WEAPON_M4A1_SILENCER,
		WEAPON_USP_SILENCER,
		WEAPON_CZ75A = 63,
		WEAPON_REVOLVER,
		WEAPON_TAGRENADE = 68,
		WEAPON_FISTS,
		WEAPON_BREACHCHARGE,
		WEAPON_TABLET = 72,
		WEAPON_MELEE = 74,
		WEAPON_AXE,
		WEAPON_HAMMER,
		WEAPON_SPANNER = 78,
		WEAPON_KNIFE_GHOST = 80,
		WEAPON_FIREBOMB,
		WEAPON_DIVERSION,
		WEAPON_FRAG_GRENADE,
		WEAPON_SNOWBALL,
		WEAPON_BUMPMINE,
		WEAPON_BAYONET = 500,
		WEAPON_KNIFE_FLIP = 505,
		WEAPON_KNIFE_GUT,
		WEAPON_KNIFE_KARAMBIT,
		WEAPON_KNIFE_M9_BAYONET,
		WEAPON_KNIFE_TACTICAL,
		WEAPON_KNIFE_FALCHION = 512,
		WEAPON_KNIFE_SURVIVAL_BOWIE = 514,
		WEAPON_KNIFE_BUTTERFLY,
		WEAPON_KNIFE_PUSH,
		WEAPON_KNIFE_URSUS = 519,
		WEAPON_KNIFE_GYPSY_JACKKNIFE,
		WEAPON_KNIFE_STILETTO = 522,
		WEAPON_KNIFE_WIDOWMAKER
	};
	struct CCSWeaponInfo {
		char pad_vtable[0x4]; // 0x0
		char *consoleName; // 0x4
		char pad_0[0xc];// 0x8
		int32_t iMaxClip1; // 0x14
		int32_t iMaxClip2; // 0x18
		int32_t iDefaultClip1; // 0x1c
		int32_t iDefaultClip2; // 0x20
		int32_t iPrimaryReserveAmmoMax; // 0x24
		int32_t iSecondaryReserveAmmoMax; // 0x28
		char *szWorldModel; // 0x2c
		char *szViewModel; // 0x30
		char *szDroppedModel; // 0x34
		char pad_9[0x50];// 0x38
		char *szHudName; // 0x88
		char *szWeaponName; // 0x8c
		char pad_11[0x2];// 0x90
		bool bIsMeleeWeapon; // 0x92
		char pad_12[0x9];// 0x93
		float flWeaponWeight; // 0x9c
		char pad_13[0x2c];// 0xa0
		int32_t iWeaponType; // 0xcc
		int32_t iWeaponPrice; // 0xd0
		int32_t iKillAward; // 0xd4
		char pad_16[0x4];// 0xd8
		float flCycleTime; // 0xdc
		float flCycleTimeAlt; // 0xe0
		char pad_18[0x8];// 0xe4
		bool bFullAuto; // 0xec
		char pad_19[0x3];// 0xed
		int32_t iDamage; // 0xf0
		float flArmorRatio; // 0xf4
		int32_t iBullets; // 0xf8
		float flPenetration; // 0xfc
		char pad_23[0x8];// 0x100
		float flWeaponRange; // 0x108
		float flRangeModifier; // 0x10c
		float flThrowVelocity; // 0x110
		char pad_26[0xc];// 0x114
		bool bHasSilencer; // 0x120
		char pad_27[0xb];// 0x121
		char *szBulletType; // 0x12c
		float flMaxSpeed; // 0x130
		char pad_29[0x50];// 0x134
		int32_t iRecoilSeed; // 0x184
	};
	class WeaponWrapper : public EntityWrapper {
	public:
		WeaponWrapper(void *);
		CCSWeaponInfo *GetInfo() const;
		WeaponIds GetId() const;
	};

}  // namespace source_enginw
