package io.github.kenneycode.fusionjava.common;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * 引用计数类
 *
 */

public class Ref {

    public int refCount = 1;

    public Ref() { }

    /**
     *
     * 增加1个引用计数
     *
     */
    public void addRef() {
        addRef(1);
    }

    /**
     *
     * 增加count个引用计数
     *
     * @param count 要增加的引用计数
     *
     */
    public void addRef(int count) {
        refCount += count;
    }

    /**
     *
     * 减少1个引用计数
     *
     */
    public void releaseRef() {
        --refCount;
    }

}
