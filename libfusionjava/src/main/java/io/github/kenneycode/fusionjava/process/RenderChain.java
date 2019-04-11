package io.github.kenneycode.fusionjava.process;

import java.util.List;
import java.util.Map;

import io.github.kenneycode.fusionjava.context.GLContext;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.outputtarget.OutputTarget;
import io.github.kenneycode.fusionjava.renderer.Renderer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 渲染过程管理类，将Renderer/OutputTarget按单链的方式连接，并执行渲染过程
 *
 */

public class RenderChain implements Renderer {

    private RenderGraph renderGraph;
    private Renderer tailRenderer;

    public RenderChain(Renderer rootRenderer) {
        tailRenderer = rootRenderer;
        renderGraph = new RenderGraph(rootRenderer);
    }

    /**
     *
     * 初始化，会对chain中所有Node都调用其初始化方法
     *
     */
    @Override
    public void init() {
        renderGraph.init();
    }

    /**
     *
     * 更新数据，会对chain中所有Node都调用其更新数据的方法
     *
     * @param data 数据
     *
     * @return 是否需要执行当前渲染
     *
     */
    @Override
    public boolean update(Map<String, Object> data) {
        return renderGraph.update(data);
    }

    /**
     *
     * 添加后一个Renderer
     *
     * @param next 后一个Renderer
     *
     * @return 返回此RenderChain
     *
     */
    public RenderChain addNextRenderer(Renderer next) {
        renderGraph.addNextRenderer(tailRenderer, next);
        tailRenderer = next;
        return this;
    }

    /**
     *
     * 为最后一个Renderer添加一个后续OutputTarget
     *
     * @param next OutputTarget
     *
     */
    public void setOutputTarget(OutputTarget next) {
        renderGraph.addOutputTarget(tailRenderer, next);
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    @Override
    public void setInput(FrameBuffer frameBuffer) {
        renderGraph.setInput(frameBuffer);
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffers 输入FrameBuffer
     */
    @Override
    public void setInput(List<FrameBuffer> frameBuffers) {
        renderGraph.setInput(frameBuffers);
    }

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    @Override
    public void setOutput(FrameBuffer frameBuffer) {
        renderGraph.setOutput(frameBuffer);
    }

    /**
     *
     * 设置输出尺寸
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    @Override
    public void setOutputSize(int width, int height) {
        renderGraph.setOutputSize(width, height);
    }

    /**
     *
     * 执行渲染
     *
     * @return 输出FrameBuffer
     *
     */
    @Override
    public FrameBuffer render() {
        return renderGraph.render();
    }

    /**
     *
     * 获取输出目标的context
     *
     * @return 输出目标的context
     *
     */
    public GLContext getOutputTargetGLContext() {
        return renderGraph.outputTargetGLContext;
    }

    /**
     *
     * 设置输出目标的context
     *
     * @param outputTargetGLContext 输出目标的context
     *
     */
    public void setOutputTargetGLContext(GLContext outputTargetGLContext) {
        renderGraph.outputTargetGLContext = outputTargetGLContext;
    }

    /**
     *
     * 释放资源
     *
     */
    @Override
    public void release() {
        renderGraph.release();
    }

}