package io.github.kenneycode.fusionjava.parameter;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * OES纹理参数
 *
 */

public class OESTextureParameter extends Parameter {

    private int value = 0;

    public OESTextureParameter(String key, int value) {
        super(key);
        this.value = value;
    }

    @Override
    public void bindUniform(int program) {
        if (location < 0) {
            location = glGetUniformLocation(program, key);
        }
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, value);
        glUniform1i(location, 0);
    }

}