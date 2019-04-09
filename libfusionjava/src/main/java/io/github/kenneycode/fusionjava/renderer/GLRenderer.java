package io.github.kenneycode.fusionjava.renderer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GL渲染器接口
 *
 */

public interface GLRenderer extends Renderer {

    /**
     *
     * 初始化参数
     *
     */
    void initParameter() ;

    /**
     *
     * 设置attribute float数组
     *
     * @param key attribute参数名
     * @param value float数组
     * @param componentCount 每个顶点的成份数（一维，二维..）
     *
     */
    void setAttributeFloats(String key, float[] value, int componentCount);

    /**
     *
     * 设置顶点坐标，默认是二维坐标
     *
     * @param positions 顶点坐标数组
     *
     */
    void setPositions(float[] positions);

    /**
     *
     * 设置纹理坐标
     *
     * @param textureCoordinates 纹理坐标数组
     *
     */
    void setTextureCoordinates(float[] textureCoordinates);

    /**
     *
     * 设置纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    void setUniformTexture2D(String key, int value);

    /**
     *
     * 设置OES纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    void setUniformOESTexture(String key, int value);

    /**
     *
     * 设置渲染翻转
     *
     * @param flipX 水平翻转
     * @param flipY 垂直翻转
     *
     */
    void setFlip(Boolean flipX, Boolean flipY);

    /**
     *
     * 绑定输入
     *
     */
    void bindInput();

    /**
     *
     * 绑定输出
     *
     */
    void bindOutput();

    /**
     *
     * 绑定参数
     *
     */
    void bindParameters();

    /**
     *
     * 解绑输入
     *
     */
    void unBindInput();

}
