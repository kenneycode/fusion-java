package io.github.kenneycode.fusionjava.renderer;

import io.github.kenneycode.fusionjava.framebuffer.FrameBufferCache;

import static android.opengl.GLES20.glViewport;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 显示渲染器，渲染到0号FrameBuffer，通常是屏幕
 *
 */

public class DisplayRenderer extends SimpleRenderer {

    private int displayWidth = 0;
    private int displayHeight = 0;

    public void setDisplaySize(int width, int height) {
        this.displayWidth = width;
        this.displayHeight = height;
    }

    @Override
    public void bindOutput() {
        if (outputFrameBuffer ==  null) {
            outputFrameBuffer = FrameBufferCache.getInstance().obtainFrameBuffer(0, 0);
        }
        outputFrameBuffer.bind(0, 0);
        glViewport(0, 0, displayWidth, displayHeight);
    }
}