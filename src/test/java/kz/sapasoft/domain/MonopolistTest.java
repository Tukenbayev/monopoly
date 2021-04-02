package kz.sapasoft.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kz.sapasoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonopolistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Monopolist.class);
        Monopolist monopolist1 = new Monopolist();
        monopolist1.setId(1L);
        Monopolist monopolist2 = new Monopolist();
        monopolist2.setId(monopolist1.getId());
        assertThat(monopolist1).isEqualTo(monopolist2);
        monopolist2.setId(2L);
        assertThat(monopolist1).isNotEqualTo(monopolist2);
        monopolist1.setId(null);
        assertThat(monopolist1).isNotEqualTo(monopolist2);
    }
}
