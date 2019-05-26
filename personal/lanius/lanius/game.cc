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

#include "stdafx.h"  // NOLINT

#include "game.h"  // NOLINT

namespace lanius {

	Game::Game(MemoryStream &mem_stream, Overlay &renderer) : mem_stream_(mem_stream), renderer_(renderer) {}
	Game::Game(const Game &source) : Game(source.mem_stream_, source.renderer_) {}
	MemoryStream &Game::mem_stream() const {
		return mem_stream_;
	}
	bool Game::IsRunning() const {
		return mem_stream_.IsOpen();
	}
	bool Game::operator==(const Game &operand) const {
		return mem_stream_ == operand.mem_stream_;
	}
	bool Game::operator!=(const Game &operand) const {
		return !(*this == operand);
	}
	Overlay &Game::renderer() const {
		return renderer_;
	}

}  // namespace lanius
