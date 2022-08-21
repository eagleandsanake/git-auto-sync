package org.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wuxin
 * @date 2022/08/20 20:56:01
 */
@AllArgsConstructor
@Getter
public enum AuthInfoEnums {

    // ssh
    LOCAL_SYNC_PATH("同步到的本地目标地址"),
    REMOTE_REPO_PATH("远程repo地址"),
    PRIVATE_KEY_DIR_PATH("私钥地址"),

    // https
    USER_NAME("用户名"),
    PASSWORD("密码"),

    // proxy conf
    PROXY_SWITCH("是否开启代理"),
    IP_ADDRESS("代理地址"),
    PORT("代理端口");
    private String desc;

}
