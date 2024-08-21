package com.backend.infocare.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllPropertiesEquals(Company expected, Company actual) {
        assertCompanyAutoGeneratedPropertiesEquals(expected, actual);
        assertCompanyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllUpdatablePropertiesEquals(Company expected, Company actual) {
        assertCompanyUpdatableFieldsEquals(expected, actual);
        assertCompanyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAutoGeneratedPropertiesEquals(Company expected, Company actual) {
        assertThat(expected)
            .as("Verify Company auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyUpdatableFieldsEquals(Company expected, Company actual) {
        assertThat(expected)
            .as("Verify Company relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getSiret()).as("check siret").isEqualTo(actual.getSiret()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getContactPerson()).as("check contactPerson").isEqualTo(actual.getContactPerson()))
            .satisfies(e -> assertThat(e.getContactPersonPhone()).as("check contactPersonPhone").isEqualTo(actual.getContactPersonPhone()))
            .satisfies(e -> assertThat(e.getContactPersonEmail()).as("check contactPersonEmail").isEqualTo(actual.getContactPersonEmail()))
            .satisfies(e -> assertThat(e.getSize()).as("check size").isEqualTo(actual.getSize()))
            .satisfies(e -> assertThat(e.getNotes()).as("check notes").isEqualTo(actual.getNotes()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyUpdatableRelationshipsEquals(Company expected, Company actual) {}
}
