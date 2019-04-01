package io.github.kenneycode.fusionjava.context;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * OpenGL Context 接口
 *
 */

public interface GLContext {

    /**
     *
     * 将task在此OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    void runOnGLContext(Runnable task);

}
