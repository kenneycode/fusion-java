package io.github.kenneycode.fusionjava.util;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteFramebuffers;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glIsTexture;
import static android.opengl.GLES20.glReadPixels;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GL util类
 *
 */

public class GLUtil {

    /**
     *
     * 创建一个纹理
     *
     * @return 纹理id
     *
     */
    public static int createTexture() {
        int[] textures = new int[1];
        glGenTextures(1, textures, 0);
        glBindTexture(GL_TEXTURE_2D, textures[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textures[0];
    }

    /**
     *
     * 删除一个纹理
     *
     * @param texture 纹理id
     *
     */
    public static void deleteTexture(int texture) {
        if (glIsTexture(texture)) {
            int[] temp = { texture };
            glDeleteTextures(1, temp, 0);
        }
    }

    /**
     *
     * 创建一个FrameBuffer
     *
     * @return frame buffer id
     *
     */
    public static int createFrameBuffer() {
        int[] frameBuffer = new int[1];
        glGenFramebuffers(1, frameBuffer, 0);
        return frameBuffer[0];
    }

    /**
     *
     * 删除一个FrameBuffer
     *
     * @param frameBuffer frame buffer id
     *
     */
    public static void deleteFrameBuffer(int frameBuffer) {
        int[] temp = { frameBuffer };
        glDeleteFramebuffers(1, temp, 0);
    }

    public static void bitmap2Texture(int texture, Bitmap bitmap) {
        glBindTexture(GL_TEXTURE_2D, texture);
        ByteBuffer b = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
        bitmap.copyPixelsToBuffer(b);
        b.position(0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA, bitmap.getWidth(),
                bitmap.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, b);
    }
    
    public static Bitmap texture2Bitmap(int texture, int width, int height) {
        ByteBuffer buffer = ByteBuffer.allocate(width * height * 4);
        int[] frameBuffers = new int[1];
        glGenFramebuffers(frameBuffers.length, frameBuffers, 0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffers[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buffer.position(0);
        bitmap.copyPixelsFromBuffer(buffer);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 0, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
        return bitmap;
    }

    public static Bitmap frameBuffer2Bitmap(FrameBuffer frameBuffer) {
        return texture2Bitmap(
            frameBuffer.texture,
            frameBuffer.width,
            frameBuffer.height
        );
    }

}
