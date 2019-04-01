package io.github.kenneycode.fusionjava.context;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * EGL封装类
 *
 */

public class EGL {

    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;
    EGLContext eglContext = EGL14.EGL_NO_CONTEXT;

    private EGLDisplay previousDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface previousDrawSurface = EGL14.EGL_NO_SURFACE;
    private EGLSurface previousReadSurface = EGL14.EGL_NO_SURFACE;
    private EGLContext previousContext = EGL14.EGL_NO_CONTEXT;

    /**
     *
     * 初始化
     *
     * @param surface 用于创建window surface的surface，如果传null则创建pbuffer surface
     *
     */
    public void init(Surface surface) {
        init(surface, EGL14.EGL_NO_CONTEXT);
    }

    /**
     *
     * 初始化
     *
     * @param surface 用于创建window surface的surface，如果传null则创建pbuffer surface
     * @param shareContext 共享的GL Context
     *
     */
    public void init(Surface surface, EGLContext shareContext) {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1);
        int[] attribList = new int[] {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT | EGLExt.EGL_OPENGL_ES3_BIT_KHR,
                EGL14.EGL_NONE, 0,
                EGL14.EGL_NONE
        };
        EGLConfig[] eglConfig = new EGLConfig[1];
        int[] numConfigs = new int[1];
        EGL14.eglChooseConfig(
                eglDisplay,
                attribList, 0,
                eglConfig, 0,
                eglConfig.length,
                numConfigs, 0
        );
        eglContext = EGL14.eglCreateContext(
            eglDisplay, eglConfig[0], shareContext,
            new int[] { EGL14.EGL_CONTEXT_CLIENT_VERSION, 3, EGL14.EGL_NONE }, 0
        );
        int[] surfaceAttribs = new int[] { EGL14.EGL_NONE };
        if (surface == null) {
            eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig[0], surfaceAttribs, 0);
        } else {
            eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig[0], surface, surfaceAttribs, 0);
        }
    }

    /**
     *
     * 将EGL环境绑定到调用线程
     *
     */
    public void bind() {
        previousDisplay = EGL14.eglGetCurrentDisplay();
        previousDrawSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW);
        previousReadSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_READ);
        previousContext = EGL14.eglGetCurrentContext();
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
    }

    /**
     *
     * 将之前的EGL环境恢复
     *
     */
    public void unbind() {
        EGL14.eglMakeCurrent(previousDisplay, previousDrawSurface, previousReadSurface, previousContext);
    }

    /**
     *
     * 交换显示buffer
     *
     */
    public Boolean swapBuffers() {
        return EGL14.eglSwapBuffers(eglDisplay, eglSurface);
    }

    /**
     *
     * 释放资源
     *
     */
    public void release() {
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(eglDisplay, eglSurface);
            EGL14.eglDestroyContext(eglDisplay, eglContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(eglDisplay);
        }
        eglDisplay = EGL14.EGL_NO_DISPLAY;
        eglContext = EGL14.EGL_NO_CONTEXT;
        eglSurface = EGL14.EGL_NO_SURFACE;
    }


}