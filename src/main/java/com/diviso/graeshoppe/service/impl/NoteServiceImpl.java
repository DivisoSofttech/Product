package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.NoteService;
import com.diviso.graeshoppe.domain.Note;
import com.diviso.graeshoppe.repository.NoteRepository;
import com.diviso.graeshoppe.repository.search.NoteSearchRepository;
import com.diviso.graeshoppe.service.dto.NoteDTO;
import com.diviso.graeshoppe.service.mapper.NoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Note.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    private final NoteSearchRepository noteSearchRepository;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, NoteSearchRepository noteSearchRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.noteSearchRepository = noteSearchRepository;
    }

    /**
     * Save a note.
     *
     * @param noteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        log.debug("Request to save Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        NoteDTO result = noteMapper.toDto(note);
        noteSearchRepository.save(note);
        return result;
    }

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable)
            .map(noteMapper::toDto);
    }


    /**
     * Get one note by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NoteDTO> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id)
            .map(noteMapper::toDto);
    }

    /**
     * Delete the note by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);        noteRepository.deleteById(id);
        noteSearchRepository.deleteById(id);
    }

    /**
     * Search for the note corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notes for query {}", query);
        return noteSearchRepository.search(queryStringQuery(query), pageable)
            .map(noteMapper::toDto);
    }
}
