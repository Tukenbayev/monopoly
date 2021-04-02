package kz.sapasoft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kz.sapasoft.domain.Monopolist;
import kz.sapasoft.repository.MonopolistRepository;
import kz.sapasoft.service.MonopolistService;
import kz.sapasoft.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link kz.sapasoft.domain.Monopolist}.
 */
@RestController
@RequestMapping("/open-api")
public class MonopolistResource {

    private final Logger log = LoggerFactory.getLogger(MonopolistResource.class);

    private static final String ENTITY_NAME = "monopolist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonopolistService monopolistService;

    private final MonopolistRepository monopolistRepository;

    public MonopolistResource(MonopolistService monopolistService, MonopolistRepository monopolistRepository) {
        this.monopolistService = monopolistService;
        this.monopolistRepository = monopolistRepository;
    }

    /**
     * {@code POST  /monopolists} : Create a new monopolist.
     *
     * @param monopolist the monopolist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monopolist, or with status {@code 400 (Bad Request)} if the monopolist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monopolists")
    public ResponseEntity<Monopolist> createMonopolist(@Valid @RequestBody Monopolist monopolist) throws URISyntaxException {
        log.debug("REST request to save Monopolist : {}", monopolist);
        if (monopolist.getId() != null) {
            throw new BadRequestAlertException("A new monopolist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Monopolist result = monopolistService.save(monopolist);
        return ResponseEntity
            .created(new URI("/api/monopolists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monopolists/:id} : Updates an existing monopolist.
     *
     * @param id the id of the monopolist to save.
     * @param monopolist the monopolist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monopolist,
     * or with status {@code 400 (Bad Request)} if the monopolist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monopolist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monopolists/{id}")
    public ResponseEntity<Monopolist> updateMonopolist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Monopolist monopolist
    ) throws URISyntaxException {
        log.debug("REST request to update Monopolist : {}, {}", id, monopolist);
        if (monopolist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monopolist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monopolistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Monopolist result = monopolistService.save(monopolist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monopolist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /monopolists/:id} : Partial updates given fields of an existing monopolist, field will ignore if it is null
     *
     * @param id the id of the monopolist to save.
     * @param monopolist the monopolist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monopolist,
     * or with status {@code 400 (Bad Request)} if the monopolist is not valid,
     * or with status {@code 404 (Not Found)} if the monopolist is not found,
     * or with status {@code 500 (Internal Server Error)} if the monopolist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monopolists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Monopolist> partialUpdateMonopolist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Monopolist monopolist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Monopolist partially : {}, {}", id, monopolist);
        if (monopolist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monopolist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monopolistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Monopolist> result = monopolistService.partialUpdate(monopolist);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monopolist.getId().toString())
        );
    }

    /**
     * {@code GET  /monopolists} : get all the monopolists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monopolists in body.
     */
    @GetMapping("/monopolists")
    public List<Monopolist> getAllMonopolists() {
        log.debug("REST request to get all Monopolists");
        return monopolistService.findAll();
    }

    /**
     * {@code GET  /monopolists/:id} : get the "id" monopolist.
     *
     * @param id the id of the monopolist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monopolist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monopolists/{id}")
    public ResponseEntity<Monopolist> getMonopolist(@PathVariable Long id) {
        log.debug("REST request to get Monopolist : {}", id);
        Optional<Monopolist> monopolist = monopolistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monopolist);
    }

    /**
     * {@code DELETE  /monopolists/:id} : delete the "id" monopolist.
     *
     * @param id the id of the monopolist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monopolists/{id}")
    public ResponseEntity<Void> deleteMonopolist(@PathVariable Long id) {
        log.debug("REST request to delete Monopolist : {}", id);
        monopolistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
