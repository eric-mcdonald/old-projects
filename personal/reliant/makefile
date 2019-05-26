# Define a variable for classpath
CLASS_PATH = $(PWD)bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

BUILD = 4

DLLPATH64 = natives/reliant_b$(BUILD)_64.dll

all_64 : src/test/cpp/win64/jni_test.h src/main/cpp/win64/renderer.h src/main/cpp/win64/key_option.h src/main/cpp/win64/main.h src/main/cpp/win64/memory_stream.h src/main/cpp/win64/reliant.h $(DLLPATH64)

OBJECTS64 = src/test/cpp/win64/org_bitbucket_reliant_test_JniTest.o src/main/cpp/win64/org_bitbucket_reliant_render_Renderer.o src/main/cpp/win64/org_bitbucket_reliant_Main.o src/main/cpp/win64/org_bitbucket_reliant_Reliant.o src/main/cpp/win64/org_bitbucket_reliant_memory_MemoryStream.o src/main/cpp/win64/org_bitbucket_reliant_cfg_KeyOption.o

LIBS64 = -ld3d9 -ld3dx9 -ldwmapi -lgdi32 -lpsapi

# $@ matches the target, $< matches the first dependency
$(DLLPATH64) : $(OBJECTS64)
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast -Wl,--add-stdcall-alias -shared -o $@ $(OBJECTS64) $(LIBS64)

JDKPATH64 = C:/Program Files/Java/jdk1.6.0_45

INCLUDES64 = -I"$(JDKPATH64)/include" -I"$(JDKPATH64)/include/win32"

# $@ matches the target, $< matches the first dependency
src/test/cpp/win64/org_bitbucket_reliant_test_JniTest.o : src/test/cpp/win64/org_bitbucket_reliant_test_JniTest.cc src/test/cpp/win64/org_bitbucket_reliant_test_JniTest.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
src/main/cpp/win64/org_bitbucket_reliant_Main.o : src/main/cpp/win64/org_bitbucket_reliant_Main.cc src/main/cpp/win64/org_bitbucket_reliant_Main.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
src/main/cpp/win64/org_bitbucket_reliant_Reliant.o : src/main/cpp/win64/org_bitbucket_reliant_Reliant.cc src/main/cpp/win64/org_bitbucket_reliant_Reliant.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
src/main/cpp/win64/org_bitbucket_reliant_memory_MemoryStream.o : src/main/cpp/win64/org_bitbucket_reliant_memory_MemoryStream.cc src/main/cpp/win64/org_bitbucket_reliant_memory_MemoryStream.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
src/main/cpp/win64/org_bitbucket_reliant_cfg_KeyOption.o : src/main/cpp/win64/org_bitbucket_reliant_cfg_KeyOption.cc src/main/cpp/win64/org_bitbucket_reliant_cfg_KeyOption.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
src/main/cpp/win64/org_bitbucket_reliant_render_Renderer.o : src/main/cpp/win64/org_bitbucket_reliant_render_Renderer.cc src/main/cpp/win64/org_bitbucket_reliant_render_Renderer.h
	g++ -m64 -static-libgcc -static-libstdc++ -static -Ofast $(INCLUDES64) -c $< $(LIBS64) -o $@
	
OBJECTS32 = src/test/cpp/win32/org_bitbucket_reliant_test_JniTest.o src/main/cpp/win32/org_bitbucket_reliant_render_Renderer.o src/main/cpp/win32/org_bitbucket_reliant_Main.o src/main/cpp/win32/org_bitbucket_reliant_Reliant.o src/main/cpp/win32/org_bitbucket_reliant_memory_MemoryStream.o src/main/cpp/win32/org_bitbucket_reliant_cfg_KeyOption.o

DLLPATH32 = natives/reliant_b$(BUILD)_32.dll

all_32 : src/test/cpp/win32/jni_test.h src/main/cpp/win32/renderer.h src/main/cpp/win32/key_option.h src/main/cpp/win32/main.h src/main/cpp/win32/memory_stream.h src/main/cpp/win32/reliant.h $(DLLPATH32)

LIBS32 = -ld3d9 -L"$(MINGW_HOME32_EXT)/lib" -ld3dx9 -ldwmapi -lgdi32 -lpsapi

# $@ matches the target, $< matches the first dependency
$(DLLPATH32) : $(OBJECTS32)
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast -Wl,--add-stdcall-alias -shared -o $@ $(OBJECTS32) $(LIBS32)

JDKPATH32 = C:/Program Files (x86)/Java/jdk1.6.0_45

INCLUDES32 = -I"$(JDKPATH32)/include" -I"$(JDKPATH32)/include/win32" -I"$(MINGW_HOME32_EXT)/include"

# $@ matches the target, $< matches the first dependency
src/test/cpp/win32/org_bitbucket_reliant_test_JniTest.o : src/test/cpp/win32/org_bitbucket_reliant_test_JniTest.cc src/test/cpp/win32/org_bitbucket_reliant_test_JniTest.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@
	
src/main/cpp/win32/org_bitbucket_reliant_Main.o : src/main/cpp/win32/org_bitbucket_reliant_Main.cc src/main/cpp/win32/org_bitbucket_reliant_Main.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@
	
src/main/cpp/win32/org_bitbucket_reliant_Reliant.o : src/main/cpp/win32/org_bitbucket_reliant_Reliant.cc src/main/cpp/win32/org_bitbucket_reliant_Reliant.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@
	
src/main/cpp/win32/org_bitbucket_reliant_memory_MemoryStream.o : src/main/cpp/win32/org_bitbucket_reliant_memory_MemoryStream.cc src/main/cpp/win32/org_bitbucket_reliant_memory_MemoryStream.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@
	
src/main/cpp/win32/org_bitbucket_reliant_cfg_KeyOption.o : src/main/cpp/win32/org_bitbucket_reliant_cfg_KeyOption.cc src/main/cpp/win32/org_bitbucket_reliant_cfg_KeyOption.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@
	
src/main/cpp/win32/org_bitbucket_reliant_render_Renderer.o : src/main/cpp/win32/org_bitbucket_reliant_render_Renderer.cc src/main/cpp/win32/org_bitbucket_reliant_render_Renderer.h
	"$(MINGW_HOME32)\bin\g++" -m32 -static-libgcc -static-libstdc++ -Ofast $(INCLUDES32) -c $< $(LIBS32) -o $@

src/test/cpp/win64/jni_test.h : bin/org/bitbucket/reliant/test/JniTest.class
	javah -d src/test/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.test.JniTest

src/main/cpp/win64/main.h : bin/org/bitbucket/reliant/Main.class
	javah -d src/main/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.Main
	
src/main/cpp/win64/reliant.h : bin/org/bitbucket/reliant/Reliant.class
	javah -d src/main/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.Reliant

src/main/cpp/win64/memory_stream.h : bin/org/bitbucket/reliant/memory/MemoryStream.class
	javah -d src/main/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.memory.MemoryStream
	
src/main/cpp/win64/key_option.h : bin/org/bitbucket/reliant/cfg/KeyOption.class
	javah -d src/main/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.cfg.KeyOption
	
src/main/cpp/win64/renderer.h : bin/org/bitbucket/reliant/render/Renderer.class
	javah -d src/main/cpp/win64 -classpath $(CLASS_PATH) org.bitbucket.reliant.render.Renderer
	
src/test/cpp/win32/jni_test.h : bin/org/bitbucket/reliant/test/JniTest.class
	javah -d src/test/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.test.JniTest

src/main/cpp/win32/main.h : bin/org/bitbucket/reliant/Main.class
	javah -d src/main/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.Main
	
src/main/cpp/win32/reliant.h : bin/org/bitbucket/reliant/Reliant.class
	javah -d src/main/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.Reliant

src/main/cpp/win32/memory_stream.h : bin/org/bitbucket/reliant/memory/MemoryStream.class
	javah -d src/main/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.memory.MemoryStream
	
src/main/cpp/win32/key_option.h : bin/org/bitbucket/reliant/cfg/KeyOption.class
	javah -d src/main/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.cfg.KeyOption
	
src/main/cpp/win32/renderer.h : bin/org/bitbucket/reliant/render/Renderer.class
	javah -d src/main/cpp/win32 -classpath $(CLASS_PATH) org.bitbucket.reliant.render.Renderer
	
all : all_32 all_64
	
clean :
	rm -f src/main/cpp/win64/org_bitbucket_reliant_render_Renderer.h src/test/cpp/win64/org_bitbucket_reliant_test_JniTest.h src/main/cpp/win64/org_bitbucket_reliant_Main.h src/main/cpp/win64/org_bitbucket_reliant_Reliant.h src/main/cpp/win64/org_bitbucket_reliant_memory_MemoryStream.h src/main/cpp/win64/org_bitbucket_reliant_cfg_KeyOption.h src/main/cpp/win32/org_bitbucket_reliant_render_Renderer.h src/test/cpp/win32/org_bitbucket_reliant_test_JniTest.h src/main/cpp/win32/org_bitbucket_reliant_Main.h src/main/cpp/win32/org_bitbucket_reliant_Reliant.h src/main/cpp/win32/org_bitbucket_reliant_memory_MemoryStream.h src/main/cpp/win32/org_bitbucket_reliant_cfg_KeyOption.h $(OBJECTS64) $(OBJECTS32) $(DLLPATH64) $(DLLPATH32)