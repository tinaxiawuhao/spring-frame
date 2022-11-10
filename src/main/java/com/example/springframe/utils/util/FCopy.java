package com.example.springframe.utils.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;


/**
 * 文件复制操作
 * 注：copy() 方法不支持指定目标文件绝对路径；即目标永远是目录，而不是确切的文件名；若要复制到指定目标文件，请调用copyTo() 方法指定
 */
@AllArgsConstructor
@Slf4j
public final class FCopy {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Options {
        /**
         * 是否重命名文件为uuid文件,true:重命名，false不重命名
         */
        private boolean isRename;
//		/**
//		 * 是否校验源文件，true：源文件不存在则抛出异常， false：源文件不存在不抛出异常，返回null
//		 */
//		private boolean isCheck = true;
        /**
         * 需要复制的文件名
         */
        private Set<String> names;
        /**
         * 源文件不存在时，是否忽略，不进行复制操作，也不抛异常
         * 但获取复制的新文件时可能抛出异常
         */
        @Builder.Default
        private boolean ignore = false;
    }

    public static FCopy ofDefault() {
        return of(Options.builder().build());
    }

    public static FCopy of(final Options ops) {
        return new FCopy(ops, null, null, new ArrayList<>());
    }

    private final Options ops;
    /**
     * 源文件或目录：绝对路径
     */
    @Getter
    private File from;
    /**
     * 目标目录：绝对路径
     */
    @Getter
    private File to;

    /**
     * 存储复制后产生的新文件集合
     */
    @Getter
    private final List<File> newFiles;

    public FCopy from(File from) {
        this.from = from;
        return this;
    }

    public FCopy from(String from, String... names) {
        return from(FPath.of(from, names).file());
    }

    public FCopy to(File to) {
        this.to = to;
        return this;
    }

    public FCopy to(String to, String... names) {
        return to(FPath.of(to, names).file());
    }

    /**
     * 将文件重命名为 uuid 文件名
     */
    public FCopy rename() {
        ops.isRename = true;
        return this;
    }

    /**
     * 源文件不存在时，忽略，不进行复制操作，也不抛异常
     * 但获取复制的新文件时可能抛出异常
     */
    public FCopy ignoreNotFound() {
        ops.ignore = true;
        return this;
    }

    public FCopy names(final List<String> names) {
        ops.names = new HashSet<>(names);
        return this;
    }

    public FCopy names(final Set<String> names) {
        ops.names = names;
        return this;
    }

    public FCopy names(String... names) {
        ops.names = CollectionUtil.newHashSet(names);
        return this;
    }

    public Optional<File> getNewFile() {
        return getNewFile(0);
    }

    public Optional<File> getNewFile(int index) {
        return (Util.isEmpty(newFiles)) ? Optional.empty() : Optional.of(newFiles.get(index));
    }

    public Optional<String> getNewFilePath() {
        return getNewFile().map(File::getAbsolutePath);
    }

    public Optional<String> getNewFilePath(int index) {
        return getNewFile(index).map(File::getAbsolutePath);
    }

    public Optional<String> getNewFileName() {
        return getNewFile().map(File::getName);
    }

    public Optional<String> getNewFileName(int index) {
        return getNewFile(index).map(File::getName);
    }

    /**
     * 直接从 from 复制到 to ；即 from 和 to 都是文件绝对路径
     *
     * @return {@link FCopy}
     */
    @SneakyThrows
    public FCopy copyTo() {
        copy(from, to);
        return this;
    }

    @SneakyThrows
    public FCopy copy() {
        Objects.requireNonNull(from, "请指定源文件或目录");
        Objects.requireNonNull(to, "请指定目标文件或目录");
        if (!from.exists()) {
            if (ops.ignore) {
                return this;
            }
            throw new FileNotFoundException("源文件不存在:".concat(from.getAbsolutePath()));
        }
        if (from.isFile()) {
            // from 为文件则直接复制，忽略 names 属性
            copy(from, to.toPath().resolve(ops.isRename ? FPath.FileName.of(from.getName()).getUuidFileName() : from.getName()).toFile());
        } else if (from.isDirectory()) {
            // from 为目录，则遍历 names 文件名集合
            Objects.requireNonNull(ops.names, "请指定需要复制的源文件名");
            for (String name : ops.names) {
                copy(
                        from.toPath().resolve(name).toFile(),
                        to.toPath().resolve(ops.isRename ? FPath.FileName.of(name).getUuidFileName() : name).toFile()
                );
            }
        } else {
            throw new RuntimeException(String.format("无效的文件:%s:%s:%s", from.getAbsolutePath(), from.isDirectory(), from.isFile()));
        }
        return this;
    }

    /**
     * 复制目录下的所有文件到指定目录；不包含原始目录名【只复制原始目录下的文件及子目录】 <br>
     * {src}/* > {dist}/ <br>
     * dir/[1.txt,2.txt,3.txt] > newDir/[1.txt,2.txt,3.txt]
     *
     * @return {@link FCopy}
     */
    @SneakyThrows
    public FCopy copyDir() {
        Objects.requireNonNull(from, "请指定源文件或目录");
        Objects.requireNonNull(to, "请指定目标文件或目录");
        if (!from.exists()) {
            if (ops.ignore) {
                return this;
            }
            throw new FileNotFoundException("源目录不存在:".concat(from.getAbsolutePath()));
        }
        Objects.requireNonNull(from.isDirectory() ? true : null, "复制源不是目录");
//        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        for (Path path : Files.newDirectoryStream(from.toPath())) {
            if (path.toFile().isDirectory()) {
                copyDir(path.toFile(), to.toPath().toFile());
            } else {
                copy(path.toFile(), to.toPath().resolve(path.getFileName()).toFile());
            }
        }
        return this;
    }

    @SneakyThrows
    private void copyDir(final File src, final File dist) {
        if (src.isDirectory()) {
            FPath.of(dist.toPath().resolve(src.getName())).mkdirs();
            for (Path path : Files.newDirectoryStream(src.toPath())) {
                if (path.toFile().isDirectory()) {
                    copyDir(path.toFile(), dist.toPath().resolve(src.getName()).toFile());
                } else {
                    copy(path.toFile(), dist.toPath().resolve(src.getName()).resolve(path.getFileName()).toFile());
                }
            }
        } else {
            copy(src, dist);
        }
    }

    private void copy(final File from, final File to) throws IOException {
        if (!to.getParentFile().exists()) {
            if (!to.getParentFile().mkdirs())
                throw new NullPointerException(String.format("目录创建失败：%s", to.getAbsolutePath()));
            FPath.of(to.getParentFile()).chmod(755);
        }
        if (ops.ignore && !from.exists()) {
            log.info("忽略不存在的源文件：{}", from.getAbsolutePath());
            return;
        }
        log.info("{} > {}", from.getAbsolutePath(), to.getAbsolutePath());
//        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        @Cleanup final FileChannel fromFileChannel = FileChannel.open(from.toPath(), EnumSet.of(StandardOpenOption.READ));
        @Cleanup final FileChannel toFileChannel = FileChannel.open(to.toPath(), EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        fromFileChannel.transferTo(0L, fromFileChannel.size(), toFileChannel);
        FPath.of(to).chmod(644);
        newFiles.add(to);
    }

    public static void main(String[] args) {
        Dates dates = Dates.now();
        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/temp", "json.zip")
                    .to("src/test/files/temp", FPath.FileName.of("json.zip").getUuidFileName())
                    .copyTo();
            log.info("==================copyTo");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }
        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/temp", "json.zip")
                    .to("src/test/files/temp", "json-bak.zip")
                    .copyTo();
            log.info("==================copyTo");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }
        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/libs", "README.md")
                    .to("src/test/files/temp")
                    .copy();
            log.info("==================copy");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }

        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/temp", "json.zip")
                    .rename()
                    .to("src/test/files/temp")
                    .copy();
            log.info("==================copy rename");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }

        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/libs")
                    .names("alipay-sdk-java20180104135026.jar", "alipay-sdk-java20180104135026-source.jar", "README.md")
                    .rename()
                    .to("src/test/files/temp")
                    .copy();
            log.info("==================copy Multi rename");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }
        {
            FCopy copy = FCopy.ofDefault()
                    .from("src/test/files/temp/libs")
                    .to("src/test/files/temp/test")
                    .copyDir();
            log.info("==================copy dir");
            log.info(copy.getNewFileName().orElse(null));
            log.info(copy.getNewFilePath().orElse(null));
            log.info("{}", copy.getNewFile().orElse(null));
            log.info(JSON.toJSONString(copy.getNewFiles()));
        }
        System.out.println(dates.getTimeConsuming());
    }
}
