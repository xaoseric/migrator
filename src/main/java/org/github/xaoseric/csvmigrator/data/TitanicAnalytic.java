package org.github.xaoseric.csvmigrator.data;

import com.opencsv.bean.CsvBindByName;

public class TitanicAnalytic {

    @CsvBindByName(column = "PassengerId")
    private int passengerId;

    @CsvBindByName(column = "Survived")
    private int survived;

    @CsvBindByName(column = "Pclass")
    private int pclass;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Sex")
    private String sex;

    @CsvBindByName(column = "Age")
    private double age;

    @CsvBindByName(column = "SibSp")
    private int sibsp;

    @CsvBindByName(column = "Parch")
    private int parch;

    @CsvBindByName(column = "Ticket")
    private String ticket;

    @CsvBindByName(column = "Fare")
    private double fare;

    @CsvBindByName(column = "Cabin")
    private String cabin;

    @CsvBindByName(column = "Embarked")
    private String embarked;

    public int getPassengerId() {
        return passengerId;
    }

    public int getSurvived() {
        return survived;
    }

    public int getPclass() {
        return pclass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public double getAge() {
        return age;
    }

    public int getSibsp() {
        return sibsp;
    }

    public int getParch() {
        return parch;
    }

    public String getTicket() {
        return ticket;
    }

    public double getFare() {
        return fare;
    }

    public String getCabin() {
        return cabin;
    }

    public String getEmbarked() {
        return embarked;
    }
}
