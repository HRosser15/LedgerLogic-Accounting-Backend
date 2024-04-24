package com.ledgerlogic.services;

import com.ledgerlogic.exceptions.ResourceNotFoundException;
import com.ledgerlogic.models.JournalLine;
import com.ledgerlogic.repositories.JournalLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalLineService {

    private final JournalLineRepository journalLineRepository;

    @Autowired
    public JournalLineService(JournalLineRepository journalLineRepository) {
        this.journalLineRepository = journalLineRepository;
    }

    public JournalLine createJournalLine(JournalLine journalLine) {
        return journalLineRepository.save(journalLine);
    }

    public List<JournalLine> getAllJournalLines() {
        return journalLineRepository.findAll();
    }

    public JournalLine getJournalLineById(Long id) {
        return journalLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal line not found with id: " + id));
    }



    public JournalLine updateJournalLine(Long id, JournalLine updatedJournalLine) {
        JournalLine existingJournalLine = getJournalLineById(id);
        existingJournalLine.setAccount(updatedJournalLine.getAccount());
        existingJournalLine.setDebit(updatedJournalLine.getDebit());
        existingJournalLine.setCredit(updatedJournalLine.getCredit());
        return journalLineRepository.save(existingJournalLine);
    }

    public void deleteJournalLine(Long id) {
        JournalLine journalLine = getJournalLineById(id);
        journalLineRepository.delete(journalLine);
    }
}