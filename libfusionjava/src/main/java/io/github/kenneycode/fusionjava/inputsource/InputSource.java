package io.github.kenneycode.fusionjava.inputsource;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.kenneycode.fusionjava.context.GLContextPool;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.process.RenderGraph;
import io.github.kenneycode.fusionjava.renderer.Renderer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 输入源基类
 *
 */

public abstract class InputSource {

    public List<Renderer> renderers = new ArrayList<>();

    /**
     *
     * 添加渲染器
     *
     * @param renderer 渲染器
     *
     */
    public void addRenderer(Renderer renderer) {
        renderers.add(renderer);
        if (renderer instanceof RenderGraph) {
            RenderGraph renderGraph = (RenderGraph) renderer;
            if (renderGraph.outputTargetGLContext != null) {
                GLContextPool.getInstance().setGLContextForInputSource(this, renderGraph.outputTargetGLContext);
            }
        }
    }

    /**
     *
     * 通知初始化
     *
     */
    public void notifyInit() {
        for (Renderer renderer : renderers) {
            renderer.init();
        }
    }

    /**
     *
     * 通知输入已准备好
     *
     * @param data 传入的数据
     * @param frameBuffer 输入FrameBuffer
     *
     */
    public void notifyInputReady(Map<String, Object> data, FrameBuffer frameBuffer) {
        List<FrameBuffer> input = new ArrayList<>();
        input.add(frameBuffer);
        notifyInputReady(data, input);
    }

    /**
     *
     * 通知输入已准备好
     *
     * @param data 传入的数据
     * @param frameBuffers 输入FrameBuffer数组
     *
     */
    public void  notifyInputReady(Map<String, Object> data, List<FrameBuffer> frameBuffers) {
        for (Renderer renderer : renderers) {
            renderer.setInput(frameBuffers);
            renderer.update(data);
            renderer.render();
        }
    }

    /**
     *
     * 是否原地执行GL调用
     *
     * @return  是否原地执行GL调用
     *
     */
    public Boolean runGLCommandInPlace() {
        return false;
    }

}