package mvc.panels;

import mvc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyField extends JPanel {
    private View view;
    private ChoosePanel choosePanel; //панель выбора настроек при добавлении корабля

    public void setChoosePanel(ChoosePanel choosePanel) {
        this.choosePanel = choosePanel;
    }

    //это игровое поле
    //размер 11*40, потому что потому
    //иконка игрового поля 40 пикселей
    //и 11, т к само поле 10х10, плюс буквы и цифры для навигации
    public MyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
        //слушатель мышки
        this.addMouseListener(new ActionMouse());
    }

    //тут отрисовка квадратиков поля
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintMyField(g);
    }

    //класс слушатель на нажатие кнопки мыши по панели
    private class ActionMouse extends MouseAdapter {
        @Override
        //mousePressed(MouseEvent event) — кнопка мыши нажата в момент, когда курсор находится над наблюдаемым объектом
        public void mousePressed(MouseEvent e) {
            //получаем координаты и округляем
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            //получаем кол-во палуб и ориентацию корабля
            int countDeck = choosePanel.getCountDeck();
            int placement = choosePanel.getPlacement();
            Ship ship;
            Ship removedShip;
            //если была нажата ЛКМ то добавляем корабль
            //BUTTON1 - это левая кнопка
            if (e.getButton() == MouseEvent.BUTTON1 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE)) {
                //короче, если корабль одно- двух- трех- четырехпалубный, то чекаем ориентацию
                //причем, создается корабль той палубности, что выбрал пользователь
                if (countDeck > 0 && countDeck <= 4) {
                    switch (placement) {
                        //корабль вертикальной ориентации
                        case 1: {
                            ship = new Ship(countDeck, false);
                            ship.createVerticalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        //корабль горизонтальной ориентации
                        case 2: {
                            ship = new Ship(countDeck, true);
                            ship.createHorizontalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        //если не выбрана ориентация, то вывод предупреждающего окошка
                        default:
                            View.callInformationWindow("Не выбрана ориентация размещения.");
                    }
                } else {
                    //если не выбрано количество палуб, то вывод предупреждающего окошка
                    View.callInformationWindow("Не выбрано количество палуб.");
                    return;
                }
                //если ПКМ то удаляем корабль
                //BUTTON3 - это правая кнопка
            } else if (e.getButton() == MouseEvent.BUTTON3 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) &&
                    (removedShip = view.removeShip(x, y)) != null) {
                //меняем в имени радиоБаттона кол-во оставшихся кораблей в связи с удалением корабля
                view.changeCountShipOnChoosePanel(removedShip.getCountDeck());
            }
            repaint(); //переотрисовываем панель
            //меняем в имени радиоБаттона кол-во оставшихся кораблей в связи с добавлением корабля
            view.changeCountShipOnChoosePanel(countDeck);
        }
    }
}
