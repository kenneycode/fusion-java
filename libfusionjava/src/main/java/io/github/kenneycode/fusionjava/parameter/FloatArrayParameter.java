package io.github.kenneycode.fusionjava.parameter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * Shader float数组参数
 *
 */

public class FloatArrayParameter extends Parameter {

    private float[] value = null;
    private int componentCount = 0;

    public FloatArrayParameter(String key, float[] value, int componentCount) {
        this.key = key;
        this.value = value;
        this.componentCount = componentCount;
    }

    @Override
    public void bindAttribute(int program) {
        if (location < 0) {
            location = glGetAttribLocation(program, key);
        }
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(value.length * java.lang.Float.SIZE / 8)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(value);
        vertexBuffer.position(0);
        glVertexAttribPointer(location, componentCount, GL_FLOAT, false,0, vertexBuffer);
        glEnableVertexAttribArray(location);
    }

    @Override
    public void updateValue(Object value) {
        this.value = (float[]) value;
    }

}
