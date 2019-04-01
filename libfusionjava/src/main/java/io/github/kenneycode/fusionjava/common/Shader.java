package io.github.kenneycode.fusionjava.common;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * Shader包装类，包括vertex shader和fragment shader
 *
 */

public class Shader {

    public String vertexShader;
    public String fragmentShader;

    public Shader() {
        this(Constants.COMMON_VERTEX_SHADER, Constants.COMMON_FRAGMENT_SHADER);
    }

    public Shader(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shader shader = (Shader) o;
        return vertexShader.equals(shader.vertexShader) && fragmentShader.equals(shader.fragmentShader);
    }

}
