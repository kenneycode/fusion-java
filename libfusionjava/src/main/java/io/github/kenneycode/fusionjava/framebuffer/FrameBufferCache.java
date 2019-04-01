package io.github.kenneycode.fusionjava.framebuffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import io.github.kenneycode.fusionjava.common.Size;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * FrameBuffer缓存池
 *
 */

public class FrameBufferCache {

    private Map<Long, Map<Size, LinkedList<FrameBuffer>>> cache = new HashMap<>();

    private FrameBufferCache() { }

    public static FrameBufferCache getInstance() {
        return FrameBufferCacheInstance.instance;
    }

    /**
     *
     * 获取指定尺寸的FrameBuffer，如果cache中不存在，会创建
     *
     * @param width 宽度
     * @param height 高度
     *
     * @return 对应尺寸的FrameBuffer
     *
     */
    public FrameBuffer obtainFrameBuffer(int width, int height) {
        long tid = Thread.currentThread().getId();
        if (!cache.containsKey(tid)) {
            cache.put(tid, new HashMap<Size, LinkedList<FrameBuffer>>());
        }
        Size size = new Size(width, height);
        if (!cache.get(tid).containsKey(size)) {
            cache.get(tid).put(size, new LinkedList<FrameBuffer>());
        }
        if (cache.get(tid).get(size).isEmpty()) {
            cache.get(tid).get(size).add(new FrameBuffer());
        } else {
            cache.get(tid).get(size).peekFirst().addRef();
        }
        return cache.get(tid).get(size).pollFirst();
    }

    /**
     *
     * 向cache中归还FrameBuffer
     *
     * @param frameBuffer 归还的FrameBuffer
     *
     */
    public void releaseFrameBuffer(FrameBuffer frameBuffer) {
        long tid = Thread.currentThread().getId();
        if (cache.containsKey(tid)) {
            Size size = new Size(frameBuffer.width, frameBuffer.height);
            cache.get(tid).get(size).add(frameBuffer);
        }
    }

    private static class FrameBufferCacheInstance {
        private static final FrameBufferCache instance = new FrameBufferCache();
    }

}

