package com.example.springframe.utils.enums;

import java.awt.*;

/**
 * 颜色枚举
 */
public enum Colors {
    // 黑色
    Black(new Color(0, 0, 0, 255)),
    Brown(new Color(153, 51, 0, 255)),
    OliveGreen(new Color(51, 51, 0, 255)),
    DarkGreen(new Color(0, 51, 0, 255)),
    DarkTeal(new Color(0, 51, 102, 255)),
    DarkBlue(new Color(0, 0, 128, 255)),
    Indigo(new Color(51, 51, 153, 255)),
    Grey80Percent(new Color(51, 51, 51, 255)),
    Orange(new Color(255, 102, 0, 255)),
    DarkYellow(new Color(128, 128, 0, 255)),
    Green(new Color(0, 128, 0, 255)),
    Teal(new Color(0, 128, 128, 255)),
    Blue(new Color(0, 0, 255, 255)),
    BlueGrey(new Color(102, 102, 153, 255)),
    Grey50Percent(new Color(128, 128, 128, 255)),
    Red(new Color(255, 0, 0, 255)),
    LightOrange(new Color(255, 153, 0, 255)),
    Lime(new Color(153, 204, 0, 255)),
    SeaGreen(new Color(51, 153, 102, 255)),
    Aqua(new Color(51, 204, 204, 255)),
    LightBlue(new Color(51, 102, 255, 255)),
    Violet(new Color(128, 0, 128, 255)),
    Grey40Percent(new Color(150, 150, 150, 255)),
    Pink(new Color(255, 0, 255, 255)),
    Gold(new Color(255, 204, 0, 255)),
    Yellow(new Color(255, 255, 0, 255)),
    BrightGreen(new Color(0, 255, 0, 255)),
    Turquoise(new Color(0, 255, 255, 255)),
    DarkRed(new Color(128, 0, 0, 255)),
    SkyBlue(new Color(0, 204, 255, 255)),
    Plum(new Color(153, 51, 102, 255)),
    Grey25Percent(new Color(192, 192, 192, 255)),
    Rose(new Color(255, 153, 204, 255)),
    LightYellow(new Color(255, 255, 153, 255)),
    LightGreen(new Color(204, 255, 204, 255)),
    LightTurquoise(new Color(204, 255, 255, 255)),
    PaleBlue(new Color(153, 204, 255, 255)),
    Lavender(new Color(204, 153, 255, 255)),
    White(new Color(255, 255, 255, 255)),
    CornflowerBlue(new Color(153, 153, 255, 255)),
    LemonChiffon(new Color(255, 255, 204, 255)),
    Maroon(new Color(127, 0, 0, 255)),
    Orchid(new Color(102, 0, 102, 255)),
    Coral(new Color(255, 128, 128, 255)),
    RoyalBlue(new Color(0, 102, 204, 255)),
    LightCornflowerBlue(new Color(204, 204, 255, 255)),
    Tan(new Color(255, 204, 153, 255)),
    ;

    public final Color color;

    Colors(final Color color) {
        this.color = color;
    }
}
