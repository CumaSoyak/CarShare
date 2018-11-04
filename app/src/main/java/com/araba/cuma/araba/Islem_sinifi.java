package com.araba.cuma.araba;
public class Islem_sinifi {
    public static boolean asal_sayi_bul(int sayi) {
        double kok = Math.sqrt(sayi);
        for (int i = 2; i <= kok; i++) {//bir sayının köküne kadar bölümünü denersek o sayının asal olup olmadığını anlarız.
            if (sayi % i == 0) {// mod işlemi yapar. sayı tam bölündüğü an return false komutuna iner
                return false;//sayı bölündüğü an asal değildir. Bu yüzden false değeri dönülmesi gerekmektedir.
            }
        }
        return true;

    }
    public static int kare_al(int girilenSayi){

        if(girilenSayi<=1)
            return girilenSayi;
        else
            return (int)Math.pow(girilenSayi, 2)+kare_al(girilenSayi-1);
    }
}
