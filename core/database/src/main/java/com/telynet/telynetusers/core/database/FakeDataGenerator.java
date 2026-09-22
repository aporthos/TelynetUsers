package com.telynet.telynetusers.core.database;

import com.telynet.telynetusers.core.database.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDataGenerator {

    public static List<UserEntity> generate200FakeUsers() {
        List<UserEntity> fakeUsers = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"Carlos", "Ana", "Luis", "Maria", "Juan", "Sofia", "Diego", "Elena"};
        String[] lastNames = {"Garcia", "Martinez", "Lopez", "Gonzalez", "Rodriguez", "Perez"};
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com"};

        // Nuevos bancos de datos para Address y Company
        String[] streets = {"Av. Reforma", "Calle Juárez", "Av. Insurgentes", "Calle 5 de Mayo", "Av. Hidalgo"};
        String[] cities = {"CDMX", "Guadalajara", "Monterrey", "Puebla", "Querétaro"};
        String[] companies = {"Tech Solutions Inc", "Global Logistics", "Innovatech", "Nova Industries", "Pixel Craft Studio"};

        for (int i = 1; i <= 200; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String fullName = firstName + " " + lastName;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@" + domains[random.nextInt(domains.length)];
            String phone = "+52" + (1000000000L + (long) (random.nextDouble() * 9000000000L));
            boolean isVisited = random.nextBoolean();
            String code = "USR-" + String.format("%04d", i);

            String address = streets[random.nextInt(streets.length)] + " #" + (10 + random.nextInt(900)) + ", " + cities[random.nextInt(cities.length)];

            String imageUrl = "https://picsum.photos/id/" + (i + 10) + "/200/200";

            String company = companies[random.nextInt(companies.length)];

            fakeUsers.add(new UserEntity(code, fullName, email, phone, isVisited, address, imageUrl, company, false));
        }

        return fakeUsers;
    }
}
