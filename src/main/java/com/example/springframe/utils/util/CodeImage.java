package com.example.springframe.utils.util;

import cn.hutool.core.util.RandomUtil;
import com.example.springframe.utils.enums.Image;
import lombok.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 生成图片验证码
 *
 */
@AllArgsConstructor
public final class CodeImage {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Options {
        /**
         * 验证码位数
         */
        @Builder.Default
        private final int length = 4;
        /**
         * 验证码宽度
         */
        @Builder.Default
        private final int width = 60;
        /**
         * 验证码高度
         */
        @Builder.Default
        private final int height = 20;
        /**
         * 图片类型
         */
        @Builder.Default
        private final Image type = Image.JPEG;
    }

    public static CodeImage ofDefault() {
        return of(Options.builder().build());
    }

    public static CodeImage of(final Options ops) {
        return new CodeImage(ops, null);
    }

    private final Options ops;
    /**
     * 图片
     */
    @Getter
    private BufferedImage image;

    /**
     * 生成图片
     *
     * @param consumer {@link Consumer}{@link Consumer<String:code:生成的验证码>} 处理验证码
     * @return {@link CodeImage}
     */
    public CodeImage generate(final Consumer<String> consumer) {
        Objects.requireNonNull(consumer, "参数【consumer】是必须的");
        image = new BufferedImage(ops.width, ops.height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        final Graphics g = image.getGraphics();
        // 生成随机类
        final Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, ops.width, ops.height);
        // 设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN,
                ops.height / 4 + 12
//                Math.max(18, (int)(height/1.2))
        ));
        for (int i = 0; i < Math.max(80, ops.width * ops.height / 25); i++) {
            // 随机产生最少80条干扰线
            g.setColor(getRandColor(100, 200));
            final int x = random.nextInt(ops.width);
            final int y = random.nextInt(ops.height);
            final int xl = random.nextInt(ops.width / 4 + 12);
            final int yl = random.nextInt(ops.height / 4 + 12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码
        final String code = RandomUtil.randomNumbers(ops.length);
        for (int i = 0; i < ops.length; i++) {
            // 将认证码显示到图象中
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
//            g.drawString(Objects.toString(code.charAt(i)), i * 17 + random.nextInt(17), 15);
            g.drawString(Objects.toString(code.charAt(i)),
                    ops.width / ops.length * i + random.nextInt(ops.width / ops.length - 12), // y = 0 则文字在左侧
                    ops.height / 2 + random.nextInt((int) (ops.height / 2.5)) // y = height 则文字在底部
            );
        }
        // 图象生效
        g.dispose();
        consumer.accept(code.toLowerCase());
        return this;
    }

    /*
     * 给定范围获得随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        final Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        final int r = fc + random.nextInt(bc - fc);
        final int g = fc + random.nextInt(bc - fc);
        final int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @SneakyThrows
    public File write(final File file) {
        Objects.requireNonNull(file, "参数【consumer】是必须的");
        Objects.requireNonNull(image, "请先生成图片:generate(System.out::println)");
        if (!ImageIO.write(image, ops.type.name(), file)) {
            throw new IOException(String.format("Could not write an image of format %s to %s", ops.type.name(), file.getAbsolutePath()));
        }
        return file;
    }

    @SneakyThrows
    public void write(final OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "参数【outputStream】是必须的");
        Objects.requireNonNull(image, "请先生成图片:generate(System.out::println)");
        if (!ImageIO.write(image, ops.type.name(), outputStream)) {
            throw new IOException("Could not write an image of format ".concat(ops.type.name()));
        }
    }

    @SneakyThrows
    public String base64() {
        Objects.requireNonNull(image, "请先生成图片:generate(System.out::println)");
        @Cleanup final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!ImageIO.write(image, ops.type.name(), byteArrayOutputStream)) {
            throw new IOException("Could not write an image of format ".concat(ops.type.name()));
        }
        return ops.type.base64(Base64.encoder(byteArrayOutputStream.toByteArray()));
    }

    public static void main(String[] args) {
        try {
            System.out.println(
                    CodeImage.ofDefault()
                            .generate(System.out::println)
                            .base64()
            );
            System.out.println(
                    CodeImage.of(
                            Options.builder()
                                    .type(Image.PNG)
                                    .build()
                    )
                            .generate(System.out::println)
                            .write(Paths.get("desc", "验证码.png").toAbsolutePath().toFile())
            );
            System.out.println(
                    CodeImage.of(
                            Options.builder()
                                    .type(Image.JPEG)
                                    .length(6)
                                    .width(200)
                                    .height(40)
                                    .build()
                    )
                            .generate(System.out::println)
                            .write(Paths.get("desc", "验证码.jpeg").toAbsolutePath().toFile())
            );
            Stream.iterate(0, n -> n + 1).limit(100).forEach(i ->
                    System.out.println(
                            CodeImage.ofDefault()
                                    .generate(System.out::println)
                                    .write(
                                            Paths.get("desc", Util.uuid().concat(".jpeg")).toAbsolutePath().toFile()
                                    )
                                    .getAbsolutePath()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}