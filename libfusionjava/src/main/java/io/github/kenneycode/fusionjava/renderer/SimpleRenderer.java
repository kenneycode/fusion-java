package io.github.kenneycode.fusionjava.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.kenneycode.fusionjava.common.Constants;
import io.github.kenneycode.fusionjava.common.Shader;
import io.github.kenneycode.fusionjava.framebuffer.FrameBuffer;
import io.github.kenneycode.fusionjava.framebuffer.FrameBufferCache;
import io.github.kenneycode.fusionjava.parameter.FloatArrayParameter;
import io.github.kenneycode.fusionjava.parameter.Parameter;
import io.github.kenneycode.fusionjava.parameter.Texture2DParameter;
import io.github.kenneycode.fusionjava.program.GLProgram;
import io.github.kenneycode.fusionjava.program.GLProgramCache;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 简单渲染器，可继承此类进行扩展
 *
 */

public class SimpleRenderer implements GLRenderer {

    protected FrameBuffer outputFrameBuffer;
    private Shader shader = null;
    private GLProgram glProgram = null;
    private Set<Parameter> attributes = new HashSet<>();
    private Set<Parameter> uniforms = new HashSet<>();
    private List<FrameBuffer> input;
    private int specifiedOutputWidth;
    private int specifiedOutputHeight;
    private int vertexCount = 0;

    public SimpleRenderer() {
       this(Constants.COMMON_VERTEX_SHADER_2, Constants.COMMON_FRAGMENT_SHADER_2);
    }

    public SimpleRenderer(String vertexShader, String fragmentShader) {
        shader = new Shader(vertexShader, fragmentShader);
    }

    /**
     *
     * 初始化
     *
     */
    @Override
    public void init() {
        glProgram = GLProgramCache.getInstance().obtainGLProgram(shader);
        glProgram.init();
        initParameter();
    }

    /**
     *
     * 初始化参数
     *
     */
    @Override
    public void initParameter() {
        setPositions(Constants.COMMON_VERTEX);
        setTextureCoordinates(Constants.COMMON_TEXTURE_COORDINATE);
    }

    /**
     *
     * 设置attribute float数组
     *
     * @param key attribute参数名
     * @param value float数组
     * @param componentCount 每个顶点的成份数（一维，二维..）
     *
     */
    @Override
    public void setAttributeFloats(String key, float[] value, int componentCount) {
        Parameter parameter = findParameter(attributes, key);
        if (parameter == null) {
            attributes.add(new FloatArrayParameter(key, value, componentCount));
        } else {
            parameter.updateValue(value);
        }
    }

    /**
     *
     * 设置顶点坐标，默认是二维坐标
     *
     * @param positions 顶点坐标数组
     *
     */
    @Override
    public void setPositions(float[] positions) {
        setAttributeFloats(Constants.POSITION_PARAM_KEY, positions, 2);
        vertexCount = positions.length / 2;
    }

    /**
     *
     * 设置纹理坐标
     *
     * @param textureCoordinates 纹理坐标数组
     *
     */
    @Override
    public void setTextureCoordinates(float[] textureCoordinates) {
        setAttributeFloats(Constants.TEXTURE_COORDINATE_PARAM_KEY, textureCoordinates, 2);
    }

    /**
     *
     * 设置纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    @Override
    public void setUniformTexture2D(String key, int value) {
        Parameter parameter = findParameter(uniforms, key);
        if (parameter == null) {
            uniforms.add(new Texture2DParameter(key, value));
        } else {
            parameter.updateValue(value);
        }
    }

    /**
     *
     * 绑定输入
     *
     */
    @Override
    public void bindInput() {
        if (!input.isEmpty()) {
            char textureIndex = '0';
            for (int i = 0; i < input.size(); ++i, ++textureIndex) {
                String textureKey = "u_texture";
                if (i > 0) {
                    textureKey += textureIndex;
                }
                setUniformTexture2D(textureKey, input.get(i).texture);
            }
        }
    }

    /**
     *
     * 绑定输出
     *
     */
    @Override
    public void bindOutput() {
        int outputWidth = specifiedOutputWidth > 0 ? specifiedOutputWidth : input.get(0).width;
        int outputHeight = specifiedOutputHeight > 0 ? specifiedOutputHeight : input.get(0).height;
        if (outputFrameBuffer == null) {
            outputFrameBuffer = FrameBufferCache.getInstance().obtainFrameBuffer(outputWidth, outputHeight);
        }
        outputFrameBuffer.bind(outputWidth, outputHeight);
    }

    /**
     *
     * 执行渲染（draw call）
     *
     */
    private void performRendering() {
        glProgram.bindAttribute(attributes);
        glProgram.bindUniform(uniforms);
        glEnable(GL_BLEND);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glDisable(GL_BLEND);
    }

    /**
     *
     * 解绑输入
     *
     */
    @Override
    public void unBindInput() {
        for (FrameBuffer frameBuffer : input) {
            frameBuffer.releaseRef();
        }
    }

    /**
     *
     * 更新数据
     *
     * @param data 传入的数据
     *
     * @return 是否执行当次渲染
     *
     */
    @Override
    public boolean update(Map<String, Object> data) {
        return false;
    }

    /**
     *
     * 设置单个输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    @Override
    public void setInput(FrameBuffer frameBuffer) {
        List<FrameBuffer> frameBuffers = new ArrayList<>();
        frameBuffers.add(frameBuffer);
        setInput(frameBuffers);
    }
    /**
     *
     * 设置多个输入
     *
     * @param frameBuffers 输入FrameBuffer list
     *
     */
    @Override
    public void setInput(List<FrameBuffer> frameBuffers) {
        this.input = frameBuffers;
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
        this.outputFrameBuffer = frameBuffer;
    }

    /**
     *
     * 设置输出宽高
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    @Override
    public void setOutputSize(int width, int height) {
        this.specifiedOutputWidth = width;
        this.specifiedOutputHeight = height;
    }

    /**
     *
     * 查找参数
     *
     * @param parameters 参数集合
     * @param key 参数名
     *
     * @return 对应的参数Paramter类，若找不到返回null
     *
     */
    private Parameter findParameter(Set<Parameter> parameters, String key) {
        for (Parameter p : parameters) {
            if (p.key.equals(key)) {
                return p;
            }
        }
        return null;
    }

    /**
     *
     * 渲染
     *
     * @return 渲染结果FrameBuffer
     *
     */
    @Override
    public FrameBuffer render() {
        bindInput();
        bindOutput();
        performRendering();
        unBindInput();
        return outputFrameBuffer;
    }

    /**
     *
     * 释放资源
     *
     */
    @Override
    public void release() {
        glProgram.releaseRef();
    }

}
