package io.github.kenneycode.fusionjava.context;

import java.util.HashMap;
import java.util.Map;

import io.github.kenneycode.fusionjava.inputsource.InputSource;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * GL Context池
 *
 */

public class GLContextPool {

    private Map<Integer, GLContext> glContextMap = new HashMap<>();

    public static GLContextPool getInstance() {
        return GLContextPool.GLContextPoolInstance.instance;
    }

    /**
     *
     * 获取GL Context
     *
     * @param inputSource 输入源
     *
     * @return GL Context
     *
     */
    public GLContext obtainGLContext(InputSource inputSource) {
        int id = inputSource.hashCode();
        if (glContextMap.containsKey(id)) {
            return glContextMap.get(id);
        } else {
            GLContext glContext = createGLContext(inputSource);
            glContextMap.put(id, glContext);
            return glContext;
        }
    }

    /**
     *
     * 创建GL Context
     *
     * @param inputSource 输入源
     *
     * @return GL Context
     *
     */
    private GLContext createGLContext(InputSource inputSource) {
        if (inputSource.runGLCommandInPlace()) {
            return new InPlaceGLContext();
        }
        return null;
    }

    /**
     *
     * 为输入源设置GL Context
     *
     * @param inputSource 输入源
     * @param glContext GL Context
     *
     */
    public void setGLContextForInputSource(InputSource inputSource, GLContext glContext) {
        int id = inputSource.hashCode();
        if (!glContextMap.containsKey(id)) {
            glContextMap.put(id, glContext);
        }
    }

    private static class GLContextPoolInstance {
        private static final GLContextPool instance = new GLContextPool();
    }

}