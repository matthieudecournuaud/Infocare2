package com.backend.infocare.domain;

import static com.backend.infocare.domain.CompanyTestSamples.*;
import static com.backend.infocare.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void materialTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        company.addMaterial(materialBack);
        assertThat(company.getMaterials()).containsOnly(materialBack);
        assertThat(materialBack.getCompany()).isEqualTo(company);

        company.removeMaterial(materialBack);
        assertThat(company.getMaterials()).doesNotContain(materialBack);
        assertThat(materialBack.getCompany()).isNull();

        company.materials(new HashSet<>(Set.of(materialBack)));
        assertThat(company.getMaterials()).containsOnly(materialBack);
        assertThat(materialBack.getCompany()).isEqualTo(company);

        company.setMaterials(new HashSet<>());
        assertThat(company.getMaterials()).doesNotContain(materialBack);
        assertThat(materialBack.getCompany()).isNull();
    }
}
