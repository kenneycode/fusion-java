package io.github.kenneycode.fusionjava.common;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 常用常量
 *
 */

public class Constants {

    public static final String SIMPLE_VERTEX_SHADER =
            "precision mediump float;\n" +
            "attribute vec4 a_position;\n" +
            "attribute vec2 a_textureCoordinate;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "void main() {\n" +
            "    v_textureCoordinate = a_textureCoordinate;\n" +
            "    gl_Position = a_position;\n" +
            "}";

    public static final String SIMPLE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "uniform sampler2D u_texture;\n" +
            "void main() {\n" +
            "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
            "}";


    public static final float[] SIMPLE_VERTEX = {-1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, -1};
    public static final float[] SIMPLE_VERTEX_FLIP_X = {1, -1, 1, 1, -1, 1, 1, -1, -1, 1, -1, -1};
    public static final float[] SIMPLE_VERTEX_FLIP_Y = {-1, 1, -1, -1, 1, -1, -1, 1, 1, -1, 1, 1};
    public static final float[] SIMPLE_VERTEX_FLIP_XY = {1, 1, 1, -1, -1, -1, 1, 1, -1, -1, -1, 1};
    public static final float[] SIMPLE_TEXTURE_COORDINATE = {0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0};

    public static final String POSITION_PARAM_KEY = "a_position";
    public static final String TEXTURE_COORDINATE_PARAM_KEY = "a_textureCoordinate";

}
