package mvc;

import java.io.Serializable;

public class Box implements Serializable {
    //координаты бокса х и y на поле
    private int x;
    private int y;
    private Picture picture;  //значение соответствующей картинки для данного бокса
    private boolean isOpen = false;  //флаг открыт ли данный бокс (== стрелял ли соперник в данный бокс)

    //проверка на открытость бокса
    public boolean isOpen() {
        return isOpen;
    }

    //установка статуса бокса в открытое состояние
    public void setOpen(boolean open) {
        isOpen = open;
    }

    //получаем вид бокса
    public Picture getPicture() {
        return picture;
    }

    //отрисовываем вид бокса в зависимости от его состояния
    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    //координаты бокса
    public Box(Picture picture, int x, int y) {
        this.picture = picture;
        this.x = x;
        this.y = y;
    }

    //геттеры для координат x и y
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

