package io.github.kenneycode.fusionjava.program;

import java.util.HashMap;
import java.util.Map;
import io.github.kenneycode.fusionjava.common.Shader;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GL Program缓存池
 *
 */

public class GLProgramCache {

    private Map<Shader, GLProgram> cache = new HashMap<>();

    private GLProgramCache() { }

    public static GLProgramCache getInstance() {
        return GLProgramCacheInstance.instance;
    }

    /**
     *
     * 获取指定Shader对应的GL Program，如果缓存池中没有，将创建
     *
     * @param shader shader
     *
     * @return Shader对应的GL Program
     *
     */
    public GLProgram obtainGLProgram(Shader shader) {
        if (!cache.containsKey(shader)) {
            cache.put(shader, new GLProgram(shader));
        } else {
            cache.get(shader).addRef();
        }
        return cache.get(shader);
    }

    /**
     *
     * 将GL Program放回cache
     *
     * @param glProgram 要放回的GL Program
     *
     */
    public void releaseGLProgram(GLProgram glProgram) {
        cache.put(glProgram.shader, glProgram);
    }

    private static class GLProgramCacheInstance {
        private static final GLProgramCache instance = new GLProgramCache();
    }

}

