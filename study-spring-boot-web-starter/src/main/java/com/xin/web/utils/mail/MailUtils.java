package com.xin.web.utils.mail;

import com.xin.web.base.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * 邮件工具类
 *
 * @author creator mafh 2019/12/17 11:02
 * @author updater
 * @version 1.0.0
 */
@Component
public class MailUtils extends Base {

    /**
     * 邮件发送
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送文本邮件
     *
     * @param sender      发送方
     * @param receiver    接收方
     * @param mailContent 邮件文本内容
     */
    public void sendMail(String sender, String receiver, String mailSubject, String mailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject(mailSubject);
        message.setText(mailContent);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送文本邮件时发生异常，错误：{}", e);
        }
        logger.info("发送文本邮件成功！");
    }

    /**
     * 发送html邮件
     *
     * @param sender      发送方
     * @param receiver    接收方
     * @param mailContent 邮件html内容
     */
    public void sendHtmlMail(String sender, String receiver, String mailSubject, String mailContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(mailSubject);
            helper.setText(mailContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送html邮件时发生异常，错误：{}", e);
        }
        logger.info("发送html邮件成功！");
    }

    /**
     * 发送附件邮件
     *
     * @param sender      发送方
     * @param receiver    接收方
     * @param mailContent 邮件内容
     * @param filePath    附件路径
     */
    public void sendFileMail(String sender, String receiver, String mailSubject, String mailContent, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(mailSubject);
            helper.setText(mailContent);
            // 读取附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            // 添加附件
            helper.addAttachment(file.getFilename(), file);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送附件邮件时发生异常，错误：{}", e);
        }
        logger.info("发送附件邮件成功！");
    }

    /**
     * 发送静态图片邮件
     * 模板 <html><body>这是有图片的邮件：<img src='cid:img0' ></body></html>
     *
     * @param sender      发送方
     * @param receiver    接收方
     * @param mailContent 邮件内容
     * @param imgPathList 图片路径列表
     */
    public void sendInlineImgMail(String sender, String receiver, String mailSubject, String mailContent, List<String> imgPathList) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
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
            logger.error("发送静态图片邮件时发生异常，错误：{}", e);
        }
        logger.info("发送静态图片邮件成功！");
    }


}
