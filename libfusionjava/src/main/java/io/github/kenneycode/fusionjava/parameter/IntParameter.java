package io.github.kenneycode.fusionjava.parameter;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * Shader int参数
 *
 */

public class IntParameter extends Parameter {

    private int value = 0;

    public IntParameter(String key, int value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void bindUniform(int program) {
        if (location < 0) {
            location = glGetUniformLocation(program, key);
        }
        glUniform1i(location, value);
    }

    @Override
    public void updateValue(Object value) {
        this.value = (int) value;
    }

}
