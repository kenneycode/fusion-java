package io.github.kenneycode.fusionjava.outputtarget;

import java.util.List;
import java.util.Map;

import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 渲染输出目标接口，实现此接口的类都可以作为渲染输出目标
 *
 */

public interface OutputTarget {

    /**
     *
     * 初始化回调
     *
     */
    void onInit();

    /**
     *
     * 更新数据回调
     *
     * @param data 传入的数据
     *
     */
    void onUpdate(Map<String, Object> data);

    /**
     *
     * 通知渲染输出目标输入已经准备好了
     *
     * @param frameBuffers 输入FrameBuffer
     *
     */
    void onInputReady(List<FrameBuffer> frameBuffers);

}
