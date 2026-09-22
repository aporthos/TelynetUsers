package com.telynet.telynetusers.core.database;

import com.telynet.telynetusers.core.database.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDataGenerator {

    public static List<UserEntity> generate200FakeUsers() {
        List<UserEntity> fakeUsers = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"Carlos", "Ana", "Luis", "Maria", "Juan", "Sofia", "Diego", "Elena", "Javier", "Laura", "Pedro", "Lucia", "Miguel", "Carmen", "Andres"};
        String[] lastNames = {"Garcia", "Martinez", "Lopez", "Gonzalez", "Rodriguez", "Perez", "Sanchez", "Ramirez", "Flores", "Gomez", "Fernandez", "Torres", "Diaz"};
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com"};
        String[] countryCodes = {"+1", "+34", "+52", "+54", "+56", "+57"};

        for (int i = 1; i <= 200; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String fullName = firstName + " " + lastName;

            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@" + domains[random.nextInt(domains.length)];

            String code = "MX-" + (1000 + random.nextInt(9000));

            StringBuilder phoneNum = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                phoneNum.append(random.nextInt(10));
            }
            String phone = countryCodes[random.nextInt(countryCodes.length)] + phoneNum;

            boolean isVisited = random.nextBoolean();
            fakeUsers.add(new UserEntity(code, fullName, email, phone, isVisited));
        }

        return fakeUsers;
    }
}
