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

// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once

#include "targetver.h"

#include <stdio.h>
#include <tchar.h>



// TODO: reference additional headers your program requires here
#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <sstream>

#include <Windows.h>
#include <VersionHelpers.h>
#include <TlHelp32.h>
#include <atlconv.h>
#include <winternl.h>
#include <d3d9.h>
#include <d3dx9.h>
#include <dwmapi.h>

#include "native_util.h"
#include "string_util.h"
#include "memory_stream.h"
#include "feature_interface.h"
#include "font.h"
#include "window_rect.h"
#include "window_id.h"
#include "render_data.h"
#include "language.h"
