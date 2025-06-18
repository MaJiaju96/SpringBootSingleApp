package com.xiaomna;

import com.spire.pdf.PdfDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class ExpiringPdfViewer extends JFrame {
    private byte[] pdfContent;
    private LocalDateTime expirationTime;

    public ExpiringPdfViewer(byte[] pdfContent, LocalDateTime expirationTime) {
        this.pdfContent = pdfContent;
        this.expirationTime = expirationTime;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("PDF Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // 检查是否已过期
        if (isExpired()) {
            JOptionPane.showMessageDialog(this, "PDF已过期，无法查看！", "过期提示", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // 显示PDF内容
        displayPdf();
    }

    private boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    private void displayPdf() {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfContent))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageCount = document.getNumberOfPages();

            JTabbedPane tabbedPane = new JTabbedPane();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = pdfRenderer.renderImage(i);
                ImageIcon icon = new ImageIcon(image);
                JLabel label = new JLabel(icon);
                tabbedPane.addTab("Page " + (i + 1), label);
            }

            add(tabbedPane);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法加载PDF文件！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        PdfDocument document = new PdfDocument();
        document.loadFromFile("E:\\WinDoc\\Downloads\\4026666-1.pdf");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.saveToStream(baos);
        // 获取byte[]
        // 从SpringBoot项目中获取PDF内容和过期时间
        byte[] pdfContent = baos.toByteArray();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // 设置过期时间为7天后

        SwingUtilities.invokeLater(() -> {
            new ExpiringPdfViewer(pdfContent, expirationTime).setVisible(true);
        });
    }
}