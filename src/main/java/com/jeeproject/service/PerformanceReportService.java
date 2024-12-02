package com.jeeproject.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.jeeproject.model.Course;
import com.jeeproject.model.Result;
import com.jeeproject.model.Student;
import com.jeeproject.util.MathUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceReportService {

    public static void generate(HttpServletResponse response, Student student) throws IOException {
        // get students results by course
        Map<Course, List<Result>> resultsByCourse = ResultService.getResultsByStudentIdGroupedByCourse(student.getId());

        // get students averages by course
        Map<Course, Double> studentAveragesByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));

        // get courses average by course
        Map<Course, Double> courseAverages = resultsByCourse.keySet().stream()
                .collect(Collectors.toMap(
                        course -> course,
                        course -> {
                            Map<Student, List<Result>> resultsGroupedByStudent = ResultService.getResultsByCourseIdGroupedByStudent(course.getId());
                            // get student averages
                            List<Double> studentAverages = resultsGroupedByStudent.values().stream()
                                    .map(MathUtil::calculateAverageFromResults)
                                    .collect(Collectors.toList());
                            // compute course average
                            return studentAverages.stream()
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElse(0.0);
                        }
                ));

        // get student ranks by course
        Map<Course, Integer> studentRanksByCourse = calculateRankByCourse(student.getId(), resultsByCourse.keySet());

        Map<Course, Integer> studentCountByCourse = resultsByCourse.keySet().stream()
                .collect(Collectors.toMap(
                        course -> course,
                        course -> {
                            List<Student> students = StudentService.getStudentsByCourseId(course.getId());
                            return students.size();
                        }
                ));

        // student general average
        double studentGeneralAverage = MathUtil.calculateAverageFromDouble(studentAveragesByCourse.values());


        // Generate PDF
        downloadPdf(
                response,
                student,
                studentAveragesByCourse,
                courseAverages,
                studentRanksByCourse,
                studentCountByCourse,
                studentGeneralAverage
        );
    }

    private static void downloadPdf(
        HttpServletResponse response,
        Student student,
        Map<Course, Double> studentAveragesByCourse,
        Map<Course, Double> courseAverages,
        Map<Course, Integer> studentRanksByCourse,
        Map<Course, Integer> studentCountByCourse,
        double studentGeneralAverage
    ) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        // title
        document.add(new Paragraph("Rapport de Performance Étudiant")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        // student details
        document.add(new Paragraph("Nom : " + student.getLastName()));
        document.add(new Paragraph("Prénom : " + student.getFirstName()));
        document.add(new Paragraph("ID : " + student.getId()));
        // course details list
        float[] columnWidths = {40f,250f, 80f, 50f, 50f}; // Largeur des colonnes (matière, moyenne, résultat)
        Table table = new Table(columnWidths);
        table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Matière").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Moyenne des étudiants").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Rang").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Moyenne de l'étudiant").setBold()));
        document.add(new Paragraph("\nDétails par cours :").setBold());
        for (Course course : courseAverages.keySet()) {
            table.addCell(new Cell().add(new Paragraph(Integer.toString(course.getId()))));
            table.addCell(new Cell().add(new Paragraph(course.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f / 20", courseAverages.get(course)))));
            table.addCell(new Cell().add(new Paragraph(String.format("%d / %d", studentRanksByCourse.get(course), studentCountByCourse.get(course)))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f / 20", studentAveragesByCourse.get(course))).setBold()));
        }
        // General average
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph("Moyenne générale").setBold()));
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph(Double.toString(studentGeneralAverage)).setBold()));

        document.add(table);
        document.close();
    }


    private static Map<Course, Integer> calculateRankByCourse(int studentId, Set<Course> courses) {
        Map<Course, Integer> rankByCourse = new HashMap<>();

        for (Course course : courses) {
            // get students enrolled in course
            List<Student> students = StudentService.getStudentsByCourseId(course.getId());
            // get average per student
            Map<Student, Double> studentAverages = new HashMap<>();
            for (Student student : students) {
                List<Result> results = ResultService.getResultsByCourseIdGroupedByStudent(course.getId()).getOrDefault(student, List.of());
                double average = MathUtil.calculateAverageFromResults(results);
                studentAverages.put(student, average);
            }
            // sort students average
            List<Map.Entry<Student, Double>> sortedAverages = new ArrayList<>(studentAverages.entrySet());
            sortedAverages.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            // get rank
            int rank = 1;
            boolean found = false;
            for (Map.Entry<Student, Double> entry : sortedAverages) {
                if (entry.getKey().getId() == studentId) {
                    rankByCourse.put(course, rank);
                    found = true;
                    break;
                }
                rank++;
            }
            // if student not found
            if (!found) {
                rankByCourse.put(course, -1);
            }
        }

        return rankByCourse;
    }

}
