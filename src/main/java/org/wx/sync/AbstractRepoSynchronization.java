package org.wx.sync;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.wx.support.AuthInfo;
import org.wx.utils.GitUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author wuxin
 * @date 2022/08/21 16:00:56
 */
public abstract class AbstractRepoSynchronization implements Synchronization{

    AuthInfo authInfo;

    @Override
    public Boolean synchronize () throws GitAPIException, IOException {
        // read localDir has .git file inside or not
        // if contains just checkout and pull
        Boolean containGitFile = checkLocalDirFiles(authInfo);
        LsRemoteCommand lsRemoteCommandWithAuth = getLsRemoteCommandWithAuth(authInfo);
        PullCommand pullCommandWithAuth = getPullCommandWithAuth(authInfo);
        if(containGitFile){
            // checkout all branches and pull
            GitUtils.gitCheckOutAllBranch(authInfo,lsRemoteCommandWithAuth);
//            List<String> branches = GitUtils.gitRemoteBranches(authInfo, lsRemoteCommandWithAuth);
//            if(branches != null & branches.size() > 0){
//                for (String branch : branches) {
//                    GitUtils.gitCheckout(authInfo,branch);
////                    GitUtils.gitPull(authInfo,pullCommandWithAuth);
//                }
//            }
            return Boolean.TRUE;
        }
        // if dose not contains .git file
        // clone repo and checkout all branches
        GitUtils.gitSSHClone(authInfo,getCloneCommandWithAuth(authInfo));
        // checkout all branches
        GitUtils.gitCheckOutAllBranch(authInfo,lsRemoteCommandWithAuth);
        return null;
    }

    public abstract CloneCommand getCloneCommandWithAuth(AuthInfo authInfo);

    public abstract LsRemoteCommand getLsRemoteCommandWithAuth(AuthInfo authInfo);

    public abstract PullCommand getPullCommandWithAuth(AuthInfo authInfo) throws IOException;


    private Boolean checkLocalDirFiles(AuthInfo authInfo){
        File localDir = new File(authInfo.getLocalCodeDir());
        File[] files = localDir.listFiles();
        if(files == null){
            return Boolean.FALSE;
        }
        for (File file : files) {
            if(file.getName().equals(".git")){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
