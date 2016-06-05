import java.io.*;
import java.util.ArrayList;

/**
 * Klasa dla bankomatu
 */
public class Bankomat {
    private int dziesiecZl; //ilość poszczególnych banknotów
    private int dwadziesciaZl;
    private int piedziesiatZl;
    private int stoZl;
    private int dwiesciezl;
    PrintWriter pw; //buffor do wyjścia
    private String nazwa;
    private boolean pom=false; //pomocnicza

    public Bankomat (int dzisiec, int dwadziescia, int piedziesiat, int sto, int dwiescie, String nazwa) //konstruktor
    {
        dziesiecZl=dzisiec;
        dwadziesciaZl=dwadziescia;
        piedziesiatZl=piedziesiat;
        stoZl=sto;
        dwiesciezl=dwiescie;
        try {
            pw= new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.nazwa=nazwa;
    }
    public String getNazwa()
    {return this.nazwa;}
    public ArrayList <Integer> wyplac (int kwota) //metoda wyrzucająca listę z pojedynczymi banknotami czyli 400 -> 200 200
    {
        /*
        * Tutaj jest strasznie dużo tłumaczenia, ale generalnie działa to tak, że wydaje zawsze od nawiększych dostepnych banknotów
        * jak brakuje w bankomacie to wpisuje je z powrotem. Generalnie jak jest za mało hajsu w bankomacie to nie wypłaca nic i robi komunikat
        * jak się wszystko zgadza to robi listę którą obsłuzy inna metoda
        * np. 490 -> 200 200 50 20 20
        * dla biednego bankomatu nie posiadającego 200 i 50 = 490->100 100 100 100 20 20 20 20 10
        * */
        pom=true;
        int kwotapocz= kwota;
        ArrayList<Integer> doWyplaty = new ArrayList<Integer>();
        if (kwota%10!=0){System.out.println("Nie można wypłacić");
            pw.println("Zła kwota");return null;}
        else {
            int bankntot=200;
            while (kwota>0)
            {
                if (hasMoney(bankntot))
                {   while (bankntot>kwota&&nextBanknot(bankntot)>=10)bankntot=nextBanknot(bankntot);
                   /* System.out.println(bankntot);
                    System.out.println(nextBanknot(bankntot));*/
                    doWyplaty.add(bankntot);
                kwota-=bankntot;
                wwypll(bankntot,1);}
                else if (bankntot>kwota||!hasMoney(bankntot)) bankntot=nextBanknot(bankntot);
                if (bankntot==0) {System.out.println("Nie można wypłacić, za mało środków w bankomacie");
                    pom=false;
                    try {
                        pw= new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pw.println("Brak wystaczających środków w bankomacie na wypłatę " + kwotapocz);
                    for (Integer f:doWyplaty)
                    {
                        switch (f){
                        case 200:wplac(200,1); break;
                        case 100:wplac(100,1); break;
                        case 50:wplac(50,1); break;
                        case 20:wplac(20,1); break;
                        case 10:wplac(10,1); break;
                    }}
                    return null;
                }
                if(bankntot>kwota&&kwota==0) break;
            }
        }

        return doWyplaty;
    }
public boolean getPom()
{
    return pom;
}

    public int [] pubWypl (int kwota) //metoda liczy ilość poszególnych banknotów dla danej kwoty na postawie listy z metody wyżej
    {
        int [] tab= new int[5];
        ArrayList<Integer> wyplata = wyplac(kwota);
        if (wyplata==null) return null;
        for (Integer a:wyplata)
        {
         switch (a)
         {
             case 200:tab[0]++; break;
             case 100:tab[1]++; break;
             case 50:tab[2]++; break;
             case 20:tab[3]++; break;
             case 10:tab[4]++; break;
         }
        }
        return tab;
    }

    public boolean wypiszWyplate (int kwota) { //metoda na podstawie 2 poprzednich wypisuje wypłatę do logu i na konsole

        int[] tab = pubWypl(kwota);
        if (tab!=null) {
            System.out.println("200 x " + tab[0]);
            System.out.println("100 x " + tab[1]);
            System.out.println("50 x " + tab[2]);
            System.out.println("20 x " + tab[3]);
            System.out.println("10 x " + tab[4]);

            try {
                pw= new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.println();
            pw.println("Wypłacono:");
            pw.println("200 x " + tab[0]);
            pw.println("100 x " + tab[1]);
            pw.println("50 x " + tab[2]);
            pw.println("20 x " + tab[3]);
            pw.println("10 x " + tab[4]);

        }
        return tab==null;
    }


    private int nextBanknot (int banknot) //metoda pomocnicza zwracająca potrzebny do wydawania nastepny dostepny banknot
    {
        switch (banknot)
        {
            case 200:{
                if (hasMoney(100)) return 100;
                else if (hasMoney(50)) return 50;
                else if (hasMoney(20)) return 20;
                else if (hasMoney(10)) return 10;
                else return 0;
            }
            case 100:{if (hasMoney(50)) return 50;
                else if (hasMoney(20)) return 20;
                else if (hasMoney(10)) return 10;
            else return 0;}
            case 50:{if (hasMoney(20)) return 20;
            else if (hasMoney(10)) return 10;
            else return 0;}
            case 20:{if (hasMoney(10)) return 10;
            else return 0;}
            case 10:return 0;
        }
        return 0;
    }

    private boolean hasMoney (int banknot) //sprawdzamy czy bankomat ma tego rodzaju banknoty
    {
        switch (banknot)
        {
            case 200:return dwiesciezl>0;
            case 100:return stoZl>0;
            case 50:return piedziesiatZl>0;
            case 20:return dwadziesciaZl>0;
            case 10:return dziesiecZl>0;
        }
        return false;
    }

    public void wplacPub(int banknot, int ilosc) { //wpłacanie do bankomatu z logiem
        try {
            pw= new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (banknot) {
            case 10:
                dziesiecZl += ilosc;
                pw.println("Wplacono 10 zl razy " + ilosc +"do bankomatu "+this.nazwa);
                break;
            case 20:
                dwadziesciaZl += ilosc;
                pw.println("Wplacono 20 zl razy " + ilosc +"do bankomatu "+this.nazwa);
                break;
            case 50:
                piedziesiatZl += ilosc;
                pw.println("Wplacono 50 zl razy " + ilosc +"do bankomatu "+this.nazwa);
                break;
            case 100:
                stoZl += ilosc;
                pw.println("Wplacono 100 zl razy " + ilosc +"do bankomatu "+this.nazwa);
                break;
            case 200:
                pw.println("Wplacono 200 zl razy " + ilosc +"do bankomatu "+this.nazwa);
                dwiesciezl += ilosc;
                break;
            default:
                System.out.println("Nie można wpłacić takiego banknotu");
        }
        pw.close();
    }

    private void wplac (int banknot, int ilosc) //pomocnicze wpłacanie bez loga używane podczas braku częsci gotówki w bankomacie
    {
        switch (banknot)
        {
            case 10:dziesiecZl+=ilosc; break;
            case 20:dwadziesciaZl+=ilosc; break;
            case 50:piedziesiatZl+=ilosc; break;
            case 100:stoZl+=ilosc; break;
            case 200:dwiesciezl+=ilosc;break;
            default:System.out.println("Nie można wpłacić takiego banknotu");
        }
    }

    public void wwypll (int banknot, int ilosc) //chyba pomocnicza wypłata danego banknotu z bankomatu
    {
        switch (banknot)
        {
            case 10:dziesiecZl-=ilosc; break;
            case 20:dwadziesciaZl-=ilosc; break;
            case 50:piedziesiatZl-=ilosc; break;
            case 100:stoZl-=ilosc; break;
            case 200:dwiesciezl-=ilosc;break;
        }
    }
    public void zamknij ()
    {
        pw.close();
    } //w razie czaego dodatekowe zamykanie buffora


    public void stan () //sprawdzenie stanu bankomatu
    {
        try {
            pw= new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("200 x " +dwiesciezl);
        System.out.println("100 x " +stoZl);
        System.out.println("50 x " +piedziesiatZl);
        System.out.println("20 x " + dwiesciezl);
        System.out.println("10 x " +dziesiecZl);
        pw.println("Sprawdzono stan bankomatu " + this.nazwa);
    }

}
