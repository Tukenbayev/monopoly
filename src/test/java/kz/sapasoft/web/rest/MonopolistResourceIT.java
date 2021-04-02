package kz.sapasoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import kz.sapasoft.IntegrationTest;
import kz.sapasoft.domain.Monopolist;
import kz.sapasoft.repository.MonopolistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MonopolistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonopolistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    private static final Boolean DEFAULT_IS_BANK = false;
    private static final Boolean UPDATED_IS_BANK = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/monopolists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonopolistRepository monopolistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonopolistMockMvc;

    private Monopolist monopolist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monopolist createEntity(EntityManager em) {
        Monopolist monopolist = new Monopolist()
            .name(DEFAULT_NAME)
            .balance(DEFAULT_BALANCE)
            .is_bank(DEFAULT_IS_BANK)
            .is_active(DEFAULT_IS_ACTIVE);
        return monopolist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monopolist createUpdatedEntity(EntityManager em) {
        Monopolist monopolist = new Monopolist()
            .name(UPDATED_NAME)
            .balance(UPDATED_BALANCE)
            .is_bank(UPDATED_IS_BANK)
            .is_active(UPDATED_IS_ACTIVE);
        return monopolist;
    }

    @BeforeEach
    public void initTest() {
        monopolist = createEntity(em);
    }

    @Test
    @Transactional
    void createMonopolist() throws Exception {
        int databaseSizeBeforeCreate = monopolistRepository.findAll().size();
        // Create the Monopolist
        restMonopolistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monopolist)))
            .andExpect(status().isCreated());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeCreate + 1);
        Monopolist testMonopolist = monopolistList.get(monopolistList.size() - 1);
        assertThat(testMonopolist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMonopolist.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testMonopolist.getIs_bank()).isEqualTo(DEFAULT_IS_BANK);
        assertThat(testMonopolist.getIs_active()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createMonopolistWithExistingId() throws Exception {
        // Create the Monopolist with an existing ID
        monopolist.setId(1L);

        int databaseSizeBeforeCreate = monopolistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonopolistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monopolist)))
            .andExpect(status().isBadRequest());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = monopolistRepository.findAll().size();
        // set the field null
        monopolist.setName(null);

        // Create the Monopolist, which fails.

        restMonopolistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monopolist)))
            .andExpect(status().isBadRequest());

        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonopolists() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        // Get all the monopolistList
        restMonopolistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monopolist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].is_bank").value(hasItem(DEFAULT_IS_BANK.booleanValue())))
            .andExpect(jsonPath("$.[*].is_active").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getMonopolist() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        // Get the monopolist
        restMonopolistMockMvc
            .perform(get(ENTITY_API_URL_ID, monopolist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monopolist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.is_bank").value(DEFAULT_IS_BANK.booleanValue()))
            .andExpect(jsonPath("$.is_active").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMonopolist() throws Exception {
        // Get the monopolist
        restMonopolistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMonopolist() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();

        // Update the monopolist
        Monopolist updatedMonopolist = monopolistRepository.findById(monopolist.getId()).get();
        // Disconnect from session so that the updates on updatedMonopolist are not directly saved in db
        em.detach(updatedMonopolist);
        updatedMonopolist.name(UPDATED_NAME).balance(UPDATED_BALANCE).is_bank(UPDATED_IS_BANK).is_active(UPDATED_IS_ACTIVE);

        restMonopolistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMonopolist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMonopolist))
            )
            .andExpect(status().isOk());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
        Monopolist testMonopolist = monopolistList.get(monopolistList.size() - 1);
        assertThat(testMonopolist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMonopolist.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testMonopolist.getIs_bank()).isEqualTo(UPDATED_IS_BANK);
        assertThat(testMonopolist.getIs_active()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monopolist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monopolist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monopolist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monopolist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonopolistWithPatch() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();

        // Update the monopolist using partial update
        Monopolist partialUpdatedMonopolist = new Monopolist();
        partialUpdatedMonopolist.setId(monopolist.getId());

        partialUpdatedMonopolist.name(UPDATED_NAME);

        restMonopolistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonopolist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonopolist))
            )
            .andExpect(status().isOk());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
        Monopolist testMonopolist = monopolistList.get(monopolistList.size() - 1);
        assertThat(testMonopolist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMonopolist.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testMonopolist.getIs_bank()).isEqualTo(DEFAULT_IS_BANK);
        assertThat(testMonopolist.getIs_active()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateMonopolistWithPatch() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();

        // Update the monopolist using partial update
        Monopolist partialUpdatedMonopolist = new Monopolist();
        partialUpdatedMonopolist.setId(monopolist.getId());

        partialUpdatedMonopolist.name(UPDATED_NAME).balance(UPDATED_BALANCE).is_bank(UPDATED_IS_BANK).is_active(UPDATED_IS_ACTIVE);

        restMonopolistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonopolist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonopolist))
            )
            .andExpect(status().isOk());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
        Monopolist testMonopolist = monopolistList.get(monopolistList.size() - 1);
        assertThat(testMonopolist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMonopolist.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testMonopolist.getIs_bank()).isEqualTo(UPDATED_IS_BANK);
        assertThat(testMonopolist.getIs_active()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monopolist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monopolist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monopolist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonopolist() throws Exception {
        int databaseSizeBeforeUpdate = monopolistRepository.findAll().size();
        monopolist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonopolistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(monopolist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monopolist in the database
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonopolist() throws Exception {
        // Initialize the database
        monopolistRepository.saveAndFlush(monopolist);

        int databaseSizeBeforeDelete = monopolistRepository.findAll().size();

        // Delete the monopolist
        restMonopolistMockMvc
            .perform(delete(ENTITY_API_URL_ID, monopolist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Monopolist> monopolistList = monopolistRepository.findAll();
        assertThat(monopolistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
