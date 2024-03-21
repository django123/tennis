package org.django.tennis.data;

import org.django.tennis.entities.Player;
import org.django.tennis.entities.PlayerEntity;
import org.django.tennis.entities.Rank;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class PlayerList {

    public static PlayerEntity RAFAEL_NADAL = new PlayerEntity(

            "Nadal",
            "Rafael",
            LocalDate.of(1986, Month.JUNE, 3),
            5000,
            1
    );

    public static PlayerEntity NOVAK_DJOKOVIC = new PlayerEntity(

            "Djokovic",
            "Novak",
            LocalDate.of(1987, Month.MAY, 22),
            4000,
            2
    );
    public static PlayerEntity ROGER_FEDERER = new PlayerEntity(

            "Federer",
            "Roger",
            LocalDate.of(1981, Month.AUGUST, 8),
            3000,
            3
    );

    public static PlayerEntity ANDY_MURRAY = new PlayerEntity(

            "Murray",
            "Andy",
            LocalDate.of(1987, Month.MAY, 15),
            2000,
            4
    );
    public static  List<PlayerEntity> ALL = Arrays.asList(RAFAEL_NADAL, NOVAK_DJOKOVIC, ROGER_FEDERER, ANDY_MURRAY);

}
