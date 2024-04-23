package models;

import java.util.Objects;

public class Voter {

   private String name;
   private String surname;
   private String patronymic;
   private String passport;
   private String address;

   public Voter(String surname,String name,String patronymic,String passport, String address){
       this.surname = surname;
       this.name = name;
       this.patronymic = patronymic;
       this.passport = passport;
       this.address = address;
   }


   public String getName(){
       return name;
    }
    public String getSurname(){
       return surname;
    }
    public String getPatronymic(){
       return patronymic;
    }
    public String getPassport(){
       return passport;
    }
    public String getAddress(){
       return address;
    }


    public void setName(){
       this.name = name;
    }
    public void setSurname(){
       this.surname = surname;
    }
    public void setPatronymic(){
       this.patronymic = patronymic;
    }
    public void setPassport(){
       this.passport = passport;
    }
    public void setAddress(){
       this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voter voter = (Voter) o;
        return Objects.equals(name, voter.name) && Objects.equals(surname, voter.surname) &&
                Objects.equals(patronymic, voter.patronymic) && Objects.equals(passport, voter.passport) &&
                Objects.equals(address, voter.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic, passport, address);
    }
}
