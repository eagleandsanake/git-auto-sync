package org.wx.utils;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;
import org.wx.enums.AuthInfoEnums;
import org.wx.support.AuthInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author wuxin
 * @date 2022/08/20 12:34:00
 */
public class GitUtils {

    public static void main(String[] args) throws IOException, GitAPIException {
        Properties config = PropertiesUtils.getPropertiesFromCP("config/personal-notes.properties");
//        gitSSHClone(config);
//        gitSSHPull(config);
//        gitCheckOutAllBranch(config);
//        sjsss(config);
    }

    public static void gitSSHClone(AuthInfo authInfo, CloneCommand cloneCommand) throws GitAPIException {
        // lode user info
        String remoteRepoPath = authInfo.getRemoteRepoPath();
        String localCodeDir = authInfo.getLocalCodeDir();
//        SshdSessionFactory sshSessionFactory = getSshdSessionFactory(authInfo);

        //克隆代码库命令
//        CloneCommand cloneCommand = Git.cloneRepository();
        Git git = null;
        try {
            git = cloneCommand.setURI(remoteRepoPath) //设置远程URI
//                    .setTransportConfigCallback(transport -> {
//                        SshTransport sshTransport = ( SshTransport )transport;
//                        sshTransport.setSshSessionFactory( sshSessionFactory );
//                    })
                    .setDirectory(new File(localCodeDir)) //设置下载存放路径
                    .setCloneAllBranches(true)
                    .call();
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
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


    public static void gitCheckout(AuthInfo authInfo,String branchName){
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
                git.pull().call();//拉取最新的提交
            } finally {
                git.close();
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }


    public static void gitCheckOutAllBranch(AuthInfo authInfo,
                                            LsRemoteCommand lsRemoteCommand) throws IOException, GitAPIException {
        // lode user info
//        AuthInfo authInfo = parseSSHAuthInfo(properties);
        String localCodeDir = authInfo.getLocalCodeDir();
        localCodeDir+="\\.git";
//        SshdSessionFactory sshSessionFactory = getSshdSessionFactory(authInfo);


        Collection<Ref> allBranch = lsRemoteCommand.setRemote(authInfo.getRemoteRepoPath())
             /*   .setTransportConfigCallback(transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        })*/.setHeads(true).call();

        if(allBranch != null && allBranch.size() > 0){
            List<String> allBranchNames = allBranch.stream().map(e -> {
                String name = e.getName();
                if (name.startsWith("refs/heads/")) {
                    name = name.replace("refs/heads/", "");
                }
                return name;
            }).collect(Collectors.toList());

            for (String branchName : allBranchNames) {
//                try {
//                    FileRepository fileRepository = new FileRepository(new File(localCodeDir));
//                    Git git = new Git(fileRepository);
//                    try {
//                        if (branchNameExist(git, branchName)) {//如果分支在本地已存在，直接checkout即可。
//                            git.checkout().setCreateBranch(false).setName(branchName).call();
//                        } else {//如果分支在本地不存在，需要创建这个分支，并追踪到远程分支上面。
//                            git.checkout().setCreateBranch(true).setName(branchName).setStartPoint("origin/" + branchName).call();
//                        }
//                        git.pull().call();//拉取最新的提交
//                    } finally {
//                        git.close();
//                    }
//                } catch (IOException | GitAPIException e) {
//                    e.printStackTrace();
//                }
                gitCheckout(authInfo,branchName);
            }
        }
    }

    public static void gitPull(AuthInfo authInfo,PullCommand pullCommand) {
        // lode user info
//        AuthInfo authInfo = parseSSHAuthInfo(properties);
//        String localCodeDir = authInfo.getLocalCodeDir();
//        localCodeDir+="\\.git";
//        SshdSessionFactory sshSessionFactory = getSshdSessionFactory(authInfo);
        try {
            //关联到本地仓库
//            FileRepository fileRepository = new FileRepository(new File(localCodeDir));
//            Git pullGit = new Git(fileRepository);
            //设置密钥,拉取文件
//            PullCommand pullCommand = pullGit
//                        .pull()
//                        .setTransportConfigCallback(
//                                transport -> {
//                                    SshTransport sshTransport = ( SshTransport )transport;
//                                    sshTransport.setSshSessionFactory( sshSessionFactory );
//                                });
                try {
                    PullResult pullResult = pullCommand.call();
                    if(pullResult != null){
                        System.out.println(pullResult.getMergeResult());
                    }
                } catch (GitAPIException ex) {
                    throw new RuntimeException(ex);
                }
        }catch (Exception e) {
            e.printStackTrace();
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


//    private static SshdSessionFactory getSshdSessionFactory(AuthInfo authInfo) {
//        SshdSessionFactory sshSessionFactory = new SshdSessionFactoryBuilder()
//                .setHomeDirectory(FS.DETECTED.userHome())
//                .setSshDirectory(new File(authInfo.getPrivateKeyPath()))
//
//                .build(null);
//        return sshSessionFactory;
//    }
//
//    private static AuthInfo parseSSHAuthInfo(Properties properties){        // lode user info
//        AuthInfo authInfo = new AuthInfo();
//        String privateKeyPath = (String) properties.get(AuthInfoEnums.PRIVATE_KEY_DIR_PATH.name());
//        String remoteRepoPath = (String) properties.get(AuthInfoEnums.REMOTE_REPO_PATH.name());
//        String localCodeDir = (String) properties.get(AuthInfoEnums.LOCAL_SYNC_PATH.name());
//        return authInfo.setPrivateKeyPath(privateKeyPath)
//                .setRemoteRepoPath(remoteRepoPath)
//                .setLocalCodeDir(localCodeDir);
//    }

}
