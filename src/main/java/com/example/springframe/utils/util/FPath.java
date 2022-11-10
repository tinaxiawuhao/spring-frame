package com.example.springframe.utils.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * 文件路径处理及文件对象操作
 *
 */
@Slf4j
public final class FPath {
    private Path path;

    private FPath(final Path path) {
        this.path = path.toAbsolutePath();
    }

    /**
     * 格式化路径
     *
     * @return {@link FPath}
     */
    public static FPath of(final String dir, String... names) {
        return new FPath(Paths.get(dir, names));
    }

    /**
     * 格式化路径
     *
     * @return {@link FPath}
     */
    public static FPath of(final File file) {
        Objects.requireNonNull(file, "参数【file】是必须的");
        return new FPath(file.toPath());
    }

    /**
     * 格式化路径
     *
     * @return {@link FPath}
     */
    public static FPath of(final Path path) {
        Objects.requireNonNull(path, "参数【path】是必须的");
        return new FPath(path);
    }

    /**
     * 格式化路径
     *
     * @param absolute String 文件绝对路径
     * @return {@link FPath}
     */
    public static FPath of(final String absolute) {
        Objects.requireNonNull(absolute, "参数【absolute】是必须的");
        return new FPath(Paths.get(absolute));
    }

    /**
     * 追加目录并返回新的 FPath 对象，原始对象不会发生变化
     *
     * @param name {@link String} 目录名
     * @return {@link FPath} 新的 FPath 对象
     */
    public FPath append(final String name) {
        return FPath.of(path.resolve(name));
    }

    /**
     * 获取文件名
     *
     * @return String 文件名
     */
    public String fileName() {
        return Objects.toString(path.getFileName(), null);
    }

    /**
     * 获取文件绝对路径
     *
     * @return String
     */
    public String absolute() {
        return path.toString();
    }

    /**
     * 获取文件对象
     *
     * @return File
     */
    public File file() {
        return path.toFile();
    }

    /**
     * 获取文件路径对象
     *
     * @return {@link Path}
     */
    public Path get() {
        return path;
    }

    /**
     * 是否为目录，当为目录时执行consumer
     *
     * @param hasTrue  {@link Consumer}{@link Consumer<FPath>} 为 true 时执行
     * @param hasFalse {@link Consumer}{@link Consumer<FPath>} 为 false 时执行
     * @return {@link FPath}
     */
    public FPath isDirectory(final Consumer<FPath> hasTrue, final Consumer<FPath> hasFalse) {
        if (path.toFile().isDirectory()) {
            if (Objects.nonNull(hasTrue)) hasTrue.accept(this);
        } else {
            if (Objects.nonNull(hasFalse)) hasFalse.accept(this);
        }
        return this;
    }

    /**
     * 是否为目录
     *
     * @return true：目录，fasle：非目录
     */
    public boolean isDirectory() {
        return path.toFile().isDirectory();
    }

    /**
     * 文件或目录是否存在，当文件或目录存在时执行consumer
     *
     * @param hasTrue  {@link Consumer}{@link Consumer<FPath>} 为 true 时执行
     * @param hasFalse {@link Consumer}{@link Consumer<FPath>} 为 false 时执行
     * @return {@link FPath}
     */
    public FPath exist(final Consumer<FPath> hasTrue, final Consumer<FPath> hasFalse) {
        if (path.toFile().exists()) {
            if (Objects.nonNull(hasTrue)) hasTrue.accept(this);
        } else {
            if (Objects.nonNull(hasFalse)) hasFalse.accept(this);
        }
        return this;
    }

    /**
     * 文件或目录是否存在
     *
     * @return true：存在，fasle：不存在
     */
    public boolean exist() {
        return path.toFile().exists();
    }

    /**
     * 创建目录；成功后返回file对象，便于链式调用，失败时抛出异常
     *
     * @return File 返回File对象，便于链式调用
     */
    public FPath mkdirsParent() {
        FPath.of(path.toFile().getParentFile()).exist(null, fPath -> {
            if (!fPath.file().mkdirs()) {
                throw new RuntimeException("目录创建失败:".concat(fPath.file().getAbsolutePath()));
            }
            fPath.chmod(755);
        });
        return this;
    }

    /**
     * 创建目录；成功后返回file对象，便于链式调用，失败时抛出异常
     *
     * @return File 返回File对象，便于链式调用
     */
    public FPath mkdirs() {
        final File file = path.toFile();
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("文件目录创建失败:".concat(file.getAbsolutePath()));
            }
        }
        chmod(755);
        return this;
    }

    /**
     * 删除路径下的文件
     *
     * @param names String[] 文件名
     */
    public void delete(final String... names) {
        delete(Arrays.asList(names));
    }

    /**
     * 删除路径下的文件
     *
     * @param names {@link List}{@link List<String>} 文件名
     */
    public void delete(final List<String> names) {
        if (Util.isEmpty(names)) {
            return;
        }
        names.forEach(name -> {
            if (Util.isNotEmpty(name)) {
                boolean delete = path.resolve(name).toFile().delete();
                if (log.isDebugEnabled()) {
                    log.debug("删除文件【{}】{}", path.resolve(name).toString(), delete);
                }
            }
        });
    }

    /**
     * 清除目录下的子目录及文件
     */
    public void deleteAll() {
        deleteAll(false);
    }

    /**
     * 清除文件目录
     *
     * @param self boolean，是否清除本身：true是（递归清除完子目录和文件之后，再清除自己），false否（只清除子目录和文件）
     */
    public void deleteAll(boolean self) {
        log.info("清除目录:{}", path.toString());
        File[] files = path.toFile().listFiles();
        if (Util.isNotEmpty(files)) {
            for (File file : Objects.requireNonNull(files)) {
                if (file.isDirectory()) {
                    FPath.of(file).deleteAll(true);
                } else {
                    if (!file.delete())
                        throw new NullPointerException(String.format("文件删除失败：%s", file.getAbsolutePath()));
                }
            }
        }
        if (self) {
            // 删除自己
            if (!path.toFile().delete())
                throw new NullPointerException(String.format("文件删除失败：%s", path.toFile().getAbsolutePath()));
        }
    }

    /**
     * 读取文件内容
     *
     * @return String
     */
    @SneakyThrows
    public String read() {
        { // 按字符读取文件内容不会出现乱码
            log.debug("read file:{}", path.toString());
            if (!path.toFile().exists()) {
                log.warn("文件不存在：{}", path.toAbsolutePath());
                return null;
            }
            return new String(Files.readAllBytes(path.toFile().toPath()), StandardCharsets.UTF_8);
//            final StringBuilder sb = new StringBuilder();
//            @Cleanup final BufferedReader reader = Files.newBufferedReader(path, Charsets.UTF_8.charset);
//            // 一次读多个字符
//            final char[] chars = new char[2048];
//            int length;
//            // 读入多个字符到字符数组中，count为一次读取字符数
//            while ((length = reader.read(chars)) != -1) {
//                sb.append(chars, 0, length);
////                log.info(new String(chars, 0, count).replaceAll("\r\n", ""));
//            }
//            return sb.toString();
        }

//        { // ByteBuffer ；按字节读取，在构建String对象时可能产生乱码
//            long start = System.currentTimeMillis();
//            StringBuilder sb = new StringBuilder();
//            @Cleanup FileChannel channel = FileChannel.open(file.toPath(), EnumSet.of(StandardOpenOption.READ));
//            int allocate = 1024, length;
//            byte[] bytes = new byte[allocate];
//            ByteBuffer buffer = ByteBuffer.allocate(allocate);
//            while ((length = channel.read(buffer)) != -1) {
//                buffer.flip();
//                buffer.get(bytes, 0, length);
//                sb.append(new String(bytes, 0, length));
//                buffer.clear();
//            }
////                log.info(sb.toString());
//            log.info(System.currentTimeMillis() - start);
//        }
//        { // MappedByteBuffer 比 ByteBuffer快 ；按字节读取，在构建String对象时可能产生乱码
//            long start = System.currentTimeMillis();
//            StringBuilder sb = new StringBuilder();
//            @Cleanup FileChannel channel = FileChannel.open(file.toPath(), EnumSet.of(StandardOpenOption.READ));
//            int allocate = 1024, count = (int) channel.size() / allocate, mode = (int) channel.size() % allocate;
//            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//            byte[] bytes = new byte[allocate];
//            for (int i = 0; i < count; i++) {
//                buffer.get(bytes);
//                sb.append(new String(bytes));
//            }
//            if (mode > 0) {
//                buffer.get(bytes, 0, mode);
//                sb.append(new String(bytes, 0, mode));
//            }
////                log.info(sb.toString());
//            log.info(System.currentTimeMillis() - start);
//        }
    }

    /**
     * 读取文件内容
     *
     * @return byte[]
     */
    @SneakyThrows
    public byte[] readByte() {
        log.debug("read file:{}", path.toString());
        return Files.readAllBytes(path);
//        { // MappedByteBuffer 比 ByteBuffer快 ；按字节读取，在构建String对象时可能产生乱码；必须要读完之后才能toString()
//            long start = System.currentTimeMillis();
//            StringBuilder sb = new StringBuilder();
//            @Cleanup final FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.READ));
//            int allocate = 1024, count = (int) channel.size() / allocate, mode = (int) channel.size() % allocate;
//            final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//            byte[] bytes = new byte[allocate];
//            for (int i = 0; i < count; i++) {
//                buffer.get(bytes);
//                sb.append(new String(bytes));
//            }
//            if (mode > 0) {
//                buffer.get(bytes, 0, mode);
//                sb.append(new String(bytes, 0, mode));
//            }
////                log.info(sb.toString());
//            log.info("{}", System.currentTimeMillis() - start);
//        }
    }

    /**
     * 读取文件内容；按 \n 返回所有行
     *
     * @return {@link List}{@link List<String>}
     */
    @SneakyThrows
    public List<String> readLines() {
        log.debug("read file:{}", path.toString());
        return Files.readAllLines(path);
    }

    /**
     * 读取文件内容；按 \n 返回所有行
     *
     * @param charset {@link Charset} 指定编码
     * @return {@link List}{@link List<String>}
     */
    @SneakyThrows
    public List<String> readLines(final Charset charset) {
        log.debug("read file:{}", path.toString());
        return Files.readAllLines(path, charset);
    }

    /**
     * 按 \n 返回流
     *
     * @return {@link Stream}{@link Stream<String>}
     */
    @SneakyThrows
    public Stream<String> lines() {
        log.debug("read file:{}", path.toString());
        return Files.lines(path);
    }

    /**
     * 按 \n 返回流
     *
     * @param charset {@link Charset} 指定编码
     * @return {@link List}{@link List<String>}
     */
    @SneakyThrows
    public Stream<String> lines(final Charset charset) {
        log.debug("read file:{}", path.toString());
        return Files.lines(path, charset);
    }

    /**
     * 设置文件权限
     */
    public FPath chmod() {
        try {
            if (isDirectory()) {
                Runtime.getRuntime().exec("chmod 755 ".concat(absolute()));
            } else {
                Runtime.getRuntime().exec("chmod 644 ".concat(absolute()));
            }
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * 设置目录权限
     */
    public FPath chmodDirectory() {
        try {
            Runtime.getRuntime().exec("chmod 755 ".concat(absolute()));
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * 设置文件权限
     */
    public FPath chmodFile() {
        try {
            Runtime.getRuntime().exec("chmod 644 ".concat(absolute()));
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * 设置文件权限
     *
     * @param value int
     */
    public FPath chmod(final int value) {
        try {
            Runtime.getRuntime().exec(String.format("chmod %d %s", value, absolute()));
        } catch (Exception e) {
        }
        return this;
    }

    /**
     * 文件名处理
     */
    public static class FileName {
        private String name;

        private FileName(String name) {
            this.name = name;
        }

        public static FileName of(final String filename) {
            Objects.requireNonNull(filename, "参数【filename】是必须的");
            return new FileName(filename);
        }

        /**
         * 获取文件后缀名,带"."
         *
         * @return String
         */
        public String getSubfix() {
            return getSubfix(true);
        }

        /**
         * 获取文件后缀名，可选择是否带点；例：test.txt，带点则返回：.txt，不带点则返回：txt
         *
         * @param offset 是否带.,true：带点.，false不带点.
         * @return String
         */
        public String getSubfix(boolean offset) {
            return name.replaceFirst("^.+\\.", (offset ? "." : ""));
        }

        /**
         * 获取文件名，不带后缀
         *
         * @return String
         */
        public String getPrefix() {
            return name.substring(0, name.lastIndexOf("."));
        }

        /**
         * 文件名转换为UUID文件名
         *
         * @return String UUID文件名，带后缀
         */
        public String getUuidFileName() {
            return Util.uuid() + getSubfix();
        }
    }

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        log.info("{}", runtime.maxMemory());
        log.info("{}", runtime.totalMemory());
        log.info("{}", runtime.freeMemory());
        Path path = Paths.get("src/test/files/temp");
        log.info("{}", path.resolve(".zip"));
        log.info(path.getParent().toString());
        log.info(path.getName(path.getNameCount() - 1) + ".zip");
        log.info(Paths.get(path.getParent().toString(), path.getFileName() + ".zip").toAbsolutePath().toString());
        try {
            Dates date = Dates.now();
            System.out.println(
                    FPath
                            .of("src/test/files/json/test.json")
                            .read()
            );
            log.info("{}", date.getTimeConsuming());
        } catch (Exception e) {
            e.printStackTrace();
        }
//

    }
}
