// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once

#include "targetver.h"

#include <stdio.h>
#include <tchar.h>



// TODO: reference additional headers your program requires here
#include <map>
#include <string>
#include <vector>
#include <iostream>
#include <thread>
#include <ios>
#include <sstream>
#include <fstream>
#include <algorithm>
#include <cmath>
#include <ctime>
#include <streambuf>
#include <cstdint>

#include <Windows.h>
// Windows.h defines a 'max' macro which conflicts with std::numeric_limits<std::streamsize>::max()
#undef max
#include <atlconv.h>
#include <detours.h>
#include <TlHelp32.h>
#include <Psapi.h>
#include <d3d9.h>
#include <dwmapi.h>
#include <DbgHelp.h>
#include <lodepng.h>
#include <ft2build.h>
#include FT_FREETYPE_H

#include "cheat_def.h"
#include "logger.h"
