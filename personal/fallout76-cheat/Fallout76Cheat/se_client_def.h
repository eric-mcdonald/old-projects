#pragma once

#include "se_dt_def.h"
#include "se_iclient_def.h"

namespace source_engine {

	struct ClientClass;
	typedef void *(*CreateClientClassFn)(int entnum, int serialNum);
	typedef void *(*CreateEventFn)();
	class IBaseClientDLL {
	public:
		// Called once when the client DLL is loaded
		virtual int				Init(void *appSystemFactory,
			void *physicsFactory,
			void *pGlobals) = 0;
		virtual void			PostInit() = 0;
		// Called once when the client DLL is being unloaded
		virtual void			Shutdown(void) = 0;
		// Called once the client is initialized to setup client-side replay interface pointers
		virtual bool			ReplayInit(void *replayFactory) = 0;
		virtual bool			ReplayPostInit() = 0;
		// Called at the start of each level change
		virtual void			LevelInitPreEntity(char const* pMapName) = 0;
		// Called at the start of a new level, after the entities have been received and created
		virtual void			LevelInitPostEntity() = 0;
		// Called at the end of a level
		virtual void			LevelShutdown(void) = 0;
		// Request a pointer to the list of client datatable classes
		virtual ClientClass		*GetAllClasses(void) = 0;
		// Called once per level to re-initialize any hud element drawing stuff
		virtual int				HudVidInit(void) = 0;
		// Called by the engine when gathering user input
		virtual void			HudProcessInput(bool bActive) = 0;
		// Called oncer per frame to allow the hud elements to think
		virtual void			HudUpdate(bool bActive) = 0;
		// Reset the hud elements to their initial states
		virtual void			HudReset(void) = 0;
		// Display a hud text message
		virtual void			HudText(const char * message) = 0;
		// Mouse Input Interfaces
		// Activate the mouse (hides the cursor and locks it to the center of the screen)
		virtual void			IN_ActivateMouse(void) = 0;
		// Deactivates the mouse (shows the cursor and unlocks it)
		virtual void			IN_DeactivateMouse(void) = 0;
		// This is only called during extra sound updates and just accumulates mouse x, y offets and recenters the mouse.
		//  This call is used to try to prevent the mouse from appearing out of the side of a windowed version of the engine if 
		//  rendering or other processing is taking too long
		virtual void			IN_Accumulate(void) = 0;
		// Reset all key and mouse states to their initial, unpressed state
		virtual void			IN_ClearStates(void) = 0;
		// If key is found by name, returns whether it's being held down in isdown, otherwise function returns false
		virtual bool			IN_IsKeyDown(const char *name, bool& isdown) = 0;
		// Notify the client that the mouse was wheeled while in game - called prior to executing any bound commands.
		virtual void			IN_OnMouseWheeled(int nDelta) = 0;
		// Raw keyboard signal, if the client .dll returns 1, the engine processes the key as usual, otherwise,
		//  if the client .dll returns 0, the key is swallowed.
		virtual int				IN_KeyEvent(int eventcode, int keynum, const char *pszCurrentBinding) = 0;
		// This function is called once per tick to create the player CUserCmd (used for prediction/physics simulation of the player)
		// Because the mouse can be sampled at greater than the tick interval, there is a separate input_sample_frametime, which
		//  specifies how much additional mouse / keyboard simulation to perform.
		virtual void Padding0() = 0; // Eric McDonald: vtable index 21
		virtual void			CreateMove(
			int sequence_number,			// sequence_number of this cmd
			float input_sample_frametime,	// Frametime for mouse input sampling
			bool active) = 0;				// True if the player is active (not paused)
	};
	enum ClassIds {
		CAI_BaseNPC = 0,
		CAK47,
		CBaseAnimating,
		CBaseAnimatingOverlay,
		CBaseAttributableItem,
		CBaseButton,
		CBaseCombatCharacter,
		CBaseCombatWeapon,
		CBaseCSGrenade,
		CBaseCSGrenadeProjectile,
		CBaseDoor,
		CBaseEntity,
		CBaseFlex,
		CBaseGrenade,
		CBaseParticleEntity,
		CBasePlayer,
		CBasePropDoor,
		CBaseTeamObjectiveResource,
		CBaseTempEntity,
		CBaseToggle,
		CBaseTrigger,
		CBaseViewModel,
		CBaseVPhysicsTrigger,
		CBaseWeaponWorldModel,
		CBeam,
		CBeamSpotlight,
		CBoneFollower,
		CBRC4Target,
		CBreachCharge,
		CBreachChargeProjectile,
		CBreakableProp,
		CBreakableSurface,
		CBumpMine,
		CBumpMineProjectile,
		CC4,
		CCascadeLight,
		CChicken,
		CColorCorrection,
		CColorCorrectionVolume,
		CCSGameRulesProxy,
		CCSPlayer,
		CCSPlayerResource,
		CCSRagdoll,
		CCSTeam,
		CDangerZone,
		CDangerZoneController,
		CDEagle,
		CDecoyGrenade,
		CDecoyProjectile,
		CDrone,
		CDronegun,
		CDynamicLight,
		CDynamicProp,
		CEconEntity,
		CEconWearable,
		CEmbers,
		CEntityDissolve,
		CEntityFlame,
		CEntityFreezing,
		CEntityParticleTrail,
		CEnvAmbientLight,
		CEnvDetailController,
		CEnvDOFController,
		CEnvGasCanister,
		CEnvParticleScript,
		CEnvProjectedTexture,
		CEnvQuadraticBeam,
		CEnvScreenEffect,
		CEnvScreenOverlay,
		CEnvTonemapController,
		CEnvWind,
		CFEPlayerDecal,
		CFireCrackerBlast,
		CFireSmoke,
		CFireTrail,
		CFish,
		CFists,
		CFlashbang,
		CFogController,
		CFootstepControl,
		CFunc_Dust,
		CFunc_LOD,
		CFuncAreaPortalWindow,
		CFuncBrush,
		CFuncConveyor,
		CFuncLadder,
		CFuncMonitor,
		CFuncMoveLinear,
		CFuncOccluder,
		CFuncReflectiveGlass,
		CFuncRotating,
		CFuncSmokeVolume,
		CFuncTrackTrain,
		CGameRulesProxy,
		CGrassBurn,
		CHandleTest,
		CHEGrenade,
		CHostage,
		CHostageCarriableProp,
		CIncendiaryGrenade,
		CInferno,
		CInfoLadderDismount,
		CInfoMapRegion,
		CInfoOverlayAccessor,
		CItem_Healthshot,
		CItemCash,
		CItemDogtags,
		CKnife,
		CKnifeGG,
		CLightGlow,
		CMaterialModifyControl,
		CMelee,
		CMolotovGrenade,
		CMolotovProjectile,
		CMovieDisplay,
		CParadropChopper,
		CParticleFire,
		CParticlePerformanceMonitor,
		CParticleSystem,
		CPhysBox,
		CPhysBoxMultiplayer,
		CPhysicsProp,
		CPhysicsPropMultiplayer,
		CPhysMagnet,
		CPhysPropAmmoBox,
		CPhysPropLootCrate,
		CPhysPropRadarJammer,
		CPhysPropWeaponUpgrade,
		CPlantedC4,
		CPlasma,
		CPlayerPing,
		CPlayerResource,
		CPointCamera,
		CPointCommentaryNode,
		CPointWorldText,
		CPoseController,
		CPostProcessController,
		CPrecipitation,
		CPrecipitationBlocker,
		CPredictedViewModel,
		CProp_Hallucination,
		CPropCounter,
		CPropDoorRotating,
		CPropJeep,
		CPropVehicleDriveable,
		CRagdollManager,
		CRagdollProp,
		CRagdollPropAttached,
		CRopeKeyframe,
		CSCAR17,
		CSceneEntity,
		CSensorGrenade,
		CSensorGrenadeProjectile,
		CShadowControl,
		CSlideshowDisplay,
		CSmokeGrenade,
		CSmokeGrenadeProjectile,
		CSmokeStack,
		CSnowball,
		CSnowballPile,
		CSnowballProjectile,
		CSpatialEntity,
		CSpotlightEnd,
		CSprite,
		CSpriteOriented,
		CSpriteTrail,
		CStatueProp,
		CSteamJet,
		CSun,
		CSunlightShadowControl,
		CSurvivalSpawnChopper,
		CTablet,
		CTeam,
		CTeamplayRoundBasedRulesProxy,
		CTEArmorRicochet,
		CTEBaseBeam,
		CTEBeamEntPoint,
		CTEBeamEnts,
		CTEBeamFollow,
		CTEBeamLaser,
		CTEBeamPoints,
		CTEBeamRing,
		CTEBeamRingPoint,
		CTEBeamSpline,
		CTEBloodSprite,
		CTEBloodStream,
		CTEBreakModel,
		CTEBSPDecal,
		CTEBubbles,
		CTEBubbleTrail,
		CTEClientProjectile,
		CTEDecal,
		CTEDust,
		CTEDynamicLight,
		CTEEffectDispatch,
		CTEEnergySplash,
		CTEExplosion,
		CTEFireBullets,
		CTEFizz,
		CTEFootprintDecal,
		CTEFoundryHelpers,
		CTEGaussExplosion,
		CTEGlowSprite,
		CTEImpact,
		CTEKillPlayerAttachments,
		CTELargeFunnel,
		CTEMetalSparks,
		CTEMuzzleFlash,
		CTEParticleSystem,
		CTEPhysicsProp,
		CTEPlantBomb,
		CTEPlayerAnimEvent,
		CTEPlayerDecal,
		CTEProjectedDecal,
		CTERadioIcon,
		CTEShatterSurface,
		CTEShowLine,
		CTesla,
		CTESmoke,
		CTESparks,
		CTESprite,
		CTESpriteSpray,
		CTest_ProxyToggle_Networkable,
		CTestTraceline,
		CTEWorldDecal,
		CTriggerPlayerMovement,
		CTriggerSoundOperator,
		CVGuiScreen,
		CVoteController,
		CWaterBullet,
		CWaterLODControl,
		CWeaponAug,
		CWeaponAWP,
		CWeaponBaseItem,
		CWeaponBizon,
		CWeaponCSBase,
		CWeaponCSBaseGun,
		CWeaponCycler,
		CWeaponElite,
		CWeaponFamas,
		CWeaponFiveSeven,
		CWeaponG3SG1,
		CWeaponGalil,
		CWeaponGalilAR,
		CWeaponGlock,
		CWeaponHKP2000,
		CWeaponM249,
		CWeaponM3,
		CWeaponM4A1,
		CWeaponMAC10,
		CWeaponMag7,
		CWeaponMP5Navy,
		CWeaponMP7,
		CWeaponMP9,
		CWeaponNegev,
		CWeaponNOVA,
		CWeaponP228,
		CWeaponP250,
		CWeaponP90,
		CWeaponSawedoff,
		CWeaponSCAR20,
		CWeaponScout,
		CWeaponSG550,
		CWeaponSG552,
		CWeaponSG556,
		CWeaponShield,
		CWeaponSSG08,
		CWeaponTaser,
		CWeaponTec9,
		CWeaponTMP,
		CWeaponUMP45,
		CWeaponUSP,
		CWeaponXM1014,
		CWorld,
		CWorldVguiText,
		DustTrail,
		MovieExplosion,
		ParticleSmokeGrenade,
		RocketTrail,
		SmokeTrail,
		SporeExplosion,
		SporeTrail,
	};
	struct ClientClass {
		CreateClientClassFn		m_pCreateFn;
		CreateEventFn			m_pCreateEventFn;	// Only called for event objects.
		const char				*m_pNetworkName;
		RecvTable				*m_pRecvTable;
		ClientClass				*m_pNext;
		int						m_ClassID;	// Managed by the engine.
	};
	struct VMatrix {
		float buffer[4][4];
	};
	class IVEngineClient013
	{
	public:
		// Find the model's surfaces that intersect the given sphere.
		// Returns the number of surfaces filled in.
		virtual int					GetIntersectingSurfaces(
			const void *model,
			const Vector &vCenter,
			const float radius,
			const bool bOnlyVisibleSurfaces,	// Only return surfaces visible to vCenter.
			void *pInfos,
			const int nMaxInfos) = 0;

		// Get the lighting intensivty for a specified point
		// If bClamp is specified, the resulting Vector is restricted to the 0.0 to 1.0 for each element
		virtual Vector				GetLightForPoint(const Vector &pos, bool bClamp) = 0;

		// Traces the line and reports the material impacted as well as the lighting information for the impact point
		virtual void			*TraceLineMaterialAndLighting(const Vector &start, const Vector &end,
			Vector &diffuseLightColor, Vector& baseColor) = 0;

		// Given an input text buffer data pointer, parses a single token into the variable token and returns the new
		//  reading position
		virtual const char			*ParseFile(const char *data, char *token, int maxlen) = 0;
		virtual bool				CopyLocalFile(const char *source, const char *destination) = 0;

		// Gets the dimensions of the game window
		virtual void				GetScreenSize(int& width, int& height) = 0;

		// Forwards szCmdString to the server, sent reliably if bReliable is set
		virtual void				ServerCmd(const char *szCmdString, bool bReliable = true) = 0;
		// Inserts szCmdString into the command buffer as if it was typed by the client to his/her console.
		// Note: Calls to this are checked against FCVAR_CLIENTCMD_CAN_EXECUTE (if that bit is not set, then this function can't change it).
		//       Call ClientCmd_Unrestricted to have access to FCVAR_CLIENTCMD_CAN_EXECUTE vars.
		virtual void				ClientCmd(const char *szCmdString) = 0;

		// Fill in the player info structure for the specified player index (name, model, etc.)
		virtual bool				GetPlayerInfo(int ent_num, void *pinfo) = 0;

		// Retrieve the player entity number for a specified userID
		virtual int					GetPlayerForUserID(int userID) = 0;

		// Retrieves text message system information for the specified message by name
		virtual void *TextMessageGet(const char *pName) = 0;

		// Returns true if the console is visible
		virtual bool				Con_IsVisible(void) = 0;

		// Get the entity index of the local player
		virtual int					GetLocalPlayer(void) = 0;  // Eric McDonald: Index 12

		// Client DLL is hooking a model, loads the model into memory and returns  pointer to the model_t
		virtual const void		*LoadModel(const char *pName, bool bProp = false) = 0;

		// Get accurate, sub-frame clock ( profiling use )
		//virtual float				Time(void) = 0;

		// Get the exact server timesstamp ( server time ) from the last message received from the server
		virtual float				GetLastTimeStamp(void) = 0;

		// Given a CAudioSource (opaque pointer), retrieve the underlying CSentence object ( stores the words, phonemes, and close
		//  captioning data )
		virtual void			*GetSentence(void *pAudioSource) = 0;
		// Given a CAudioSource, determines the length of the underlying audio file (.wav, .mp3, etc.)
		virtual float				GetSentenceLength(void *pAudioSource) = 0;
		// Returns true if the sound is streaming off of the hard disk (instead of being memory resident)
		virtual bool				IsStreaming(void *pAudioSource) const = 0;

		// Copy current view orientation into va
		virtual void				GetViewAngles(QAngle& va) = 0;  // Eric McDonald: Index 18
		// Set current view orientation from va
		virtual void				SetViewAngles(QAngle& va) = 0;

		// Retrieve the current game's maxclients setting
		virtual int					GetMaxClients(void) = 0;

		// Given the string pBinding which may be bound to a key, 
		//  returns the string name of the key to which this string is bound. Returns NULL if no such binding exists
		virtual	const char			*Key_LookupBinding(const char *pBinding) = 0;

		// Given the name of the key "mouse1", "e", "tab", etc., return the string it is bound to "+jump", "impulse 50", etc.
		virtual const char			*Key_BindingForKey(int code) = 0;

		// key trapping (for binding keys)
		virtual void				StartKeyTrapMode(void) = 0;
		virtual bool				CheckDoneKeyTrapping(int &code) = 0;

		// Returns true if the player is fully connected and active in game (i.e, not still loading)
		virtual bool				IsInGame(void) = 0;
		// Returns true if the player is connected, but not necessarily active in game (could still be loading)
		virtual bool				IsConnected(void) = 0;
		// Returns true if the loading plaque should be drawn
		virtual bool				IsDrawingLoadingImage(void) = 0;

		// Prints the formatted string to the notification area of the screen ( down the right hand edge
		//  numbered lines starting at position 0
		virtual void				Con_NPrintf(int pos, const char *fmt, ...) = 0;
		// Similar to Con_NPrintf, but allows specifying custom text color and duration information
		virtual void				Con_NXPrintf(const struct con_nprint_s *info, const char *fmt, ...) = 0;

		// Is the specified world-space bounding box inside the view frustum?
		virtual int					IsBoxVisible(const Vector& mins, const Vector& maxs) = 0;

		// Is the specified world-space boudning box in the same PVS cluster as the view origin?
		virtual int					IsBoxInViewCluster(const Vector& mins, const Vector& maxs) = 0;

		// Returns true if the specified box is outside of the view frustum and should be culled
		virtual bool				CullBox(const Vector& mins, const Vector& maxs) = 0;

		// Allow the sound system to paint additional data (during lengthy rendering operations) to prevent stuttering sound.
		virtual void				Sound_ExtraUpdate(void) = 0;

		// Get the current game directory ( e.g., hl2, tf2, cstrike, hl1 )
		virtual const char			*GetGameDirectory(void) = 0;

		// Get access to the world to screen transformation matrix
		virtual const VMatrix& 		WorldToScreenMatrix() = 0;
	};
	// TODO(Eric McDonald): Figure out if these vtable indexes are correct.

	struct GlowObjectDefinition_t {
		void* m_hEntity;
		Vector m_vGlowColor;
		float m_flGlowAlpha;
		char padding0[4];
		float flUnk;  // Confirmed to be treated as a float while reversing glow functions 
		float m_flBloomAmount;
		float localplayeriszeropoint3;
		bool m_bRenderWhenOccluded;
		bool m_bRenderWhenUnoccluded;
		bool m_bFullBloomRender;
		char padding1[1];
		int m_nFullBloomStencilTestValue;  // 0x28 
		int iUnk;  // Appears like it needs to be zero  
		int m_nSplitScreenSlot;  // Should be -1 
		int m_nNextFreeSlot;  // Linked list of free slots 
		static const int END_OF_FREE_LIST = -1;
		static const int ENTRY_IN_USE = -2;
	};
	enum SignonStates {
		kNone = 0,
		kChallenge = 1,
		kConnected = 2,
		kNew = 3,
		kPrespawn = 4,
		kSpawn = 5,
		kFull = 6,
		kChangeLevel = 7
	};

	static constexpr char *kClientDllVer = "VClient018", *kEngineClientVer = "VEngineClient014";

}  // namespace source_engine
