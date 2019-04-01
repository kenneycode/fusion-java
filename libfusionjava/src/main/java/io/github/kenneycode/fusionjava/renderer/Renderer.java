package io.github.kenneycode.fusionjava.renderer;

import java.util.List;
import java.util.Map;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 渲染器接口
 *
 */

public interface Renderer {

    /**
     *
     * 初始化
     *
     */
    void init();

    /**
     *
     * 更新数据
     *
     * @param data 传入的数据
     *
     * @return 是否执行当次渲染
     *
     */
    boolean update(Map<String, Object> data);

    /**
     *
     * 设置单个输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    void setInput(FrameBuffer frameBuffer);

    /**
     *
     * 设置多个输入
     *
     * @param frameBuffers 输入FrameBuffer list
     *
     */
    void setInput(List<FrameBuffer> frameBuffers);

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    void setOutput(FrameBuffer frameBuffer);

    /**
     *
     * 设置输出宽高
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    void setOutputSize(int width, int height);

    /**
     *
     * 渲染
     *
     * @return 渲染结果FrameBuffer
     *
     */
    FrameBuffer render();

    /**
     *
     * 释放资源
     *
     */
    void release();

}
