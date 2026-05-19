package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        Scanner scanner = new Scanner(System.in);
        int N = Integer.parseInt(scanner.nextLine());

        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))){
            for (int i = 0; i < N; i++) {
                String[] parts = scanner.nextLine().split(" ");
                int id = Integer.parseInt(parts[0]);
                double suma = Double.parseDouble(parts[1]);
                String data = parts[2];
                TipTranzactie tip = TipTranzactie.valueOf(parts[3]);
                writeRecord(dos, id, suma, data, tip);
            }
        }

        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")){
            while(scanner.hasNextLine()) {
                String[] linie = scanner.nextLine().split(" ");
                switch (linie[0]) {
                    case "READ" -> {
                        int idx = Integer.parseInt(linie[1]);
                        raf.seek(idx * RECORD_SIZE);
                        byte[] record = new byte[RECORD_SIZE];
                        raf.read(record);

                        ByteBuffer buf = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
                        int id = buf.getInt();
                        double suma = buf.getDouble();

                        byte[] dataBytes = new byte[10];
                        buf.get(dataBytes);
                        String data = new String(dataBytes).trim();

                        int tipByte = buf.get() & 0xFF;
                        int statusByte = buf.get() & 0xFF;

                        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
                        String status = switch (statusByte) {
                            case 0 -> "PENDING";
                            case 1 -> "PROCESSED";
                            case 2 -> "REJECTED";
                            default -> "UNKNOWN";
                        };

                        System.out.println(String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                                idx, id, data, tip, suma, status));
                    }
                    case "UPDATE" -> {
                        int idx = Integer.parseInt(linie[1]);
                        String statusStr = linie[2];
                        int statusByte = switch (statusStr) {
                            case "PROCESSED" -> 1;
                            case "REJECTED" -> 2;
                            default -> 0;
                        };
                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.write(statusByte);
                        System.out.println("Updated [" + idx + "]: " + statusStr);
                    }
                    case "PRINT_ALL" -> {
                        raf.seek(0);
                        long numRecords = raf.length() / RECORD_SIZE;
                        for (int idx = 0; idx < numRecords; idx++) {
                            byte[] record = new byte[RECORD_SIZE];
                            raf.read(record);

                            ByteBuffer buf = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
                            int id = buf.getInt();
                            double suma = buf.getDouble();

                            byte[] dataBytes = new byte[10];
                            buf.get(dataBytes);
                            String data = new String(dataBytes).trim();

                            int tipByte = buf.get() & 0xFF;
                            int statusByte = buf.get() & 0xFF;

                            String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
                            String status = switch (statusByte) {
                                case 0 -> "PENDING";
                                case 1 -> "PROCESSED";
                                case 2 -> "REJECTED";
                                default -> "UNKNOWN";
                            };

                            System.out.println(String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                                    idx, id, data, tip, suma, status));
                        }
                    }
                }
            }
        }


//        System.out.println("TODO: implementează exercițiul 2");
    }

    private static void writeRecord(DataOutputStream dos, int id, double suma, String data, TipTranzactie tip) throws IOException {
        dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());
        dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

        dos.write(data.getBytes());
        for (int i = data.getBytes().length; i < 10; i++) {
            dos.write(' ');
        }

        if (tip == TipTranzactie.CREDIT) {
            dos.write(0);
        } else {
            dos.write(1);
        }
        dos.write(0);
        dos.write(new byte[8]);
    }
}
