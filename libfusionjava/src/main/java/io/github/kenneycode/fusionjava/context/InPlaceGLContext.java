package io.github.kenneycode.fusionjava.context;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 原地执行的GL Context类
 *
 */

public class InPlaceGLContext implements GLContext {

    /**
     *
     * 将task在此OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    @Override
    public void runOnGLContext(Runnable task) {
        task.run();
    }

}