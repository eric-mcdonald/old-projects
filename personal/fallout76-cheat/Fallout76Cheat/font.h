#pragma once

#include <string>
#include <map>
#include <ft2build.h>
#include FT_FREETYPE_H

#include "cheat_def.h"
#include "overlay.h"

namespace internals {

	static constexpr unsigned int kExtAscii = 255;

	struct GlyphData {
		IDirect3DTexture9 *char_texture;
		unsigned int inc_amount;
		unsigned int width, height;
		std::vector<Byte> bitmap;
	};
	class Font {
		std::string font_file;
		FT_Face face;
		Overlay &renderer;
		std::map<unsigned int, GlyphData> char_bitmaps;
		unsigned int size, font_resolution;
		unsigned int max_char;
		unsigned int current_color;
		static FT_Library library;
	public:
		Font(const std::string &/*font_file_*/, unsigned int /*size_*/, Overlay &, unsigned int /*max_char_*/ = kExtAscii, unsigned int /*font_resolution_*/ = 24);
		Font(const Font &) = delete;
		Font(Font &&);
		~Font();
	private:
		void DeleteTextures();
		void LoadTextures(unsigned int /*current_color_*/, const std::string &/*characters*/);
	public:
		std::string get_font_file() const;
		FT_Face get_face() const;
		Overlay &get_renderer() const;
		const auto &get_char_bitmaps() const;
		unsigned int get_size() const;
		void set_size(unsigned int);
		void set_font_resolution(unsigned int);
		unsigned int get_max_char() const;
		unsigned int get_font_resolution() const;
		cheat::ErrorCode Render(const std::string &/*text*/, cheat::Vector2d<float> /*position*/, unsigned int /*color*/);
		cheat::ErrorCode RenderWithShadow(const std::string &/*text*/, cheat::Vector2d<float> /*position*/, unsigned int /*color*/);
		bool operator==(const Font &) const;
		bool operator!=(const Font &) const;
		Font &operator=(const Font &) = delete;
		Font &operator=(Font &&);
		static FT_Library GetLibrary();
	};

}  // namespace internals
