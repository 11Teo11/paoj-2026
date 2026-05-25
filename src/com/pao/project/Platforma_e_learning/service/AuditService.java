package com.pao.project.Platforma_e_learning.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {
    private static AuditService instance;
    // Fisierul de audit (relativ la working directory = radacina proiectului)
    private static final String AUDIT_FILE = "audit.csv";
    // ReentrantLock protejeaza scrierea in fisier daca mai multe thread-uri
    // apeleaza log() simultan (ex: intr-o aplicatie web sau multi-threaded)
    private final ReentrantLock lock = new ReentrantLock();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {
        File f = new File(AUDIT_FILE);
        if (!f.exists() || f.length() == 0) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
                pw.println(String.format("%-30s %s","actiune","timestamp"));
            } catch (IOException e) {
                System.err.println("[AUDIT] Eroare la scriere header: " + e.getMessage());
            }
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    /**
     * Logheaza o actiune in audit.csv.
     * Metoda este thread-safe prin ReentrantLock.
     *
     * @param actionName Numele actiunii (ex: "add_book", "borrow_book")
     */
    public void log(String actionName) {
        lock.lock();                              // blocare exclusiva
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(AUDIT_FILE, true))) { // true = append mode
            String timestamp = LocalDateTime.now().format(formatter);
            pw.println(String.format("%-30s %s",actionName,timestamp));
        } catch (IOException e) {
            System.err.println("[AUDIT] Eroare la scriere: " + e.getMessage());
        } finally {
            lock.unlock();                        // eliberare garantata
        }
    }

}
