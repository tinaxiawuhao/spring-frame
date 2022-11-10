package com.example.springframe.utils.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件或目录压缩操作 <br>
 * 可以不指定 to ，会默认在 from 同级的目录下产生一个同名的压缩包； <br>
 * 例1：当 from = D:\files\dir ；则默认 to = D:\files\dir.zip <br>
 * 例2：当 from = D:\files\content.txt ；则默认 to = D:\files\content.zip <br>
 */
@AllArgsConstructor
@Slf4j
public final class FZip {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Options {
        /**
         * 是否递归压缩子目录, true:是，false：否
         */
        @Builder.Default
        private final boolean isRecursion = true;
        /**
         * {@link Consumer}{@link Consumer<Integer>}压缩进度回调
         */
        private Consumer<Integer> progress;
        /**
         * <pre>
         * 设置不包含文件的正则表达式
         * {@link Predicate}{@link Predicate<String>}
         */
        private Predicate<String> exclude;
    }

    public static FZip ofDefault() {
        return of(Options.builder().build());
    }

    public static FZip of(final Options ops) {
        return new FZip(ops, null, null);
    }

    final Options ops;
    /**
     * 源文件或目录：绝对路径
     */
    @Getter
    private File from;
    /**
     * 目标文件：绝对路径
     */
    @Getter
    private File to;


    public FZip from(File from) {
        this.from = from;
        return this;
    }

    public FZip from(String from, String... names) {
        return from(FPath.of(from, names).file());
    }

    public FZip to(File to) {
        this.to = to;
        return this;
    }

    public FZip to(String to, String... names) {
        return to(FPath.of(to, names).file());
    }

    public String getFromFileName() {
        return from.getName();
    }

    public String getToFileName() {
        return to.getName();
    }

    /**
     * 执行压缩操作
     *
     * @return {@link FZip}
     */
    public FZip zip() throws RuntimeException {
        try {
            { // 检查参数是否正确
                Objects.requireNonNull(from, "文件或目录不存在:".concat(from.getAbsolutePath()));
                Objects.requireNonNull(from.exists() ? true : null, "文件或目录不存在:".concat(from.getAbsolutePath()));
                if (Util.isEmpty(to)) {
                    to = from.getParentFile()
                            .toPath()
                            .resolve(
                                    (from.isDirectory() ? getFromFileName() : FPath.FileName.of(getFromFileName()).getPrefix()).concat(".zip")
                            )
                            .toFile();
                } else {
                    Objects.requireNonNull(to.getName().endsWith(".zip") ? true : null, "目标后缀必须是 .zip 的文件，不能是目录或其他后缀:".concat(to.getAbsolutePath()));
                }
            }
//        Dates dates = Dates.now();
            final File[] files = from.isDirectory()
                    ? from.listFiles((dir, name) -> Objects.isNull(ops.exclude) || !ops.exclude.test(name))
                    : new File[]{from};
            Objects.requireNonNull(files, "压缩目录文件列表为空");
            @Cleanup final BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(to.toPath()));
            @Cleanup final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            int p = 0; // 进度
            for (int i = 0; i < files.length; i++) {
                write(files[i], zipOutputStream, Paths.get(""));
                if (Objects.nonNull(ops.progress)) {
                    if ((int) ((i + 1.0) / files.length * 100) > p) {
                        p = (int) ((i + 1.0) / files.length * 100);
                        ops.progress.accept(p);
                    }
                }
            }
            zipOutputStream.finish();
            zipOutputStream.flush();
//        System.out.println(dates.getTimeConsuming());
            FPath.of(to.getParentFile()).chmod(755);
            FPath.of(to).chmod(644);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return this;
    }

    private void write(final File source, final ZipOutputStream output, final Path parent) throws Exception {
        if (source.isDirectory()) {
            if (ops.isRecursion) {
                for (Path path : Files.newDirectoryStream(source.toPath())) {
                    write(path.toFile(), output, parent.resolve(source.getName()));
                }
            }
        } else {
            @Cleanup final FileChannel channel = FileChannel.open(source.toPath(), StandardOpenOption.READ);
            final ByteBuffer buffer = ByteBuffer.allocate(2048);
            output.putNextEntry(new ZipEntry(parent.resolve(source.getName()).toString()));
            int length;
            while (-1 != (length = channel.read(buffer))) {
                buffer.flip();
                output.write(buffer.array(), 0, length);
                buffer.clear();
            }
            output.closeEntry();
        }
    }


    public static void main(String[] args) {
        try {
            FZip zip = FZip.ofDefault()
                    .from("src/test/files/json")
                    .to("src/test/files/temp/json.zip")
                    .zip();
            log.info(zip.getTo().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}