package io.github.kenneycode.fusionjava.parameter;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * Shader参数基类
 *
 */

public abstract class Parameter {

    // 参数名
    public String key = null;
    // 参数位置
    protected int location = -1;

    Parameter() { }

    public Parameter(String key) {
        this.key = key;
    }

    /**
     *
     * 绑定attribute参数
     *
     * @param program GL Program
     *
     */
    public void bindAttribute(int program) { }

    /**
     *
     * 绑定uniform参数
     *
     * @param program GL Program
     *
     */
    public void bindUniform(int program) { }

    /**
     *
     * 更新参数
     *
     * @param value 新值
     *
     */
    public void updateValue(Object value) { }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parameter parameter = (Parameter) o;
        return key != null && key.equals(parameter.key);
    }

}
