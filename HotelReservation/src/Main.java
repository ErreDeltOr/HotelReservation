import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap <Integer, Integer> hotelRoomAmount = new HashMap<>();
        HashMap <Integer, Integer> hotelPrices = new HashMap<>();
        /* Одоместные - (1) 1 человек, полулюкс - (2) 1 человек, двумест. простое - (3) 2 человека,
        двумест. с диваном - (4) 2 человека, люкс - (5) 2 человека
         */
        hotelRoomAmount.put(1, 5);
        hotelPrices.put(1, 70);

        hotelRoomAmount.put(2, 1);
        hotelPrices.put(2, 80);

        hotelRoomAmount.put(3, 14);
        hotelPrices.put(3, 90);

        hotelRoomAmount.put(4, 4);
        hotelPrices.put(4, 100);

        hotelRoomAmount.put(5, 2);
        hotelPrices.put(5, 110);

        int [] K = new int[1]; K[0] = 26;
        int [] M = new int[1]; M[0] = 20;
        int [] minInterval = new int[1]; minInterval[0] = 2;
        int [] maxInterval = new int[1]; maxInterval[0] = 5;
        int [] stepSize = new int[1]; stepSize[0] = 20;
        int [] discountValue = new int[1]; discountValue[0] = 70;

        Experiment[] exp = new Experiment[1];

        // Окно для вывода статистики (становится видимым только после окончания периода моделирования)
        JFrame jFrameStat = new JFrame("Hotel Reservation Statistics");
        jFrameStat.getContentPane().setBackground(new Color(146,85,38));
        jFrameStat.setSize(950,700);
        jFrameStat.setLocation(500, 100);
        jFrameStat.setLayout(null);

        JButton jButtonExitStat = new JButton("ЗАКРЫТЬ");
        jButtonExitStat.setBounds(400, 500, 130, 50);
        jButtonExitStat.setBackground(new Color(214, 12, 15));
        jButtonExitStat.setBorder(BorderFactory.createLineBorder(new Color(119, 4, 6), 3));
        jFrameStat.add(jButtonExitStat);
        jButtonExitStat.addActionListener(e -> jFrameStat.setVisible(false));

        //Главная панель в окне статистики
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.setBounds(10, 10, 910, 500);
        mainPanel.setBackground(new Color(146, 85, 38));
        jFrameStat.add(mainPanel);

        // Создаём окно
        JFrame jFrame = new JFrame("Hotel Reservation");
        jFrame.getContentPane().setBackground(new Color(146,85,38));

        //Кнопка закрытия основного окна
        JButton jButtonExit = new JButton("ВЫХОД");
        jButtonExit.setBounds(1250, 610, 130, 50);
        jButtonExit.setBackground(new Color(214, 12, 15));
        jButtonExit.setBorder(BorderFactory.createLineBorder(new Color(119, 4, 6), 3));
        jFrame.add(jButtonExit);
        jButtonExit.addActionListener(e -> {
            jFrame.dispose();
            jFrameStat.dispose();
        });

        // Панель для M
        JPanel jPanelM = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelM.setPreferredSize(new Dimension(170, 50));
        jPanelM.setBackground(new Color(230, 173, 137));

        // Поле ввода M
        JLabel jLabelTopM = new JLabel("ПРОДОЛЖИТЕЛЬНОСТЬ (ч)");
        jPanelM.add(jLabelTopM);

        JLabel jLabelLeftM = new JLabel("M = ");
        jPanelM.add(jLabelLeftM);

        String[] numbersM = {"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                            "25", "26", "27", "28", "29", "30"};
        JComboBox<String> jComboBoxM = new JComboBox<>(numbersM);
        jComboBoxM.setSelectedItem("20");
        jComboBoxM.addActionListener(e -> {
            M[0] = Integer.parseInt((String) jComboBoxM.getSelectedItem());
        });
        jPanelM.add(jComboBoxM);

        // Панель для K
        JPanel jPanelK = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelK.setPreferredSize(new Dimension(170, 50));
        jPanelK.setBackground(new Color(230, 173, 137));

        // Поле ввода K
        JLabel jLabelTopK = new JLabel("КОЛ-ВО НОМЕРОВ (шт)");
        jPanelK.add(jLabelTopK);

        JLabel jLabelLeftK = new JLabel("K = ");
        jPanelK.add(jLabelLeftK);

        String[] numbersK = {"20", "21", "22", "23", "24",
                            "25", "26", "27", "28", "29", "30"};
        JComboBox<String> jComboBoxK = new JComboBox<>(numbersK);
        jComboBoxK.setSelectedItem("26");
        jPanelK.add(jComboBoxK);
        jComboBoxK.addActionListener(e -> {
            K[0] = Integer.parseInt((String) jComboBoxK.getSelectedItem());
        });

        // Объемлющая панель для панелей K и M
        JPanel jPanelKM = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        jPanelKM.setBounds(15, 15, 400, 65);
        jPanelKM.setBackground(new Color(230, 173, 137));
        jPanelKM.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelKM.add(jPanelM);
        jPanelKM.add(jPanelK);
        jFrame.add(jPanelKM);


        // Панель для min interval и max interval
        JPanel jPanelMinMax = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelMinMax.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelMinMax.setBackground(new Color(230, 173, 137));
        jPanelMinMax.setBounds(15, 90, 400, 90);
        jFrame.add(jPanelMinMax);
        // Надпись для min и max
        JLabel jLabelMinMaxTop = new JLabel("МАКСИМАЛЬНЫЙ И МИНИМАЛЬНЫЙ ИНТЕРВАЛ МЕЖДУ ЗАЯВКАМИ");
        jPanelMinMax.add(jLabelMinMaxTop);

        //Панель для minInterval
        JPanel jPanelMin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelMin.setPreferredSize(new Dimension(100, 55));
        jPanelMin.setBackground(new Color(230, 173, 137));
        jPanelMinMax.add(jPanelMin);

        // Поле ввода minInterval
        JLabel jLabelMinTop = new JLabel("    MIN (ч)    ");
        jPanelMin.add(jLabelMinTop);

        String[] numbersMinI = {"1", "2", "3", "4"};
        JComboBox<String> jComboBoxMinI = new JComboBox<>(numbersMinI);
        jComboBoxMinI.setSelectedItem("2");
        jPanelMin.add(jComboBoxMinI);
        jComboBoxMinI.addActionListener(e -> {
            try {
                minInterval[0] = Integer.parseInt((String) jComboBoxMinI.getSelectedItem());
            }
            catch (NullPointerException ex) {
            }
        });

        //Панель для maxInterval
        JPanel jPanelMax = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelMax.setPreferredSize(new Dimension(100, 55));
        jPanelMax.setBackground(new Color(230, 173, 137));
        jPanelMinMax.add(jPanelMax);

        // Поле ввода maxInterval
        JLabel jLabelMaxTop = new JLabel("    MAX (ч)    ");
        jPanelMax.add(jLabelMaxTop);

        String[] numbersMaxI = {"2", "3", "4", "5", "6"};
        JComboBox<String> jComboBoxMaxI = new JComboBox<>(numbersMaxI);
        jComboBoxMaxI.setSelectedItem("5");
        jPanelMax.add(jComboBoxMaxI);
        jComboBoxMaxI.addActionListener(e -> {
            maxInterval[0] = Integer.parseInt((String) jComboBoxMaxI.getSelectedItem());
        });

        //Объемлющаяя панель для номеров
        JPanel jPanelRoomTypes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelRoomTypes.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelRoomTypes.setBounds(15, 190, 400, 220);
        jPanelRoomTypes.setBackground(new Color(230, 173, 137));
        jFrame.add(jPanelRoomTypes);

        // Надпись для типа номеров
        JPanel jPanelRTypesText = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelRTypesText.setPreferredSize(new Dimension(250, 40));
        jPanelRTypesText.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelRTypesText);

        JLabel jLabelRTypes = new JLabel("<html>КОЛ-ВО НОМЕРОВ ОПРЕДЕЛЁННОЙ<br> КАТЕГОРИИ И ИХ ЦЕНА (шт, у.е.)<html>");
        jPanelRTypesText.add(jLabelRTypes);

        // Панель для одномеcтных номеров
        JPanel jPanelSingleRoom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelSingleRoom.setPreferredSize(new Dimension(150, 50));
        jPanelSingleRoom.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelSingleRoom);
        // Номера одноместные
        JLabel jLabelSingleRoom = new JLabel("    1-МЕСТНЫЕ    ");
        jPanelSingleRoom.add(jLabelSingleRoom);

        String[] numbersAmount = {"0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
                                  "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
                                  "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        JComboBox<String> jComboSingleAmount = new JComboBox<>(numbersAmount);
        jComboSingleAmount.setSelectedItem("5");
        jPanelSingleRoom.add(jComboSingleAmount);
        jComboSingleAmount.addActionListener(e -> {
            hotelRoomAmount.put(1, Integer.parseInt((String) jComboSingleAmount.getSelectedItem()));
        });
        String[] numbersPrice = {"40", "50", "60", "70", "80", "90", "100", "110", "120",
                                 "130", "140", "150", "160", "170", "180", "190", "200"};
        JComboBox<String> jComboSinglePrice = new JComboBox<>(numbersPrice);
        jComboSinglePrice.setSelectedItem("70");
        jPanelSingleRoom.add(jComboSinglePrice);
        jComboSinglePrice.addActionListener(e -> {
            hotelPrices.put(1, Integer.parseInt((String) jComboSinglePrice.getSelectedItem()));
        });

        // Панель для полулюкс номеров
        JPanel jPanelSLuxRoom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelSLuxRoom.setPreferredSize(new Dimension(150, 50));
        jPanelSLuxRoom.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelSLuxRoom);

        // Номера полулюкс
        JLabel jTextLabelSLUX = new JLabel("    ПОЛУЛЮКС    ");
        jPanelSLuxRoom.add(jTextLabelSLUX);

        JComboBox<String> jComboHLuxAmount = new JComboBox<>(numbersAmount);
        jComboHLuxAmount.setSelectedItem("1");
        jPanelSLuxRoom.add(jComboHLuxAmount);
        jComboHLuxAmount.addActionListener(e -> {
            hotelRoomAmount.put(2, Integer.parseInt((String) jComboHLuxAmount.getSelectedItem()));
        });
        JComboBox<String> jComboHLuxPrice = new JComboBox<>(numbersPrice);
        jComboHLuxPrice.setSelectedItem("80");
        jPanelSLuxRoom.add(jComboHLuxPrice);
        jComboHLuxPrice.addActionListener(e -> {
            hotelPrices.put(2, Integer.parseInt((String) jComboHLuxPrice.getSelectedItem()));
        });

        // Панель для двуместных номеров
        JPanel jPanelDoubleRoom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelDoubleRoom.setPreferredSize(new Dimension(150, 50));
        jPanelDoubleRoom.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelDoubleRoom);

        // Номера двуместные
        JLabel jLabelDoubleRoom = new JLabel("    2-МЕСТНЫЕ    ");
        jPanelDoubleRoom.add(jLabelDoubleRoom);

        JComboBox<String> jComboDoubleAmount = new JComboBox<>(numbersAmount);
        jComboDoubleAmount.setSelectedItem("14");
        jPanelDoubleRoom.add(jComboDoubleAmount);
        jComboDoubleAmount.addActionListener(e -> {
            hotelRoomAmount.put(3, Integer.parseInt((String) jComboDoubleAmount.getSelectedItem()));
        });
        JComboBox<String> jComboDoublePrice = new JComboBox<>(numbersPrice);
        jComboDoublePrice.setSelectedItem("90");
        jPanelDoubleRoom.add(jComboDoublePrice);
        jComboDoublePrice.addActionListener(e -> {
            hotelPrices.put(3, Integer.parseInt((String) jComboDoublePrice.getSelectedItem()));
        });

        // Панель для двуместных номеров с диваном
        JPanel jPanelSofaDoubleRoom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelSofaDoubleRoom.setPreferredSize(new Dimension(150, 50));
        jPanelSofaDoubleRoom.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelSofaDoubleRoom);

        // Номера двуместные с диваном
        JLabel jLabelSofaDoubleRoom = new JLabel("2-МЕСТНЫЕ С ДИВАНОМ");
        jPanelSofaDoubleRoom.add(jLabelSofaDoubleRoom);

        JComboBox<String> jComboSofaDoubleAmount = new JComboBox<>(numbersAmount);
        jComboSofaDoubleAmount.setSelectedItem("4");
        jPanelSofaDoubleRoom.add(jComboSofaDoubleAmount);
        jComboSofaDoubleAmount.addActionListener(e -> {
            hotelRoomAmount.put(4, Integer.parseInt((String) jComboSofaDoubleAmount.getSelectedItem()));
        });
        JComboBox<String> jComboSofaDoublePrice = new JComboBox<>(numbersPrice);
        jComboSofaDoublePrice.setSelectedItem("100");
        jPanelSofaDoubleRoom.add(jComboSofaDoublePrice);
        jComboSofaDoublePrice.addActionListener(e -> {
            hotelPrices.put(4, Integer.parseInt((String) jComboSofaDoublePrice.getSelectedItem()));
        });

        // Панель люкс номеров
        JPanel jPanelLuxRoom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelLuxRoom.setPreferredSize(new Dimension(150, 50));
        jPanelLuxRoom.setBackground(new Color(230, 173, 137));
        jPanelRoomTypes.add(jPanelLuxRoom);

        // Номера ЛЮКС
        JLabel jLabelLuxRoom = new JLabel("         ЛЮКС         ");
        jPanelLuxRoom.add(jLabelLuxRoom);

        JComboBox<String> jComboLuxAmount = new JComboBox<>(numbersAmount);
        jComboLuxAmount.setSelectedItem("2");
        jPanelLuxRoom.add(jComboLuxAmount);
        jComboLuxAmount.addActionListener(e -> {
            hotelRoomAmount.put(5, Integer.parseInt((String) jComboLuxAmount.getSelectedItem()));
        });
        JComboBox<String> jComboLuxPrice = new JComboBox<>(numbersPrice);
        jComboLuxPrice.setSelectedItem("110");
        jPanelLuxRoom.add(jComboLuxPrice);
        jComboLuxPrice.addActionListener(e -> {
            hotelPrices.put(5, Integer.parseInt((String) jComboLuxPrice.getSelectedItem()));
        });

        // Панель для скидки и размера шага моделирования
        JPanel jPanelDiscountStepSize = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelDiscountStepSize.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelDiscountStepSize.setBounds(15, 420, 400, 70);
        jPanelDiscountStepSize.setBackground(new Color(230, 173, 137));
        jFrame.add(jPanelDiscountStepSize);

        //Панель для скидки
        JPanel jPanelDiscount = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelDiscount.setPreferredSize(new Dimension(120, 60));
        jPanelDiscount.setBackground(new Color(230, 173, 137));
        jPanelDiscountStepSize.add(jPanelDiscount);

        // Скидка на номера при отсутствии нужного
        JLabel jLabelDiscount = new JLabel("СКИДКА (%)");
        jPanelDiscount.add(jLabelDiscount);

        String[] numbersDiscount = {"10", "20", "30", "40", "50", "60", "70"};
        JComboBox<String> jComboDiscount = new JComboBox<>(numbersDiscount);
        jComboDiscount.setSelectedItem("70");
        jPanelDiscount.add(jComboDiscount);
        jComboDiscount.addActionListener(e -> {
            discountValue[0] = Integer.parseInt((String) jComboDiscount.getSelectedItem());
        });


        //Панель для шага моделирования
        JPanel jPanelStepSize = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jPanelStepSize.setPreferredSize(new Dimension(120, 60));
        jPanelDiscountStepSize.add(jPanelStepSize);
        jPanelStepSize.setBackground(new Color(230, 173, 137));

        // Размер шага моделирования
        JLabel jLabelStepSize = new JLabel("РАЗМЕР ШАГА (ч)");
        jPanelStepSize.add(jLabelStepSize);

        String[] numbersStepSize = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                                    "13", "14", "15", "16", "17", "18", "19", "20"};
        JComboBox<String> jComboStepSize = new JComboBox<>(numbersStepSize);
        jComboStepSize.setSelectedItem("20");
        jComboStepSize.setBounds(135, 485, 50, 20);
        jPanelStepSize.add(jComboStepSize);
        jComboStepSize.addActionListener(e -> {
            stepSize[0] = Integer.parseInt((String) jComboStepSize.getSelectedItem());
        });

        // Панель для управления моделированием
        JPanel jPanelControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelControl.setBounds(15,590,400,50);
        jPanelControl.setBackground(new Color(146,85,38));
        jFrame.add(jPanelControl);

        // Кнопка старта моделирования
        boolean[] flag = new boolean[1];
        flag[0] = true; // Для отслеживания того, что кнопка СТАРТ ещё не была нажата пользователем
        JButton jButtonStart = new JButton("CТАРТ");
        jButtonStart.setPreferredSize(new Dimension(100, 40));
        jButtonStart.setBackground(new Color(74, 208, 67));
        jButtonStart.setBorder(BorderFactory.createLineBorder(new Color(8, 80, 5), 3));
        JLabel[] jLabelCurrentTime = new JLabel[1];
        JLabel[] jLabelCurrentRevenue = new JLabel[1];
        JLabel[] jLabelCurrentLoad = new JLabel[1];
        JPanel[] jPanelRecentRequests = new JPanel[1];
        JLabel[] jLabelAcceptedPercentage = new JLabel[1];
        JLabel[] jLabelAllRequestsCount = new JLabel[1];

        JPanel[] jPanelSingleRoomDisplay = new JPanel[1];
        JPanel[] jPanelSLuxRoomDisplay = new JPanel[1];
        JPanel[] jPanelDoubleRoomDisplay = new JPanel[1];
        JPanel[] jPanelSofaDoubleRoomDisplay = new JPanel[1];
        JPanel[] jPanelLuxRoomDisplay = new JPanel[1];

        JPanel[] jPanelHotelState = new JPanel[1];

        JButton[] jButtonFinish = new JButton[1];
        jButtonFinish[0] = new JButton("ДО КОНЦА");
        JButton[] jButtonStep = new JButton[1];
        jButtonStep[0] = new JButton("ШАГ");

        jButtonStart.addActionListener(e -> {
            exp[0] = new Experiment(K[0], M[0], minInterval[0], maxInterval[0], stepSize[0], discountValue[0],
                    hotelPrices, hotelRoomAmount);
            jFrameStat.setVisible(false);
            if (!flag[0]) {
                jPanelHotelState[0].removeAll();
                if (hotelRoomAmount.get(1) != 0) {
                    //Панель с одноместными номерами
                    jPanelSingleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSingleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSingleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSingleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSingleRoomDisplay[0]);

                    makeOneTypeDisplay(1, jPanelSingleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(2) != 0) {
                    //Панель с одноместными номерами
                    jPanelSLuxRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSLuxRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSLuxRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSLuxRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSLuxRoomDisplay[0]);

                    makeOneTypeDisplay(2, jPanelSLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(3) != 0) {
                    //Панель с одноместными номерами
                    jPanelDoubleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelDoubleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelDoubleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelDoubleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelDoubleRoomDisplay[0]);

                    makeOneTypeDisplay(3, jPanelDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(4) != 0) {
                    //Панель с одноместными номерами
                    jPanelSofaDoubleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSofaDoubleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSofaDoubleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSofaDoubleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSofaDoubleRoomDisplay[0]);

                    makeOneTypeDisplay(4, jPanelSofaDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(5) != 0) {
                    //Панель с одноместными номерами
                    jPanelLuxRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelLuxRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelLuxRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelLuxRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelLuxRoomDisplay[0]);

                    makeOneTypeDisplay(5, jPanelLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                }

            }
            jButtonStep[0].setEnabled(true);
            jButtonFinish[0].setEnabled(true);
            if (flag[0]) {
                //Панель информации о заявках
                JPanel jPanelRequests = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                jPanelRequests.setBounds(870, 15, 510, 458);
                jPanelRequests.setBackground(new Color(57, 241, 121));
                jPanelRequests.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jFrame.add(jPanelRequests);

                JLabel jLabelRequestProperties = new JLabel("  ТипКомн  |  Засел  |  Высел  |  ДатаЗаявки  |  Одобрена  |  ФактТипКомн  |  НомКомн");
                jPanelRequests.add(jLabelRequestProperties);

                jPanelRecentRequests[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelRecentRequests[0].setPreferredSize(new Dimension(510, 440));
                jPanelRecentRequests[0].setBackground(new Color(230, 173, 137));
                jPanelRecentRequests[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jPanelRequests.add(jPanelRecentRequests[0]);

                //Вывод текущего времени
                JPanel jPanelCurrentTime = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelCurrentTime.setBounds(500, 15, 300, 30);
                jPanelCurrentTime.setBackground(new Color(230, 173, 137));
                jPanelCurrentTime.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jFrame.add(jPanelCurrentTime);

                JLabel jLabelCurrentTimeText = new JLabel("ТЕКУЩЕЕ ВРЕМЯ (день-час):");
                jPanelCurrentTime.add(jLabelCurrentTimeText);

                jLabelCurrentTime[0] = new JLabel("0д - 0ч");
                jPanelCurrentTime.add(jLabelCurrentTime[0]);

                //Панель для вывода статистики
                JPanel jPanelStatistics = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelStatistics.setBounds(870, 490, 510, 115);
                jPanelStatistics.setBackground(new Color(230, 173, 137));
                jPanelStatistics.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jFrame.add(jPanelStatistics);

                //Вывод текущего дохода
                JPanel jPanelCurrentRevenue = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelCurrentRevenue.setBackground(new Color(233, 238, 155));
                jPanelCurrentRevenue.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jPanelStatistics.add(jPanelCurrentRevenue);

                JLabel jLabelCurrentRevenueText = new JLabel("ТЕКУЩИЙ ДОХОД (у.е):");
                jPanelCurrentRevenue.add(jLabelCurrentRevenueText);

                jLabelCurrentRevenue[0] = new JLabel("0");
                jPanelCurrentRevenue.add(jLabelCurrentRevenue[0]);

                //Вывод текущей загруженности
                JPanel jPanelCurrentLoad = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelCurrentLoad.setBackground(new Color(233, 238, 155));
                jPanelCurrentLoad.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jPanelStatistics.add(jPanelCurrentLoad);

                JLabel jLabelCurrentLoadText = new JLabel("ТЕКУЩАЯ ЗАГРУЖЕННОСТЬ (%):");
                jPanelCurrentLoad.add(jLabelCurrentLoadText);

                jLabelCurrentLoad[0] = new JLabel("0");
                jPanelCurrentLoad.add(jLabelCurrentLoad[0]);

                //Вывод процента принятых заявок
                JPanel jPanelAcceptedPercentage = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelAcceptedPercentage.setBackground(new Color(233, 238, 155));
                jPanelAcceptedPercentage.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jPanelStatistics.add(jPanelAcceptedPercentage);

                JLabel jLabelAcceptedPercentageText = new JLabel("ПРОЦЕНТ ПРИНЯТЫХ ЗАЯВОК (%):");
                jPanelAcceptedPercentage.add(jLabelAcceptedPercentageText);

                jLabelAcceptedPercentage[0] = new JLabel("0");
                jPanelAcceptedPercentage.add(jLabelAcceptedPercentage[0]);

                //Вывод общего количества поступивших заявок
                JPanel jPanelAllRequestsCount = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelAllRequestsCount.setBackground(new Color(233, 238, 155));
                jPanelAllRequestsCount.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jPanelStatistics.add(jPanelAllRequestsCount);

                JLabel jLabelAllRequestsCountText = new JLabel("КОЛ-ВО ПОСТУПИВШИХ ЗАЯВОК (шт):");
                jPanelAllRequestsCount.add(jLabelAllRequestsCountText);

                jLabelAllRequestsCount[0] = new JLabel("0");
                jPanelAllRequestsCount.add(jLabelAllRequestsCount[0]);

                //Панель показа текущего состояния отеля. Красные номера - заселённые в данный момент,
                // зелёные номера - незаселённые в данный момент
                jPanelHotelState[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                jPanelHotelState[0].setBounds(442, 50, 400, 600);
                jPanelHotelState[0].setBackground(new Color(230, 173, 137));
                jPanelHotelState[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                jFrame.add(jPanelHotelState[0]);

                if (hotelRoomAmount.get(1) != 0) {
                    //Панель с одноместными номерами
                    jPanelSingleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSingleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSingleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSingleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSingleRoomDisplay[0]);

                    makeOneTypeDisplay(1, jPanelSingleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(2) != 0) {
                    //Панель с одноместными номерами
                    jPanelSLuxRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSLuxRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSLuxRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSLuxRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSLuxRoomDisplay[0]);

                    makeOneTypeDisplay(2, jPanelSLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(3) != 0) {
                    //Панель с одноместными номерами
                    jPanelDoubleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelDoubleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelDoubleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelDoubleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelDoubleRoomDisplay[0]);

                    makeOneTypeDisplay(3, jPanelDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(4) != 0) {
                    //Панель с одноместными номерами
                    jPanelSofaDoubleRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelSofaDoubleRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelSofaDoubleRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelSofaDoubleRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelSofaDoubleRoomDisplay[0]);

                    makeOneTypeDisplay(4, jPanelSofaDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                }
                if (hotelRoomAmount.get(5) != 0) {
                    //Панель с одноместными номерами
                    jPanelLuxRoomDisplay[0] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                    jPanelLuxRoomDisplay[0].setPreferredSize(new Dimension(390, 114));
                    jPanelLuxRoomDisplay[0].setBackground(new Color(230, 173, 137));
                    jPanelLuxRoomDisplay[0].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                    jPanelHotelState[0].add(jPanelLuxRoomDisplay[0]);

                    makeOneTypeDisplay(5, jPanelLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                }

                // Кнопка для совершения шага моделирования
                jButtonStep[0].setPreferredSize(new Dimension(100, 40));
                jButtonStep[0].setBackground(new Color(228, 216, 12));
                jButtonStep[0].setBorder(BorderFactory.createLineBorder(new Color(133, 126, 7), 3));
                jButtonStep[0].addActionListener(e1 -> {
                    jPanelRecentRequests[0].removeAll();
                    ArrayList<HandledRequest> handledRequests;
                    exp[0].makeStep();
                    if (exp[0].getStatistics().getCurrentTime() == M[0] * 24) {
                        displayStatistics(jFrameStat, mainPanel, exp[0], hotelRoomAmount);
                        jButtonStep[0].setEnabled(false);
                        jButtonFinish[0].setEnabled(false);
                    }
                    handledRequests = exp[0].getStatistics().getLastHandledRequests();
                    JPanel[] panelRequests = new JPanel[handledRequests.size()];
                    JLabel[] labelRequests = new JLabel[handledRequests.size()];
                    for (int i = 0; i < handledRequests.size(); i++) {
                        panelRequests[i] = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
                        panelRequests[i].setPreferredSize(new Dimension(500, 20));
                        panelRequests[i].setBackground(new Color(153, 89, 230));
                        jPanelRecentRequests[0].add(panelRequests[i]);

                        labelRequests[i] = new JLabel(handledRequests.get(i).toString());
                        panelRequests[i].add(labelRequests[i]);
                    }

                    makeOneTypeDisplay(1, jPanelSingleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(2, jPanelSLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(3, jPanelDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(4, jPanelSofaDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(5, jPanelLuxRoomDisplay[0], hotelRoomAmount, exp[0]);

                    jLabelCurrentTime[0].setText(exp[0].getStatistics().getCurrentTimeToString());
                    jLabelCurrentRevenue[0].setText(String.valueOf(exp[0].getStatistics().getCurrentRevenue()));
                    jLabelCurrentLoad[0].setText(String.valueOf(exp[0].getStatistics().getOverallOccupation()));
                    jLabelAcceptedPercentage[0].setText(String.valueOf(exp[0].getStatistics().getAcceptedPercentage()));
                    jLabelAllRequestsCount[0].setText(String.valueOf(exp[0].getStatistics().getAllRequestsCount()));
                    jFrame.revalidate();
                    jFrame.repaint();
                });

                // Кнопка для совершения оставшихся шагов моделирвоания
                jButtonFinish[0].setPreferredSize(new Dimension(100, 40));
                jButtonFinish[0].setBackground(new Color(214, 12, 15));
                jButtonFinish[0].setBorder(BorderFactory.createLineBorder(new Color(119, 4, 6), 3));
                jButtonFinish[0].addActionListener(e2 -> {
                    exp[0].makeRestSteps();
                    jLabelCurrentTime[0].setText(exp[0].getStatistics().getCurrentTimeToString());
                    jLabelCurrentRevenue[0].setText(String.valueOf(exp[0].getStatistics().getCurrentRevenue()));
                    jLabelCurrentLoad[0].setText(String.valueOf(exp[0].getStatistics().getOverallOccupation()));
                    jLabelAcceptedPercentage[0].setText(String.valueOf(exp[0].getStatistics().getAcceptedPercentage()));
                    jLabelAllRequestsCount[0].setText(String.valueOf(exp[0].getStatistics().getAllRequestsCount()));
                    makeOneTypeDisplay(1, jPanelSingleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(2, jPanelSLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(3, jPanelDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(4, jPanelSofaDoubleRoomDisplay[0], hotelRoomAmount, exp[0]);
                    makeOneTypeDisplay(5, jPanelLuxRoomDisplay[0], hotelRoomAmount, exp[0]);
                    displayStatistics(jFrameStat, mainPanel, exp[0], hotelRoomAmount);
                    jFrame.revalidate();
                    jFrame.repaint();
                    jButtonFinish[0].setEnabled(false);
                    jButtonStep[0].setEnabled(false);
                });
                jPanelControl.add(jButtonStep[0]);
                jPanelControl.add(jButtonFinish[0]);
                jFrame.revalidate();
                jFrame.repaint();
                flag[0] = false;
            }
            else {

            }
            jLabelCurrentTime[0].setText("0д - 0ч");
            jLabelCurrentRevenue[0].setText("0");
            jLabelCurrentLoad[0].setText("0");

            jLabelAcceptedPercentage[0].setText("0");
            jLabelAllRequestsCount[0].setText("0");
            jPanelRecentRequests[0].removeAll();
            jFrame.revalidate();
            jFrame.repaint();
        });
        jPanelControl.add(jButtonStart);


        //Experiment[] exp0 = new Experiment[1];
        //exp0[0] = new Experiment(25, 20, 2, 5, 20, 70,
        //                                 hotelPrices, hotelRoomAmount);


        //jPanelRecentRequests.removeAll();

        jFrame.setSize(1400,700);
        jFrame.setLocation(50, 100);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
    }

    public static void makeOneTypeDisplay(int type, JPanel panel, HashMap<Integer, Integer> map, Experiment exp) {
        if (map.get(type) != 0) {
            panel.removeAll();
            String s;
            if (type == 1) {
                s = "1-МЕСТНЫЕ";
            }
            else if (type == 2) {
                s = "ПОЛУЛЮКС";
            }
            else if (type == 3) {
                s = "2-МЕСТНЫЕ";
            }
            else if (type == 4) {
                s = "2-МЕСТНЫЕ С ДИВАНОМ";
            }
            else {
                s = "ЛЮКС";
            }
            JLabel jLabelSingleRoomDisplay = new JLabel(s);
            panel.add(jLabelSingleRoomDisplay);

            JPanel[] jPanelTemp = new JPanel[exp.getStatistics().getCurrentOccupation().get(type).size()];
            JLabel[] jLabelTemp = new JLabel[exp.getStatistics().getCurrentOccupation().get(type).size()];
            int j = 0;
            for (int i : exp.getStatistics().getCurrentOccupation().get(type).keySet()) {
                jPanelTemp[j] = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
                jPanelTemp[j].setPreferredSize(new Dimension(30, 30));
                jPanelTemp[j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                panel.add(jPanelTemp[j]);
                if (exp.getStatistics().getCurrentOccupation().get(type).get(i) == 0) {
                    jPanelTemp[j].setBackground(Color.GREEN);
                } else {
                    jPanelTemp[j].setBackground(Color.RED);
                }
                jLabelTemp[j] = new JLabel(String.valueOf(i));
                jPanelTemp[j].add(jLabelTemp[j]);
                j++;
            }
        }
    }

    static void displayStatistics(JFrame jFrameStat, JPanel mainPanel, Experiment experimentFinalState, HashMap<Integer, Integer> hotelRoomAmount) {
        mainPanel.removeAll();
        //Вывод текущего дохода
        JPanel jPanelOverallRevenue = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelOverallRevenue.setBackground(new Color(233, 238, 155));
        jPanelOverallRevenue.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        mainPanel.add(jPanelOverallRevenue);

        JLabel jLabelOverallRevenueText = new JLabel("ОБЩИЙ ДОХОД (у.е):");
        jPanelOverallRevenue.add(jLabelOverallRevenueText);

        JLabel jLabelOverallRevenue = new JLabel(String.valueOf(experimentFinalState.getStatistics().getCurrentRevenue()));
        jPanelOverallRevenue.add(jLabelOverallRevenue);

        //Вывод информации о выполненных заявках
        JPanel jPanelRequestInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelRequestInfo.setPreferredSize(new Dimension(500, 75));
        jPanelRequestInfo.setBackground(new Color(230, 173, 137));
        jPanelRequestInfo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        mainPanel.add(jPanelRequestInfo);

        //Панель для вывода кол-ва поступивших заявок
        JPanel jPanelAllRequestsCount = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelAllRequestsCount.setBackground(new Color(233, 238, 155));
        jPanelAllRequestsCount.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelRequestInfo.add(jPanelAllRequestsCount);

        JLabel jLabelAllRequestsCountText = new JLabel("КОЛ-ВО ПОСТУПИВШИХ ЗАЯВОК (шт):");
        jPanelAllRequestsCount.add(jLabelAllRequestsCountText);

        JLabel jLabelAllRequestsCount = new JLabel(String.valueOf(experimentFinalState.getStatistics().getAllRequestsCount()));
        jPanelAllRequestsCount.add(jLabelAllRequestsCount);

        //Панель для вывода кол-ва принятых заявок
        JPanel jPanelAcceptedRequestsCount = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelAcceptedRequestsCount.setBackground(new Color(233, 238, 155));
        jPanelAcceptedRequestsCount.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelRequestInfo.add(jPanelAcceptedRequestsCount);

        JLabel jLabelAcceptedRequestsCountText = new JLabel("КОЛ-ВО ПРИНЯТЫХ ЗАЯВОК (шт):");
        jPanelAcceptedRequestsCount.add(jLabelAcceptedRequestsCountText);

        JLabel jLabelAcceptedRequestsCount = new JLabel(String.valueOf(experimentFinalState.getStatistics().getAcceptedRequestsCount()));
        jPanelAcceptedRequestsCount.add(jLabelAcceptedRequestsCount);

        //Панель для вывода кол-ва принятых заявок
        JPanel jPanelAcceptedPercentage = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelAcceptedPercentage.setBackground(new Color(233, 238, 155));
        jPanelAcceptedPercentage.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelRequestInfo.add(jPanelAcceptedPercentage);

        JLabel jLabelAcceptedPercentageText = new JLabel("ПРОЦЕНТ ПРИНЯТЫХ ЗАЯВОК (%):");
        jPanelAcceptedPercentage.add(jLabelAcceptedPercentageText);

        JLabel jLabelAcceptedPercentage = new JLabel(String.valueOf(experimentFinalState.getStatistics().getAcceptedPercentage()));
        jPanelAcceptedPercentage.add(jLabelAcceptedPercentage);

        //Панель для отображения информации по заселению
        JPanel jPanelResidentInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelResidentInfo.setPreferredSize(new Dimension(600, 370));
        jPanelResidentInfo.setBackground(new Color(230, 173, 137));
        jPanelResidentInfo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        mainPanel.add(jPanelResidentInfo);

        //Панель для информации о средней загруженности номеров всего отеля
        JPanel jPanelAverageLoad = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelAverageLoad.setPreferredSize(new Dimension(500, 30));
        jPanelAverageLoad.setBackground(new Color(233, 238, 155));
        jPanelAverageLoad.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelResidentInfo.add(jPanelAverageLoad);

        JLabel jLabelAverageLoadText = new JLabel("СРЕДНЯЯ ЗАГРУЖЕННОСТЬ ОТЕЛЯ (%):");
        jPanelAverageLoad.add(jLabelAverageLoadText);

        JLabel jLabelAverageLoad = new JLabel(String.valueOf(Statistics.getOverallLAverageLoad(experimentFinalState.getStatistics().getEveryTypeAverageLoad())));
        jPanelAverageLoad.add(jLabelAverageLoad);

        //Серия панелей для информации заселения и загруженности по каждому номеру
        for (int key: hotelRoomAmount.keySet()) {
            if (hotelRoomAmount.get(key) != 0) {
                jPanelResidentInfo.add(makeOneRoomInfoPanel(key, experimentFinalState));
            }
        }

        jFrameStat.setVisible(true);
        jFrameStat.revalidate();
        jFrameStat.repaint();
    }

    static JPanel makeOneRoomInfoPanel(int type, Experiment experimentFinalState) {
        String s;
        if (type == 1) {
            s = "1-МЕСТНЫЕ";
        }
        else if (type == 2) {
            s = "ПОЛУЛЮКС";
        }
        else if (type == 3) {
            s = "2-МЕСТНЫЕ";
        }
        else if (type == 4) {
            s = "2-МЕСТНЫЕ С ДИВАНОМ";
        }
        else {
            s = "ЛЮКС";
        }
        JPanel jPanelOneTypeInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelOneTypeInfo.setPreferredSize(new Dimension(200, 100));
        jPanelOneTypeInfo.setBackground(new Color(233, 238, 155));
        jPanelOneTypeInfo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JLabel jLabelOneTypeInfoText = new JLabel(s);
        jPanelOneTypeInfo.add(jLabelOneTypeInfoText);

        //Панель содержащая количество человек, которым была выделена комната данного типа
        JPanel jPanelRequestCount = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelRequestCount.setBackground(new Color(233, 238, 155));
        jPanelRequestCount.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelOneTypeInfo.add(jPanelRequestCount);

        JLabel jLabelRequestCountText = new JLabel("КОЛ-ВО ЗАСЕЛЕНИЙ (шт):");
        jPanelRequestCount.add(jLabelRequestCountText);

        JLabel jLabelRequestCount = new JLabel(String.valueOf(experimentFinalState.getStatistics().getRoomTypeFactRequestCount().get(type)));
        jPanelRequestCount.add(jLabelRequestCount);

        //Панель содержащая загруженность
        JPanel jPanelLoad = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jPanelLoad.setBackground(new Color(233, 238, 155));
        jPanelLoad.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        jPanelOneTypeInfo.add(jPanelLoad);

        JLabel jLabelLoadText = new JLabel("ЗАГРУЖЕННОСТЬ (%):");
        jPanelLoad.add(jLabelLoadText);

        JLabel jLabelLoad = new JLabel(String.valueOf(experimentFinalState.getStatistics().getEveryTypeAverageLoad().get(type)));
        jPanelLoad.add(jLabelLoad);

        return jPanelOneTypeInfo;
    }
}