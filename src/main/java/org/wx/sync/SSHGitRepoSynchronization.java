package org.wx.sync;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.wx.support.AuthInfo;
import org.wx.support.AuthInfoBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author wuxin
 * @date 2022/08/21 16:37:43
 */
public class SSHGitRepoSynchronization extends AbstractRepoSynchronization{

    public SSHGitRepoSynchronization(Properties properties) {
        super.authInfo = AuthInfoBuilder.build(properties);
    }

    @Override
    public CloneCommand getCloneCommandWithAuth(AuthInfo authInfo) {
        return Git.cloneRepository()
                .setTransportConfigCallback(authInfo.getTransportConfigCallback());
    }

    @Override
    public LsRemoteCommand getLsRemoteCommandWithAuth(AuthInfo authInfo) {
        return Git.lsRemoteRepository()
                .setTransportConfigCallback(authInfo.getTransportConfigCallback());
    }

    @Override
    public PullCommand getPullCommandWithAuth(AuthInfo authInfo) throws IOException {
        String localCodeDir = authInfo.getLocalCodeDir();
        localCodeDir+="\\.git";
        FileRepository fileRepository = new FileRepository(new File(localCodeDir));
        Git pullGit = new Git(fileRepository);
        return pullGit.pull().setTransportConfigCallback(authInfo.getTransportConfigCallback());
    }
}
