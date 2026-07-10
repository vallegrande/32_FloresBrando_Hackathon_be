package com.example.demo.service;

import com.example.demo.entity.Career;
import com.example.demo.entity.Students;
import com.example.demo.entity.Ubigeo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CareerRepository;
import com.example.demo.repository.StudentsRepository;
import com.example.demo.repository.UbigeoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tabla maestra: students.
 * Registra, edita, elimina (baja lógica) y restaura estudiantes, reflejando
 * siempre los cambios en la base de datos vía StudentsRepository.
 */
@Service
public class StudentsService {

    private final StudentsRepository studentsRepository;
    private final UbigeoRepository ubigeoRepository;
    private final CareerRepository careerRepository;

    public StudentsService(StudentsRepository studentsRepository,
                            UbigeoRepository ubigeoRepository,
                            CareerRepository careerRepository) {
        this.studentsRepository = studentsRepository;
        this.ubigeoRepository = ubigeoRepository;
        this.careerRepository = careerRepository;
    }

    public List<Students> findAll() {
        return studentsRepository.findAllActive();
    }

    public Optional<Students> findById(Integer id) {
        return studentsRepository.findActiveById(id);
    }

    @Transactional
    public Students save(Students student) {
        if (student.getCareer() == null || student.getCareer().getIdCareer() == null) {
            throw new BusinessException("La carrera (id_career) es obligatoria");
        }
        if (student.getUbigeo() == null || student.getUbigeo().getIdUbigeo() == null) {
            throw new BusinessException("El ubigeo (id_ubigeo) es obligatorio");
        }

        Career career = careerRepository.findById(student.getCareer().getIdCareer())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la carrera con id: " + student.getCareer().getIdCareer()));
        Ubigeo ubigeo = ubigeoRepository.findById(student.getUbigeo().getIdUbigeo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el ubigeo con id: " + student.getUbigeo().getIdUbigeo()));

        if (studentsRepository.findActiveByEmail(student.getEmail()).isPresent()) {
            throw new BusinessException("Ya existe un estudiante activo con este email");
        }
        if (studentsRepository.findActiveByNumerDoc(student.getNumerDoc()).isPresent()) {
            throw new BusinessException("Ya existe un estudiante activo con este documento");
        }

        student.setCareer(career);
        student.setUbigeo(ubigeo);
        if (student.getStatus() == null) {
            student.setStatus(true);
        }
        return studentsRepository.save(student);
    }

    @Transactional
    public Students update(Integer id, Students studentDetails) {
        Students student = studentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante con id: " + id));

        // Evita registrar el mismo email/documento en OTRO estudiante activo
        studentsRepository.findActiveByEmail(studentDetails.getEmail())
                .filter(s -> !s.getIdStudents().equals(id))
                .ifPresent(s -> {
                    throw new BusinessException("Ya existe otro estudiante activo con este email");
                });
        studentsRepository.findActiveByNumerDoc(studentDetails.getNumerDoc())
                .filter(s -> !s.getIdStudents().equals(id))
                .ifPresent(s -> {
                    throw new BusinessException("Ya existe otro estudiante activo con este documento");
                });

        if (studentDetails.getCareer() != null && studentDetails.getCareer().getIdCareer() != null) {
            Career career = careerRepository.findById(studentDetails.getCareer().getIdCareer())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe la carrera con id: " + studentDetails.getCareer().getIdCareer()));
            student.setCareer(career);
        }
        if (studentDetails.getUbigeo() != null && studentDetails.getUbigeo().getIdUbigeo() != null) {
            Ubigeo ubigeo = ubigeoRepository.findById(studentDetails.getUbigeo().getIdUbigeo())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el ubigeo con id: " + studentDetails.getUbigeo().getIdUbigeo()));
            student.setUbigeo(ubigeo);
        }

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setDocumentType(studentDetails.getDocumentType());
        student.setNumerDoc(studentDetails.getNumerDoc());
        student.setEmail(studentDetails.getEmail());
        student.setPhone(studentDetails.getPhone());
        student.setAdress(studentDetails.getAdress());
        return studentsRepository.save(student);
    }

    /** Baja lógica: status = false. El registro permanece en la BD. */
    @Transactional
    public void delete(Integer id) {
        Students student = studentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante con id: " + id));
        student.setStatus(false);
        studentsRepository.save(student);
    }

    /** Restauración lógica: status = true. */
    @Transactional
    public void restore(Integer id) {
        Students student = studentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante con id: " + id));
        student.setStatus(true);
        studentsRepository.save(student);
    }

    @Transactional
    public void changeStatus(Integer id, Boolean status) {
        Students student = studentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante con id: " + id));
        student.setStatus(status);
        studentsRepository.save(student);
    }

    @Transactional
    public void importExcel(MultipartFile file) throws IOException {
        List<Students> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                // Columnas esperadas:
                // 0 firstName | 1 lastName | 2 idCareer | 3 documentType | 4 numerDoc
                // 5 email | 6 phone | 7 adress | 8 idUbigeo
                Students student = new Students();
                student.setFirstName(getCellValue(row.getCell(0)));
                student.setLastName(getCellValue(row.getCell(1)));

                String idCareerStr = getCellValue(row.getCell(2));
                if (idCareerStr.isBlank()) {
                    throw new BusinessException("Fila " + (row.getRowNum() + 1) + ": id_career es obligatorio");
                }
                Career career = careerRepository.findById(Integer.valueOf(idCareerStr))
                        .orElseThrow(() -> new ResourceNotFoundException("No existe la carrera con id: " + idCareerStr));
                student.setCareer(career);

                student.setDocumentType(getCellValue(row.getCell(3)));
                student.setNumerDoc(getCellValue(row.getCell(4)));
                student.setEmail(getCellValue(row.getCell(5)));
                student.setPhone(getCellValue(row.getCell(6)));
                student.setAdress(getCellValue(row.getCell(7)));

                String ubigeoId = getCellValue(row.getCell(8));
                Ubigeo ubigeo = ubigeoRepository.findById(ubigeoId)
                        .orElseThrow(() -> new ResourceNotFoundException("No existe el ubigeo con id: " + ubigeoId));
                student.setUbigeo(ubigeo);

                student.setStatus(true);
                students.add(student);
            }
        }

        studentsRepository.saveAll(students);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public ByteArrayInputStream exportExcel() {
        String[] columns = {"ID", "First Name", "Last Name", "Document Type", "Document Number", "Email", "Phone", "Address", "Status"};
        try (Workbook workbook = new XSSFWorkbook()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet("Students");
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            List<Students> students = findAll();
            int rowIdx = 1;
            for (Students student : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getIdStudents());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getDocumentType());
                row.createCell(4).setCellValue(student.getNumerDoc());
                row.createCell(5).setCellValue(student.getEmail());
                row.createCell(6).setCellValue(student.getPhone() != null ? student.getPhone() : "");
                row.createCell(7).setCellValue(student.getAdress() != null ? student.getAdress() : "");
                row.createCell(8).setCellValue(Boolean.TRUE.equals(student.getStatus()) ? "Activo" : "Inactivo");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }

    public ByteArrayInputStream exportPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
            Paragraph title = new Paragraph("Students List", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            String[] headers = {"ID", "First Name", "Last Name", "Doc Type", "Doc Number", "Email", "Phone", "Address"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            List<Students> students = findAll();
            for (Students student : students) {
                table.addCell(String.valueOf(student.getIdStudents()));
                table.addCell(student.getFirstName());
                table.addCell(student.getLastName());
                table.addCell(student.getDocumentType());
                table.addCell(student.getNumerDoc());
                table.addCell(student.getEmail());
                table.addCell(student.getPhone() != null ? student.getPhone() : "");
                table.addCell(student.getAdress() != null ? student.getAdress() : "");
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Failed to export PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
