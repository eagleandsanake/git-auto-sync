package org.wx.support;

import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;
import org.wx.enums.AuthInfoEnums;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
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
        // 是否开启代理
        if(authInfo.getProxySwitch() != null && authInfo.getProxySwitch()){
            ProxySelector.setDefault(new ProxySelector() {
                final ProxySelector delegate = ProxySelector.getDefault();

                @Override
                public List<Proxy> select(URI uri) {
                    return Arrays.asList(new Proxy(Proxy.Type.HTTP, InetSocketAddress
                            .createUnresolved(authInfo.getIpAddress(), authInfo.getPort())));
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    if (uri == null || sa == null || ioe == null) {
                        throw new IllegalArgumentException(
                                "Arguments can't be null.");
                    }
                }
            });
        }

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
        String proxySwitch = (String) properties.get(AuthInfoEnums.PROXY_SWITCH.name());
        String ipAddress = (String) properties.get(AuthInfoEnums.IP_ADDRESS.name());
        String port = (String) properties.get(AuthInfoEnums.PORT.name());
        return authInfo.setPrivateKeyPath(privateKeyPath)
                .setRemoteRepoPath(remoteRepoPath)
                .setLocalCodeDir(localCodeDir)
                .setProxySwitch(Boolean.valueOf(proxySwitch))
                .setIpAddress(ipAddress)
                .setPort(Integer.valueOf(port));
    }

}
