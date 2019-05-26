#include "stdafx.h"

#include "font.h"

namespace internals {

	FT_Library Font::library = nullptr;

	Font::Font(const std::string &font_file_, unsigned int size_, Overlay &renderer_, unsigned int max_char_, unsigned int font_resolution_) : font_file(font_file_), size(size_), font_resolution(0), renderer(renderer_), face(nullptr), max_char(max_char_), current_color(0xFFFFFFFF) {
		GetLibrary();  // Eric McDonald: Ensures that FreeType has been initialized.
		int font_err = FT_New_Face(GetLibrary(), font_file_.c_str(), 0, &face);
		if (font_err) {
			*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to create a FreeType font face from file \"" << font_file << "\" with error code " << font_err << "." << std::endl;
		}
		set_font_resolution(font_resolution_);
	}
	Font::Font(Font &&src) : renderer(std::move(src.renderer)) {
		face = src.face;
		font_file = src.font_file;
		size = src.size;
		font_resolution = src.font_resolution;
		char_bitmaps = src.char_bitmaps;
		max_char = src.max_char;
		current_color = src.current_color;
		src.face = nullptr;
		src.font_file = "";
		src.size = src.font_resolution = 0;
		src.char_bitmaps.clear();
		src.current_color = 0xFFFFFFFF;
	}
	Font::~Font() {
		DeleteTextures();
	}
	void Font::DeleteTextures() {
		for (auto &it : char_bitmaps) {
			it.second.char_texture->Release();
		}
		char_bitmaps.clear();
	}
	std::string Font::get_font_file() const {
		return font_file;
	}
	FT_Face Font::get_face() const {
		return face;
	}
	Overlay &Font::get_renderer() const {
		return renderer;
	}
	const auto &Font::get_char_bitmaps() const {
		return char_bitmaps;
	}
	unsigned int Font::get_size() const {
		return size;
	}
	unsigned int Font::get_max_char() const {
		return max_char;
	}
	void Font::LoadTextures(unsigned int current_color_, const std::string &characters) {
		std::vector<char> unique_chars;
		for (const auto &it : characters) {
			if (std::find(unique_chars.begin(), unique_chars.end(), it) == unique_chars.end()) {
				unique_chars.push_back(it);
			}
		}
		Byte red = (current_color_ >> 8) & 0xFF, green = (current_color_ >> 16) & 0xFF, blue = (current_color_ >> 24) & 0xFF, alpha = current_color_ & 0xFF;
		const bool bitmaps_empty = char_bitmaps.empty();
		for (const auto &it : unique_chars) {
			FT_ULong char_idx = it;
			const auto &found_texture = bitmaps_empty ? char_bitmaps.end() : char_bitmaps.find(char_idx);
			if (found_texture == char_bitmaps.end()) {
				int font_err = FT_Load_Char(face, char_idx, FT_LOAD_RENDER);
				if (font_err) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to load FreeType font character " << char_idx << " with error code " << font_err << "." << std::endl;
					continue;
				}
				if (face->glyph->bitmap.pixel_mode != FT_PIXEL_MODE_GRAY) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "FreeType font's pixel mode must be " << FT_PIXEL_MODE_GRAY << " (pixel mode was " << face->glyph->bitmap.pixel_mode << ")." << std::endl;
					continue;
				}
				std::vector<Byte> bitmap;
				const unsigned int x_max = face->glyph->bitmap.width, y_max = face->glyph->bitmap.rows;
				if (x_max == 0 || y_max == 0) {
					continue;  // Eric McDonald: Skips over the empty image.
				}
				Byte **img_data = new Byte*[y_max] { 0 };
				for (size_t i = 0; i < y_max; ++i) {
					img_data[i] = new Byte[x_max]{ 0 };
				}
				for (unsigned int x = 0, p = 0; x < x_max; ++x, ++p) {
					for (unsigned int y = 0, q = 0; y < y_max; ++y, ++q) {
						img_data[y][x] |= face->glyph->bitmap.buffer[q * face->glyph->bitmap.width + p];
					}
				}
				for (unsigned int y = 0; y < y_max; ++y) {
					for (unsigned int x = 0; x < x_max; ++x) {
						bitmap.push_back(red);
						bitmap.push_back(green);
						bitmap.push_back(blue);
						bitmap.push_back(img_data[y][x] == 0 ? 0 : alpha);
					}
				}
				IDirect3DTexture9 *d3d_texture;
				cheat::ErrorCode error = CreateTextureBuf(bitmap, x_max, y_max, renderer.get_d3d_device(), &d3d_texture);
				if (error != cheat::CheatErrorCodes::kSuccess) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to create a D3D texture for '" << static_cast<char>(char_idx) << "' with error code " << error << "." << std::endl;
					for (size_t i = 0; i < y_max; ++i) {
						delete[] img_data[i];
						img_data[i] = nullptr;
					}
					delete[] img_data;
					img_data = nullptr;
					continue;
				}
				GlyphData char_data;
				char_data.char_texture = d3d_texture;
				char_data.inc_amount = face->glyph->advance.x >> 6;
				char_data.width = x_max;
				char_data.height = y_max;
				char_data.bitmap = bitmap;
				char_bitmaps[char_idx] = char_data;
				for (size_t i = 0; i < y_max; ++i) {
					delete[] img_data[i];
					img_data[i] = nullptr;
				}
				delete[] img_data;
				img_data = nullptr;
			}
			else {
				for (size_t i = 0; i < found_texture->second.bitmap.size() - 4; i += 4) {
					found_texture->second.bitmap[i] = red;
					found_texture->second.bitmap[i + 1] = green;
					found_texture->second.bitmap[i + 2] = blue;
					if (found_texture->second.bitmap[i + 3] != 0) {
						found_texture->second.bitmap[i + 3] = alpha;
					}
				}
				cheat::ErrorCode error = UpdateTextureBuf(found_texture->second.bitmap, found_texture->second.char_texture);
				if (error != cheat::CheatErrorCodes::kSuccess) {
					*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to update the D3D texture for '" << static_cast<char>(char_idx) << "' with error code " << error << "." << std::endl;
				}
			}
		}
		current_color = current_color_;
	}
	unsigned int Font::get_font_resolution() const {
		return font_resolution;
	}
	void Font::set_font_resolution(unsigned int font_resolution_) {
		if (font_resolution == font_resolution_) {
			return;
		}
		int font_err = FT_Set_Pixel_Sizes(face, 0, font_resolution_);
		if (font_err) {
			*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to set FreeType font size to " << font_resolution_ << " with error code " << font_err << "." << std::endl;
			return;
		}
		font_resolution = font_resolution_;
		DeleteTextures();
	}
	void Font::set_size(unsigned int size_) {
		size = size_;
	}
	cheat::ErrorCode Font::Render(const std::string &text, cheat::Vector2d<float> position, unsigned int color) {
		if (text.empty() || color == 0) {
			return cheat::CheatErrorCodes::kInvalidParam;
		}
		if (color != current_color) {
			LoadTextures(color, text);
		}
		for (size_t i = 0; i < text.length(); ++i) {
			unsigned int char_code = text[i];
			if (char_code > max_char) {
				return cheat::CheatErrorCodes::kInvalidParam;
			}
			auto &found_char = char_bitmaps.find(char_code);
			if (found_char == char_bitmaps.end()) {
				position.set_x(position.get_x() + size / 2.0F);
				continue;
			}
			const GlyphData &glyph = found_char->second;
			const float scale = static_cast<float>(size) / static_cast<float>(font_resolution);
			float char_width = static_cast<float>(glyph.inc_amount) * scale;
			cheat::ErrorCode error = renderer.DrawTexturedQuad(position, char_width, static_cast<float>(glyph.height) * scale, glyph.char_texture);
			if (error != cheat::CheatErrorCodes::kSuccess) {
				return error;
			}
			position.set_x(position.get_x() + char_width);
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	cheat::ErrorCode Font::RenderWithShadow(const std::string &text, cheat::Vector2d<float> position, unsigned int color) {
		cheat::Vector2d<float> shadow_pos(position);
		const float shadow_offs = 2.0F * (static_cast<float>(size) / static_cast<float>(font_resolution));
		shadow_pos.set_x(shadow_pos.get_x() + shadow_offs);
		shadow_pos.set_y(shadow_pos.get_y() + shadow_offs);
		cheat::ErrorCode error = Render(text, shadow_pos, color & 0xFF);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		error = Render(text, position, color);
		if (error != cheat::CheatErrorCodes::kSuccess) {
			return error;
		}
		return cheat::CheatErrorCodes::kSuccess;
	}
	bool Font::operator==(const Font &rhs) const {
		return font_file == rhs.font_file && size == rhs.size && max_char == rhs.max_char && font_resolution == rhs.font_resolution;
	}
	bool Font::operator!=(const Font &rhs) const {
		return !(*this == rhs);
	}
	Font &Font::operator=(Font &&src) {
		if (this != &src) {
			face = src.face;
			font_file = src.font_file;
			size = src.size;
			font_resolution = src.font_resolution;
			char_bitmaps = src.char_bitmaps;
			max_char = src.max_char;
			current_color = src.current_color;
			src.face = nullptr;
			src.font_file = "";
			src.size = src.font_resolution = 0;
			src.char_bitmaps.clear();
			src.current_color = 0xFFFFFFFF;
		}
		return *this;
	}
	FT_Library Font::GetLibrary() {
		if (library == nullptr) {
			int font_err = FT_Init_FreeType(&library);
			if (font_err) {
				*cheat::g_err_log << cheat::g_err_log->GetPrefix() << "Failed to initialize FreeType with error code " << font_err << "." << std::endl;
			}
		}
		return library;
	}

}  // namespace internals
