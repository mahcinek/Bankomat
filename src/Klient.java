
public class Klient {
    /*
    * Klasa klient w sumie nic ciekawego
    * */
    private int nik;
    private String password;
    private Double balance;
/*
* Konstruktor
* */
    public Klient (int nik, String password, Double balance)
    {
    this.nik=nik;
    this.password=password;
    this.balance=balance;
    }

    public int getNik ()//potrzebne getery/setery
    {return nik;}
    public void setBalance (Double balance) //zabezpieczenie przed ustawieniem ujemnej wartości salda, reszta programu jest wewnętrzenie przed tym zabezpieczona to tylko dla celów wyświetlania
    {
        if (balance>0)
        this.balance=balance;
    }
    public String getPassword()
    {
        return password;
    }
    public Double getBalance()
    {
        return balance;
    }
    public boolean hasMoney (int kwota)
    {
        return balance>=kwota;
    }
    public void wyplata (int kwota)
    {
        balance-=kwota;
    }

}

