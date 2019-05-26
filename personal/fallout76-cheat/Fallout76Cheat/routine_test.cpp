#include "stdafx.h"

#include "event_manager.h"
#include "se_event_impl.h"
#include "se_client_def.h"
#include "cheat.h"
#include "signatures.h"
#include "source_engine.h"
#include "native_utils.h"
#include "math_helper.h"
#include "vector3d.h"
#include "se_player_wrapper.h"
#include "se_offset_cache.h"
#include "se_utils.h"

#include "routine_test.h"

namespace cheat {

	namespace test {

		inline void ApplyGlow(source_engine::GlowObjectDefinition_t *glow_def, const Vector3d<float> &color, float alpha) {
			source_engine::Vector color_vec;
			color_vec.x = color.get_x();
			color_vec.y = color.get_y();
			color_vec.z = color.get_z();
			glow_def->m_vGlowColor = color_vec;
			glow_def->m_flGlowAlpha = alpha;
			glow_def->m_bRenderWhenOccluded = true;
			glow_def->m_bRenderWhenUnoccluded = false;
			glow_def->m_flBloomAmount = 1.0F;
		}
		inline Vector3d<float> CalcHealthColor(int health) {
			float health_percent = LimitBetween(health / 100.0F, 0.0F, 1.0F);
			return Vector3d<float>(1.0F - health_percent, health_percent, 0.0F);
		}
		ErrorCode OnApplyEntityGlowEffects(Event *event_obj) {
			source_engine::ApplyEntityGlowEffectsData *event_data = reinterpret_cast<source_engine::ApplyEntityGlowEffectsData*>(event_obj->data);
			if (source_engine::OffsetCache::GetInstance().glow_obj_defs_offs == nullptr || *source_engine::OffsetCache::GetInstance().glow_obj_defs_offs == nullptr) {
				return CheatErrorCodes::kInvalidAddr;
			}
			int glow_count = *reinterpret_cast<int*>(reinterpret_cast<DWORD_PTR>(source_engine::OffsetCache::GetInstance().glow_obj_defs_offs) + 0x4);
			if (glow_count <= 0) {
				return CheatErrorCodes::kNotEnoughElems;
			}
			void *local_player_ptr;
			ErrorCode error = source_engine::GetLocalPlayer(&local_player_ptr);
			if (error != CheatErrorCodes::kSuccess) {
				return error;
			}
			source_engine::PlayerWrapper local_player(local_player_ptr);
			if (!local_player.IsAlive()) {
				return CheatErrorCodes::kValInvalidState;
			}
			int local_team = local_player.GetTeam();
			static Routine *routine = nullptr;
			if (routine == nullptr) {
				error = Cheat::GetInstance().GetRoutine("test", &routine);
				if (error != CheatErrorCodes::kSuccess) {
					return error;
				}
			}
			bool glow_enemy = routine->get_config()->GetEntry<bool>("glow_enemy"), glow_team = routine->get_config()->GetEntry<bool>("glow_team");
			bool glow_enemy_col_health = routine->get_config()->GetEntry<bool>("glow_enemy_col_health"), glow_team_col_health = routine->get_config()->GetEntry<bool>("glow_team_col_health");
			Vector3d<float> glow_enemy_col_static = routine->get_config()->GetEntry<Vector3d<float>>("glow_enemy_col_static"), glow_team_col_static = routine->get_config()->GetEntry<Vector3d<float>>("glow_team_col_static");
			float glow_enemy_alpha_static = routine->get_config()->GetEntry<float>("glow_enemy_alpha_static"), glow_team_alpha_static = routine->get_config()->GetEntry<float>("glow_team_alpha_static");
			for (int i = 0; i < glow_count; ++i) {
				source_engine::GlowObjectDefinition_t *glow_ent_def = &((*source_engine::OffsetCache::GetInstance().glow_obj_defs_offs)[i]);
				if (glow_ent_def == nullptr || glow_ent_def->m_nNextFreeSlot != source_engine::GlowObjectDefinition_t::ENTRY_IN_USE || glow_ent_def->m_hEntity == local_player_ptr) {
					continue;
				}
				source_engine::PlayerWrapper glow_player(glow_ent_def->m_hEntity);
				source_engine::ClassIds class_id;
				error = glow_player.GetClassId(&class_id);
				if (error != CheatErrorCodes::kSuccess || class_id != source_engine::ClassIds::CCSPlayer) {
					continue;
				}
				if (!glow_player.IsAlive()) {
					continue;
				}
				Vector3d<float> color;
				float alpha;
				if (glow_player.GetTeam() == local_team) {
					if (!glow_team) {
						continue;
					}
					color = glow_team_col_health ? CalcHealthColor(glow_player.GetHealth()) : glow_team_col_static;
					alpha = glow_team_alpha_static;
				}
				else {
					if (!glow_enemy) {
						continue;
					}
					color = glow_enemy_col_health ? CalcHealthColor(glow_player.GetHealth()) : glow_enemy_col_static;
					alpha = glow_enemy_alpha_static;
				}
				ApplyGlow(glow_ent_def, color, alpha);
			}
			return CheatErrorCodes::kSuccess;
		}
		TestRoutine::TestRoutine() : BasicRoutine(false, "test_routine.cfg", "test", "Test description.") {
			get_config()->SetEntry("glow_enemy", true);
			get_config()->SetEntry("glow_team", false);
			get_config()->SetEntry("glow_enemy_col_health", true);
			get_config()->SetEntry("glow_team_col_health", false);
			get_config()->SetEntry("glow_enemy_col_static", Vector3d<float>(1.0F, 0.0F, 0.0F));
			get_config()->SetEntry("glow_team_col_static", Vector3d<float>(0.0F, 0.0F, 1.0F));
			get_config()->SetEntry("glow_enemy_alpha_static", 1.0F);
			get_config()->SetEntry("glow_team_alpha_static", 1.0F);
		}
		void TestRoutine::AddListeners(std::vector<RoutineListener> &listeners) {
			RoutineListener listener;
			EventManager *glow_evt_manager;
			if (Cheat::GetInstance().GetEvtManager("HApplyEntityGlowEffects", &glow_evt_manager) != CheatErrorCodes::kSuccess) {
				return;
			}
			listener.event_bus = glow_evt_manager;
			listener.listener_func = OnApplyEntityGlowEffects;
			listeners.push_back(listener);
		}

	}  // namespace test

}  // namespace cheat
