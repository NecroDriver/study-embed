package com.xin.web.utils.mail;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * MailDto
 *
 * @author mfh 2022/11/22 17:39
 * @version 1.0.0
 **/
@Data
public class MailDto implements Serializable {
    /**
     * 主题
     */
    private String subject;
    /**
     * 发件人
     */
    private String from;
    /**
     * 收件人
     */
    private String receiveAddress;
    /**
     * 发送时间
     */
    private Date sentDate;
    /**
     * 邮件正文
     */
    private String content;
    /**
     * 文件大小 byte
     */
    private Long size;
    /**
     * 是否已读
     */
    private boolean seen;
    /**
     * 邮件优先级
     */
    private String priority;
    /**
     * 是否需要回执
     */
    private boolean replySign;
    /**
     * 是否包含附件
     */
    private boolean containAttachment;
    /**
     * 附件列表
     */
    private List<Attachment> attachmentList;

    @Data
    public static class Attachment {
        /**
         * 文件名称
         */
        private String fileName;
        /**
         * 文件流
         */
        private InputStream file;

        public Attachment() {
        }

        public Attachment(String fileName, InputStream file) {
            this.fileName = fileName;
            this.file = file;
        }
    }
}
