package io.github.kenneycode.fusionjava.inputsource;

import android.graphics.Bitmap;

import java.util.Map;

import io.github.kenneycode.fusionjava.context.GLContext;
import io.github.kenneycode.fusionjava.context.GLContextPool;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.framebuffer.FrameBufferCache;
import io.github.kenneycode.fusionjava.util.GLUtil;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 图片输入源
 *
 */

public class FusionImageSource extends InputSource {

    private Bitmap image = null;

    public FusionImageSource(Bitmap bitmap) {
        this.image = bitmap;
    }

    /**
     *
     * 开始处理
     *
     */
    public void process() {
        process(null);
    }

    /**
     *
     * 开始处理
     *
     * @param data 传入的数据
     *
     */
    public void process(final Map<String, Object> data) {
        GLContext glContext = GLContextPool.getInstance().obtainGLContext(this);
        glContext.runOnGLContext(new Runnable() {
            @Override
            public void run() {
                int texture = GLUtil.createTexture();
                GLUtil.bitmap2Texture(texture, image);
                FrameBuffer frameBuffer = FrameBufferCache.getInstance().obtainFrameBuffer(0, 0);
                frameBuffer.width = image.getWidth();
                frameBuffer.height = image.getHeight();
                frameBuffer.texture = texture;
                notifyInit();
                notifyInputReady(data, frameBuffer);
            }
        });
    }

}