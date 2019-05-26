#pragma once

#include "se_render_def.h"
#include "se_client_def.h"

namespace source_engine {

	struct CreateMoveData {
		int sequence_number;
		float input_sample_frametime;
		bool active;
	};
	struct ApplyEntityGlowEffectsData {
		void *this_ptr;
		void *edx;
		const void *pSetup;
		int nSplitScreenSlot;
		void *pRenderContext;
		float flBloomScale;
		int x;
		int y;
		int w;
		int h;
	};
	struct FrameStageNotifyData {
		void *ecx;
		void *edx;
		ClientFrameStage_t curStage;
	};

}  // namespace source_engine
