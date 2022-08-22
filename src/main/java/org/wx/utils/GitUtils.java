package org.wx.utils;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.wx.support.AuthInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuxin
 * @date 2022/08/20 12:34:00
 */
public class GitUtils {

    public static Boolean gitSSHClone(AuthInfo authInfo, CloneCommand cloneCommand) throws GitAPIException {
        // lode user info
        String remoteRepoPath = authInfo.getRemoteRepoPath();
        String localCodeDir = authInfo.getLocalCodeDir();
        Git git = null;
        try {
            git = cloneCommand.setURI(remoteRepoPath) //设置远程URI
                    .setDirectory(new File(localCodeDir)) //设置下载存放路径
                    .setCloneAllBranches(true)
                    .call();
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    public static List<String> gitRemoteBranches(AuthInfo authInfo,LsRemoteCommand lsRemoteCommand) throws GitAPIException {
        Collection<Ref> allBranch = lsRemoteCommand
                .setRemote(authInfo.getRemoteRepoPath())
                .setHeads(true).call();
        if(allBranch == null || allBranch.size() == 0){
            return new ArrayList<>();
        }
        return allBranch.stream().map(e -> {
            String name = e.getName();
            if (name.startsWith("refs/heads/")) {
                name = name.replace("refs/heads/", "");
            }
            return name;
        }).collect(Collectors.toList());
    }


    public static void gitCheckout(AuthInfo authInfo,String branchName) throws GitAPIException, IOException {
        try {
            String localCodeDir = authInfo.getLocalCodeDir();
            localCodeDir+="\\.git";
            FileRepository fileRepository = new FileRepository(new File(localCodeDir));
            Git git = new Git(fileRepository);
            try {
                if (branchNameExist(git, branchName)) {//如果分支在本地已存在，直接checkout即可。
                    git.checkout().setCreateBranch(false).setName(branchName).call();
                } else {//如果分支在本地不存在，需要创建这个分支，并追踪到远程分支上面。
                    git.checkout().setCreateBranch(true).setName(branchName).setStartPoint("origin/" + branchName).call();
                }
                git.pull().setTransportConfigCallback(authInfo.getTransportConfigCallback()).call();
//                git.pull().call();//拉取最新的提交
            } finally {
                git.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }


    public static Boolean gitCheckOutAllBranch(AuthInfo authInfo,
                                            LsRemoteCommand lsRemoteCommand) throws GitAPIException {
        Collection<Ref> allBranch = lsRemoteCommand.setRemote(authInfo.getRemoteRepoPath())
                .setHeads(true).call();

        try {
            if(allBranch != null && allBranch.size() > 0){
                List<String> allBranchNames = allBranch.stream().map(e -> {
                    String name = e.getName();
                    if (name.startsWith("refs/heads/")) {
                        name = name.replace("refs/heads/", "");
                    }
                    return name;
                }).collect(Collectors.toList());

                for (String branchName : allBranchNames) {
                    gitCheckout(authInfo,branchName);
                }
            }
            return Boolean.TRUE;
        } catch (Exception e){
            return Boolean.FALSE;
        }
    }

    public static void gitPull(AuthInfo authInfo,PullCommand pullCommand) {
        try {
            PullResult pullResult = pullCommand.call();
            if(pullResult != null){
                System.out.println(pullResult.getMergeResult());
            }
        } catch (GitAPIException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean branchNameExist(Git git, String branchName) throws GitAPIException {
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs) {
            if (ref.getName().contains(branchName)) {
                return true;
            }
        }
        return false;
    }

}
