package mvc.panels;

import mvc.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelButtons extends JPanel {
    private View view;
    private JTextField infoField; //статус игры
    private JButton startGameButton; //кнопка начать игру
    private JButton exitButton; //кнопка выйти из игры
    private JButton restartGameButton; //кнопка рестарт гейм

    public PanelButtons(View view) {
        this.view = view;
        setLayout(null);
        //как будто это часть формы с полем и кнопками в подвале формы
        setPreferredSize(new Dimension(400, 50));
        infoField = new JTextField();
        //это поле со статусом игры
        setTextInfo("РАССТАВЛЯЕМ КОРАБЛИ");
        infoField.setEnabled(false);
        //границы и размещения плашки со статусом игры
        infoField.setBounds(10, 15, 180, 20);
        //кнопка "Начать игру" и её границы и размещение
        startGameButton = new JButton("Начать игру");
        startGameButton.setBounds(210, 5, 150, 40);
        startGameButton.addActionListener(new ActionButtonStartClass());
        //кнопка выхода из игры и её границы и размещение
        exitButton = new JButton("Прервать и выйти");
        exitButton.setBounds(370, 5, 150, 40);
        exitButton.addActionListener(new ActionButtonDisconnect());
        exitButton.setEnabled(false);
        //кнопка новой игры и её границы и размещение
        restartGameButton = new JButton("Играть еще");
        restartGameButton.setBounds(530, 5, 150, 40);
        restartGameButton.setEnabled(false);
        restartGameButton.addActionListener(new ActionButtonRestartGame());
        //добавление кнопок
        add(infoField);
        add(startGameButton);
        add(exitButton);
        add(restartGameButton);
    }

    public JButton getRestartGameButton() {
        return restartGameButton;
    }

    public JButton getStartGameButton() {
        return startGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    //это аппер кейс, короче все буквы в статусе игры - заглавные
    public void setTextInfo(String text) {
        infoField.setText(text.toUpperCase());
    }

    //класс слушатель для кнопки "Начать игру"
    private class ActionButtonStartClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.startGame();
        }
    }

    //класс слушатель для кнопки отключения
    //отключились от комнаты
    //кнопка выхода из игры недоступна
    //кнопка рестарт доступна
    private class ActionButtonDisconnect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.disconnectGameRoom();
            exitButton.setEnabled(false);
            restartGameButton.setEnabled(true);
        }
    }

    //класс слушатель для кнопки "играть еще"
    private class ActionButtonRestartGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.init();
        }
    }
}
