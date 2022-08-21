package org.wx.support;

import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;
import org.wx.enums.AuthInfoEnums;

import java.io.File;
import java.util.Properties;

/**
 * @author wuxin
 * @date 2022/08/21 16:26:58
 */
public class AuthInfoBuilder {

    public static AuthInfo build(Properties properties){
        AuthInfo authInfo = parseSSHAuthInfo(properties);
        authInfo.setSshSessionFactory(getSshdSessionFactory(authInfo));
        authInfo.setTransportConfigCallback(transport -> {
            SshTransport sshTransport = ( SshTransport )transport;
            sshTransport.setSshSessionFactory( authInfo.getSshSessionFactory() );
        });
        return authInfo;
    }

    private static SshdSessionFactory getSshdSessionFactory(AuthInfo authInfo) {
        SshdSessionFactory sshSessionFactory = new SshdSessionFactoryBuilder()
                .setHomeDirectory(FS.DETECTED.userHome())
                .setSshDirectory(new File(authInfo.getPrivateKeyPath()))
                .build(null);
        return sshSessionFactory;
    }

    private static AuthInfo parseSSHAuthInfo(Properties properties){        // lode user info
        AuthInfo authInfo = new AuthInfo();
        String privateKeyPath = (String) properties.get(AuthInfoEnums.PRIVATE_KEY_DIR_PATH.name());
        String remoteRepoPath = (String) properties.get(AuthInfoEnums.REMOTE_REPO_PATH.name());
        String localCodeDir = (String) properties.get(AuthInfoEnums.LOCAL_SYNC_PATH.name());
        return authInfo.setPrivateKeyPath(privateKeyPath)
                .setRemoteRepoPath(remoteRepoPath)
                .setLocalCodeDir(localCodeDir);
    }

}
