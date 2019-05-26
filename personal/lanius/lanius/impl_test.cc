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

#include "impl_test.h"  // NOLINT
#include "game.h"  // NOLINT

namespace lanius {

	namespace test {

		// TODO(Eric McDonald): Decide whether or not to include UE4 structures in the cheat (could be liable for copyright infringement).
		struct FVector {
			float x, y, z;
		};
		struct FVector2D {
			float x, y;
		};

		ErrorCode DoTestImpl() {
			// TODO(Eric McDonald): Move these variables off the stack to prevent them from being deallocated implicitly.
			WindowId fortnite_window_id("Fortnite", WindowIdTypes::kPrefix);
			Overlay overlay("Lanius Test Window", fortnite_window_id);
			MemoryStream fortnite_mem(kTargetProc);
			Game fortnite(fortnite_mem, overlay);

			if (!fortnite.IsRunning()) {
				return ErrorCodes::kProcNotFound;
			}
			ErrorCode error = ErrorCodes::kSuccess;
			ExternalAddr gworld_ptr = kNull;
			/*error = fortnite.mem_stream().FindSig("48 89 ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? ?? 75 13 8B 54 24 68 0F BA F2 07 81 CA ?? ?? ?? ?? E8", fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr, fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_sz, gworld_ptr);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			int gworld_dist = 0;
			error = fortnite.mem_stream().Read(gworld_ptr + 0x3, &gworld_dist, sizeof(gworld_dist));
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			gworld_ptr = gworld_ptr + 0x7 + gworld_dist;*/
			ExternalAddr project_world_to_screen = kNull;
			/*error = fortnite.mem_stream().FindSig("48 89 5C 24 ?? 48 89 6C 24 ?? 48 89 74 24 ?? 57 48 81 EC ?? ?? ?? ?? 41 0F B6 E9", fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr, fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_sz, project_world_to_screen);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}*/
			//std::cout << std::hex << gworld_ptr - fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr << "," << project_world_to_screen - fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr << std::endl;
			gworld_ptr = fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr + 0x4DDE2A0;
			project_world_to_screen = fortnite.mem_stream().proc_info().modules.find("FortniteClient-Win64-Shipping.exe")->second.module_addr + 0x220A7D0;
			Byte project_world_to_screen_shellcode[] = {
				0x50,  // push rax
				0x53,  // push rbx
				0x51,  // push rcx
				0x52,  // push rdx
				0x55,  // push rbp
				0x56,  // push rsi
				0x57,  // push rdi
				0x41, 0x50,  // push r8
				0x41, 0x51,  // push r9
				0x9C,  // pushf
				0x48, 0xA1, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC,  // mov rax,player_controller
				0x48, 0x89, 0xC1,  // mov rcx,rax
				0x48, 0xA1, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC,  // mov rax,world_pos
				0x48, 0x89, 0xC2,  // mov rdx,rax
				0x48, 0xA1, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC,  // mov rax,screen_pos
				0x48, 0x89, 0xC0,  // mov r8,rax
				0x49, 0xC7, 0xC1, 0x00, 0x00, 0x00, 0x00,  // mov r9,0
				0xE8, 0xCC, 0xCC, 0xCC, 0xCC,  // call project_world_to_screen
				0x48, 0xA3, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC, 0xCC,  // mov ret_val_ptr,rax
				0x9D,  // popf
				0x41, 0x59,  // pop r9
				0x41, 0x58,  // pop r8
				0x5F,  // pop rdi
				0x5E,  // pop rsi
				0x5D,  // pop rbp
				0x5A,  // pop rdx
				0x59,  // pop rcx
				0x5B,  // pop rbx
				0x58,  // pop rax
			};
			ExternalAddr w2s_shellcode_ptr = /*0xDEADBEEF*/kNull;
			error = fortnite.mem_stream().Allocate(w2s_shellcode_ptr, sizeof(project_world_to_screen_shellcode));
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			error = fortnite.mem_stream().Write(w2s_shellcode_ptr, project_world_to_screen_shellcode, sizeof(project_world_to_screen_shellcode));
			if (error != ErrorCodes::kSuccess) {
				return error;
			}

			//error = fortnite.mem_stream().Execute(project_world_to_screen);
			while (fortnite.IsRunning()) {
				ExternalAddr gworld = kNull;
				error = fortnite.mem_stream().Read(gworld_ptr, &gworld, sizeof(gworld));
				if (error != ErrorCodes::kSuccess) {
					return error;
				}
				if (gworld) {
					ExternalAddr game_instance = kNull;
					error = fortnite.mem_stream().Read(gworld + 0x190, &game_instance, sizeof(game_instance));
					if (error != ErrorCodes::kSuccess) {
						return error;
					}
					if (game_instance) {
						ExternalAddr local_players = kNull;
						error = fortnite.mem_stream().Read(game_instance + 0x38, &local_players, sizeof(local_players));
						if (error != ErrorCodes::kSuccess) {
							return error;
						}
						if (local_players) {
							size_t local_players_sz = 0U;
							error = fortnite.mem_stream().Read(game_instance + 0x38 + sizeof(local_players), &local_players_sz, sizeof(local_players_sz));
							if (error != ErrorCodes::kSuccess) {
								return error;
							}
							if (local_players_sz != 0U) {
								ExternalAddr client_player = kNull;  // The client player is the first ULocalPlayer instance in the LocalPlayers array.
								error = fortnite.mem_stream().Read(local_players + 0U * sizeof(client_player), &client_player, sizeof(client_player));
								if (error != ErrorCodes::kSuccess) {
									return error;
								}
								if (client_player) {
									ExternalAddr client_controller = kNull;
									error = fortnite.mem_stream().Read(client_player + 0x30, &client_controller, sizeof(client_controller));
									if (error != ErrorCodes::kSuccess) {
										return error;
									}
									if (client_controller) {
										for (size_t i = 1U; i < local_players_sz; ++i) {
											ExternalAddr player = kNull;
											error = fortnite.mem_stream().Read(local_players + i * sizeof(player), &player, sizeof(player));
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											if (!player) {
												continue;
											}
											ExternalAddr player_controller = kNull;
											/*error = */fortnite.mem_stream().Read(player + 0x30, &player_controller, sizeof(player_controller));
											//if (error != ErrorCodes::kSuccess) {
												//return error;
											//}
											if (!player_controller) {
												continue;
											}
											FVector position = { 0 };
											error = fortnite.mem_stream().Read(player + 0x70, &position, sizeof(position));
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											ExternalAddr world_pos_ptr = /*0xDEADCAFE*/kNull;
											ExternalAddr screen_pos_ptr = /*0xBABEDEAD*/kNull;
											FVector2D screen_pos = { 0 };
											static const unsigned long long kUninitRetVal = 0xCCCCCCCCCCCCCCCC;
											ExternalAddr ret_val_ptr = /*0xFEEDFACE*/kNull;
											size_t replace_count = 0;
											for (size_t i = 0; i < sizeof(project_world_to_screen_shellcode) && replace_count < 5; ++i) {
												if (i == 14) {
													error = fortnite.mem_stream().Write(w2s_shellcode_ptr + i, &client_controller, sizeof(client_controller));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													++replace_count;
												}
												else if (i == 27) {
													FVector world_pos = position;
													error = fortnite.mem_stream().Allocate(world_pos_ptr, sizeof(world_pos));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(world_pos_ptr, &world_pos, sizeof(world_pos));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(w2s_shellcode_ptr + i, &world_pos_ptr, sizeof(world_pos_ptr));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													++replace_count;
												}
												else if (i == 40) {
													error = fortnite.mem_stream().Allocate(screen_pos_ptr, sizeof(screen_pos));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(screen_pos_ptr, &screen_pos, sizeof(screen_pos));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(w2s_shellcode_ptr + i, &screen_pos_ptr, sizeof(screen_pos_ptr));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													++replace_count;
												}
												else if (i == 58) {
													int distance = static_cast<int>(project_world_to_screen - (w2s_shellcode_ptr + i + 0x5));
													error = fortnite.mem_stream().Write(w2s_shellcode_ptr + i + 0x1, &distance, sizeof(distance));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													++replace_count;
												}
												else if (i == 65) {
													unsigned long long uninit_ret_val = kUninitRetVal;  // TODO(Eric McDonald): Stop the write function from changing the buffer.
													error = fortnite.mem_stream().Allocate(ret_val_ptr, sizeof(uninit_ret_val));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(ret_val_ptr, &uninit_ret_val, sizeof(uninit_ret_val));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													error = fortnite.mem_stream().Write(w2s_shellcode_ptr + i, &ret_val_ptr, sizeof(ret_val_ptr));
													if (error != ErrorCodes::kSuccess) {
														return error;
													}
													++replace_count;
												}
											}
											if (replace_count < 5) {
												return ErrorCodes::kSuccess;
											}
											Byte read_shellcode[85] = { 0 };
											error = fortnite.mem_stream().Read(w2s_shellcode_ptr, read_shellcode, sizeof(read_shellcode));
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											for (size_t i = 0; i < sizeof(read_shellcode); ++i) {
												std::cout << std::hex << static_cast<unsigned int>(read_shellcode[i]) << "," << std::endl;
											}

											//error = fortnite.mem_stream().Execute(w2s_shellcode_ptr);
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											unsigned long long ret_val = kUninitRetVal;
											do {
												error = fortnite.mem_stream().Read(ret_val_ptr, &ret_val, sizeof(ret_val));
												Sleep(1);
											} while (error == ErrorCodes::kSuccess && ret_val == kUninitRetVal);
											error = fortnite.mem_stream().Read(screen_pos_ptr, &screen_pos, sizeof(screen_pos));
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											std::cout << screen_pos.x << "," << screen_pos.y << std::endl;
											error = fortnite.mem_stream().Free(ret_val_ptr);
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											error = fortnite.mem_stream().Free(screen_pos_ptr);
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
											error = fortnite.mem_stream().Free(world_pos_ptr);
											if (error != ErrorCodes::kSuccess) {
												return error;
											}
										}
									}
								}
							}
						}
					}
				}
				Sleep(2000);
			}

			error = fortnite.mem_stream().Free(w2s_shellcode_ptr);
			if (error != ErrorCodes::kSuccess) {
				return error;
			}
			return error;
		}

	}  // namespace test

}  // namespace lanius
