package com.backend.infocare.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Company getCompanySample1() {
        return new Company()
            .id(1L)
            .name("name1")
            .phone("phone1")
            .siret("siret1")
            .address("address1")
            .email("email1")
            .contactPerson("contactPerson1")
            .contactPersonPhone("contactPersonPhone1")
            .contactPersonEmail("contactPersonEmail1")
            .size("size1")
            .notes("notes1");
    }

    public static Company getCompanySample2() {
        return new Company()
            .id(2L)
            .name("name2")
            .phone("phone2")
            .siret("siret2")
            .address("address2")
            .email("email2")
            .contactPerson("contactPerson2")
            .contactPersonPhone("contactPersonPhone2")
            .contactPersonEmail("contactPersonEmail2")
            .size("size2")
            .notes("notes2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .siret(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .contactPerson(UUID.randomUUID().toString())
            .contactPersonPhone(UUID.randomUUID().toString())
            .contactPersonEmail(UUID.randomUUID().toString())
            .size(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
