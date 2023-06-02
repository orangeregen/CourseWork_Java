package mvc;

import mvc.panels.ChoosePanel;
import mvc.panels.EnemyField;
import mvc.panels.MyField;
import mvc.panels.PanelButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;


public class View extends JFrame {

    private Controller controller;
    private Model model;
    private MyField myField; //панель игрового поля первого игрока
    private EnemyField enemyField; //панель игрового поля соперника
    private ChoosePanel choosePanel; //панель выбора настроек при добавлении корабля
    private PanelButtons panelButtons; //панель кнопок

    public View() {
        try {
            //позволяет изменить одной командой стиль всех окон и диалогов
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //теперь у приложеньки есть название (тайтл по-умному)
        setTitle("Морской бой");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        //и иконочка)))
        setIconImage(Picture.getImage("icon"));
    }

    //просто сеттеры
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    //инициализация графического интерфейса
    public void init() {
        if (enemyField != null) {
            remove(enemyField);
            remove(myField);
            remove(panelButtons);
        }
        controller.loadEmptyMyField();
        add(choosePanel = new ChoosePanel(this), BorderLayout.EAST);
        add(myField = new MyField(this), BorderLayout.WEST);
        add(panelButtons = new PanelButtons(this), BorderLayout.SOUTH);
        myField.setChoosePanel(choosePanel);
        pack();
        revalidate();
        setVisible(true);
    }

    //метод для вызова информационного диалогового окна с заданным текстом
    public static void callInformationWindow(String message) {
        JOptionPane.showMessageDialog(null, message, "Внимание!", JOptionPane.INFORMATION_MESSAGE);
    }

    //метод для загрузки пустого игрового поля первого игрока
    public void loadEmptyMyField() {
        controller.loadEmptyMyField();
        //переотрисовка нашего игрового поля
        myField.repaint();
        //установка имени радиоБаттонов на панели выбора настроек добавления корабля
        choosePanel.setNameOneDeck(4);
        choosePanel.setNameTwoDeck(3);
        choosePanel.setNameThreeDeck(2);
        choosePanel.setNameFourDeck(1);
    }

    //добавление корабля
    public void addShip(Ship ship) {
        controller.addShip(ship);
    }

    //удаление корабля с нашего поля по координатам
    public Ship removeShip(int x, int y) {
        return controller.removeShip(x, y);
    }

    //метод, который изменяет имя у радиоБаттонов при удалении/добавлении кораблей по параметру число палуб
    public void changeCountShipOnChoosePanel(int countDeck) {
        switch (countDeck) {
            case 1: {
                //параметр - число кораблей которое осталось добавить (максимальное число кораблей данного типа -
                //число кораблей уже добавленных в соответствующий список в model
                choosePanel.setNameOneDeck(4 - model.getShipsOneDeck().size());
                break;
            }
            case 2: {
                choosePanel.setNameTwoDeck(3 - model.getShipsTwoDeck().size());
                break;
            }
            case 3: {
                choosePanel.setNameThreeDeck(2 - model.getShipsThreeDeck().size());
                break;
            }
            case 4: {
                choosePanel.setNameFourDeck(1 - model.getShipsFourDeck().size());
                break;
            }
        }
        choosePanel.revalidate();
    }

    //метод перерисовывает наше игровое поле
    public void repaintMyField(Graphics g) {
        //получаем матрицу нашего поля
        Box[][] matrix = model.getMyField();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                //присваиваем боксу значение элемента матрицы
                Box box = matrix[i][j];
                //continue - продолжить выполнение цикла, но прекратить обработку остатка
                //кода в его теле для данной итерации
                if (box == null) continue;
                //подгружаем картинку на панель игрового поля первого игрока
                g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), myField);
            }
        }
    }

    //метод перерисовывает игровое поле соперника
    public void repaintEnemyField(Graphics g) {
        Box[][] matrix = model.getEnemyField(); //получаем матрицу поля соперника
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j];
                if (box == null) continue;
                //если значение картинки = пустой клетки или клетки с кораблем то...
                if ((box.getPicture() == Picture.EMPTY || box.getPicture() == Picture.SHIP)) {
                    //если бокс открыт и картинка = пустая клетка, то отрисовываем эту клетку картинкой "пустая клетка с точкой"
                    if (box.isOpen() && box.getPicture() == Picture.EMPTY) {
                        g.drawImage(Picture.getImage(Picture.POINT.name()), box.getX(), box.getY(), enemyField);
                    }
                    //иначе если бокс открыт и картинка = клетка с кораблем, то отрисовываем эту клетку картинкой "клетка с зачеркнутым кораблем"
                    else if ((box.isOpen() && box.getPicture() == Picture.SHIP)) {
                        g.drawImage(Picture.getImage(Picture.DESTROY_SHIP.name()), box.getX(), box.getY(), enemyField);
                    }
                    //в остальных случаях отрисовываем клетку картинкой "закрытая клетка"
                    else g.drawImage(Picture.getImage(Picture.CLOSED.name()), box.getX(), box.getY(), enemyField);
                }
                //иначе отрисовываем той картинкой которая хранится в матрице - для клеток нумерации столбцов и строк
                else g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), enemyField);
            }
        }
    }

    //метод который  отрабатывает после нажатия кнопки СтартИгры
    public void startGame() {
        // проверка на то, что игрок добавил полный комплект кораблей
        if (controller.checkFullSetShips()) {
            //вызов окна с созданием комнаты либо подключением к комнате
            String[] options = {"Создать комнату", "Подключиться к комнате"};
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Создайте комнату, введя 4-ех значный номер комнаты,");
            JLabel label2 = new JLabel("либо подключитесь к уже созданной:");
            JTextField field = new JTextField(25);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label1);
            panel.add(label2);
            panel.add(field);

            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Создание комнаты:",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try {
                //если отжали кнопку "создать комнату"
                if (selectedOption == 0) {
                    int port = Integer.parseInt(field.getText().trim());
                    //создается игровая комната (запускается сервер с сокетными соединениями)
                    controller.createGameRoom(port);
                    panelButtons.setTextInfo("ОЖИДАЕМ СОПЕРНИКА");
                    panelButtons.revalidate();
                    View.callInformationWindow("Ожидаем соперника: после того как соперник подключится к комнате, появится уведомление. Затем начнется игра. Ваш ход первый.");
                    //коннект клиента к серверу
                    controller.connectToRoom(port);
                    View.callInformationWindow("Второй игрок подключился! Можно начинать сражение.");
                    //обновление интерфейса клиента после подключения второго игрока
                    refreshGuiAfterConnect();
                    panelButtons.setTextInfo("СЕЙЧАС ВАШ ХОД");
                    //активация кнопки Выхода
                    panelButtons.getExitButton().setEnabled(true);
                    //добавляем слушателя к объекту панели игрового поля соперника
                    enemyField.addListener();
                } else if (selectedOption == 1) { //если отжата кнопка "подключиться к комнате"
                    int port = Integer.parseInt(field.getText().trim());
                    //коннект клиента к серверу
                    controller.connectToRoom(port);
                    View.callInformationWindow("Вы успешно подключились к комнате. Ваш соперник ходит первым.");
                    //обновление интерфейса клиента после подключения
                    refreshGuiAfterConnect();
                    panelButtons.setTextInfo("СЕЙЧАС ХОД СОПЕРНИКА");
                    //запуск нити, которая ожидает сообщение от сервера
                    new ReceiveThread().start();
                }
            } catch (Exception e) {
                View.callInformationWindow("Произошла ошибка при создании комнаты, либо номер комнаты некорректен, попробуйте еще раз.");
                e.printStackTrace();
            }
        } else View.callInformationWindow("Вы добавили не все корабли на своем поле!");
    }

    //метод отключения клиента от сервера
    public void disconnectGameRoom() {
        try {
            controller.disconnectGameRoom();
            View.callInformationWindow("Вы отключились от комнаты. Игра окончена. Вы потерпели техническое поражение.");
            //удаляем слушателя у панели игрового поля соперника
            enemyField.removeListener();
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отключении от комнаты.");
        }
    }

    //обновляет интерфейс клиента после подключения обоих игроков
    public void refreshGuiAfterConnect() {
        MouseListener[] listeners = myField.getMouseListeners();
        for (MouseListener lis : listeners) {
            //удаление слушателя у панели игрового поля 1го игрока
            myField.removeMouseListener(lis);
        }
        choosePanel.setVisible(false);
        //удаление панели настроек добавления корабля
        remove(choosePanel);
        //добавление панели игрового поля соперника
        add(enemyField = new EnemyField(this), BorderLayout.EAST);
        //отрисовка поля соперника
        enemyField.repaint();
        pack();
        //деактивация кнопки "Начать игру"
        panelButtons.getStartGameButton().setEnabled(false);
        revalidate();
    }

    //отправление на сервер сообщения с координатами отстреленной клетки
    public void sendShot(int x, int y) {
        try {
            //непосредственная отправка сообщения через контроллер
            boolean isSendShot = controller.sendMessage(x, y);
            if (isSendShot) { //если сообщение отправлено, то ...
                //переотрисовка поля соперника
                enemyField.repaint();
                //удаление слушателя у панели поля соперника
                enemyField.removeListener();
                panelButtons.setTextInfo("СЕЙЧАС ХОД СОПЕРНИКА");
                //деактивация кнопки выхода
                panelButtons.getExitButton().setEnabled(false);
                //запуск нити, которая ожидает сообщение от сервера
                new ReceiveThread().start();
            }
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отправке выстрела.");
            e.printStackTrace();
        }
    }

    //класс-поток, который ожидает сообщение от сервера
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                //контроллер принял сообщение
                boolean continueGame = controller.receiveMessage();
                myField.repaint();
                if (continueGame) { //если вернулось true, то...
                    panelButtons.setTextInfo("СЕЙЧАС ВАШ ХОД");
                    //активация кнопки выход
                    panelButtons.getExitButton().setEnabled(true);
                    //добавление слушателя к полю соперника
                    enemyField.addListener();
                } else { //если вернулось false, то игра окончена
                    panelButtons.setTextInfo("ИГРА ОКОНЧЕНА");
                    panelButtons.getExitButton().setEnabled(false);
                    enemyField.removeListener();
                    panelButtons.getRestartGameButton().setEnabled(true);
                }

            } catch (IOException | ClassNotFoundException e) {
                View.callInformationWindow("Произошла ошибка при приеме сообщения от сервера");
                e.printStackTrace();
            }
        }
    }
}
