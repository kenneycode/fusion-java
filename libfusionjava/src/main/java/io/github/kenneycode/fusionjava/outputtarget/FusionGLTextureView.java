package io.github.kenneycode.fusionjava.outputtarget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.kenneycode.fusionjava.common.FusionGLView;
import io.github.kenneycode.fusionjava.context.GLThread;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.renderer.DisplayRenderer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 渲染显示View
 *
 */

public class FusionGLTextureView extends TextureView implements FusionGLView {

    private GLThread glThread = null;
    private DisplayRenderer displayRenderer = new DisplayRenderer();
    private int surfaceWidth = 0;
    private int surfaceHeight = 0;
    private LinkedList<Runnable> pendingTasks = new LinkedList<>();

    public FusionGLTextureView(Context context) {
        super(context);
        init();
    }

    public FusionGLTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FusionGLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
                glThread = new GLThread();
                glThread.init(new Surface(surface));
                for (Runnable task : pendingTasks) {
                    runOnGLContext(task);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                surface.release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }

        });
    }

    /**
     *
     * 初始化回调
     *
     */
    @Override
    public void onInit() {
        displayRenderer.setFlip(false, true);
        displayRenderer.init();
    }

    /**
     *
     * 更新数据回调
     *
     * @param data 传入的数据
     *
     */
    @Override
    public void onUpdate(Map<String, Object> data) {

    }

    /**
     *
     * 通知渲染输出目标输入已经准备好了
     *
     * @param frameBuffers 输入FrameBuffer
     *
     */
    @Override
    public void onInputReady(List<FrameBuffer> frameBuffers) {
        displayRenderer.setDisplaySize(surfaceWidth, surfaceHeight);
        displayRenderer.setInput(frameBuffers);
        displayRenderer.render();
        if (glThread != null) {
            glThread.swapBuffers();
        }
    }

    /**
     *
     * 在该view对应的GL Context中执行一个任务
     *
     * @param task 要执行的任务
     *
     */
    @Override
    public void runOnGLContext(final Runnable task) {
        post(new Runnable() {
            @Override
            public void run() {
                if (glThread != null) {
                    glThread.post(task);
                } else {
                    pendingTasks.add(task);
                }
            }
        });
    }

    /**
     *
     * detached from window回调，此时销毁资源
     *
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (glThread != null) {
            glThread.post(new Runnable() {
                @Override
                public void run() {
                    displayRenderer.release();
                    glThread.release();
                }
            });
        }
    }

}