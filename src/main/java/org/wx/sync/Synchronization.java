package org.wx.sync;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * @author wuxin
 * @date 2022/08/21 00:13:57
 */
public interface Synchronization {

     Boolean synchronize () throws GitAPIException, IOException;

}
