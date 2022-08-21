package org.wx.test;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.wx.sync.SSHGitRepoSynchronization;
import org.wx.utils.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author wuxin
 * @date 2022/08/21 17:20:27
 */

public class TestGitSSHSync {


    public static void main(String[] args) throws IOException, GitAPIException {
        Properties config = PropertiesUtils.getPropertiesFromCP("config/personal-notes.properties");
        SSHGitRepoSynchronization sshGitRepoSynchronization = new SSHGitRepoSynchronization(config);
        sshGitRepoSynchronization.synchronize();

    }

}
