import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Bank b = new Bank(); //Bank to główna klasa posiadająca bankomaty i klientów
        ArrayList<Klient> kl = new ArrayList<Klient>(); // Tworzymy listę z klientali
            kl.add(new Klient(123,"admin1",10000.0)); //po kolei pola to Nik, hasło i stan konta
            kl.add(new Klient(111,"haslo", 300.0));
            kl.add(new Klient(333,"bieda",0.0));
        ArrayList<Bankomat> bk = new ArrayList<Bankomat>(); //lista z bankomatami
        bk.add(new Bankomat(10,10,10,10,10,"SA")); //po polei ilośc banknotów od 10 do 200 i na koniec nazwa
        bk.add(new Bankomat(800,400,10,1,1,"Drobne"));
        bk.add(new Bankomat(1000,1000,1000,1000,1000,"Bogaty"));
        bk.add(new Bankomat(1,1,1,1,1,"Pojedyncze"));
        bk.add(new Bankomat(0,0,0,0,0,"Pusty"));
        b.bankomaty=bk;
        b.klienci=kl;
        while (b.menu()); //odpalamy menu

    }
}
