package org.wx.support;


import lombok.Data;
import lombok.experimental.Accessors;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;

@Data
@Accessors(chain = true)
public class AuthInfo{

      private String privateKeyPath;

      private String remoteRepoPath;

      private String localCodeDir;

      private SshdSessionFactory sshSessionFactory;

      private TransportConfigCallback transportConfigCallback;

}