package kz.sapasoft.service;

import java.util.List;
import java.util.Optional;
import kz.sapasoft.domain.Monopolist;
import kz.sapasoft.domain.Transaction;
import kz.sapasoft.dto.ListItem;
import kz.sapasoft.dto.MonopolistInfo;
import kz.sapasoft.dto.TransactionInfo;
import kz.sapasoft.mapper.MonopolyMapper;
import kz.sapasoft.repository.MonopolistRepository;
import kz.sapasoft.repository.TransactionRepository;
import kz.sapasoft.web.rest.errors.MonopolyException;
import kz.sapasoft.web.rest.vm.MonopolistVM;
import kz.sapasoft.web.rest.vm.TransactionVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Monopolist}.
 */
@Service
@Transactional
public class MonopolistService {

    private final Logger log = LoggerFactory.getLogger(MonopolistService.class);

    private final MonopolistRepository monopolistRepository;

    private final TransactionRepository transactionRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    public MonopolistService(
        MonopolistRepository monopolistRepository,
        TransactionRepository transactionRepository,
        SimpMessageSendingOperations messagingTemplate
    ) {
        this.monopolistRepository = monopolistRepository;
        this.transactionRepository = transactionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Save a monopolist.
     *
     * @param monopolist the entity to save.
     * @return the persisted entity.
     */
    public Monopolist save(Monopolist monopolist) {
        log.debug("Request to save Monopolist : {}", monopolist);
        return monopolistRepository.save(monopolist);
    }

    /**
     * Partially update a monopolist.
     *
     * @param monopolist the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Monopolist> partialUpdate(Monopolist monopolist) {
        log.debug("Request to partially update Monopolist : {}", monopolist);

        return monopolistRepository
            .findById(monopolist.getId())
            .map(
                existingMonopolist -> {
                    if (monopolist.getName() != null) {
                        existingMonopolist.setName(monopolist.getName());
                    }
                    if (monopolist.getBalance() != null) {
                        existingMonopolist.setBalance(monopolist.getBalance());
                    }
                    if (monopolist.getIs_bank() != null) {
                        existingMonopolist.setIs_bank(monopolist.getIs_bank());
                    }
                    if (monopolist.getIs_active() != null) {
                        existingMonopolist.setIs_active(monopolist.getIs_active());
                    }

                    return existingMonopolist;
                }
            )
            .map(monopolistRepository::save);
    }

    /**
     * Get all the monopolists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Monopolist> findAll() {
        log.debug("Request to get all Monopolists");
        return monopolistRepository.findAll();
    }

    /**
     * Get one monopolist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Monopolist> findOne(Long id) {
        log.debug("Request to get Monopolist : {}", id);
        return monopolistRepository.findById(id);
    }

    /**
     * Delete the monopolist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Monopolist : {}", id);
        monopolistRepository.deleteById(id);
    }

    @Transactional
    public void dropToDefault() {
        monopolistRepository.dropToDefault();
        monopolistRepository.dropBankToDefault();
        transactionRepository.deleteAll();
    }

    public MonopolistInfo getInfo(Long id) throws MonopolyException {
        Optional<Monopolist> opt = monopolistRepository.findById(id);
        if (opt.isPresent()) {
            Monopolist m = opt.get();
            if (!m.getIs_active()) {
                //throw exception "back to login"
                throw new MonopolyException("back-to-login");
            }
            MonopolistInfo i = MonopolyMapper.toInfo(m);

            if (i.isBank) {
                monopolistRepository
                    .findById(1818L)
                    .ifPresent(
                        mon -> {
                            i.bank = MonopolyMapper.toInfo(mon);
                            i.transactions = transactionRepository.getMonopolistTransactions(1818L);
                        }
                    );
            }

            i.transactions = transactionRepository.getMonopolistTransactions(id);
            return i;
        }
        return null;
    }

    @Transactional
    public MonopolistInfo login(MonopolistVM monopolist) throws MonopolyException {
        Optional<Monopolist> opt = monopolistRepository.findById(monopolist.id);
        if (opt.isPresent()) {
            Monopolist m = opt.get();
            if (m.getIs_active()) {
                // throw exception "player already active"
                throw new MonopolyException("player-already-active");
            }

            if (monopolistRepository.isBankAlreadyExists() && monopolist.isBank) {
                // throw exception "bank already exists"
                throw new MonopolyException("bank-player-exists");
            }

            m.setIs_active(true);
            m.setIs_bank(monopolist.isBank);
            m.setName(monopolist.name);
            m = monopolistRepository.save(m);

            MonopolistInfo info = MonopolyMapper.toInfo(m);
            if (m.getIs_bank()) {
                monopolistRepository.findById(1818L).ifPresent(mon -> info.bank = MonopolyMapper.toInfo(mon));
            }
            return info;
        }

        //throw exception "player doesn't exists"
        throw new MonopolyException("player-dont-exist");
    }

    @Transactional
    public void sendMoney(TransactionVM transaction) {
        Transaction saved = transactionRepository.save(MonopolyMapper.toTransaction(transaction));
        monopolistRepository.updateBalanceFrom(transaction.fromId, transaction.amount);
        monopolistRepository.updateBalanceTo(transaction.toId, transaction.amount);
        TransactionInfo info = transactionRepository.getInfoById(saved.getId());
        messagingTemplate.convertAndSend("/topic/transaction/" + info.getFrom_id(), info);
        messagingTemplate.convertAndSend("/topic/transaction/" + info.getTo_id(), info);
    }

    public List<ListItem> getListOfMonopolist(Long except) {
        return monopolistRepository.getListOfMonopolist(except);
    }
}
