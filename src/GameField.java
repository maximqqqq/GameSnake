import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    //Игровое поле
    private final int SIZE = 300;//Размер поля в пикселях
    private final int DOT_SIZE = 16;// Размер в пикселях, 1на секция змейки
    private final int ALL_DOTS = 400;//Размер доступен для игровых едениц на поле
    private Image dot;//Под игровую ячейку
    private Image apple;
    private int appleX;//Позиция яблока
    private int appleY;
    private int[] x = new int[ALL_DOTS];//Для хранения всех положений змейки
    private int[] y = new int[ALL_DOTS];
    private int dots;//Размер змейки в данный момент времени
    private Timer timer;//Стондартный таймер
    private boolean left;//Поля Boolean отвечаящие за текущее направление движение змейки
    private boolean right = true;
    private boolean up;
    private boolean down;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.BLACK);
//        setBorder(new LineBorder(Color.WHITE,10));
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());//Оброботчик событий
        setFocusable(true);//Начало взаимодействия клавиш было сконекчено с игровым полем
    }

    public void initGame() {
        dots = 3;//Инициолизация начального количества точек
        for (int i = 0; i < dots; i++) {
            //Задаем начальные значение из х-позиций и у-позиций
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);//250- то с какой частотой он будет идти (в милисикундах)
        timer.start();
        createApple();

    }

    public void createApple() {
        appleX = new Random().nextInt(19) * DOT_SIZE;//20 16ти пиксельных квадратиков может поместится на поле
        appleY = new Random().nextInt(19) * DOT_SIZE;
    }

    public void loadImage() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();

        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {//Если в игре(ОТРИСОВКА ЕСЛИ СОСТОЯНИЕ В ИГРЕ)
            g.drawImage(apple, appleX, appleY, this);//рисуем яблоко
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }else {//Если в не игре(ОТРИСОВКА ЕСЛИ СОСТОЯНИЕ В НЕ ИГРЕ)
            String str = "YOU DIED";
            String str1 = String.valueOf(dots-3);
//            Font f = new Font("Arial",20,Font.BOLD);
            g.setColor(Color.red);
//            g.setFont(f);
            g.drawString(str,125,SIZE/2);
            g.drawString(str1 + " Apples",125,100);
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];//1я точка сдвигается на позицию второй 3я на поз 4й и т.д.(се точки что не голова мы переместили)
            y[i] = y[i - 1];
        }
        if (left) {//для головы мы переместим точки туда куда указывает направление
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void cheackApple() {
        if (x[0] == appleX && y[0] == appleY) {//если голова встретит яблоко(голова Х0 и У0)
            dots++;
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {//Условие при котором змейка столкнулась сама с собой
                inGame = false;
            }

            if (x[0] > SIZE) {//роверка выхода за игровое поле
                inGame = false;
            }

            if (x[0] <= 0) {//роверка выхода за игровое поле
                inGame = false;
            }

            if (y[0] > SIZE) {//роверка выхода за игровое поле
                inGame = false;
            }
            if (y[0] < 0) {//роверка выхода за игровое поле
                inGame = false;
            }
        }
    }

    @Override
//Мтод будет обробатываться(вызываться) каждый раз когда будет тикать таймер(имплементировали от ActionListener)
    public void actionPerformed(ActionEvent e) {
        if (inGame) {//Если я в игре
            cheackApple();//Проверяем не встретили ли яблоко
            checkCollisions();//Проверка на столкновение
            move();//метод для двигания змейки
        }
        repaint();//Перерисовывает поле
    }


    //Оброботка нажатия клавиш
    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();//Клавиша которая была нажата
            if (key == KeyEvent.VK_LEFT && !right) {//В соответствии какая клавиша была нажата будет происходить направление движения
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {//В соответствии какая клавиша была нажата будет происходить направление движения
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {//В соответствии какая клавиша была нажата будет происходить направление движения
                left = false;
                up = true;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {//В соответствии какая клавиша была нажата будет происходить направление движения
                left = false;
                down = true;
                right = false;
            }
        }
    }

}

