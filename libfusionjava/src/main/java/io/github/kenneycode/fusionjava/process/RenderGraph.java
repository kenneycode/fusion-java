package io.github.kenneycode.fusionjava.process;

import android.opengl.GLSurfaceView;
import io.github.kenneycode.fusionjava.context.GLContext;
import io.github.kenneycode.fusionjava.context.GLSurfaceViewGLContext;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.outputtarget.FusionGLTextureView;
import io.github.kenneycode.fusionjava.outputtarget.OutputTarget;
import io.github.kenneycode.fusionjava.renderer.Renderer;
import java.util.*;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 渲染过程管理类，将Renderer/OutputTarget按指定规则连接成Graph，并执行渲染过程
 *
 */

public class RenderGraph implements Renderer {


    public GLContext outputTargetGLContext = null;

    protected List<FrameBuffer> inputFrameBuffers = new ArrayList<>();
    protected FrameBuffer outputFrameBuffer = null;
    protected int outputWidth = 0;
    protected int outputHeight = 0;

    private Node LAYER_SEPERATOR = null;
    private Map<Renderer, Node> rendererNodeMap = new HashMap<>();
    private Node rootNode = null;

    /**
     *
     * 构造方法
     *
     * @param rootRenderer 根Renderer
     *
     */
    public RenderGraph(Renderer rootRenderer) {
        this.rootNode = new RendererNode(rootRenderer);
        rendererNodeMap.put(rootRenderer, this.rootNode);
    }

    /**
     *
     * 初始化，会对Graph中所有Node都调用其初始化方法
     *
     */
    public void init() {
        LinkedList<Node> traversalQueue = new LinkedList<>();
        traversalQueue.addLast(rootNode);
        while (!traversalQueue.isEmpty()) {
            Node node = traversalQueue.removeFirst();
            if (node instanceof RendererNode) {
                ((RendererNode) node).renderer.init();
            } else if (node instanceof OutputTargetNode) {
                ((OutputTargetNode) node).outputTarget.onInit();
            }
            traversalQueue.addAll(node.nextNodes);
        }
    }

    /**
     *
     * 更新数据，会对Graph中所有Node都调用其更新数据的方法
     *
     * @param data 数据
     *
     * @return 是否需要执行当前渲染
     *
     */
    public boolean update(Map<String, Object> data) {
        LinkedList<Node> traversalQueue = new LinkedList<>();
        traversalQueue.addLast(rootNode);
        while (!traversalQueue.isEmpty()) {
            Node node = traversalQueue.removeFirst();
            if (node instanceof RendererNode) {
                ((RendererNode) node).renderer.update(data);
            } else if (node instanceof OutputTargetNode) {
                ((OutputTargetNode) node).outputTarget.onUpdate(data);
            }
            traversalQueue.addAll(node.nextNodes);
        }
        return true;
    }

    /**
     *
     * 为一个Renderer添加一个后续Renderer
     *
     * @param pre   前一个Renderer
     * @param next  后一个Renderer
     *
     * @return 返回此RenderGraph
     *
     */
    public RenderGraph addNextRenderer(Renderer pre, Renderer next) {
        Node preNode = rendererNodeMap.get(pre);
        if (!rendererNodeMap.containsKey(next)) {
            rendererNodeMap.put(next, new RendererNode(next, preNode.layer + 1));
        }
        Node nextNode = rendererNodeMap.get(next);
        preNode.addNext(nextNode);
        nextNode.layer = (preNode.layer + 1 > nextNode.layer) ? (preNode.layer + 1) : nextNode.layer;
        return this;
    }

    /**
     *
     * 为一个Renderer添加一个后续OutputTarget
     *
     * @param pre   前一个Renderer
     * @param next  后一个OutputTarget
     *
     */
    public void addOutputTarget(Renderer pre, OutputTarget next) {
        Node preNode = rendererNodeMap.get(pre);
        Node nextNode = new OutputTargetNode(next);
        preNode.addNext(nextNode);
        nextNode.layer = (preNode.layer + 1 > nextNode.layer) ? (preNode.layer + 1) : nextNode.layer;
        if (next instanceof GLSurfaceView) {
            outputTargetGLContext = new GLSurfaceViewGLContext((GLSurfaceView) next);
        } else if (next instanceof FusionGLTextureView) {
            outputTargetGLContext = (FusionGLTextureView) next;
        }
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    public void setInput(FrameBuffer frameBuffer) {
        List<FrameBuffer> frameBuffers = new ArrayList<>();
        frameBuffers.add(frameBuffer);
        setInput(frameBuffers);
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffers 输入FrameBuffer
     *
     */
    public void setInput(List<FrameBuffer> frameBuffers) {
        inputFrameBuffers = frameBuffers;
    }

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    public void setOutput(FrameBuffer frameBuffer) {
        outputFrameBuffer = frameBuffer;
    }

    /**
     *
     * 设置输出尺寸
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    public void setOutputSize(int width, int height) {
        outputWidth = width;
        outputHeight = height;
    }

    /**
     *
     * 执行渲染
     *
     * @return 输出FrameBuffer
     *
     */
    public FrameBuffer render() {
        return performTraversal(inputFrameBuffers);
    }

    /**
     *
     * 执行Graph遍历，执行渲染过程
     *
     * @return 输出FrameBuffer
     *
     */
    private FrameBuffer performTraversal(List<FrameBuffer> input) {
        FrameBuffer output = null;
        rootNode.input.addAll(input);
        LinkedList<Node> traversalQueue = new LinkedList<>();
        traversalQueue.addLast(rootNode);
        traversalQueue.addLast(LAYER_SEPERATOR);
        int currentLayer = 0;
        while (!traversalQueue.isEmpty()) {
            Node node = traversalQueue.removeFirst();
            if (node == LAYER_SEPERATOR) {
                ++currentLayer;
                continue;
            }
            if (node.layer != currentLayer) {
                continue;
            }
            if (node instanceof RendererNode) {
                RendererNode rendererNode = (RendererNode) node;
                if (rendererNode.needRender) {
                    rendererNode.renderer.setInput(node.input);
                    if (rendererNode.nextNodes.isEmpty() && outputFrameBuffer != null && outputWidth > 0 && outputHeight > 0) {
                        rendererNode.renderer.setOutput(outputFrameBuffer);
                        rendererNode.renderer.setOutputSize(outputWidth, outputHeight);
                    }
                    output = rendererNode.renderer.render();
                } else {
                    output = node.input.get(0);
                }
            } else if (node instanceof OutputTargetNode) {
                ((OutputTargetNode) node).outputTarget.onInputReady(node.input);
            }
            if (!node.nextNodes.isEmpty()) {
                output.addRef(node.nextNodes.size() - 1);
            }
            for (Node nextNode : node.nextNodes) {
                nextNode.input.add(output);
                traversalQueue.addLast(nextNode);
            }
            traversalQueue.addLast(LAYER_SEPERATOR);
        }
        return output;
    }

    /**
     *
     * 释放资源
     *
     */
    public void release() {
        LinkedList<Node> traversalQueue = new LinkedList<>();
        traversalQueue.addLast(rootNode);
        while (!traversalQueue.isEmpty()) {
            Node node = traversalQueue.removeFirst();
            if (node instanceof RendererNode) {
                ((RendererNode) node).renderer.release();
            }
            traversalQueue.addAll(node.nextNodes);
        }
    }

    /**
     *
     * Graph Node基类
     *
     */
    private class Node {

        public int layer = 0;
        public boolean needRender = true;
        public List<FrameBuffer> input = new ArrayList<>();
        public List<Node> nextNodes = new ArrayList<>();

        public Node(int layer) {
            this.layer = layer;
        }

        public void addNext(Node nextNode) {
            nextNodes.add(nextNode);
        }

    }

    /**
     *
     * 渲染器Node类
     *
     */
    private class RendererNode extends Node {

        public Renderer renderer = null;

        public RendererNode(Renderer renderer) {
            this(renderer, 0);
        }

        public RendererNode(Renderer renderer, int layer) {
            super(layer);
            this.renderer = renderer;
        }

    }

    /**
     *
     * 输出目标Node类
     *
     */
    private class OutputTargetNode extends Node {

        public OutputTarget outputTarget = null;

        public OutputTargetNode(OutputTarget outputTarget) {
            this(outputTarget, 0);
        }

        public OutputTargetNode(OutputTarget outputTarget, int layer) {
            super(layer);
            this.outputTarget = outputTarget;
        }

    }

}