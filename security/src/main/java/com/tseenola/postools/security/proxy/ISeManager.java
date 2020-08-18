package com.tseenola.postools.security.proxy;

import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lenovo on 2020/8/18.
 * 描述：
 */
public interface ISeManager {
    /**
     * 用指定的密钥号做加密
     * tips
     * 如果第三个参数 Algorithm 为 ALG_DES_CBC 或者 ALG_DES_ECB，当主密钥长度为 8 字节，
     * 加密数据使用DES，若主密钥长度为 16 字节 或者 24 字节，加密数据使用3DES
     *
     * @param KeyUsage 密钥用途：1：磁道密钥  2：PIN密钥 3：MAC密钥 4：主密钥
     * @param KeyNo 密钥索引 范围【0 to 149】
     * @param Algorithm 加密方式 1:ECB  2:CBC
     * @param StartValue 初始向量：Algorithm 为ECB 时为null。 Algorithm 为CBC时为8
     * @param StartValueLen StartValue 的长度
     * @param PaddingChar 填充字符：范围 0x0-0XF
     * @param EncryptData 被加密数据 长度 0-128 字节
     * @param EncryptDataLen 被加密数据 长度
     * @param ResponseData 加密结果
     * @param ResponseDataLen  加密结果长度
     * @return
     */
    public int doEncry(int KeyUsage,
                       int KeyNo,
                       @Constant.AlgType int Algorithm,
                       byte[] StartValue,
                       int StartValueLen,
                       int PaddingChar,
                       byte[] EncryptData,
                       int EncryptDataLen,
                       byte[] ResponseData,
                       byte[] ResponseDataLen);

    /**
     * 用指定的密钥号做解密
     * tips
     * 如果第三个参数 Algorithm 为 ALG_DES_CBC 或者 ALG_DES_ECB，当主密钥长度为 8 字节，
     * 解密数据使用DES，若主密钥长度为 16 字节 或者 24 字节，解密数据使用3DES
     *
     * @param KeyUsage 密钥用途：1：磁道密钥  2：PIN密钥 3：MAC密钥 4：主密钥
     * @param KeyNo 密钥索引 范围【0 to 149】
     * @param Algorithm 解密方式 1:ECB  2:CBC
     * @param StartValue 初始向量：Algorithm 为ECB 时为null。 Algorithm 为CBC时为8
     * @param StartValueLen StartValue 的长度
     * @param PaddingChar 填充字符：范围 0x0-0XF
     * @param EncryptData 被解密数据 长度 0-128 字节
     * @param EncryptDataLen 被解密数据 长度
     * @param ResponseData 解密结果
     * @param ResponseDataLen  解密结果长度
     * @return
     */
    public int doDecry(int KeyUsage,
                       int KeyNo,
                       @Constant.AlgType int Algorithm,
                       byte[] StartValue,
                       int StartValueLen,
                       int PaddingChar,
                       byte[] EncryptData,
                       int EncryptDataLen,
                       byte[] ResponseData,
                       byte[] ResponseDataLen);
}
