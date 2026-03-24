package com.pao.laboratory05.audit;

import com.pao.laboratory05.biblioteca.BibliotecaService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AngajatService {
    private Angajat[] angajati;
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return AngajatService.Holder.INSTANCE;
    }

    public void addAngajat(Angajat a){
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;
        System.out.println("Angajat adăugat: " + a.getNume());
        logAction("ADD", a.getNume());
    }

    public void printAll(){
        for(Angajat a : angajati){
            System.out.println(a);
        }
    }

    public void listBySalary(){
        System.out.println("--- Angajați după salariu (descrescător) ---");
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for(Angajat a : copy){
            System.out.println(a);
        }
    }

    public void findByDepartment(String numeDept){
        logAction("FIND_BY_DEPT", numeDept);
        System.out.println("--- Angajați din " + numeDept + " ---");
        int gasit = 0;
        for(Angajat a : angajati){
            if(a.getDepartament().nume().equalsIgnoreCase(numeDept)){
                System.out.println(a);
                gasit = 1;
            }
        }
        if(gasit == 0)
            System.out.println("Niciun angajat în departamentul: " + numeDept);
    }


    private void logAction(String action, String target){
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] tmp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, tmp, 0, auditLog.length);
        tmp[tmp.length - 1] = entry;
        auditLog = tmp;
    }

    public void printAuditLog(){
        System.out.println("--- Audit Log ---");
        for (AuditEntry e : auditLog) {
            System.out.println(e);
        }
    }
}
