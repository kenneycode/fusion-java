package io.github.kenneycode.fusionjava.parameter;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * Shader uniform sampler2D数组参数
 *
 */

public class Texture2DParameter extends Parameter {

    private int value = 0;

    public Texture2DParameter(String key, int value) {
        super(key);
        this.value = value;
    }

    @Override
    public void bindUniform(int program) {
        if (location < 0) {
            location = glGetUniformLocation(program, key);
        }
        glBindTexture(GL_TEXTURE_2D, value);
        glUniform1i(location, 0);
    }
}
