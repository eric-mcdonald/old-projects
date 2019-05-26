/*
Copyright 2018 Eric McDonald

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#pragma once

#include "memory_stream.h"  // NOLINT
#include "overlay.h"  // NOLINT
#include "lanius.h"  // NOLINT

namespace lanius {

	class Game {
		MemoryStream &mem_stream_;
		Overlay &renderer_;
	public:
		Game(MemoryStream &, Overlay &);
		Game(const Game &);
		MemoryStream &mem_stream() const;
		bool IsRunning() const;
		bool operator==(const Game &) const;
		bool operator!=(const Game &) const;
		Overlay &renderer() const;
		// TODO(Eric McDonald): Add more functionality related to a game (cached data, associated cheat instance, etc) here:

	};

}  // namespace lanius
