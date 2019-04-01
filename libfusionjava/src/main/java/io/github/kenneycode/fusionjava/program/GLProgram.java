package io.github.kenneycode.fusionjava.program;

import java.util.Set;
import io.github.kenneycode.fusionjava.common.Ref;
import io.github.kenneycode.fusionjava.common.Shader;
import io.github.kenneycode.fusionjava.parameter.Parameter;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glIsProgram;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUseProgram;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GL Program类，可通过GLProgramCache获取
 *
 */

public class GLProgram extends Ref {

    public Shader shader = null;
    private int program = 0;

    /**
     *
     * 构造方法，通过Shader类构造对应的GLProgram
     *
     * @param shader 指定的shader
     *
     */
    public GLProgram(Shader shader) {
        this.shader = shader;
    }

    /**
     *
     * 初始化方法，重复调用不会初始化多次
     *
     */
    public void init() {

        if (!glIsProgram(program)) {

            program = glCreateProgram();

            int vertexShader = glCreateShader(GL_VERTEX_SHADER);
            int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(vertexShader, shader.vertexShader);
            glShaderSource(fragmentShader, shader.fragmentShader);

            glCompileShader(vertexShader);
            glCompileShader(fragmentShader);

            glAttachShader(program, vertexShader);
            glAttachShader(program, fragmentShader);

            glLinkProgram(program);

            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

        }

    }

    /**
     *
     * 将attribute参数绑定到此GL Program
     *
     * @param attributes 要绑定的attribute参数
     *
     */
    public void bindAttribute(Set<Parameter> attributes) {
        glUseProgram(program);
        for (Parameter p : attributes) {
            p.bindAttribute(program);
        }
    }

    /**
     *
     * 将uniform参数绑定到此GL Program
     *
     * @param uniforms 要绑定的uniform参数
     *
     */
    public void bindUniform(Set<Parameter> uniforms) {
        glUseProgram(program);
        for (Parameter p : uniforms) {
            p.bindUniform(program);
        }
    }

    /**
     *
     * 减少引用计数，当引用计数为0时放回GLProgramCache
     *
     */
    public void releaseRef() {
        super.releaseRef();
        if (refCount == 0) {
            glDeleteProgram(program);
        }
    }

}
