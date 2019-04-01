package io.github.kenneycode.fusionjava.renderer;

import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.framebuffer.FrameBufferCache;

import static android.opengl.GLES20.glViewport;

public class ScreenRenderer extends SimpleRenderer {

    private int displayWidth;
    private int displayHeight;

    public void setDisplaySize(int width, int height) {
        this.displayWidth = width;
        this.displayHeight = height;
    }

    @Override
    public void bindOutput() {
        FrameBuffer frameBuffer = outputFrameBuffer;
        if (frameBuffer ==  null) {
            frameBuffer = FrameBufferCache.getInstance().obtainFrameBuffer(0, 0);
            outputFrameBuffer = frameBuffer;
        }
        frameBuffer.bind(0, 0);
        glViewport(0, 0, displayWidth, displayHeight);
    }
}