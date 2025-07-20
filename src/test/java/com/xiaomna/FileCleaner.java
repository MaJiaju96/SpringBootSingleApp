package com.xiaomna;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileCleaner {

    /**
     * 将 scanDir 中未在 fileList.txt 里列出的文件移动到 backDir
     *
     * @param fileListPath fileList.txt 的完整路径（每行一个文件名）
     * @param scanDir      待扫描目录
     * @param backDir      备份目录
     * @throws IOException 仅当读取 fileList.txt 失败时抛出，其余异常内部消化
     */
    public static void moveUnlistedFiles(String fileListPath,
                                         String scanDir,
                                         String backDir) throws IOException {

        Path listFile  = Paths.get(fileListPath);
        Path srcFolder = Paths.get(scanDir);
        Path dstFolder = Paths.get(backDir);

        // 1. 读取白名单
        List<String> whiteList = Files.readAllLines(listFile);
        // 去掉首尾空白并过滤空行
        whiteList.replaceAll(String::trim);
        whiteList.removeIf(String::isEmpty);

        // 2. 确保备份目录存在
        if (!Files.exists(dstFolder)) {
            Files.createDirectories(dstFolder);
        }

        // 3. 遍历 scanDir 中的文件
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(srcFolder)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    String name = file.getFileName().toString();
                    if (!whiteList.contains(name)) {
                        Path target = dstFolder.resolve(name);
                        // 如果目标已存在，可追加时间戳避免覆盖
                        if (Files.exists(target)) {
                            String ts = String.valueOf(System.currentTimeMillis());
                            target = dstFolder.resolve(ts + "_" + name);
                        }
                        try {
                            Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("已移动: " + name);
                        } catch (IOException e) {
                            System.err.println("移动失败: " + file + " -> " + target);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 简单测试
    public static void main(String[] args) {
        try {
            moveUnlistedFiles("/Users/majiaju/Downloads/fileList.txt",
                    "/Users/majiaju/Downloads/N-224179017-2",
                    "/Users/majiaju/Downloads/N-224179017-2/back");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
