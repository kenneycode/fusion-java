package io.github.kenneycode.fusionjava.context;

import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import java.util.concurrent.Semaphore;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 包含GL Context和线程
 *
 */

public class GLThread {

    private HandlerThread handlerThread = new HandlerThread("GLThread");
    private Handler handler = null;
    private EGL egl = null;


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
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        egl = new EGL();
        egl.init(surface, shareContext);
        handler.post(new Runnable() {
            @Override
            public void run() {
                egl.bind();
            }
        });
    }

    /**
     *
     * 异步执行一个任务
     *
     * @param task 要执行的任务
     *
     */
    public void post(Runnable task) {
        post(task, false);
    }

    /**
     *
     * 异步执行一个任务
     *
     * @param task 要执行的任务
     * @param render 是否需要渲染
     *
     */
    public void post(final Runnable task, final Boolean render) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                task.run();
                if (render) {
                    egl.swapBuffers();
                }
            }
        });
    }

    /**
     *
     * 同步执行一个任务
     *
     * @param task 要执行的任务
     * @param render 是否需要渲染
     *
     */
    public void execute(final Runnable task, final Boolean render) {
        final Semaphore semaphore = new Semaphore(0);
        handler.post(new Runnable() {
            @Override
            public void run() {
                task.run();
                if (render) {
                    egl.swapBuffers();
                }
                semaphore.release();
            }
        });
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 交换显示buffer
     *
     */
    public void  swapBuffers() {
        egl.swapBuffers();
    }

    /**
     *
     * 停止线程和释放资源
     *
     */
    public void release() {
        handlerThread.getLooper().quit();
        egl.release();
    }

}