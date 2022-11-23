package com.xin.web.utils.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * MailSender
 *
 * @author mfh 2022/11/23 10:46
 * @version 1.0.0
 **/
public class MailSender {
    private final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static JavaMailSenderImpl mailSender = null;

    public static void main(String[] args) {
        MailSender sender = new MailSender("1193482868@qq.com", "gvihvsrklrmjhhhd", "smtp.qq.com", 587, "TLS");
        sender.sendMail("bugfix@aliyun.com", "subject", "asfasfd");
    }

    /**
     * 构造邮件发送器
     *
     * @param userName    邮箱名称
     * @param password    邮箱密码
     * @param mailServer  服务器地址
     * @param port        服务器端口
     * @param encryptType 加密类型 SSL/TLS
     */
    public MailSender(String userName, String password, String mailServer, Integer port, String encryptType) {
        try {
            Properties props = new Properties();
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");
            // 设置邮件服务器主机名
            props.setProperty("mail.smtp.host", mailServer);
            //需要经过授权，也就是用户名和密码的校验，这样才能通过验证（一定要有这一条）
            props.setProperty("mail.smtp.auth", "true");
            //设置debug模式 后台输出邮件发送的过程
            props.put("mail.debug", "true");
            if ("SSL".equals(encryptType)) {
                //ssl加密
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.ssl.socketFactory", sf);
            } else if ("TLS".equals(encryptType)) {
                props.put("mail.smtp.starttls.enable", "true");
            } else {
                throw new MessagingException("加密类型错误");
            }
            mailSender = new JavaMailSenderImpl();
            mailSender.setJavaMailProperties(props);
            mailSender.setUsername(userName);
            mailSender.setPassword(password);
            mailSender.setPort(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送文本邮件
     *
     * @param receiver    接收方
     * @param mailContent 邮件文本内容
     */
    public void sendMail(String receiver, String mailSubject, String mailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Objects.requireNonNull(mailSender.getUsername()));
        message.setTo(receiver);
        message.setSubject(mailSubject);
        message.setText(mailContent);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送文本邮件时发生异常，错误：{0}", e);
        }
        logger.info("发送文本邮件成功！");
    }

    /**
     * 发送html邮件
     *
     * @param receiver    接收方
     * @param mailContent 邮件html内容
     */
    public void sendHtmlMail(String receiver, String mailSubject, String mailContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Objects.requireNonNull(mailSender.getUsername()));
            helper.setTo(receiver);
            helper.setSubject(mailSubject);
            helper.setText(mailContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送html邮件时发生异常，错误：{0}", e);
        }
        logger.info("发送html邮件成功！");
    }

    /**
     * 发送附件邮件
     *
     * @param receiver    接收方
     * @param mailContent 邮件内容
     * @param filePath    附件路径
     */
    public void sendFileMail(String receiver, String mailSubject, String mailContent, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Objects.requireNonNull(mailSender.getUsername()));
            helper.setTo(receiver);
            helper.setSubject(mailSubject);
            helper.setText(mailContent);
            // 读取附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            if (file.getFilename() != null) {
                // 添加附件
                helper.addAttachment(file.getFilename(), file);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送附件邮件时发生异常，错误：{0}", e);
        }
        logger.info("发送附件邮件成功！");
    }

    /**
     * 发送静态图片邮件
     * 模板 <html><body>这是有图片的邮件：<img src='cid:img0' ></body></html>
     *
     * @param receiver    接收方
     * @param mailContent 邮件内容
     * @param imgPathList 图片路径列表
     */
    public void sendInlineImgMail(String receiver, String mailSubject, String mailContent, List<String> imgPathList) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Objects.requireNonNull(mailSender.getUsername()));
            helper.setTo(receiver);
            helper.setSubject(mailSubject);
            helper.setText(mailContent, true);
            // 添加附件
            for (int i = 0; i < imgPathList.size(); i++) {
                // 读取附件
                FileSystemResource file = new FileSystemResource(new File(imgPathList.get(i)));
                helper.addInline("img" + i, file);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送静态图片邮件时发生异常，错误：{0}", e);
        }
        logger.info("发送静态图片邮件成功！");
    }
}
