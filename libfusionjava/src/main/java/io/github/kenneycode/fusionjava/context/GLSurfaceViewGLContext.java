package io.github.kenneycode.fusionjava.context;

import android.opengl.GLSurfaceView;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GLSurfaceView OpenGL上下文包装类
 *
 */

public class GLSurfaceViewGLContext implements GLContext {

    private GLSurfaceView glSurfaceView = null;

    /**
     *
     * @param glSurfaceView 包装的GLSurfaceView
     *
     */
    public GLSurfaceViewGLContext(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
    }

    /**
     *
     * 将task在此GLSurfaceView的OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    @Override
    public void runOnGLContext(final Runnable task) {
        this.glSurfaceView.queueEvent(task);
    }

}
