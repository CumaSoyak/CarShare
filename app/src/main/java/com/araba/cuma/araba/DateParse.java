package com.araba.cuma.araba;

public class DateParse {
    public String dateParse(int month) {
        String monthReturn = null;
        switch (month) {
            case 1:
                monthReturn = "Ocak";
                break;

            case 2:
                monthReturn = "Şubat";

                break;

            case 3:
                monthReturn = "Mart";

                break;

            case 4:
                monthReturn = "Nisan";

                break;

            case 5:
                monthReturn = "Mayıs";

                break;

            case 6:
                monthReturn = "Haziran";

                break;

            case 7:
                monthReturn = "Temmuz";

                break;

            case 8:
                monthReturn = "Ağustos";

                break;

            case 9:
                monthReturn = "Eylül";

                break;

            case 10:
                monthReturn = "Ekim";

                break;

            case 11:
                monthReturn = "Kasım";

                break;

            case 12:
                monthReturn = "Aralık";

                break;
        }
        return monthReturn;
    }
}
