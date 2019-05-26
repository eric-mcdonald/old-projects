/*
 * org_bitbucket_reliant_render_Renderer.cc
 *
 *  Created on: Jan 3, 2017
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_render_Renderer.h"

#include <d3d9.h>
#include <d3dx9.h>

#include "org_bitbucket_reliant_memory_MemoryStream.h"

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    begin0
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_render_Renderer_begin
(JNIEnv *env, jobject this_obj, jlong d3d_device) {
	LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
	d3d_device_ptr->Clear(0, 0, D3DCLEAR_TARGET, 0, 1.0F, 0);
	d3d_device_ptr->BeginScene();
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    end0
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_bitbucket_reliant_render_Renderer_end
(JNIEnv *env, jobject this_obj, jlong d3d_device) {
	LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
	d3d_device_ptr->EndScene();
	d3d_device_ptr->Present(NULL, NULL, NULL, NULL);
}

struct D3dColVertex {
	float x, y, z, rhw;
	DWORD diffuse;
};
struct D3dTexVertex {
	float x, y, z, rhw;
	DWORD diffuse;
	float u, v;
};

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_render_Renderer_createVertexBuffer
(JNIEnv *env, jobject this_obj, jlong d3d_device, jint length, jint fvf) {
	LPDIRECT3DVERTEXBUFFER9 vertex_buffer;
	if (fvf & D3DFVF_TEX1) {
		D3dTexVertex vertices[length];
		return FAILED(reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device)->CreateVertexBuffer(sizeof(vertices), 0, fvf, D3DPOOL_DEFAULT, &vertex_buffer, NULL)) ? org_bitbucket_reliant_memory_MemoryStream_NULL : reinterpret_cast<jlong>(vertex_buffer);
	}
	D3dColVertex vertices[length];
	return FAILED(reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device)->CreateVertexBuffer(sizeof(vertices), 0, fvf, D3DPOOL_DEFAULT, &vertex_buffer, NULL)) ? org_bitbucket_reliant_memory_MemoryStream_NULL : reinterpret_cast<jlong>(vertex_buffer);
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    createLine
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_render_Renderer_createLine
  (JNIEnv *env, jobject this_obj, jlong d3d_device) {
	LPD3DXLINE d3dx_line;
	return FAILED(D3DXCreateLine(reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device), &d3dx_line)) ? org_bitbucket_reliant_memory_MemoryStream_NULL : reinterpret_cast<jlong>(d3dx_line);
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_render_Renderer_drawLine
(JNIEnv *env, jobject this_obj, jfloat x1, jfloat y1, jfloat z1, jfloat x2, jfloat y2, jfloat z2, jfloat w, jint color, jlong d3d_device, jlong vertex_buffer, jint vertex_fvf, jlong line, jfloat line_width) {
	if (line_width != 1.0F) {
		LPD3DXLINE d3dx_line = reinterpret_cast<LPD3DXLINE>(line);
		if (FAILED(d3dx_line->SetWidth(line_width)) || FAILED(d3dx_line->Begin())) {
			return JNI_FALSE;
		}
		D3DXVECTOR2 line_vecs[2];
		line_vecs[0].x = x1;
		line_vecs[0].y = y1;
		line_vecs[1].x = x2;
		line_vecs[1].y = y2;
		bool draw_success = d3dx_line->Draw(line_vecs, sizeof(line_vecs) / sizeof(D3DXVECTOR2), color) == D3D_OK;
		return d3dx_line->End() == D3D_OK && draw_success;
	}
	if (vertex_fvf & D3DFVF_TEX1) {
		D3dTexVertex line_vertices[2];
		line_vertices[0].x = x1;
		line_vertices[0].y = y1;
		line_vertices[0].z = z1;
		const float inverse_w = w == 0.0F ? 1.0F : 1.0F / w;
		line_vertices[0].rhw = inverse_w;
		line_vertices[0].diffuse = color;
		line_vertices[1].x = x2;
		line_vertices[1].y = y2;
		line_vertices[1].z = z2;
		line_vertices[1].rhw = inverse_w;
		line_vertices[1].diffuse = color;
		LPDIRECT3DVERTEXBUFFER9 vertex_buffer_ptr = reinterpret_cast<LPDIRECT3DVERTEXBUFFER9>(vertex_buffer);
		LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
		VOID *vertices;
		if (FAILED(vertex_buffer_ptr->Lock(0, sizeof(line_vertices), &vertices, 0))) {
			return JNI_FALSE;
		}
		memcpy(vertices, line_vertices, sizeof(line_vertices));
		vertex_buffer_ptr->Unlock();
		d3d_device_ptr->SetStreamSource(0, vertex_buffer_ptr, 0, sizeof(D3dTexVertex));
		d3d_device_ptr->SetFVF(vertex_fvf);
		d3d_device_ptr->SetRenderState(D3DRS_LIGHTING, FALSE);
		d3d_device_ptr->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		d3d_device_ptr->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		d3d_device_ptr->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		return d3d_device_ptr->DrawPrimitive(D3DPT_LINELIST, 0, 1) == D3D_OK;
	} else {
		D3dColVertex line_vertices[2];
		line_vertices[0].x = x1;
		line_vertices[0].y = y1;
		line_vertices[0].z = z1;
		const float inverse_w = w == 0.0F ? 1.0F : 1.0F / w;
		line_vertices[0].rhw = inverse_w;
		line_vertices[0].diffuse = color;
		line_vertices[1].x = x2;
		line_vertices[1].y = y2;
		line_vertices[1].z = z2;
		line_vertices[1].rhw = inverse_w;
		line_vertices[1].diffuse = color;
		LPDIRECT3DVERTEXBUFFER9 vertex_buffer_ptr = reinterpret_cast<LPDIRECT3DVERTEXBUFFER9>(vertex_buffer);
		LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
		VOID *vertices;
		if (FAILED(vertex_buffer_ptr->Lock(0, sizeof(line_vertices), &vertices, 0))) {
			return JNI_FALSE;
		}
		memcpy(vertices, line_vertices, sizeof(line_vertices));
		vertex_buffer_ptr->Unlock();
		d3d_device_ptr->SetStreamSource(0, vertex_buffer_ptr, 0, sizeof(D3dColVertex));
		d3d_device_ptr->SetFVF(vertex_fvf);
		d3d_device_ptr->SetRenderState(D3DRS_LIGHTING, FALSE);
		d3d_device_ptr->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		d3d_device_ptr->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		d3d_device_ptr->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		return d3d_device_ptr->DrawPrimitive(D3DPT_LINELIST, 0, 1) == D3D_OK;
	}
}

JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_render_Renderer_drawQuad
(JNIEnv *env, jobject this_obj, jfloat x1, jfloat y1, jfloat z1, jfloat x2, jfloat y2, jfloat z2, jfloat w, jint color, jlong d3d_device, jlong vertex_buffer, jint vertex_fvf, jlong texture) {
	if (vertex_fvf & D3DFVF_TEX1) {
		D3dTexVertex quad_vertices[6];
		quad_vertices[0].x = x1;
		quad_vertices[0].y = y2;
		quad_vertices[0].z = z1;
		const float inverse_w = w == 0.0F ? 1.0F : 1.0F / w;
		quad_vertices[0].rhw = inverse_w;
		quad_vertices[0].diffuse = color;
		IDirect3DBaseTexture9 *base_texture = reinterpret_cast<IDirect3DBaseTexture9*>(texture);
		if (base_texture != NULL) {
			quad_vertices[0].u = 0.0F;
			quad_vertices[0].v = 1.0F;
		}
		quad_vertices[1].x = x1;
		quad_vertices[1].y = y1;
		quad_vertices[1].z = z1;
		quad_vertices[1].rhw = inverse_w;
		quad_vertices[1].diffuse = color;
		if (base_texture != NULL) {
			quad_vertices[1].u = 0.0F;
			quad_vertices[1].v = 0.0F;
		}
		quad_vertices[2].x = x2;
		quad_vertices[2].y = y1;
		quad_vertices[2].z = z2;
		quad_vertices[2].rhw = inverse_w;
		quad_vertices[2].diffuse = color;
		if (base_texture != NULL) {
			quad_vertices[2].u = 1.0F;
			quad_vertices[2].v = 0.0F;
		}
		quad_vertices[3].x = x2;
		quad_vertices[3].y = y2;
		quad_vertices[3].z = z2;
		quad_vertices[3].rhw = inverse_w;
		quad_vertices[3].diffuse = color;
		if (base_texture != NULL) {
			quad_vertices[3].u = 1.0F;
			quad_vertices[3].v = 1.0F;
		}
		quad_vertices[4].x = x1;
		quad_vertices[4].y = y2;
		quad_vertices[4].z = z1;
		quad_vertices[4].rhw = inverse_w;
		quad_vertices[4].diffuse = color;
		if (base_texture != NULL) {
			quad_vertices[4].u = 0.0F;
			quad_vertices[4].v = 1.0F;
		}
		quad_vertices[5].x = x2;
		quad_vertices[5].y = y1;
		quad_vertices[5].z = z2;
		quad_vertices[5].rhw = inverse_w;
		quad_vertices[5].diffuse = color;
		if (base_texture != NULL) {
			quad_vertices[5].u = 1.0F;
			quad_vertices[5].v = 0.0F;
		}
		LPDIRECT3DVERTEXBUFFER9 vertex_buffer_ptr = reinterpret_cast<LPDIRECT3DVERTEXBUFFER9>(vertex_buffer);
		LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
		VOID *vertices;
		if (FAILED(vertex_buffer_ptr->Lock(0, sizeof(quad_vertices), &vertices, 0))) {
			return JNI_FALSE;
		}
		memcpy(vertices, quad_vertices, sizeof(quad_vertices));
		vertex_buffer_ptr->Unlock();
		d3d_device_ptr->SetStreamSource(0, vertex_buffer_ptr, 0, sizeof(D3dTexVertex));
		d3d_device_ptr->SetFVF(vertex_fvf);
		d3d_device_ptr->SetRenderState(D3DRS_LIGHTING, FALSE);
		d3d_device_ptr->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		d3d_device_ptr->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		d3d_device_ptr->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (base_texture != NULL) {
			d3d_device_ptr->SetTexture(0, base_texture);
			d3d_device_ptr->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
			d3d_device_ptr->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		}
		const bool success = d3d_device_ptr->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 2) == D3D_OK;
		if (base_texture != NULL) {
			d3d_device_ptr->SetTexture(0, NULL);
		}
		return success;
	} else {
		D3dColVertex quad_vertices[6];
		quad_vertices[0].x = x1;
		quad_vertices[0].y = y2;
		quad_vertices[0].z = z1;
		const float inverse_w = w == 0.0F ? 1.0F : 1.0F / w;
		quad_vertices[0].rhw = inverse_w;
		quad_vertices[0].diffuse = color;
		IDirect3DBaseTexture9 *base_texture = reinterpret_cast<IDirect3DBaseTexture9*>(texture);
		quad_vertices[1].x = x1;
		quad_vertices[1].y = y1;
		quad_vertices[1].z = z1;
		quad_vertices[1].rhw = inverse_w;
		quad_vertices[1].diffuse = color;
		quad_vertices[2].x = x2;
		quad_vertices[2].y = y1;
		quad_vertices[2].z = z2;
		quad_vertices[2].rhw = inverse_w;
		quad_vertices[2].diffuse = color;
		quad_vertices[3].x = x2;
		quad_vertices[3].y = y2;
		quad_vertices[3].z = z2;
		quad_vertices[3].rhw = inverse_w;
		quad_vertices[3].diffuse = color;
		quad_vertices[4].x = x1;
		quad_vertices[4].y = y2;
		quad_vertices[4].z = z1;
		quad_vertices[4].rhw = inverse_w;
		quad_vertices[4].diffuse = color;
		quad_vertices[5].x = x2;
		quad_vertices[5].y = y1;
		quad_vertices[5].z = z2;
		quad_vertices[5].rhw = inverse_w;
		quad_vertices[5].diffuse = color;
		LPDIRECT3DVERTEXBUFFER9 vertex_buffer_ptr = reinterpret_cast<LPDIRECT3DVERTEXBUFFER9>(vertex_buffer);
		LPDIRECT3DDEVICE9 d3d_device_ptr = reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device);
		VOID *vertices;
		if (FAILED(vertex_buffer_ptr->Lock(0, sizeof(quad_vertices), &vertices, 0))) {
			return JNI_FALSE;
		}
		memcpy(vertices, quad_vertices, sizeof(quad_vertices));
		vertex_buffer_ptr->Unlock();
		d3d_device_ptr->SetStreamSource(0, vertex_buffer_ptr, 0, sizeof(D3dColVertex));
		d3d_device_ptr->SetFVF(vertex_fvf);
		d3d_device_ptr->SetRenderState(D3DRS_LIGHTING, FALSE);
		d3d_device_ptr->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
		d3d_device_ptr->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
		d3d_device_ptr->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		if (base_texture != NULL) {
			d3d_device_ptr->SetTexture(0, base_texture);
			d3d_device_ptr->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
			d3d_device_ptr->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		}
		const bool success = d3d_device_ptr->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 2) == D3D_OK;
		if (base_texture != NULL) {
			d3d_device_ptr->SetTexture(0, NULL);
		}
		return success;
	}
}

JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_render_Renderer_createTexture
(JNIEnv *env, jobject this_obj, jlong d3d_device, jstring file) {
	LPDIRECT3DTEXTURE9 texture;
	const char *file_native = env->GetStringUTFChars(file, JNI_FALSE);
	const bool fail = FAILED(D3DXCreateTextureFromFile(reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device), file_native, &texture));
	env->ReleaseStringUTFChars(file, file_native);
	return fail ? org_bitbucket_reliant_memory_MemoryStream_NULL : reinterpret_cast<jlong>(texture);
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    resolution
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_org_bitbucket_reliant_render_Renderer_resolution
(JNIEnv *env, jobject this_obj) {
	RECT screen_rect;
	CONST HWND valve_window = FindWindow("Valve001", NULL);
	if (valve_window == NULL) {
		return NULL;
	}
	GetWindowRect(valve_window, &screen_rect);
	jint *resolution = new jint[2];
	resolution[0] = screen_rect.right - screen_rect.left;
	resolution[1] = screen_rect.bottom - screen_rect.top;
	if (GetWindowLong(valve_window, GWL_STYLE) & WS_BORDER) {
		resolution[0] -= 6;
		resolution[1] -= 28;
	}
	jintArray resolution_jni = env->NewIntArray(2);
	env->SetIntArrayRegion(resolution_jni, 0, env->GetArrayLength(resolution_jni), resolution);
	delete[] resolution;
	return resolution_jni;
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    hasBorder
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_bitbucket_reliant_render_Renderer_hasBorder
(JNIEnv *env, jobject this_obj, jstring window) {
	const char *window_class = env->GetStringUTFChars(window, JNI_FALSE);
	CONST HWND window_handle = FindWindow(window_class, NULL);
	if (window_handle == NULL) {
		env->ReleaseStringUTFChars(window, window_class);
		return JNI_FALSE;
	}
	const bool has_border = GetWindowLong(window_handle, GWL_STYLE) & WS_BORDER;
	env->ReleaseStringUTFChars(window, window_class);
	return has_border;
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    createFont
 * Signature: (JIIZLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_bitbucket_reliant_render_Renderer_createFont
(JNIEnv *env, jobject this_obj, jlong d3d_device, jint height, jint weight, jboolean italic, jstring facename) {
	LPD3DXFONT font;
	const char *facename_native = env->GetStringUTFChars(facename, JNI_FALSE);
	jlong font_ptr = FAILED(D3DXCreateFont(reinterpret_cast<LPDIRECT3DDEVICE9>(d3d_device), height, 0, weight, 0, italic, DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, ANTIALIASED_QUALITY, DEFAULT_PITCH | FF_DONTCARE, facename_native, &font)) ? org_bitbucket_reliant_memory_MemoryStream_NULL : reinterpret_cast<jlong>(font);
	env->ReleaseStringUTFChars(facename, facename_native);
	return font_ptr;
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    drawText
 * Signature: (JLjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_org_bitbucket_reliant_render_Renderer_drawText
(JNIEnv *env, jobject this_obj, jlong font, jstring text, jint x, jint y, jint color) {
	RECT rect;
	rect.left = x;
	rect.top = y;
	rect.right = rect.left + 1;
	rect.bottom = rect.top + 1;
	LPD3DXFONT font_ptr = reinterpret_cast<LPD3DXFONT>(font);
	const char *text_native = env->GetStringUTFChars(text, JNI_FALSE);
	INT draw_ret_val = font_ptr->DrawText(NULL, text_native, -1, &rect, DT_CALCRECT, color);
	if (draw_ret_val == 0) {
		env->ReleaseStringUTFChars(text, text_native);
		return draw_ret_val;
	}
	rect.bottom = rect.top + draw_ret_val;
	draw_ret_val = font_ptr->DrawText(NULL, text_native, -1, &rect, DT_NOCLIP, color);
	env->ReleaseStringUTFChars(text, text_native);
	return draw_ret_val;
}

/*
 * Class:     org_bitbucket_reliant_render_Renderer
 * Method:    textSize
 * Signature: (JLjava/lang/String;)[I
 */
JNIEXPORT jintArray JNICALL Java_org_bitbucket_reliant_render_Renderer_textSize
(JNIEnv *env, jobject this_obj, jlong font, jstring text) {
	RECT rect;
	rect.left = 0;
	rect.top = 0;
	rect.right = rect.left + 1;
	rect.bottom = rect.top + 1;
	const char *text_native = env->GetStringUTFChars(text, JNI_FALSE);
	CONST INT text_height = reinterpret_cast<LPD3DXFONT>(font)->DrawText(NULL, text_native, -1, &rect, DT_CALCRECT, 0xFF000000);
	if (text_height == 0) {
		env->ReleaseStringUTFChars(text, text_native);
		return NULL;
	}
	env->ReleaseStringUTFChars(text, text_native);
	jintArray text_sz_jni = env->NewIntArray(2);
	const jsize text_sz_len = env->GetArrayLength(text_sz_jni);
	jint text_sz[text_sz_len];
	text_sz[0] = rect.right - rect.left;
	text_sz[1] = text_height;
	env->SetIntArrayRegion(text_sz_jni, 0, text_sz_len, text_sz);
	return text_sz_jni;
}
