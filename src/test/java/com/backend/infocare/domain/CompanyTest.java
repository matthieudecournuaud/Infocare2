package com.backend.infocare.domain;

import static com.backend.infocare.domain.CompanyTestSamples.*;
import static com.backend.infocare.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
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
    void materialTest() {
        Company company = getCompanyRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        company.setMaterial(materialBack);
        assertThat(company.getMaterial()).isEqualTo(materialBack);
        assertThat(materialBack.getCompany()).isEqualTo(company);

        company.material(null);
        assertThat(company.getMaterial()).isNull();
        assertThat(materialBack.getCompany()).isNull();
    }
}
