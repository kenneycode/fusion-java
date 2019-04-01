package io.github.kenneycode.fusionjava.framebuffer;

import io.github.kenneycode.fusionjava.util.GLUtil;
import io.github.kenneycode.fusionjava.common.Ref;

import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glViewport;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * FrameBuffer类，可以做为一帧渲染的输入/输出
 *
 */

public class FrameBuffer extends Ref {

    public int width = 0;
    public int height = 0;
    public int texture = 0;
    public int frameBuffer = 0;

    /**
     *
     * 绑定此FrameBuffer，绑定后将渲染到此FrameBuffer上
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    public void bind(int width, int height) {
        bind(width, height, -1);
    }

    /**
     *
     * 绑定此FrameBuffer，绑定后将渲染到此FrameBuffer上
     *
     * @param width 宽度
     * @param height 高度
     * @param externalTexture 外部纹理，当指定外部纹理时，外部纹理会附着到此FrameBuffer上
     *
     */
    public void bind(int width, int height, int externalTexture) {
        if (width != 0 && height != 0) {
            if (externalTexture == 0) {
                if (frameBuffer != 0) {
                    GLUtil.deleteFrameBuffer(frameBuffer);
                }
                if (texture != 0) {
                    GLUtil.deleteTexture(texture);
                }
            } else {
                if (frameBuffer == 0) {
                    frameBuffer = GLUtil.createFrameBuffer();
                }
                if (externalTexture != -1 && externalTexture != texture) {
                    GLUtil.deleteTexture(texture);
                    texture = 0;
                }
                if (width != this.width || height != this.height || texture == 0) {
                    this.width = width;
                    this.height = height;
                    if (texture == 0) {
                        if (externalTexture > 0) {
                            texture = externalTexture;
                        } else {
                            texture = GLUtil.createTexture();
                        }
                        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
                        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                                texture, 0);
                    }
                    glBindTexture(GL_TEXTURE_2D, texture);
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
                    glBindTexture(GL_TEXTURE_2D, 0);
                }
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

}
