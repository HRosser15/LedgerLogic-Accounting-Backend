package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.services.JournalService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ledgerlogic.repositories.JournalRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {


    private final JournalService journalService;

    private final JournalRepository journalRepository;

    public JournalController(JournalService journalService, JournalRepository journalRepository) {
        this.journalService = journalService;
        this.journalRepository = journalRepository;
    }

    @PostMapping("/addJournal")
    public Journal addJournal(@RequestPart Journal journal,
                              @RequestPart(value = "attachments", required = false) MultipartFile attachments,
                              @RequestParam Long userId) throws IOException {

        String relativeUploadDir = "LodgerLogic/src/main/resources/uploads";
        File uploadDir = new File(relativeUploadDir);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String attachmentPath = null;
        if (attachments != null && !attachments.isEmpty()) {
            String fileName = StringUtils.cleanPath(attachments.getOriginalFilename());
            Path filePath = Paths.get(uploadDir.getAbsolutePath(), fileName);
            Files.copy(attachments.getInputStream(), filePath);
            attachmentPath = filePath.toString();
        }

        journal.setAttachmentPath(attachmentPath);

        List<JournalEntry> journalEntries = journal.getJournalEntries();
        if (journalEntries != null) {
            for (JournalEntry entry : journalEntries) {
                entry.setJournal(journal);
                entry.setDescription(entry.getDescription());
            }
            journal.setJournalEntries(journalEntries);
        }

        return this.journalService.addJournal(journal, userId);
    }

    @PostMapping("/approveJournal/{journalId}/{newStatus}")
    public Journal approveJournal(@PathVariable Long journalId, @PathVariable String newStatus) {
        Journal.Status status = Journal.Status.valueOf(newStatus.toUpperCase());
        return this.journalService.approveJournal(journalId, status);
    }

    @PostMapping("/rejectJournal")
    public Journal rejectJournal(@RequestBody JournalDTO journalDTO){
        return this.journalService.rejectJournal(journalDTO);
    }

    @GetMapping("/getAll")
    public List<Journal> getAll(){
        return this.journalService.getAllJournals();
    }

    @GetMapping("/getByStatus/{status}")
    public List<Journal> getByStatus(@PathVariable Journal.Status status){
        return this.journalService.getByStatus(status);
    }

    @GetMapping("/getByDate")
    public List<Journal> getByDate(@RequestBody Date date){
        return this.journalService.getByDate(date);
    }

    @GetMapping("/journal/attachments/{journalId}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long journalId) throws IOException {
        // Retrieve the journal entity based on the journalId
        Journal journal = journalRepository.findById(journalId).orElseThrow();

        // Check if the journal has an attachment
        if (journal.getAttachmentPath() == null) {
            return ResponseEntity.notFound().build();
        }

        // Read the file data from the specified path
        Path path = Paths.get(journal.getAttachmentPath());
        byte[] fileData = Files.readAllBytes(path);

        // Set the appropriate content type based on the file extension
        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileData);
    }
}
