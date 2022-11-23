package com.xin.web.utils.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.DateTerm;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static javax.mail.internet.MimeUtility.decodeText;

/**
 * MailRecipient
 *
 * @author mfh 2022/11/22 14:46
 * @version 1.0.0
 **/
public class MailRecipient {

    public static void main(String[] args) {
        List<MailDto> mailList = pop3SSLSearch("1193482868@qq.com", "gvihvsrklrmjhhhd", "pop.qq.com", 995, new Date(1669046400000L));
        System.out.println(mailList);
    }


    private static List<MailDto> search(Properties props, String userName, String password, String mailServer, Integer port, Date date) {
        List<MailDto> mailList = new ArrayList<>();
        try {
            // 1. 创建session
            Session session = Session.getInstance(props);
            // 2. 得到store对象
            Store store = session.getStore();
            // 3. 连接邮件服务器
            store.connect(mailServer, port, userName, password);
            // 4. 获取邮箱内的邮件夹
            Folder folder = store.getFolder("INBOX");
            // 读写
            folder.open(Folder.READ_WRITE);
            Message[] messages;
            if (date != null) {
                // 当前按日期查询对象
                messages = searchMessages(folder, date);
            } else {
                // 获得所有邮件的message对象
                messages = folder.getMessages();
            }
            // 解析邮件
            for (Message message : messages) {
                MimeMessage msg = (MimeMessage) message;
                MailDto mailDto = new MailDto();
                mailDto.setSubject(getSubject(msg));
                mailDto.setFrom(getFrom(msg));
                mailDto.setReceiveAddress(getReceiveAddress(msg, null));
                mailDto.setSentDate(msg.getSentDate());
                StringBuffer content = new StringBuffer(30);
                getMailTextContent(msg, content);
                mailDto.setContent(content.toString());
                mailDto.setSeen(isSeen(msg));
                mailDto.setPriority(getPriority(msg));
                mailDto.setReplySign(isReplySign(msg));
                mailDto.setSize((long) msg.getSize());
                boolean isContainAttachment = isContainAttachment(msg);
                mailDto.setContainAttachment(isContainAttachment);
                if (isContainAttachment) {
                    // 保存附件
                    List<MailDto.Attachment> attachmentList = new ArrayList<>();
                    saveAttachment(msg, attachmentList);
                    mailDto.setAttachmentList(attachmentList);
                }
                mailList.add(mailDto);
            }
            // 5. 关闭资源
            folder.close(true);
            store.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return mailList;
    }

    /**
     * pop3协议查询邮件列表
     *
     * @param userName   邮箱名称
     * @param password   邮箱密码
     * @param mailServer 邮箱服务
     * @param port       邮箱服务端口
     * @param date       指定日期之后的邮件
     * @return 邮件列表
     */
    public static List<MailDto> pop3Search(String userName, String password, String mailServer, Integer port, Date date) {
        // 连接信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", mailServer);
        return search(props, userName, password, mailServer, port, date);
    }

    /**
     * pop3协议SSL查询邮件列表
     *
     * @param userName   邮箱名称
     * @param password   邮箱密码
     * @param mailServer 邮箱服务
     * @param port       邮箱服务端口
     * @param date       指定日期之后的邮件
     * @return 邮件列表
     */
    public static List<MailDto> pop3SSLSearch(String userName, String password, String mailServer, Integer port, Date date) {
        // 设置SSL连接、邮件环境
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.socketFactory.port", port + "");
        props.setProperty("mail.pop3.host", mailServer);
        //ssl加密
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable", "true");
            props.put("mail.pop3.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return search(props, userName, password, mailServer, port, date);
    }

    /**
     * 根据时间筛选邮件
     *
     * @param folder 文件服务
     * @param date   日期
     * @return 邮件列表
     */
    public static Message[] searchMessages(Folder folder, Date date) throws MessagingException {
        // 1:LE 2:LT 3:EQ 4:NE 5:GT 6:GE
        DateTerm dateTerm = new DateTerm(3, date) {
            @Override
            public boolean match(Message message) {
                try {
                    Date receivedDate = message.getSentDate();
                    return this.date.before(receivedDate);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return folder.search(dateTerm);
    }

    /**
     * 获取邮件主题
     *
     * @param msg 邮件内容
     * @return 邮件主题
     */
    public static String getSubject(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        return decodeText(msg.getSubject());
    }

    /**
     * 获取邮件发件人
     *
     * @param msg 邮件内容
     * @return 姓名<Email地址>
     */
    public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            throw new MessagingException("未获取到发件人！");
        }
        InternetAddress address = (InternetAddress) froms[0];
        String personal = address.getPersonal();
        if (personal != null) {
            personal = decodeText(personal) + "";
        } else {
            personal = "";
        }
        return personal + "<" + address.getAddress() + ">";
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO收件人</p>
     * <p>Message.RecipientType.CC 抄送</p>
     * <p>Message.RecipientType.BCC密送</p>
     *
     * @param msg  邮件内容
     * @param type 收件人类型
     * @return 收件人1<邮件地址1>,收件人2<邮件地址2>,...
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuilder receiveAddress = new StringBuilder();
        Address[] addresses;
        if (type == null) {
            addresses = msg.getAllRecipients();
        } else {
            addresses = msg.getRecipients(type);
        }
        if (addresses == null || addresses.length < 1) {
            throw new MessagingException("未获取到收件人！");
        }
        for (Address value : addresses) {
            InternetAddress address = (InternetAddress) value;
            receiveAddress.append(address.toUnicodeString()).append(",");
        }
        // 删除末尾,
        receiveAddress.deleteCharAt(receiveAddress.length() - 1);
        return receiveAddress.toString();
    }

    /**
     * 获取邮件发送时间
     *
     * @param msg     邮件内容
     * @param pattern 时间格式
     * @return 时间
     */
    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date sentDate = msg.getSentDate();
        String dateStr = "";
        if (sentDate != null) {
            if (pattern == null || "".equals(pattern)) {
                pattern = "yyyy年MM月dd日 E HH:mm";
            }
            dateStr = new SimpleDateFormat(pattern).format(sentDate);
        }
        return dateStr;
    }

    /**
     * 判断是否包含附件
     *
     * @param part 邮件体
     * @return 结果
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean result = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {
                    result = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    result = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application") || contentType.contains("name")) {
                        result = true;
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            result = isContainAttachment((Part) part.getContent());
        }
        return result;
    }

    /**
     * 判断邮件是否已读
     *
     * @param msg 邮件内容
     * @return 结果
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要已读回执
     *
     * @param msg 邮件内容
     * @return 结果
     */
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        String[] headers = msg.getHeader("Disposition-Notification-To");
        return headers != null;
    }

    /**
     * 获取邮件优先级
     *
     * @param msg 邮件内容
     * @return 1：紧急（High） 3：普通（Normal） 5：低（Low）
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String result = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String priority = headers[0];
            if (priority.contains("1") || priority.contains("High")) {
                result = "紧急";
            } else if (priority.contains("5") || priority.contains("Low")) {
                result = "低";
            } else {
                result = "普通";
            }
        }
        return result;
    }

    /**
     * 获取邮件文本内容
     *
     * @param part    邮件体
     * @param content 文本内容字符串
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        if (part.isMimeType("text/*") && !part.getContentType().contains("name")) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 保存附件
     *
     * @param part           邮件体
     * @param attachmentList 附件列表
     */
    public static void saveAttachment(Part part, List<MailDto.Attachment> attachmentList) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {
                    attachmentList.add(new MailDto.Attachment(decodeText(bodyPart.getFileName()), bodyPart.getInputStream()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, attachmentList);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application") || contentType.contains("name")) {
                        attachmentList.add(new MailDto.Attachment(decodeText(bodyPart.getFileName()), bodyPart.getInputStream()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), attachmentList);
        }
    }

    /**
     * 读取文件流保存至指定目录
     *
     * @param is       输入流
     * @param destDir  存储目录
     * @param fileName 文件名
     */
    public static void saveFile(InputStream is, String destDir, String fileName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destDir + fileName));
        int len;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }
}
