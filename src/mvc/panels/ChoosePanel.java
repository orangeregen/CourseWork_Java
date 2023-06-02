package mvc.panels;

import mvc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//панель выбора настроек при добавлении корабля
//это панелька в правой части экрана, на которой расположен выбор палубности корабля, ориентация корабля
//еще кнопка "убрать все корабли"
public class ChoosePanel extends JPanel {
    private View view;
    private JPanel panelRadio; //палубность
    private JPanel panelPlacement; //ориентация
    private JRadioButton oneDeck; //однопалубный
    private JRadioButton twoDeck; //двухпалубный
    private JRadioButton threeDeck; //трехпалубный
    private JRadioButton fourDeck; //четырехпалубный
    private JRadioButton vertical; //вертикальная ориентация
    private JRadioButton horizontal; //горизонтальная ориентация
    private JButton clearField; //кнопка "убрать все корабли"
    private ButtonGroup groupDeck; //группа кнопок про палубность
    private ButtonGroup groupPlacement; //группа кнопок про ориентацию

    public ChoosePanel(View view) {
        this.view = view;
        //это компановка, чтобы определить размер и положение объекта
        setLayout(null);
        //Dimension - инкапсуляция длины и ширины объекта
        //это размеры формы, на которой располагаются радиобаттоны и правила
        this.setPreferredSize(new Dimension(255, 400));
        panelRadio = new JPanel();
        //BoxLayout - ящик с компонентами
        //компоненты располагаются сверху вниз, потому что Y_AXIS
        panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));
        //ставим границы и размеры для панельки "палубность"
        panelRadio.setBounds(13, 190, 230, 130);
        panelPlacement = new JPanel();
        panelPlacement.setLayout(new BoxLayout(panelPlacement, BoxLayout.Y_AXIS));
        //ставим границы и размеры для панельки "ориентация корабля"
        panelPlacement.setBounds(13, 330, 230, 80);
        //для кнопки "Убрать все корабли" размеры и расположение
        clearField = new JButton("Убрать все корабли");
        clearField.setBounds(13, 410, 227, 30);
        clearField.addActionListener(new ActionClearField());
        //создание рамок с заголовками
        panelRadio.setBorder(BorderFactory.createTitledBorder("Палубность"));
        panelPlacement.setBorder(BorderFactory.createTitledBorder("Ориентация корабля"));
        //кнопки для выбора кораблей с разной палубностью
        //задается изначальное количество кораблей определенного типа
        oneDeck = new JRadioButton();
        setNameOneDeck(4);
        twoDeck = new JRadioButton();
        setNameTwoDeck(3);
        threeDeck = new JRadioButton();
        setNameThreeDeck(2);
        fourDeck = new JRadioButton();
        setNameFourDeck(1);
        //кнопки для выбора ориентации корабля
        vertical = new JRadioButton("Вертикальная");
        horizontal = new JRadioButton("Горизонтальная");
        groupDeck = new ButtonGroup();
        groupPlacement = new ButtonGroup();
        //группировка в панельку палубности
        panelRadio.add(oneDeck);
        panelRadio.add(twoDeck);
        panelRadio.add(threeDeck);
        panelRadio.add(fourDeck);
        //группировка в панельку ориентации
        panelPlacement.add(vertical);
        panelPlacement.add(horizontal);
        //добавление компонентов в контейнер
        add(panelRadio);
        add(panelPlacement);
        add(clearField);
        //в группу кнопок groupDeck группируем кнопки с выбором палубности
        groupDeck.add(oneDeck);
        groupDeck.add(twoDeck);
        groupDeck.add(threeDeck);
        groupDeck.add(fourDeck);
        //в группу кнопок groupPlacement группируем кнопки с выбором ориентации кораблей
        groupPlacement.add(vertical);
        groupPlacement.add(horizontal);
    }

    //метод для отрисовки картинок (картинки)
    @Override
    public void paintComponent(Graphics g) {
        //в методе размещается код, прорисовывающий компонент
        super.paintComponent(g);
        //getImage возвращает изображение, которое получает пиксельные данные из указанного файла
        //это размещение картинки с правилами добавления кораблей на поле, и ориентация по x и y
        g.drawImage(Picture.getImage(Picture.INFO.name()), 2, 0, this);
    }

    //установка имени радиобаттанов палубности и количества оставшихся кораблей для размещения их на поле
    //для однопалубного
    public void setNameOneDeck(int count) {
        String text = "Однопалубный, осталось - " + count;
        oneDeck.setText(text);
    }

    //для двухпалубного
    public void setNameTwoDeck(int count) {
        String text = "Двухпалубный, осталось - " + count;
        twoDeck.setText(text);
    }

    //для трехпалубного
    public void setNameThreeDeck(int count) {
        String text = "Трехпалубный, осталось - " + count;
        threeDeck.setText(text);
    }

    //для четырехпалубного
    public void setNameFourDeck(int count) {
        String text = "Четырехпалубный, осталось - " + count;
        fourDeck.setText(text);
    }

    //возвращает кол-во палуб в зависимости от того какой радиоБаттон выбран
    public int getCountDeck() {
        if (oneDeck.isSelected()) return 1;
        else if (twoDeck.isSelected()) return 2;
        else if (threeDeck.isSelected()) return 3;
        else if (fourDeck.isSelected()) return 4;
        else return 0;
    }

    //возвращает число обозначающее какая ориентация корабля выбрана
    public int getPlacement() {
        if (vertical.isSelected()) return 1;
        else if (horizontal.isSelected()) return 2;
        else return 0;
    }

    //слушатель который загружает пустое поле при нажатии кнопки "Убрать все корабли"
    private class ActionClearField implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.loadEmptyMyField();
        }
    }
}
