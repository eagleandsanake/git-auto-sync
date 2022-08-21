package org.wx.sync;

/**
 * @author wuxin
 * @date 2022/08/21 00:13:57
 */
public interface Synchronization<T> {

    Boolean synchronize (T t);

}
