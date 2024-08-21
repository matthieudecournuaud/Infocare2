package com.backend.infocare.domain;

import static com.backend.infocare.domain.InterventionTestSamples.*;
import static com.backend.infocare.domain.ProcedureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcedureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedure.class);
        Procedure procedure1 = getProcedureSample1();
        Procedure procedure2 = new Procedure();
        assertThat(procedure1).isNotEqualTo(procedure2);

        procedure2.setId(procedure1.getId());
        assertThat(procedure1).isEqualTo(procedure2);

        procedure2 = getProcedureSample2();
        assertThat(procedure1).isNotEqualTo(procedure2);
    }

    @Test
    void interventionTest() {
        Procedure procedure = getProcedureRandomSampleGenerator();
        Intervention interventionBack = getInterventionRandomSampleGenerator();

        procedure.setIntervention(interventionBack);
        assertThat(procedure.getIntervention()).isEqualTo(interventionBack);
        assertThat(interventionBack.getProcedure()).isEqualTo(procedure);

        procedure.intervention(null);
        assertThat(procedure.getIntervention()).isNull();
        assertThat(interventionBack.getProcedure()).isNull();
    }
}
