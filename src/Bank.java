import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Chyba najciekawsz klasa Bank, tutaj jest sporo zabawy
 *
 * Jakby się coś nie wypisywało DO LOGA TO DAJ ZNAC bo mogłem o czymś zapomnieć
 */
public class Bank {
    ArrayList<Bankomat> bankomaty; //bank ma listę bankomatów i klientów
    ArrayList<Klient> klienci;
    private int licznik =0; //pomocniczy licznik do blokowania karty
    private boolean pom=false; //zmienna pomocnicza do zmiany stanu konta

    public Bank ()
    {
        bankomaty=new ArrayList<Bankomat>();
        klienci=new ArrayList<Klient>();
    }

    public void dodajKlienta (Klient klient)
    {
        if (!klienci.contains(klient))
        klienci.add(klient);
    }
    boolean menu() { //menu na switchu
        System.out.println("Witamy!");
        System.out.println("Jaką operację chesz wykonać? (Podaj cyfrę)");
        Scanner sc=new Scanner(System.in);
        System.out.println("1.Wypłacić pieniądze");
        System.out.println("2. Jesteś pracownikiem i wpłacasz pieniądze do bankomatu");
        System.out.println("3.Sprawdź stan bankomatu");
        System.out.println("4. Wyjdź");
        int wyb=sc.nextInt();
        switch (wyb) //Tak tutaj zaczyna się zabawa
                /*
                * pobieramy klienta poprzez autoryzację
                * jak go się nie udało pobrać włączmy menu raz jeszcze jak licznik>2 blokujemy kartę z zapisem do loga
                * */
        {
            case 1:{Klient a =autoryzacja(); Bankomat b=null; if (a==null) return( menu()); if(a!=null&&licznik>2)  {System.out.println("Zablokowano kartę");
                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
                    pw.println("Zablokowano kartę");pw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                * Niestety złe hasło blokowane jest przez pomocniczego użytkownika, bo nie bardzo byłem w stanie to już obejść
                * Dalej jeżeli hasło jest dobre odpalamy wybór bankomatu
                * Pobieramy od użytkownika kwotę, tutaj sprawdzamy czy jest hajs, jak jest prosimy bankomat o wypłatę, jak nie błąd, wpisujemy do logu i znowu menu
                * ewentualne błędy bankomatu załatwione są w bankomacie
                * */
                return false;  } if (a.getPassword().equals("zły")) return (menu()); { b= wybierzBankomat();} licznik=0; System.out.println("Podaj kwotę"); int kw = sc.nextInt(); if (a.hasMoney(kw)) {pom=b.wypiszWyplate(kw);}else if (!a.hasMoney(kw)) {System.out.println("Brak wystarczających środków");
                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
                    if (!a.hasMoney(kw)) pw.println("Brak wystarczjących środków na koncie");
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }}
                /*
                * Miliony warunków na zmiane salda na koncie, nawet już wszystkich nie pamiętam, ale działa. Dalej wypisujemy stan konta. Zamykamy po drodze też strumienie. Te zamknięcia w sumie nie są takie istotne, bo wszędzie je i tak zamykam dodatkowo.
                * */
                if (!pom&&b.getPom()&&a.getBalance()>0)a.setBalance(a.getBalance()-kw);  System.out.println("Pozostały stan konta to" + a.getBalance()); b.zamknij(); return(menu()); }

            /*
            * Proste dodawanie hajksu do bankomatu po kolei ilość banknotów po wybraniu bankomatu wcześniej
            * */
            case 2:{Bankomat b=wybierzBankomat(); System.out.println("Podaj ilość 200"); b.wplacPub(200,sc.nextInt()); System.out.println("Podaj ilość 100"); b.wplacPub(100,sc.nextInt()); System.out.println("Podaj ilość 50"); b.wplacPub(50,sc.nextInt()); System.out.println("Podaj ilość 20"); b.wplacPub(20,sc.nextInt()); System.out.println("Podaj ilość 10"); b.wplacPub(10,sc.nextInt());b.zamknij(); return( menu());}
            case 4:return false; //wyjście
            case 3:{Bankomat b=wybierzBankomat(); b.stan(); return true;} //sprawdzamy stan bankomatu
        }
        return false;
    }

    private Bankomat wybierzBankomat () //wypisujemy bnkomaty i zwracamy wybrany
    {
        int i=0;
        for (Bankomat b:bankomaty)
        {
            System.out.println(i + " "+ b.getNazwa());
            i++;
        }
        Scanner sc=new Scanner(System.in);
        System.out.println("Który bankomat wybierasz? (Podaj numer)");
        return bankomaty.get(sc.nextInt());
    }
    private Klient autoryzacja() //autoryzacja klienta sprawdzamy czy jest jest nik i czy pasuje hasło
    {
        ArrayList<Klient> kl = klienci;
        System.out.println("Podaj NIK");
        Scanner sc = new Scanner(System.in);
        int a =sc.nextInt();
        Klient wybr= null;
        for (Klient kli:kl)
        {
         if (kli.getNik()==a) wybr=kli;
        }
        if (wybr==null){System.out.println("Nie ma takiego klienta");
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
                 pw.println("Klienta o niku " +a);
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;}
        System.out.println("Podaj hasło");
        if (sc.next().equals(wybr.getPassword()))
            /*Sprawdzamy hasło*/
            return wybr;
        else
        {
            licznik++;
            int u=3-licznik;
            System.out.println("Błędne hasło");
            System.out.println("Pozostało prób: " +u);
            /*Te dziwne znaczki try catch to konieczna obsługa błędów dla wyjścia do pliku*/
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
                pw.println("Błędane hasło");
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Klient(1,"zły",-1000.0); //przy złym haśle zwracamy pomocniczego klienta niestety

        }
    }

}
